package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.event.TosType;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatchListCreator;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.DoubleMatchCreator;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.leftOverMatch.LeftOverMatchCreator;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;




@Service
public class AssignPlayersToCourts {

    private final LeftOverMatchCreator leftOverMatchCreator;
    private final TypeOfMatchListCreator typeOfMatchListCreator;
    private final DoubleMatchCreator doubleMatchCreator;


    @Autowired
    public AssignPlayersToCourts(LeftOverMatchCreator leftOverMatchCreator,
                                TypeOfMatchListCreator typeOfMatchListCreator,
                                 DoubleMatchCreator doubleMatchCreator                                ) {
        this.leftOverMatchCreator = leftOverMatchCreator;
        this.typeOfMatchListCreator = typeOfMatchListCreator;
        this.doubleMatchCreator = doubleMatchCreator;

    }


    /**
     *2A
     * Assign the players to
     * - a specific court.
     * - and a position (and thus partner and opponent(s) )
     * This list determines the matches in a round in an event.
     * convert (playerlist) & (playerBinList) --into-> (assignedPlayerList)
     */

    public List<AssignedPlayer> getAssignedPlayersToCourtsList(Event event, List<Court> courtIdList, List<Player> playerList, List<Player> playerBinList) {
        List<AssignedPlayer> assignedPlayersToCourtsList = new ArrayList<AssignedPlayer>();

        /**
         * #TODO implement different tostypes, like all singles, or all of another specific match type.
         * EVENT: Type of TOS setting
         */
        TosType tosType = event.getTosType();
        //if (tosType == TosType.DOUBLE) {}

        /** Action 1:
         * Check if a leftover match is needed.
         */
        leftOverMatchCreator.createLeftOverMatch(event, playerList, assignedPlayersToCourtsList, courtIdList, playerBinList);


        /**
         * Action 2:
         * Load event settings: get the Mix % from event
         * determine: the number of Mix Doubles, and thus, the number Male doubles and Female Doubles.
         * Make a list of the MatchTypes.
         */
        List<TypeOfMatch> typeOfMatchList = typeOfMatchListCreator.createTypeOfMatchList(event, playerList);

        /**
         * Action 3:
         * use the list of matches to create the double matches.
         */
        doubleMatchCreator.createDoubleMatch(assignedPlayersToCourtsList, typeOfMatchList, courtIdList, playerList, event);

        /**
         * Action 4:
         * return the assigned player list.
         */
        return assignedPlayersToCourtsList;
        }

}

