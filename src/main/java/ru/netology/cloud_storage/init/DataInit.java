package ru.netology.cloud_storage.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.netology.cloud_storage.entity.pdo.Role;
import ru.netology.cloud_storage.entity.pdo.UserPDO;
import ru.netology.cloud_storage.repository.FileStorageRepository;
import ru.netology.cloud_storage.repository.RoleRepository;
import ru.netology.cloud_storage.repository.UserRepository;

@Component
public class DataInit implements ApplicationRunner {
    private final UserRepository userRepository;
    private final FileStorageRepository fileStorageRepository;
    private final RoleRepository roleRepository;

    private static final String  ROLE_USER = "ROLE_USER";
    private static final String  ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public DataInit(UserRepository userRepository, FileStorageRepository fileStorageRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role roleAdmin = new Role(1L, ROLE_ADMIN);
        Role roleUser = new Role(2L, ROLE_USER);

        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);

        UserPDO user1 = new UserPDO();
        user1.setLogin("user1");
        user1.setPassword(bcryptEncoder.encode("password1"));
        user1.setRole(roleAdmin);

        UserPDO user2 = new UserPDO();
        user2.setLogin("user2");
        user2.setPassword(bcryptEncoder.encode("password2"));
        user2.setRole(roleAdmin);

        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.findAll().forEach(System.out::println);
    }
}
