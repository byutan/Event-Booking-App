package jp.co.frux.crud.eventbookingapp.repository;

import jp.co.frux.crud.eventbookingapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
