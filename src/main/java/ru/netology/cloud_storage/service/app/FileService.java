package ru.netology.cloud_storage.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    private StorageService storageService;

    @Autowired
    public FileService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void postFile(String filename, MultipartFile file) {

        storageService.store(file);
    }
}
