package ru.netology.cloud_storage.entity.pdo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Filestorage")
public class FileStorageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String filepath;

    @OneToOne
    private UserPDO user;

    public FileStorageData(String filename, String filepath, UserPDO user) {
        this.filename = filename;
        this.filepath = filepath;
        this.user = user;
    }
}
