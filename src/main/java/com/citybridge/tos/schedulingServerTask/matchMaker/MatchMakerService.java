package com.citybridge.tos.schedulingServerTask.matchMaker;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignPlayersToCourts;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.lottery.Lottery;
import com.citybridge.tos.schedulingServerTask.player.Player;
import com.citybridge.tos.schedulingServerTask.player.PlayerListChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchMakerService {

    private final Lottery lottery;
    private final PlayerListChecker playerListChecker;
    private final AssignPlayersToCourts assignPlayersToCourts;

    @Autowired
    public MatchMakerService(Lottery lottery, PlayerListChecker playerListChecker, AssignPlayersToCourts assignPlayersToCourts) {
        this.lottery = lottery;
        this.playerListChecker = playerListChecker;
        this.assignPlayersToCourts = assignPlayersToCourts;
    }


    /**
     * Split the signed up players into
     * - player who are playing.
     * - players who are benched.
     *
     * Many factors have to be considered.
     * first of all what kind of TOS?
     * Only setting for now, will be [Double Matches, both mixed sex and same sex].
     *
     * Action I:
     * Load event settings
     *
     * Action II:
     * all players get an integer lottery Roll.
     *
     * Action III:
     * a) fill all available spots with the highest rolls.
     * (assignedPlayerList)
     * b) if leftover, then they go into the (playerBinList).
     *
     * Check1:
     * 1A) courts => players?
     * Create Matches, Fill courts with double, until 3 (mexican),  2 (single), 1 (bench) players are left.
     * leftovers sex is not considered.
     *
     * 1B) courts x 4 < players?
     * there will be a bench.
     * When there are more players then courts available, low roll players will be
     * benched. Allowances are made so abit higher rolls might be benched for the
     * greater cause: male / female distribution.
     *
     * Check sex distribution.
     *
     *      * female/male divisions: The Double matches have priority, and thus if needed,
     *      * the single will consist out of a man and a female: BattleOfTheSexes.
     *      *
     *      * odd total players. If there is 1 female/male leftover, the lowest sex roll
     *      * will be benched in processing.
     *      *
     *      * If there are 3 leftovers, there is no bench , to fill a 3 player Mexican to a
     *      * 4man double. and thus a 3 man match will be assigned to the single court.
     */
    public List<AssignedPlayer> getAssignedPlayerList(Event event, List<Long> courtIdList, List<Player> playerList) {

        /** ---- 1A----- Lottery
         * add roll to players
         * sort list comparable to roll.
         */

        lottery.addRoll(playerList, event);

        /**
         *  --- 1B -------------
         *  check for surplus and sexes.
         *   divide into playerlist & playerBinList.
         */

        List<Player> playerBinList = playerListChecker.checkForBadConfiguration(playerList, courtIdList.size());

         /**
         *
         * 2A
         *  next try to convert (playerlist) & (playerBinList) --into-> (assignedPlayerList)
         *
          *  players assigned to courts
          *  players assigned to bench
          *  List [playerId][CourtId][Position][Roll]  CourtId -1 = benched    Position: 1234 (13vs24)
          *  List [Long][Long][int][int]
          *  ********  class List [AssignedPlayer]??   *****************
          *
          *
          *  2B
         * Adjust player settings
         * Set assigned players status: not benched.
         * Set binlist players status: benched.
         *
         *
         * 2C
         * return the (assignedPlayersList)
         *
         */

        List<AssignedPlayer> assignedPlayerList = assignPlayersToCourts.getAssignedPlayersToCourtsList();
        return assignedPlayerList;
    }

}
