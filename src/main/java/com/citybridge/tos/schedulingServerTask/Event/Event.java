package com.citybridge.tos.schedulingServerTask.Event;

import java.time.LocalDateTime;

public class Event {

    private Long eventId;
    private LocalDateTime startDateTime;
    private int nrOfCycles;
    private Long cycleDurationMinutes;
}
