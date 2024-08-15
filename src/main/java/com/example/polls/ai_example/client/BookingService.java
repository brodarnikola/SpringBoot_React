package com.example.polls.ai_example.client;

import com.example.polls.ai_example.services.BookingTools;
import com.example.polls.ai_example.services.FlightBookingService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class BookingService {
    private final FlightBookingService flightBookingService;

    public BookingService(FlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }

    public List<BookingTools.BookingDetails> getBookings() {
        return flightBookingService.getBookings();
    }
}
