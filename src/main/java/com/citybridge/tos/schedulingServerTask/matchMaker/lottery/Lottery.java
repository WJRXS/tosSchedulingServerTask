package com.citybridge.tos.schedulingServerTask.matchMaker.lottery;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/** ---- 1A----- Lottery
 * Load event settings & give all players an integer lottery Roll.
 * This roll determines:
 * 1) who plays and who is benched.
 * 2) Players with High rolls have a better chance to play
 * - their preferred match type
 * - on their preferred courts
 * - with their friends.
 *
 * The roll is modified when:
 * - Bench: The Player's last active round had status "benched". With this compensation the player has no significant chance
 * to be benched twice in a row.
 * - New Players:
 * To keep a fresh dynamic and stimulate "late comers" rather then "no shows": and organiser can enhance the chance that players arriving
 * at the club will immediately play.
 * - leftover match
 * Some players get bad experience from playing a non double match. This can be avoided when mostly volunteers play these matches.
 * The players who play these undesired matches get a reward; they are less likely to be benched in next round.
 */


@Service
public class Lottery {

        public void addRoll(List<Player> playerList, Event event) {
               // Lottery Settings from event set by organiser:
               final int benchCompensation = event.getBenchCompensation();
               final int newPlayersPlayImmediately = event.getNewPlayersPlayImmediately();
               final int leftoverMatchCompensation = event.getLeftoverMatchCompensation();

               for (Player player: playerList) {
                  int roll = (int)(1+Math.random()*1000);
                  if(player.isBenched()) { roll += benchCompensation;}
                  if(player.isHasNotPlayedAMatchThisTos()){ roll += newPlayersPlayImmediately;}
                  if(player.isHasPlayedLeftOverMatch()) { roll += leftoverMatchCompensation; }
                  player.setRoll(roll);
               }

               Collections.sort(playerList, new Comparator<Player>() {
                       @Override
                       public int compare(Player p1, Player p2) {
                               return p1.getRoll() > p2.getRoll() ? -1 : p1.getRoll() == p2.getRoll() ? 0 : 1;
                       }
               });
        }
}
