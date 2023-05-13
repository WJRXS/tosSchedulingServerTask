package com.citybridge.tos.schedulingServerTask.Event;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventActivation {

 //   private final ScheduleMatchRound scheduleMatchRound;

//    @Autowired
  //  public EventActivation(ScheduleMatchRound scheduleMatchRound) {
  //      this.scheduleMatchRound = scheduleMatchRound;
//    }

    private Event dummyEvent = new Event(1L, LocalDateTime.of(2023, 5, 10, 19, 30), 4, 30L);

    /**
     * activate event method
     *
     * schedule extra tasks and set event to activated.
     */
    public void activateEventList(List<Long> eventIdList) {
             for (Long eventId: eventIdList) {
            activateEvent(eventId);
        }
 }




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
        return dummyEvent;
    }

    /**
     * Method contacts DATABASE with eventId, and modifies database; active YES
     * @param event
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



    private void sheduleMatchMaker(Event event) {
        /**
         * #TODO create announce timer in database
         */
        long eventAnnounceTime = 5;
        Long eventId = event.getEventId();
        int nrOfCycles = event.getNrOfCycles();
        LocalDateTime startDateTime = event.getStartDateTime();
        Long cycleDurationMinutes = event.getCycleDurationMinutes();


        /**
         * for each round, schedule a match maker.
         * matchCreatTIme is a few minutes (eventAnnounceTime) before the actual start of the match round.
         */
        for (int i=1; i <= nrOfCycles; i++) {
           LocalDateTime matchCreateTime = startDateTime.plusMinutes(i*cycleDurationMinutes - eventAnnounceTime);
        int roundNr = i;
   //     scheduleMatchRound.scheduleMatchRound(eventId, matchCreateTime, roundNr);
   }


    }

    private void notifyClients(Long eventId) {

    }


}