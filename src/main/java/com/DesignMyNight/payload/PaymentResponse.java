package com.DesignMyNight.payload;

import com.DesignMyNight.entities.Event;
import com.DesignMyNight.entities.Ticket;
import java.util.List;

public class PaymentResponse {
    private String status;
    private Event event;
    private Ticket tickets;

    public PaymentResponse() {
    }

    public PaymentResponse(String status, Event event, Ticket tickets) {
        this.status = status;
        this.event = event;
        this.tickets = tickets;
    }

    // Getters and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Ticket getTickets() {
        return tickets;
    }

    public void setTickets(Ticket tickets) {
        this.tickets = tickets;
    }
}

