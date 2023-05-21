package com.citybridge.tos.schedulingServerTask.matchRound;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.MatchMaker;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchRoundService {

    private final MatchMaker matchMaker;

    // autowired bench / court / match ??
    @Autowired
    public MatchRoundService(MatchMaker matchMaker) {
        this.matchMaker = matchMaker;
    }

    public void activateMatchRound(List<Long> matchRoundList) {
             for (Long matchRoundId: matchRoundList) {
             createMatches(matchRoundId);
            }
    }

    /**
     * Collects all variables needed to call apon MatchMaker
     * @param matchRoundId
     */
    private void createMatches(Long matchRoundId) {

    // get matchRound from DATABASE [MATCHROUND]
    MatchRound matchRound = getMatchGroundFromMATCHROUND(matchRoundId);
    Long eventId = matchRound.getEventId();
    int roundNr = matchRound.getRoundNr();

        // the event contains the match variable settings,
        // get event from DATABASE [EVENT]
        Event event = getEventFromEVENT(eventId);

        // the available courts where apon players can be assigned, get from DATABASE [COURTSAVAILABLE]
        List<Long> courtList = getAvailableCourtList(eventId, roundNr);

        // the playerId who have signed up for the event. get from DATABASE [SIGNUP]
        // get players from DATABASE [PLAYER]
        List<Player> playerList = getSigneddUpPlayersList(eventId);


        // call matchmaker to create the matches
        matchMaker.createMatch(event, courtList, playerList);
    }





    /**
     * Method conacts DATABASE [MATCHROUND] with matchroundId, and retreives MatchRound.
     * #TODO GETMAPPING
     */
    private MatchRound getMatchGroundFromMATCHROUND(Long matchRoundId) {
    Long dummy = matchRoundId;
    MatchRound dummyMatchRound = new MatchRound(dummy, 1L, 1, LocalDateTime.of(2023, 11, 11, 19, 00));
    return dummyMatchRound;
    }


    /**
     * Method conacts DATABSE [EVENT] with eventId, and retreives event data.
     * @param eventId
     * @return
     * #TODO GETMAPPING
     */
    private Event getEventFromEVENT(Long eventId) {
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
     * Method conacts DATABASE[COURTSAVAILABLE] with eventId && roundNr and retreives the available courts.
     *
     * #TODO GETMAPPING
     */
private List<Long> getAvailableCourtList(Long eventId, int roundNr) {

    Long dummyEventId = eventId;
    int dummyRoundNr = roundNr;
    // list consists out of CourtId's , the courts available for the round.
  List<Long> dummyCourtList = List.of(1L, 2L);

  return dummyCourtList;
}



private List<Player> getSigneddUpPlayersList(Long eventId) {
    Long dummyEventId = eventId;
    boolean signedUp = true;

    List<Long> playerIdList = getSignedUpPlayerIdList(dummyEventId);

    List<Player> playerList = new ArrayList<>();

    for(Long playerId: playerIdList) {
        playerList.add(getPlayer(playerId));
    }
    return playerList;
}


    /**
     * Method conacts DATABASE[SIGNUP] with eventId and retreives a list of playerId's.
     * #TODO GETMAPPING
     */
    private List<Long> getSignedUpPlayerIdList(Long eventId) {
        Long dummyEventId = eventId;

       List<Long> playerIdList = List.of(1L, 2L);
       return playerIdList;
    }


    /**
     * Method conacts DATABASE[PLAYER] with playerId and retreives player
     * #TODO GETMAPPING
     */
private Player getPlayer(Long playerId) {
    Long dummyPlayerId = playerId;
    Player player = new Player(1L, 8.0, false);
    return player;
 }

}
