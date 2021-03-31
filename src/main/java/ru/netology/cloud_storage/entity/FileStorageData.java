package ru.netology.cloud_storage.entity;

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
    // front properties to use fileStorageData as dto object -> front must to rename "name" to "filename"
    private static final String SIZE= "size";
    private static final String FILENAME = "name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    // front property
    @JsonProperty(FILENAME)
    private String filename;

    @JsonIgnore
    private String filepath;

    // front property
    @JsonProperty(SIZE)
    private Long size;

    @JsonIgnore
    @OneToOne
    private UserEntity user;

    public FileStorageData(String filename, String filepath, Long size, UserEntity user) {
        this.filename = filename;
        this.filepath = filepath;
        this.size = size;
        this.user = user;
    }
}
