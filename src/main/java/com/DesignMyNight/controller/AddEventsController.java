package com.DesignMyNight.controller;

import com.DesignMyNight.payload.EventDTO;
import com.DesignMyNight.payload.EventWithTicketsDTO;
import com.DesignMyNight.payload.TicketDto;
import com.DesignMyNight.service.EventService;
import com.DesignMyNight.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class AddEventsController {

    private final EventService eventService;
    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    public AddEventsController(EventService eventService, TicketService ticketService, ModelMapper modelMapper) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add/event-with-tickets")
    public ResponseEntity<String> addEventWithTickets(@RequestBody EventWithTicketsDTO eventWithTicketsDTO) {
        EventDTO eventDTO = eventWithTicketsDTO.getEvent();
        List<TicketDto> ticketDTOs = eventWithTicketsDTO.getTickets();

        if (ticketDTOs == null || ticketDTOs.isEmpty()) {
            return ResponseEntity.badRequest().body("At least one ticket must be provided");
        }

        EventDTO savedEvent = eventService.addEvent(eventDTO);

        for (TicketDto ticketDTO : ticketDTOs) {
            ticketService.addTicketWithPriceId(ticketDTO, savedEvent.getId());
        }

        return ResponseEntity.ok("Event and tickets added successfully");
    }
    @PutMapping("/{ticketId}/update-ticket")
    public ResponseEntity<TicketDto> updateTicketPriceId(
            @PathVariable Long ticketId,
            @RequestBody TicketDto updatedTicketDto) {
        TicketDto updatedTicket = ticketService.updateTicketPriceId(ticketId, updatedTicketDto);
        return ResponseEntity.ok(updatedTicket);
    }
}

