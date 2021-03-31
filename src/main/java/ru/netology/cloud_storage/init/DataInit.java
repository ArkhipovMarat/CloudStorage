package ru.netology.cloud_storage.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.netology.cloud_storage.entity.Role;
import ru.netology.cloud_storage.entity.UserEntity;
import ru.netology.cloud_storage.exceptions.Errors;
import ru.netology.cloud_storage.properties.StorageProperties;
import ru.netology.cloud_storage.repository.FileStorageRepository;
import ru.netology.cloud_storage.repository.RoleRepository;
import ru.netology.cloud_storage.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DataInit implements ApplicationRunner {
    // temporary users init data
    private static final String USER1LOGIN = "user1";
    private static final String USER1PASSWORD = "password1";
    private static final String USER2LOGIN = "user2";
    private static final String USER2PASSWORD = "password2";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Path rootLocation;

    private static final String  ROLE_USER = "ROLE_USER";
    private static final String  ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public DataInit(UserRepository userRepository,
                    RoleRepository roleRepository, StorageProperties storageProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rootLocation = Paths.get(storageProperties.getRootLocation());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role roleAdmin = new Role(1L, ROLE_ADMIN);
        Role roleUser = new Role(2L, ROLE_USER);

        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);

        UserEntity user1 = new UserEntity();
        user1.setLogin(USER1LOGIN);
        user1.setPassword(bcryptEncoder.encode(USER1PASSWORD));
        user1.setRole(roleAdmin);

        UserEntity user2 = new UserEntity();
        user2.setLogin(USER2LOGIN);
        user2.setPassword(bcryptEncoder.encode(USER2PASSWORD));
        user2.setRole(roleAdmin);

        userRepository.save(user1);
        userRepository.save(user2);

        // creating user directories
        userRepository.findAll().forEach(userPDO -> {
            try {
                Files.createDirectories(rootLocation.resolve(userPDO.getLogin()));
            } catch (IOException e) {
                throw new RuntimeException(Errors.COULD_NOT_CREATE_USER_DIRECTORIES.getDescription() + e.getMessage());
            }
            System.out.println(userPDO);
        });
    }
}
