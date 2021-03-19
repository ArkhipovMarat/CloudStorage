package ru.netology.cloud_storage.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.netology.cloud_storage.dto.FileData;
import ru.netology.cloud_storage.exceptions.UserNotFoundException;
import ru.netology.cloud_storage.pdo.FileStorageData;
import ru.netology.cloud_storage.pdo.User;
import ru.netology.cloud_storage.repository.FileStorageRepository;
import ru.netology.cloud_storage.repository.UsersRepository;

@Service
public class StorageService {
    private final FileStorageRepository fileStorageRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public StorageService(FileStorageRepository fileStorageRepository, UsersRepository usersRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.usersRepository = usersRepository;
    }

    public void store(String filename, FileData fileData) throws UserNotFoundException {

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = usersRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));

        //  копируем файл куда-нибудь в каталог userfiles на сервере?
        String filepath = copyFile(filename, fileData);

        //  храним путь к файлу, привязывая его к конкретному user в БД?
        fileStorageRepository.saveAndFlush(new FileStorageData(filename, filepath, user));
    }

    //    TODO
    public String copyFile(String filename, FileData fileData) {
    //    Здесь нужно реализовать какую рандомную систему папок для каждого сохраняемого файла?
        return "PATH";
    }

    // TODO: реализовать по аналогии с методом store
    public void store2(String filename, byte[] fileData) {
    }
}
