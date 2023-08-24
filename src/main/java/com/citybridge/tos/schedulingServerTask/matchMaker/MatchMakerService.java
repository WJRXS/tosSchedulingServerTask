package com.citybridge.tos.schedulingServerTask.matchMaker;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignPlayersToCourts;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.benchUpdate.BenchUpdate;
import com.citybridge.tos.schedulingServerTask.matchMaker.lottery.Lottery;
import com.citybridge.tos.schedulingServerTask.player.Player;
import com.citybridge.tos.schedulingServerTask.matchMaker.playerListChecker.PlayerListChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchMakerService {

    private final Lottery lottery;
    private final PlayerListChecker playerListChecker;
    private final AssignPlayersToCourts assignPlayersToCourts;
    private final BenchUpdate benchUpdate;

    @Autowired
    public MatchMakerService(Lottery lottery, PlayerListChecker playerListChecker, AssignPlayersToCourts assignPlayersToCourts, BenchUpdate benchUpdate) {
        this.lottery = lottery;
        this.playerListChecker = playerListChecker;
        this.assignPlayersToCourts = assignPlayersToCourts;
        this.benchUpdate = benchUpdate;
    }


    /** Each tennis tos event will constitute out of multiple rounds wherein
     * players play tennis matches with and against other players. For each round a new
     * schedule will be made. During the event, payers will interact with many different players on different courts.
     * To increase the chance on a positive experience, many factors need to be taken into account:
     * A player needs to:
     * - face similar strength opponents.
     * - play a fair amount of matches.
     * - occasionally play with/against friends if so desired.
     *
     * A player should not:
     * - frequently face weak opponents.
     * - be benched alot more than other players.
     *
     * The balance of all these factors is what makes or breaks an event.
     * Hence it will take alot of code. On top of that, the rare circumstances,
     * especially the makeup of the players, expand the code rapidly.
     *
     * first of all what kind of TOS?
     * The event could host only singles, or only male doubles and females doubles.
     * Only setting for now, will be Double Matches:
     * The emphasis will lay on both mixed gender and same gender double matches.
     */
    public List<AssignedPlayer> getAssignedPlayerList(Event event, List<Court> courtIdList, List<Player> playerList) {

        /** ---- 1A----- Lottery
         * Load event settings & give all players an integer lottery Roll.
         */

        lottery.addRoll(playerList, event);

        /**
         *  --- 1B -------------
         *  Check if
         * - The number of players reach maximum capacity of tennis courts
         * - divide players among a benched list & a playing list
         * - Adjustments will be made for gender distribution among lists
         * whilst taking into account the lottery scores players have been given.
         *  check for surplus and and gender balance.
         */

        List<Player> playerBinList = playerListChecker.getPlayerBinList(playerList, courtIdList.size());


        /**
         *  ------ 1C --------
         * Adjust player settings
         * Fill the database with data about player activity: Playing or Benched, so that information
         * can be taken into account for the next round.
         *
         * Set playerlist (now effectively assigned players to a match) status: not benched.
         * Set binlist players status: benched.
         */

        benchUpdate.updateBenchPlay(playerList, playerBinList);


        /**
         * 2A
         * Assign the players to
         * - a specific court.
         * - a position (and thus partner and opponent(s) )
         * This list determines the matches in a round in an event.
         *
        *  convert (playerlist) & (playerBinList) --into-> (assignedPlayerList)
        *
        */
        List<AssignedPlayer> assignedPlayerList = assignPlayersToCourts.getAssignedPlayersToCourtsList(event, courtIdList, playerList, playerBinList);

         /**
         * 2B
         * return the (assignedPlayersList)
         *
         */


        return assignedPlayerList;
    }

}
