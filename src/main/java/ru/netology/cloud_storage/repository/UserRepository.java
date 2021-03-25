package ru.netology.cloud_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_storage.entity.pdo.UserPDO;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserPDO, Long> {
    Optional<UserPDO> findByLogin(String login);
}
