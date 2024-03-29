package com.citybridge.tos.schedulingServerTask.matchMaker;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchMaker {

    private final MatchMakerService matchMakerService;

    @Autowired
    public MatchMaker(MatchMakerService matchMakerService) {
        this.matchMakerService = matchMakerService;
    }


    public void createMatch(Event event, List<Court> courtList, List<Player> playerList) {
// #TODO



        /** ---- 2 -----
         * Puts all these variables into the matchMakerService
         * that returns:
         * players assigned to courts
         * players assigned to bench
         * List [playerId][CourtId][Position][Roll]  CourtId -1 = benched    Position: 1234 (13vs24)
         * List [Long][Long][int][int]
         * returns List<AssignedPlayer>
         */
        List<AssignedPlayer> assignedPlayerList = matchMakerService.getAssignedPlayerList(event, courtList, playerList);



        /** ----- 3 -----
         * #TODO
         * takes List<AssignedPlayer> and fills the database with
         * match'es: players assigned to courts  [(matchId) eventId courtId roundNr StartTime playerId1 playerId2 playerId3 playerId4]
         * Bench'es : players assigned to bench [(benchId) eventId roundNr playerId]
         * assignedPlayertoMatch: [(apmId) matchId playerId]
         *
         * @param eventId
         */



        /** ----- 4 -----
         * #TODO
         * notifies clients of the created match round
         *
         * @param eventId
         */

    }
}
