package com.citybridge.tos.schedulingServerTask.match;

import com.citybridge.tos.schedulingServerTask.MatchRound.MatchRound;
import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Match {

     private final MatchRound matchRound;

     @Autowired
     public Match(MatchRound matchmaker) {
         this.matchRound = matchmaker;
     }









    public void createMatch(Long eventId, int roundNr) {

        /**  ---- 1 ----
         * Collects all variables needed from the database.
         * ---event settings
         * ---players & variables
         * ---courts & variables
         * @param eventId roundNr
         */
        List<Integer> eventVariables = new ArrayList<>();
        eventVariables = collectEventVariables(eventId);
        int nr = 1;

        List<Player> playerList = new ArrayList<>();
        playerList = collectPlayers(eventId);

        List<Court> courtList = new ArrayList<>();
        courtList = collectCourts(eventId, nr);

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
         * match'es: players assigned to courts
         * Bench'es : players assigned to bench
         *
         * @param eventId
         */



        /** ----- 4 -----
         * notifies clients of the created match round
         *
         * @param eventId
         */



    }


    /**
     * #TODO: collect from Event DATABASE
     * @return
     */
    private List<Integer> collectEventVariables(Long eventId) {
           return List.of(1);
    }

    /**
     * #TODO collect from Signup DATABASE
     * @return
     */
    private List<Player> collectPlayers(Long eventId) {
        return List.of(new Player());
    }

    /**
     * #TODO collect from CourtsAvailable DATABASE
     * @return
     */
    private List<Court> collectCourts(Long eventId, int roundNr) {
        return List.of(new Court());
    }




}
