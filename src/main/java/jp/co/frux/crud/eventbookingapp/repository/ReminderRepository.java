package jp.co.frux.crud.eventbookingapp.repository;

import jp.co.frux.crud.eventbookingapp.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
    Optional<Reminder> findByUserId(Integer userId);
}
