package com.citybridge.tos.schedulingServerTask.MatchRound;

import com.citybridge.tos.schedulingServerTask.Event.Event;
import com.citybridge.tos.schedulingServerTask.match.MatchMaker;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchRound {

    private final MatchMaker matchMaker;

    // autowired bench / court / match ??
    @Autowired
    public MatchRound(MatchMaker matchMaker) {
        this.matchMaker = matchMaker;
    }



    public void createMatchRoundList(List<Long> matchRoundList) {

             for (Long matchRoundId: matchRoundList) {
             createMatchRound(matchRoundId);
            }
    }

    /**
     * Collects all variables needed to call apon MatchMaker
     * @param matchRoundId
     */
    private void createMatchRound(Long matchRoundId) {



        // the event contains the match variable settings,
        // get eventId from DATABASE [MATCHROUNDS]
        // get event from DATABASE [EVENT]
        Event event = getEvent(getEventId(matchRoundId));


        List<Long> courtList;  // the available courts where apon players can be assigned, get from DATABASE [COURTSAVAILABLE]


        List<Player> playerList;  // the players who have signed up for the event. get from DATABASE [SIGNUP]

        matchMaker.createMatch(event, courtList, playerList);
    }


    /**
     * Method conacts DATABASE [MATCHROUNDS] with matchroundId, and retreives eventId.
     * #TODO GETMAPPING
     */
    private Long getEventId(Long matchRoundId) {
    Long dummyEventId = 1L;
    return dummyEventId;
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





}
