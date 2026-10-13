package jp.co.frux.crud.eventbookingapp.service;

import jakarta.validation.Valid;
import jp.co.frux.crud.eventbookingapp.dto.PaymentRequest;
import jp.co.frux.crud.eventbookingapp.dto.PaymentResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentService {
    PaymentResponse<PaymentResponse.PaymentData> createPayment(@Valid @RequestBody PaymentRequest paymentRequest);
}
