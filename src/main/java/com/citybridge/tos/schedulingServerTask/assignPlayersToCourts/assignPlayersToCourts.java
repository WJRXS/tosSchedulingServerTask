package com.citybridge.tos.schedulingServerTask.assignPlayersToCourts;

import org.springframework.stereotype.Service;


/**
 *  --- 1B -------------
 *  check for surplus and sexes.
 *
 *  divide into playerlist & trash list.
 *
 *  next try to convert playerlist into assigned player, and use trash list for leftover fixes.
 *
 *  last:
 *  make assigned playerlist solid, and turn trash list into bench list.
 *
 *  next create the assignedplayer list.
 */

@Service
public class assignPlayersToCourts {




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
