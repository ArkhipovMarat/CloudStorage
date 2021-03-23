package ru.netology.cloud_storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.entity.dto.Response;
import ru.netology.cloud_storage.service.app.FileService;

@RestController
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<Response> postFile(@RequestParam("filename") String filename, MultipartFile file) {
        System.out.println("in postFile");
//        TODO: logic
//        fileService.postFile(filename, file);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/file")
    public ResponseEntity<Response> deleteFile() {
        System.out.println("deleteFile");
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/file")
    public ResponseEntity<Response> getFile() {
        System.out.println("getFile");
        return ResponseEntity.ok().body(null);
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<Response> putFile() {
        System.out.println("putFile");
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("list")
    public ResponseEntity<Response> getList() {
        System.out.println("getList");
        return ResponseEntity.ok().body(null);
    }
}
