package com.DesignMyNight.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventWithTicketsDTO {
    private EventDTO event;
    private List<TicketDto> tickets;
}

