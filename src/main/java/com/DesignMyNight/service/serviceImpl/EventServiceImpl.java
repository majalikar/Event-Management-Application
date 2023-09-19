package com.DesignMyNight.service.serviceImpl;

import com.DesignMyNight.entities.Event;
import com.DesignMyNight.payload.EventDTO;
import com.DesignMyNight.repository.EventRepository;
import com.DesignMyNight.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventServiceImpl(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EventDTO addEvent(EventDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        event.setEventTypes(String.join(",", eventDTO.getEventTypes()));
        event.setMusicTypes(String.join(",", eventDTO.getMusicTypes()));
        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDTO.class);
    }
}

