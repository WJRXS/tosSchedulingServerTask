package com.citybridge.tos.schedulingServerTask.Event;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventActivation {




    /**
     * activate all the event in the eventIdList
     *
     *
     */
    public void activateEventList(List<Long> eventIdList) {
             for (Long eventId: eventIdList) {
            activateEvent(eventId);
        }
 }


    /**
     * activate event
     * create match rounds in database for the event.
     * set event to activated.
     * @param eventId
     */
    private void activateEvent(Long eventId) {
        Event event = getEvent(eventId);

        setEventToActive(eventId);
        createMatchRound(event);

     }






    /**
     * Method conacts database with eventId, and retreives event data.
     * @param eventId
     * @return
     * #TODO GETMAPPING
     */
    private Event getEvent(Long eventId) {
        Long dummy = eventId;
        Event dummyEvent = new Event(
                1L,
                LocalDateTime.of(2023, 5, 10, 19, 30),
                4,
                30L,
                5L
        );

        return dummyEvent;
    }

    /**
     * Method contacts DATABASE with eventId, and modifies database; active YES
     * @param eventId
     * #TODO TRANSACTIONAL method.
     */
    private void setEventToActive(Long eventId) {
        Long dummyLong =eventId;
        boolean dummyBoolean = true;
    }

    /**
     * Method contacts DATABASE and adds matchRounds to [MATCH_ROUND] table
     * @param event
     * #TODO PUTMAPPING method
     */
    private void createMatchRound(Event event) {
        Long eventId = event.getEventId();
        LocalDateTime startDateTime = event.getStartDateTime();
        int nrOfCycles = event.getNrOfCycles();
        Long cycleDurationMinutes = event.getCycleDurationMinutes();
        Long announceTimeMinutes = event.getAnnounceTime();

        for(int i=1; i <= nrOfCycles; i++) {

            LocalDateTime createTime = startDateTime.plusMinutes((i*cycleDurationMinutes) - announceTimeMinutes);
            addMatchRound(eventId, i, createTime);
        }

    }

    /**
     * Method contacts DATABASE and adds matchRounds to [MATCH_ROUND] table
     *
     * #TODO PUTMAPPING method
     */
    private void addMatchRound(Long eventId, int roundNr, LocalDateTime createTime) {

    }

    /**
     * #TODO contacts all players who are signed up for the event.
     * @param eventId
     */
    private void notifyClients(Long eventId) {

    }


}
