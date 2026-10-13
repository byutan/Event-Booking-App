package jp.co.frux.crud.eventbookingapp.repository;

import jp.co.frux.crud.eventbookingapp.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT e FROM Event e WHERE e.dateTime >= CURRENT_TIMESTAMP ORDER BY e.dateTime ASC")
    List<Event> findUpcomingEvents(Pageable pageable);
    @Query("SELECT e FROM Event e WHERE " +
            "(:search IS NULL OR :search = '' OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Event> findAllEvents(@Param("search") String search, Pageable pageable);
    @Query("SELECT e FROM Event e " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(e.latitude)) * " +
            "cos(radians(e.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(e.latitude)))) ASC")
    List<Event> findNearbyEvents(@Param("lat") Double lat, @Param("lng") Double lng, Pageable pageable);
    @Query("SELECT e FROM Event e LEFT JOIN e.bookings b GROUP BY e.id ORDER BY COUNT(b) DESC")
    List<Event> findPopularEvents(Pageable pageable);
}
