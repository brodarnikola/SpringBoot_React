package com.example.polls.ai_example.data;

import java.util.ArrayList;
import java.util.List;

public class BookingData {

    private List<CustomerFlight> customers = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();


    public List<CustomerFlight> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerFlight> customers) {
        this.customers = customers;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
