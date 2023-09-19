package com.DesignMyNight.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    private String region;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "building_name")
    private String buildingName;

    private String street;

    @Column(name = "city_town")
    private String cityTown;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "event_description")
    private String eventDescription;

    @Column(name = "food_served")
    private boolean foodServed;

    @Column(name = "menu")
    private String menu;

    @Column(name = "event_types")
    private String eventTypes;

    @Column(name = "music_types")
    private String musicTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
}




