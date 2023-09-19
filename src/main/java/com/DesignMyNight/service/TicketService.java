package com.DesignMyNight.service;

import com.DesignMyNight.entities.Ticket;
import com.DesignMyNight.payload.TicketDto;

public interface TicketService {
    TicketDto addTicketWithPriceId(TicketDto ticketDto, Long eventId);
    TicketDto updateTicketPriceId(Long ticketId, TicketDto ticketDto);

    Ticket getTicketById(long ticketId);
}
