package ru.netology.cloud_storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.dto.ResponseError;
import ru.netology.cloud_storage.model.FileStorageData;
import ru.netology.cloud_storage.exceptions.StorageException;
import ru.netology.cloud_storage.service.app.FileService;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;
import java.io.IOException;


@Validated
@RestController
public class FileStorageController {
    private static final String FILE = "/file";
    private static final String LIST = "/list";
    private static final String FILE_NAME = "filename";
    private static final String PART_NAME = "file";

    private final FileService fileService;

    @Autowired
    public FileStorageController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(FILE)
    public ResponseEntity<?> postFile(@RequestParam(FILE_NAME) String filename,
                                      @RequestPart(PART_NAME) @NotNull MultipartFile file) {
        fileService.postFile(filename, file);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(FILE)
    public ResponseEntity<?> deleteFile(@RequestParam(FILE_NAME) String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = FILE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> getFile(@RequestParam(FILE_NAME) String filename) throws IOException {
        Resource resource = fileService.getFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentLength(resource.getFile().length())
                .body(resource);
    }

    @RequestMapping(value = FILE, method = RequestMethod.PUT)
    public ResponseEntity<?> putFile(@RequestParam(FILE_NAME) String filename,
                                     @RequestBody FileStorageData newFilename) {
        fileService.putFile(filename, newFilename.getFilename());
        return ResponseEntity.ok().body(null);
    }

    @GetMapping(LIST)
    public ResponseEntity<?> getList() {
        return ResponseEntity.ok().body(fileService.getList());
    }



    // handle 400 error
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ResponseError> handleStorageExceptions(StorageException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(e.getMessage(), 400));
    }

    // handle internal server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleInternalServerErrors(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage(), 500));
    }
}
