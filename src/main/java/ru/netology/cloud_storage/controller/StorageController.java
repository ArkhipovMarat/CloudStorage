package ru.netology.cloud_storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.entity.dto.ErrorInputData;
import ru.netology.cloud_storage.entity.dto.FileData;
import ru.netology.cloud_storage.exceptions.UserNotFoundException;
import ru.netology.cloud_storage.service.app.StorageService;

import java.io.IOException;

@RestController
public class StorageController {
    private StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /*
     пока пробую реализовать только данный метод

     вопросы:

     1. как извлекать файл передаваемый в requestBody? -
     (попробую сделать это двумя способами - не знаю верно ли)

     2. Согласно спецификации файл приходит в requestBody обернутый в объект
     с полями String hash, byte[] file -> соответственно в первом способе
     spring сам распарсит содержимое body в объект.

     3. Пока только очень приблизительно постмотрел,
     что можно использовать MultipartFile.
     Если верен второй способ, нужно ли как то настраивать дополнительно MultipartResolver?
     И оборачивать содержимое body в объект?
*/


//    ПЕРВЫЙ СПОСОБ
//    @PostMapping("/file")
//    public ResponseEntity<ErrorInputData> postFile1(@RequestParam String filename, @RequestBody FileData fileData) {
//        try {
//            storageService.store(filename, fileData);
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorInputData("Users not found", 500));
//        }
//        return ResponseEntity.ok().body(null);
//    }

//    ВТОРОЙ СПОСОБ
    @PostMapping("/file")
    public ResponseEntity<ErrorInputData> postFile2(@RequestParam String filename, @RequestBody MultipartFile multipartFile) throws IOException {
        // получаем файл
        byte[] fileData = multipartFile.getBytes();

        // передаем в storageService
        try {
            storageService.store2(filename, fileData);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorInputData("Users not found", 500));
        }
        return ResponseEntity.ok().body(null);
    }


//    TODO: other endpoints

    @DeleteMapping("/file")
    public ResponseEntity<ErrorInputData> deleteFile() {
        System.out.println("deleteFile");
        return null;
    }

    @GetMapping("/file")
    public ResponseEntity<ErrorInputData> getFile() {
        System.out.println("getFile");
        return null;
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<ErrorInputData> putFile() {
        System.out.println("putFile");
        return null;
    }

    @GetMapping("list")
    public ResponseEntity<ErrorInputData> getList() {
        System.out.println("getList");
        return null;
    }
}
