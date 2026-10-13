package jp.co.frux.crud.eventbookingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "ticket_code", unique = true, nullable = false)
    private String ticketCode;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public Ticket(String ticketCode, String status, LocalDateTime time, Booking booking) {
        this.ticketCode = ticketCode;
        this.status = status;
        this.time = time;
        this.booking = booking;
    }
}
