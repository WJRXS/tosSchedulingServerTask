package com.citybridge.tos.schedulingServerTask.match;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchMaker {


    public void createMatch(Event event, List<Long> courtIdList, List<Player> playerList) {
// #TODO

        /** ---- 2 -----
         * Puts all these variables into the matchmaker
         * that returns:
         * players assigned to courts
         * players assigned to bench
         * List [playerId][CourtId][Position][Roll]  CourtId -1 = benched    Position: 1234 (13vs24)
         */
        //     List<Player> assignedPlayerList = matchRound.executeMatchMaker(eventVariables, playerList, courtList);



        /** ----- 3 -----
         * Fill the database with
         * match'es: players assigned to courts  [(matchId) eventId roundNr playerId1 playerId2 playerId3 playerId4]
         * Bench'es : players assigned to bench [(benchId) eventId roundNr playerId]
         *
         * @param eventId
         */



        /** ----- 4 -----
         * notifies clients of the created match round
         *
         * @param eventId
         */

    }
}
