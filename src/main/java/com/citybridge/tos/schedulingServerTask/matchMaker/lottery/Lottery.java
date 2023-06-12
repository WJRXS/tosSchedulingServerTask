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








}
