package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.leftOverMatch;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.leftOverMatch.mexicanMatch.MexicanMatchCreator;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.leftOverMatch.singleMatch.SingleMatchCreator;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.leftOverMatch.unbalancedMatch.UnbalancedMatchCreator;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeftOverMatchCreator {

    private final SingleMatchCreator singleMatchCreator;
    private final MexicanMatchCreator mexicanMatchCreator;
    private final UnbalancedMatchCreator unbalancedMatchCreator;


    @Autowired
    public LeftOverMatchCreator(SingleMatchCreator singleMatchCreator,
                                 MexicanMatchCreator mexicanMatchCreator,
                                 UnbalancedMatchCreator unbalancedMatchCreator) {
        this.singleMatchCreator = singleMatchCreator;
        this.mexicanMatchCreator = mexicanMatchCreator;
        this.unbalancedMatchCreator = unbalancedMatchCreator;
    }



    /**
     * --------------------   CREATE COURT LIST --------------
     * #TODO only tosType setting = DOUBLE
     * @param event
     *
     *
     * Step by Step by Step plan given variables:
     *  number of Players (dont need nr of courts, you know that when you look at nr players)
     *
     *  CHeck0: LEFTOVER MATCH
     *  - Single / Mexican
     *  - assign players
     *  (add assigned players, add assigned courts?)
     *  (remove from lists: court id, playerid )
     *
     *
     *
     *
     *  Check0_1: is there going to be a SINGLE match?
     *  if [single court] go through list who wants to play single. -> make list ->
     *  highest gets +if friends who wants single +else similar strength.
     *  if none found --> bottom roll gets single. get sex. get partner = same sex &
     *  similarstrength and semi-low roll.
     *
     *  Check0_2: is there going to be a MEXICAN match?
     *
     *
     *  Check3: Mix or same Sex Matchsettings [Mix]-[Same Sex] % get lowest number
     *  from list - woman or men. max mix courts = number /2 number of mix courts =
     *  max number of mix courts * % same sex courts = nrofcourts - mix courts
     *  courtlist[] = add same sex - add mix --add same sex -- add mix --add same sex
     *  --->untill both are at zero.
     *
     *  check 3 first court same sex: Highest roll & wants to do court type: Does
     *  player have mutual friends. add highest roll friend as opponent or partner.
     *
     *  No mutual friends: friends - strength %roll
     */

    public void createLeftOverMatch(Event event,
                                     List<Player> playerList,
                                     List<AssignedPlayer> assignedPlayersToCourtsList,
                                     List<Court> courtList,
                                     List<Player> playerBinList) {

        int nrOfPlayers = playerList.size();
        int leftover = nrOfPlayers%4;


        if(leftover == 1) {
            //throw exception
        } else if(leftover == 2) {
            // SINGLE
            singleMatchCreator.createSingleMatch(playerList, assignedPlayersToCourtsList, courtList);

        } else if (leftover == 3) {
            // MEXICAN
            mexicanMatchCreator.createMexicanMatch(playerList, assignedPlayersToCourtsList, courtList);
        } else if (playerBinList.size() == 0 && (leftover == 0 | leftover == 4)) {
            // UNBALANCED DOUBLE (FMMM) or (MFFF), when bench = zero.
            unbalancedMatchCreator.createUnbalancedMatch(playerList, assignedPlayersToCourtsList, courtList);
        } else {
            // throw exception
        }
    }



}
