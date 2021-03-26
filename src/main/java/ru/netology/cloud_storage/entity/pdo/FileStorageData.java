package ru.netology.cloud_storage.entity.pdo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private Long id;

    @JsonProperty("name")
    private String filename;

    @JsonIgnore
    private String filepath;

    @JsonProperty("size")
    private Integer size;

    @JsonIgnore
    @OneToOne
    private UserPDO user;

    public FileStorageData(String filename, String filepath, Integer size,UserPDO user) {
        this.filename = filename;
        this.filepath = filepath;
        this.size = size;
        this.user = user;
    }
}
