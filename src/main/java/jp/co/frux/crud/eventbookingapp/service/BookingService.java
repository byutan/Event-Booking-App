package jp.co.frux.crud.eventbookingapp.service;

import jp.co.frux.crud.eventbookingapp.dto.BookingRequest;
import jp.co.frux.crud.eventbookingapp.dto.BookingResponse;


public interface BookingService {
    BookingResponse<BookingResponse.bookingData> createBooking(BookingRequest bookingRequest);
}
