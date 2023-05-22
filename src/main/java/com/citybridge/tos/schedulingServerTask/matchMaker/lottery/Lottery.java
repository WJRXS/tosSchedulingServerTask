package com.citybridge.tos.schedulingServerTask.matchMaker.lottery;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/** ---- 1A----- Lottery
 * add roll to players
 * sort list comparable to roll.
 */


@Service
public class Lottery {


        // player list
        // input: singUpList<Players>
        // action:  roll. & add event factors
        // action: list<AssignedPlayers> sorted by roll
        // return list  --- no need.

        public void addRoll(List<Player> playerList, Event event) {
               // Lottery Settings:
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
     * b) if leftover, then they go into the (reservePlayerList).
     *
     * Check1:
     * 1A) courts => players?
     * Create Matches, Fill courts with double, until 3 (mexican),  2 (single), 1 (bench) players are left.
     * leftovers sex is not considered.
     *
     * 1B) courts < players?
     * there will be a bench.
     * When there are more players then courts available, low roll players will be
     * benched. Allowances are made so abit higher rolls might be benched for the
     * greater cause: male / female distribution.
     *
     * Check sex distribution.
     *
     *
     *      * female/male divisions: The Double matches have priority, and thus if needed,
     *      * the single will consist out of a man and a female: BattleOfTheSexes.
     *      *
     *      * odd total players. If there is 1 female/male leftover, the lowest sex roll
     *      * will be benched in processing.
     *      *
     *      * If there are 3 leftovers, there is no bench , to fill a 3 player Mexican to a
     *      * 4man double. and thus a 3 man match will be assigned to the single court.
     *
     *
     */





}
