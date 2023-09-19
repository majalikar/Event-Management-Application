package com.DesignMyNight.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private Long id;
    private String eventName;
    private String region;
    private String venueName;
    private String buildingName;
    private String street;
    private String cityTown;
    private String postalCode;
    private Date eventDate;
    private String startTime;
    private String endTime;
    private String eventDescription;
    private boolean foodServed;
    private String menu;
    private List<String> eventTypes;
    private List<String> musicTypes;
    private List<TicketDto> tickets;
}

