package jp.co.frux.crud.eventbookingapp.repository;

import jp.co.frux.crud.eventbookingapp.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    boolean existsByTicketCode(String ticketCode);
}
