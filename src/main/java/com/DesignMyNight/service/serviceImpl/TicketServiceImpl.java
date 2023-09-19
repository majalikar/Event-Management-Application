package com.DesignMyNight.service.serviceImpl;

import com.DesignMyNight.entities.Event;
import com.DesignMyNight.entities.Ticket;
import com.DesignMyNight.payload.TicketDto;
import com.DesignMyNight.repository.EventRepository;
import com.DesignMyNight.repository.TicketRepository;
import com.DesignMyNight.service.TicketService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TicketDto addTicketWithPriceId(TicketDto ticketDto, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        ticket.setEvent(event);

        String priceId = generatePriceId(ticket);
        ticket.setPriceId(priceId);

        Ticket savedTicket = ticketRepository.save(ticket);
        return modelMapper.map(savedTicket, TicketDto.class);
    }

    @Override
    public TicketDto updateTicketPriceId(Long ticketId, TicketDto updatedTicketDto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID"));

        // Map the updatedTicketDto to the existing ticket object using ModelMapper
        modelMapper.map(updatedTicketDto, ticket);

        String priceId = generatePriceId(ticket);
        ticket.setPriceId(priceId);

        Ticket updatedTicket = ticketRepository.save(ticket);
        return modelMapper.map(updatedTicket, TicketDto.class);
    }

    @Override
    public Ticket getTicketById(long ticketId) {
        Optional<Ticket> byId = ticketRepository.findById(ticketId);
        return byId.get();
    }

    private String generatePriceId(Ticket ticket) {
        try {
            Stripe.apiKey = "sk_test_51NQmnOBsBLtmyBceeYGPml4VYNKRbMns3V4aY2nvi137ssyr9yFzsoByV2sqobKHelmO2GyDFfkLeddGgPrWNZI7003yiPgNXf";

            PriceCreateParams priceParams = PriceCreateParams.builder()
                    .setCurrency("usd")
                    .setUnitAmount((long) (ticket.getPrice() * 100))
                    .setProductData(
                            PriceCreateParams.ProductData.builder()
                                    .setName(ticket.getTicketName())
                                    .build()
                    )
                    .build();

            Price price = Price.create(priceParams);
            return price.getId();
        } catch (StripeException e) {
            // Handle any exceptions from the Stripe API request
            e.printStackTrace();
            return null;
        }
    }
}



