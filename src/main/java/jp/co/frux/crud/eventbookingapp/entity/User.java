package jp.co.frux.crud.eventbookingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role")
    private String role = "USER";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @OneToOne(mappedBy = "user")
    private Reminder reminder;

    public User(String fullName, String email, String password, String avatar, String role, LocalDateTime createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.role = role;
        this.createdAt = createdAt;
    }
}
