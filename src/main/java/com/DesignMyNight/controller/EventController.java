package com.DesignMyNight.controller;

import com.DesignMyNight.entities.Event;
import com.DesignMyNight.entities.Ticket;
import com.DesignMyNight.repository.EventRepository;
import com.DesignMyNight.repository.TicketRepository;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController


@RequestMapping("/api/search")
public class EventController {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public EventController(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> searchAllEventsByRegionAndAllDates(@RequestParam("region") String region,
                                                                          @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                          @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
                                                                          @RequestParam(value = "sortBy", defaultValue = "eventName", required = false) String sortBy,
                                                                          @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        List<Event> events;
        try {
            events = eventRepository.searchAllEventsByRegion(region, pageable);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Load ticket details for each event
        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEventId(event.getId());
            event.setTickets(tickets);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/today")
    public ResponseEntity<List<Event>> searchEventsByTodayAndRegion(@RequestParam("region") String region) {
        List<Event> events;
        try {
            events = eventRepository.searchEventsByCurrentDateAndRegion(region);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Load ticket details for each event
        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEventId(event.getId());
            event.setTickets(tickets);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/tomorrow")
    public ResponseEntity<List<Event>> searchEventsByTomorrowAndRegion(@RequestParam("region") String region) {
        // Get tomorrow's date
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);

        List<Event> events;
        try {
            events = eventRepository.searchEventsByTomorrowAndRegion(region);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Load ticket details for each event
        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEventId(event.getId());
            event.setTickets(tickets);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/weekend")
    public ResponseEntity<List<Event>> searchEventsByThisWeekendAndRegion(@RequestParam("region") String region) {
        LocalDate currentDate = LocalDate.now();
        LocalDate nextSaturday = currentDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        LocalDate nextSunday = currentDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        Date startDate = Date.valueOf(nextSaturday);
        Date endDate = Date.valueOf(nextSunday);
        List<Event> events;
        try {
            events = eventRepository.searchEventsByDatesAndRegion(region, startDate, endDate);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Load ticket details for each event
        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEventId(event.getId());
            event.setTickets(tickets);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/next7days")
    public ResponseEntity<List<Event>> searchEventsForNext7DaysAndRegion(@RequestParam("region") String region) {
        List<Event> events;
        try {
            events = eventRepository.searchEventsForNext7DaysAndRegion(region);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Load ticket details for each event
        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEventId(event.getId());
            event.setTickets(tickets);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/date")
    public ResponseEntity<List<Event>> searchEventsByDateAndRegion(
            @RequestParam("region") String region,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Date searchDate = Date.valueOf(date);
        List<Event> events;
        try {
            events = eventRepository.searchEventsByDateAndRegion(searchDate, region);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Load ticket details for each event
        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEventId(event.getId());
            event.setTickets(tickets);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/city")
    public ResponseEntity<List<Event>> searchEventsByRegionOrCity(
            @RequestParam("search") String search,
            @RequestParam("region") String region) {
        List<Event> events;
        try {
            events = eventRepository.findByCityTownAndRegion(search, region);
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Load ticket details for each event
        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEventId(event.getId());
            event.setTickets(tickets);
        }
        return ResponseEntity.ok(events);
    }
}


