package com.DesignMyNight.repository;

import com.DesignMyNight.entities.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.region = :region")
    List<Event> searchAllEventsByRegion(@Param("region") String region, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.region = :region AND DATE(e.eventDate) = CURRENT_DATE")
    List<Event> searchEventsByCurrentDateAndRegion(@Param("region") String region);

    @Query("SELECT e FROM Event e WHERE e.region = :region AND DATE(e.eventDate) = CURRENT_DATE + 1")
    List<Event> searchEventsByTomorrowAndRegion(@Param("region") String region);

    @Query("SELECT e FROM Event e WHERE e.region = :region AND DATE(e.eventDate) BETWEEN :startDate AND :endDate")
    List<Event> searchEventsByDatesAndRegion(@Param("region") String region, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT e FROM Event e WHERE e.region = :region AND DATE(e.eventDate) BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")
    List<Event> searchEventsForNext7DaysAndRegion(@Param("region") String region);

    @Query("SELECT e FROM Event e WHERE e.region = :region AND DATE(e.eventDate) = :searchDate")
    List<Event> searchEventsByDateAndRegion(@Param("searchDate") Date searchDate, @Param("region") String region);

    @Query("SELECT e FROM Event e WHERE e.region = :region AND e.cityTown = :cityTown")
    List<Event> findByCityTownAndRegion(String cityTown, String region);
}



