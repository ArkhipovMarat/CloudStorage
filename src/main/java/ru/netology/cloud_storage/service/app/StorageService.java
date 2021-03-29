package ru.netology.cloud_storage.service.app;

import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.properties.StorageProperties;
import ru.netology.cloud_storage.exceptions.StorageException;

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

    public void store(MultipartFile file, String filename, Path filepath) throws StorageException {
        try {
            Files.write(filepath, file.getBytes());
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    public void deleteFile(Path filepath) {
        try {
            Files.deleteIfExists(filepath);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file " + filepath.getFileName(), e);
        }
    }

    public void renameFile(Path filepath, String newFilename) {
        try {
            Files.move(filepath, filepath.resolveSibling(newFilename));
        } catch (IOException e) {
            throw new StorageException("Failed to rename file " + filepath.getFileName(), e);
        }
    }

    public Stream<Path> loadAll(Path searchPath) {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> path.equals(searchPath))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    public Resource loadAsResource(String filename, Path filepath) {
        try {
            Resource resource = new UrlResource(filepath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not load file: " + filename);
            }

        } catch (MalformedURLException e) {
            throw new StorageException("Could not load file: " + filename, e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
