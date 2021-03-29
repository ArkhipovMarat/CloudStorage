package ru.netology.cloud_storage.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.entity.FileStorageData;
import ru.netology.cloud_storage.entity.UserEntity;
import ru.netology.cloud_storage.properties.StorageProperties;
import ru.netology.cloud_storage.exceptions.StorageException;
import ru.netology.cloud_storage.repository.FileStorageRepository;
import ru.netology.cloud_storage.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {
    private final StorageService storageService;
    private final UserRepository userRepository;
    private final FileStorageRepository fileStorageRepository;
    private final Path rootLocation;

    @Autowired
    public FileService(StorageService storageService, UserRepository userRepository,
                       FileStorageRepository fileStorageRepository, StorageProperties storageProperties) {
        this.storageService = storageService;
        this.userRepository = userRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.rootLocation = Paths.get(storageProperties.getRootLocation());
    }

    // CREATING ROOT DIRECTORY - ONCE ON START
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Transactional
    public void postFile(String filename, MultipartFile file) {
        FileStorageData fileStorageData = getFileStorageData(filename, file);

        fileStorageRepository.saveAndFlush(fileStorageData);

        storageService.store(file, filename, Path.of(fileStorageData.getFilepath()));
    }

    @Transactional
    public void deleteFile(String filename) {
        fileStorageRepository.removeFileDataByFilename(filename);

        storageService.deleteFile(getFilePath(filename));
    }

    public Resource getFile(String filename) {
        return storageService.loadAsResource(filename, getFilePath(filename));
    }

    @Transactional
    public List<FileStorageData> getList() {
        return fileStorageRepository.findAllByUser_Login(getUser().getLogin(), Sort.by("filename"));
    }

    @Transactional
    public void putFile(String filename, String newFilename) {
        FileStorageData fileStorageData = fileStorageRepository.findByFilenameAndUser(filename, getUser());

        fileStorageData.setFilename(newFilename);

        fileStorageRepository.saveAndFlush(fileStorageData);

        storageService.renameFile(getFilePath(filename), newFilename);
    }

    // Userful Methods
    private FileStorageData getFileStorageData(String filename, MultipartFile file) {
        return new FileStorageData(filename, getFilePath(filename).toAbsolutePath().toString(),
                (int) file.getSize(), getUser());
    }

    private UserEntity getUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    private Path getFilePath(String filename) {
        return rootLocation.resolve(getUser().getLogin()).resolve(filename);
    }

    private Path getUserPath() {
        return rootLocation.resolve(getUser().getLogin());
    }
}
