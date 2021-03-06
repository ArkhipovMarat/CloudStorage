package ru.netology.cloud_storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users", indexes = {@Index(name = "SEC_INDEX", columnList = "login")})

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    private String password;

    @OneToOne()
    private Role role;

    public UserEntity(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
