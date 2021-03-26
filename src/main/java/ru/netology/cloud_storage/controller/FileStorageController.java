package ru.netology.cloud_storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.entity.pdo.FileStorageData;
import ru.netology.cloud_storage.service.app.FileService;

import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;


@Validated
@RestController
public class FileStorageController {
    private final FileService fileService;

    @Autowired
    public FileStorageController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<?> postFile(@RequestParam String filename, @RequestPart("file") @NotNull MultipartFile file) {
        fileService.postFile(filename, file);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> getFile(@RequestParam("filename") String filename) {
        return ResponseEntity.ok(fileService.getFile(filename));
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<?> putFile(@RequestParam("filename") String filename,
                                     @RequestBody FileStorageData newFilename) {
        fileService.putFile(filename, newFilename.getFilename());
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("list")
    public ResponseEntity<?> getList() {
        return ResponseEntity.ok(fileService.getList());
    }
}
