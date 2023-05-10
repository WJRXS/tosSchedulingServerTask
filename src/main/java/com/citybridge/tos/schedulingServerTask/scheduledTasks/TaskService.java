package com.citybridge.tos.schedulingServerTask.scheduledTasks;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private List<Long> dummyEvents = List.of(1L);

    public void Task() {
        System.out.println("Klok: " + dateFormat.format(new Date()));
        /**
         * Check for events that start in the next hour.
         * && AND that are not activated.
         */

        /**
         * TRY execute method if that is the case
         * set event to activated.
         */

        /**
         * create the method:
         * - schedule new tasks, that have to be executed 10 minutes before each round.
         *
         * for that you need these variables:
         * 1) start time event
         * 2) round duration
         * 3) number of rounds
         */
    }


        // checks events in the tos data base and returns a list of eventId of all the events that are about to start.
        public List<Long> findEvents() {
            return dummyEvents;
        }

        // creates a list of event data that have to start.




    }


