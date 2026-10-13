package jp.co.frux.crud.eventbookingapp.service.impl;

import jp.co.frux.crud.eventbookingapp.dto.PaymentRequest;
import jp.co.frux.crud.eventbookingapp.dto.PaymentResponse;
import jp.co.frux.crud.eventbookingapp.entity.Booking;
import jp.co.frux.crud.eventbookingapp.entity.Payment;
import jp.co.frux.crud.eventbookingapp.entity.Ticket;
import jp.co.frux.crud.eventbookingapp.exception.BusinessValidationException;
import jp.co.frux.crud.eventbookingapp.repository.BookingRepository;
import jp.co.frux.crud.eventbookingapp.repository.PaymentRepository;
import jp.co.frux.crud.eventbookingapp.repository.TicketRepository;
import jp.co.frux.crud.eventbookingapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;

    @Override
    public PaymentResponse<PaymentResponse.PaymentData> createPayment(PaymentRequest paymentRequest) {
        int bookingId = paymentRequest.getBookingId();

        String message = "Booking not found or already paid";
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new BusinessValidationException(message, HttpStatus.NOT_FOUND);
        } else if (bookingRepository.findById(bookingId).get().getStatus().equals("PAID")) {
            throw new BusinessValidationException(message, HttpStatus.CONFLICT);
        }

        Booking booking = bookingRepository.findById(bookingId).get();
        Double amount = booking.getTotalPrice();
        String status = "PAID";
        LocalDateTime createdAt = LocalDateTime.now();
        Payment payment = new Payment(amount, status, createdAt, booking);

        Payment savedPayment = paymentRepository.save(payment);
        booking.setStatus(status);
        booking.setPayment(payment);
        bookingRepository.save(booking);

        int ticketNum = booking.getQuantity();
        List<Ticket> listTicket = new ArrayList<>();
        for(int i = 0; i < ticketNum; i++) {
            String ticketStatus = "PAID";
            String ticketCode;
            do {
                ticketCode = "EVT" + "-" + booking.getId() + "-" + generateRandomCode();
            } while(ticketRepository.existsByTicketCode(ticketCode));
            Ticket newTicket = new Ticket(ticketCode, ticketStatus, createdAt, booking);
            ticketRepository.save(newTicket);
            listTicket.add(newTicket);
        }
        booking.setTicket(listTicket);

        boolean isSuccess = true;
        String returnMessage = "Payment successful";

        return PaymentResponse.<PaymentResponse.PaymentData>builder()
                .success(isSuccess)
                .message(returnMessage)
                .data(new PaymentResponse.PaymentData(
                        savedPayment.getId(),
                        savedPayment.getBooking().getId(),
                        savedPayment.getAmount(),
                        savedPayment.getStatus()
                ))
                .build();
    }

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            code.append(CHARACTERS.charAt(
                    random.nextInt(CHARACTERS.length())
            ));
        }

        return code.toString();
    }
}
