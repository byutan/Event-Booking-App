package jp.co.frux.crud.eventbookingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "quantity", nullable = false)
    int quantity;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToOne(mappedBy = "booking")
    private Payment payment;

    @OneToMany(mappedBy = "booking")
    private List<Ticket> ticket;

    public Booking(int quantity,
                   Double totalPrice,
                   String status,
                   LocalDateTime createdAt,
                   User user,
                   Event event) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.user = user;
        this.event = event;
        this.ticket = new ArrayList<>();
    }
}
