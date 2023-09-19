package com.DesignMyNight.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    private Long id;
    private String ticketName;
    private String description;
    private int totalQuantity;
    private double price;
    private String startTime;
    private String endTime;
    private Long eventId;
}

