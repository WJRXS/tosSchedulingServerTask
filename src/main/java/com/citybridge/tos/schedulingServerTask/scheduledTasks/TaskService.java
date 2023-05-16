package com.citybridge.tos.schedulingServerTask.scheduledTasks;

import com.citybridge.tos.schedulingServerTask.event.EventActivation;
import com.citybridge.tos.schedulingServerTask.matchRound.MatchRoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    private final EventActivation eventActivation;
    private final MatchRoundService matchRoundService;

    @Autowired
    public TaskService(EventActivation eventActivation, MatchRoundService matchmaker) {
        this.eventActivation = eventActivation;
        this.matchRoundService = matchmaker;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 1) any [EVENT] is about to start  // Check for events that start in the next hour.
     * --YES?---> has to be activated.
     * 2) any [MATCHROUND] is about to start and the [MATCH] & [BENCH] has to be created.
     * <p>
     * && #TODO AND events that are not alrdy activated.
     * && #TODO AND matchrounds that are not alrdy planned.
     * If any, activate them.
     */
    public void task() {
        System.out.println("Klok: " + dateFormat.format(new Date()));

        eventActivationTask();
        matchMakerTask();

    }

    /**
     * Find and Activate events
     */
    private void eventActivationTask() {
        List<Long> eventIdList = new ArrayList<>();

        /**
         * Find events.
         */
        try {
            eventIdList = findEvents();
        } catch (Exception exception) {
        }

        /**
         * Activate events.
         */
        if (!eventIdList.isEmpty()) {
            eventActivation.activateEventList(eventIdList);
        }
    }


    /**
     * checks events in the tos data base and returns a list of eventId of all the events that are about to start.
     * #TODO @GETMAPPING
     */
    private List<Long> findEvents() {
        List<Long> dummyEventList = List.of(1L);
        return dummyEventList;
    }


    private void matchMakerTask() {
        List<Long> matchRoundList = new ArrayList<>();

        /**
         * Find matchRounds.
         */
        try {
            matchRoundList = findMatchRounds();
        } catch (Exception exception) {
        }

        /**
         * create Matches for the MatchRound.
         */
        if (!matchRoundList.isEmpty()) {
            matchRoundService.activateMatchRound(matchRoundList);
        }
    }

    /**
     * checks matchrounds in the tos data base and returns a list of matchroundId of all the matchRounds that are about to start.
     * #TODO @GETMAPPING
     */
    private List<Long> findMatchRounds() {
        List<Long> dummyMatchRoundList = List.of(1L);
        return dummyMatchRoundList;
    }


}

