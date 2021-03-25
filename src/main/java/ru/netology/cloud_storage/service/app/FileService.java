package ru.netology.cloud_storage.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.entity.pdo.FileStorageData;
import ru.netology.cloud_storage.entity.pdo.UserPDO;
import ru.netology.cloud_storage.entity.properties.StorageProperties;
import ru.netology.cloud_storage.exceptions.StorageException;
import ru.netology.cloud_storage.exceptions.UserNotFoundException;
import ru.netology.cloud_storage.repository.FileStorageRepository;
import ru.netology.cloud_storage.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Transactional
    public void postFile(MultipartFile file) {
        FileStorageData fileStorageData = getFileStorageData(file);

        fileStorageRepository.saveAndFlush(fileStorageData);

        storageService.store(file, fileStorageData.getFilename(), Path.of(fileStorageData.getFilepath()));
    }

    @Transactional
    public void deleteFile(String filename) {
        fileStorageRepository.removeFileDataByFilename(filename);

        storageService.deleteFile(getFilePath(filename));
    }


    // Userful Methods

    public FileStorageData getFileStorageData(MultipartFile file) {
        String filename = file.getOriginalFilename();
        UserPDO user = getUser();
        Path filepath = getFilePath(filename);

        return new FileStorageData(filename, filepath.toAbsolutePath().toString(), user);
    }

    public UserPDO getUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    public Path getFilePath(String filename) {
        return rootLocation.resolve(getUser().getLogin()).resolve(filename);
    }

    public Path getUserPath() {
        return rootLocation.resolve(getUser().getLogin());
    }
}
