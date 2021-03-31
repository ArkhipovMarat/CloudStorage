package ru.netology.cloud_storage.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_storage.entity.FileStorageData;
import ru.netology.cloud_storage.entity.UserEntity;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorageData,Long> {
    List<FileStorageData> findAllByUser_Login(String login, Sort sort);

    void removeFileDataByFilename(String fileName);

    FileStorageData findByFilenameAndUser(String filename, UserEntity userEntity);
}
