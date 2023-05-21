package com.citybridge.tos.schedulingServerTask.event;

import java.time.LocalDateTime;

public class Event {

    private Long eventId;
    private LocalDateTime startDateTime;
    private int nrOfCycles;
    private Long cycleDurationMinutes;
    private Long announceTime;

    // Lottery Settings:
    private int benchCompensation = 1000;
    private int newPlayersPlayImmediately = 0;  // = 2000 when turned on.
    private int leftoverMatchCompensation = 200; // can be set from 0-1000 as if benched.

    public int getBenchCompensation() {
            return benchCompensation;
        }

        public void setBenchCompensation(int benchCompensation) {
        // #todo control 0-1000
            this.benchCompensation = benchCompensation;
        }

        public int getNewPlayersPlayImmediately() {

            return newPlayersPlayImmediately;
        }

        public void setNewPlayersPlayImmediately(int newPlayersPlayImmediately) {
            // #todo control 0 OR 2000
            this.newPlayersPlayImmediately = newPlayersPlayImmediately;
        }

        public int getLeftoverMatchCompensation() {
            return leftoverMatchCompensation;
    }

    public void setLeftoverMatchCompensation(int leftoverMatchCompensation) {
        // #todo control 0-1000
        this.leftoverMatchCompensation = leftoverMatchCompensation;
    }

    public TosType getTosType() {
        return tosType;
    }

    public void setTosType(TosType tosType) {
        this.tosType = tosType;
    }

    public int getMixVersusStraight() {
        return mixVersusStraight;
    }

    public void setMixVersusStraight(int mixVersusStraight) {
        this.mixVersusStraight = mixVersusStraight;
    }

    // Type of TOS setting
    private TosType tosType = TosType.DOUBLE;  // focus on double.
    private int mixVersusStraight = 0; // 0 most mix matches, 100 for most straight matches.


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
