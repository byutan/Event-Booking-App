package jp.co.frux.crud.eventbookingapp.controller;

import jakarta.validation.Valid;
import jp.co.frux.crud.eventbookingapp.dto.PaymentRequest;
import jp.co.frux.crud.eventbookingapp.dto.PaymentResponse;
import jp.co.frux.crud.eventbookingapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PaymentResponse<PaymentResponse.PaymentData>> createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse<PaymentResponse.PaymentData> res = paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
