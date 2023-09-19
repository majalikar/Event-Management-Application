package com.DesignMyNight.repository;

import com.DesignMyNight.entities.Event;
import com.DesignMyNight.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventId(Long eventId);
    Ticket findByPriceId(String priceId);
}




