package jp.co.frux.crud.eventbookingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    int id;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "role")
    String role = "USER";

    @Column(name = "created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    public User(String fullName, String email, String password, String avatar, String role, LocalDateTime createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.role = role;
        this.createdAt = createdAt;
    }
}
