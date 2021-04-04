package ru.netology.cloud_storage.service.app;

import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.exceptions.Errors;
import ru.netology.cloud_storage.properties.StorageProperties;
import ru.netology.cloud_storage.exceptions.StorageException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class StorageService {
    private final Path rootLocation;

    @Autowired
    public StorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getRootLocation());
    }

    // CREATING ROOT DIRECTORY - ONCE ON START
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException(Errors.COULD_NOT_INITIALIZE_STORAGE_LOCATION.value(), e);
        }
    }

    public void store(MultipartFile file, String filename, Path filepath) throws StorageException {
        try {
            Files.write(filepath, file.getBytes());
        } catch (IOException e) {
            throw new StorageException(Errors.FAILED_TO_STORE_FILE.value() + filename
                    + " " + e.getMessage(), e);
        }
    }

    public void deleteFile(Path filepath) {
        try {
            Files.deleteIfExists(filepath);
        } catch (IOException e) {
            throw new StorageException(Errors.FAILED_TO_DELETE_FILE.value() + filepath.getFileName()
                    + " " + e.getMessage(), e);
        }
    }

    public void renameFile(Path filepath, String newFilename) {
        try {
            Files.move(filepath, filepath.resolveSibling(newFilename));
        } catch (IOException e) {
            throw new StorageException(Errors.FAILED_TO_RENAME_FILE.value() + filepath.getFileName()
                    + " " + e.getMessage(), e);
        }
    }

    public Stream<Path> loadAll(Path searchPath) {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> path.equals(searchPath))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException(Errors.FAILED_TO_READ_STORED_FILES.value() + e.getMessage(), e);
        }
    }

    public Resource loadAsResource(String filename, Path filepath) {
        try {
            Resource resource = new UrlResource(filepath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(Errors.COULD_NOT_LOAD_FILE.value() + filename
                + Errors.FILE_NOT_READABLE.value());
            }

        } catch (MalformedURLException e) {
            throw new StorageException(Errors.COULD_NOT_LOAD_FILE.value() + filename
                    + " " + e.getMessage(), e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
