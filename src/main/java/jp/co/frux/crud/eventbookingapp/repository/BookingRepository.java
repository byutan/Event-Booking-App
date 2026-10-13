package jp.co.frux.crud.eventbookingapp.repository;

import jp.co.frux.crud.eventbookingapp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
