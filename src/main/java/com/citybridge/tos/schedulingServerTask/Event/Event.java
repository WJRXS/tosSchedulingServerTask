package com.citybridge.tos.schedulingServerTask.Event;

import java.time.LocalDateTime;

public class Event {

    private Long eventId;
    private LocalDateTime startDateTime;
    private int nrOfCycles;
    private Long cycleDurationMinutes;
    private Long announceTime;

    public Event() {
    }

    public Event(Long eventId, LocalDateTime startDateTime, int nrOfCycles, Long cycleDurationMinutes, Long announceTime) {
        this.eventId = eventId;
        this.startDateTime = startDateTime;
        this.nrOfCycles = nrOfCycles;
        this.cycleDurationMinutes = cycleDurationMinutes;
        this.announceTime = announceTime;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getNrOfCycles() {
        return nrOfCycles;
    }

    public void setNrOfCycles(int nrOfCycles) {
        this.nrOfCycles = nrOfCycles;
    }

    public Long getCycleDurationMinutes() {
        return cycleDurationMinutes;
    }

    public void setCycleDurationMinutes(Long cycleDurationMinutes) {
        this.cycleDurationMinutes = cycleDurationMinutes;
    }

    public Long getAnnounceTime() {
              return this.announceTime;

    }

    public void setAnnounceTime(Long announceTime) {
        this.announceTime = announceTime;
    }
}
