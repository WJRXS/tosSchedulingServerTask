package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleMix;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DoubleMixMatch {

    public boolean createDoubleMixMatch(List<Player> playerList,
                                        List<AssignedPlayer> assignedPlayersToCourtsList,
                                        boolean isMatchTypeLast,
                                        Event event){
        if (!isMatchTypeLast) {
            createDoubleMixMatchPlenty(playerList, assignedPlayersToCourtsList, event);
        }
        else {
            createDoubleMixMatchLast(playerList, assignedPlayersToCourtsList, event);
        }
       return true;
    }


    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param event
     * @return
     */
    private boolean createDoubleMixMatchPlenty(List<Player> playerList,
                                               List<AssignedPlayer> assignedPlayersToCourtsList,
                                               Event event) {

        int index = assignedPlayersToCourtsList.size();
        AssignedPlayer player1 = assignedPlayersToCourtsList.get(index);
        boolean isSexMaleP1 = player1.getSexMale();
        double strengthDifference = event.getStrengthDifference();

        // Partner request event setting.
        boolean partnerConnection = false; // event.isPartnerConnection(); #TODO implement later
        if (partnerConnection) {
            if (findPartnerPlayer3()) {
                findOpponentPlayer2();
                findOpponentPlayer4();
            } else {
                findPlayer234(isSexMaleP1, playerList, assignedPlayersToCourtsList, strengthDifference );
            }
        } else {
            findPlayer234(isSexMaleP1, playerList, assignedPlayersToCourtsList, strengthDifference );

        }
        return true;
    }

    /**
     * #TODO implement partner connection
     */
    private boolean findPartnerPlayer3() {
        return false;
    }

    /**
     * #TODO implement partner connection
     */
    private void findOpponentPlayer2() {

    }

    /**
     * #TODO implement partner connection
     */
    private void findOpponentPlayer4() {

    }

    private void findPlayer234(Boolean isSexMaleP1,
                               List<Player> playerList,
                               List<AssignedPlayer> assignedPlayersToCourtsList,
                               double strengthDifference) {

        int index = assignedPlayersToCourtsList.size() -1;
        Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
        Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
        Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
        TypeOfMatch typeOfMatch = TypeOfMatch.DOUBLE_MIX;

        /**
         * Player 2, same sex
         */
        findPlayer(isSexMaleP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 2, strengthDifference, player1Strength);

        int index2 = assignedPlayersToCourtsList.size() -1;
        Double player2Strength = assignedPlayersToCourtsList.get(index2).getStrength();
        Double average3Strength = (player1Strength + player2Strength) /2;

        /**
         * Player 3, opposite sex
         */
        findPlayer(!isSexMaleP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 3, strengthDifference, average3Strength);

        int index3 = assignedPlayersToCourtsList.size() -1;
        Double player3Strength = assignedPlayersToCourtsList.get(index3).getStrength();
        Double average4Strength = player1Strength + player3Strength - player2Strength;

        /**
         * Player 4, same sex
         */
        findPlayer(isSexMaleP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 4, strengthDifference, average4Strength);
    }


    private void findPlayer(Boolean pSex,
                             List<Player> playerList,
                             List<AssignedPlayer> assignedPlayersToCourtsList,
                             Long player1Id,
                             Long courtId,
                             int position,
                             Double strengthDifference,
                             Double averageStrength) {


        List<Player> playerXList = new ArrayList<>();
        /**
         * make list of potentials. sort on strength. and next pick the closest strength.
         * ADD any friend
         * ADD close in strength
         */

        outerloop: for(Player pX:playerList){
            // ADD any friend
            if(pX.getFriendIdList().contains(player1Id) && (pX.isMaleSex() == pSex)) {
                playerXList.add(pX);
            } else if(pX.getPlayerStrength() <= (averageStrength +strengthDifference) && pX.getPlayerStrength() >= (averageStrength -strengthDifference)&& (pX.isMaleSex() == pSex)) {
                playerXList.add(pX);
            }
        }

        // Sort list. last = smallest strength difference.
        if (playerXList.size() > 0) {

            Collections.sort(playerXList, new Comparator<Player>() {
                @Override
                public int compare(Player pA, Player pB) {
                    return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                }
            });

            int index = playerXList.size() -1;


            assignMixPlayer(playerXList.get(index), courtId, position, assignedPlayersToCourtsList, playerList);

        } else {
            // player2list is empty. ZERO str difference requirement!

            outerloop: for(Player pX:playerList){
                // ADD any player
                if(pX.isMaleSex() == pSex) {
                    playerXList.add(pX);
                }
            }

            if (playerXList.size() > 0) {

                Collections.sort(playerXList, new Comparator<Player>() {
                    @Override
                    public int compare(Player pA, Player pB) {
                        return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                    }
                });

                int index = playerXList.size() -1;


                assignMixPlayer(playerXList.get(index), courtId, position, assignedPlayersToCourtsList, playerList);

            } else {
                // throw exception, no list of potential opponents could be made.
            }
        }
    }

    /**
     *
     * @param player
     * @param courtId
     * @param position
     * @param assignedPlayersToCourtsList
     * @param playerList
     */
    private void assignMixPlayer(Player player, Long courtId, int position, List<AssignedPlayer> assignedPlayersToCourtsList, List<Player> playerList) {
        AssignedPlayer doubleMixPlayer =new AssignedPlayer(
                player.getPlayerId(),
                player.isMaleSex(),
                courtId,
                position,
                player.getRoll(),
                TypeOfMatch.DOUBLE_MIX
        );

        assignedPlayersToCourtsList.add(doubleMixPlayer);
        playerList.remove(player);
    }


    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean createDoubleMixMatchLast(List<Player> playerList,
                                             List<AssignedPlayer> assignedPlayersToCourtsList,
                                            Event event) {

        int index = assignedPlayersToCourtsList.size() -1;
        Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
        Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
        Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
        TypeOfMatch typeOfMatch = TypeOfMatch.DOUBLE_MIX;
        boolean sexP1 = assignedPlayersToCourtsList.get(index).getSexMale();
        double strengthDifference = event.getStrengthDifference();

        int sameSexCounter = 0;
        int oppositeSexCounter = 0;

        for (Player p: playerList) {
            if(p.isMaleSex() == sexP1) {
                sameSexCounter++;
            } else {
                oppositeSexCounter++;
            }
        }

        /**
         * HARD COPY OF PLAYERLIST BECAUSE I AM MODIFYING AND USING LIST IN LOOP!
         */
        List<Player> playerListCopy = new ArrayList<Player>(playerList);
        /**
         * Scenario A
         *  2 configurations possible.
         *  1 OPPONENT is Fixed.
         *  1 VARIABLE PARTNER <--switch --> 1 VARIABLE OPPONENT
         *  (1+B)vs(C +D)  --or-- (1+D)vs(C+B)
         *  CALCULATE BOTH SCENARIO's. take the lowest strength difference.
         */
       if (sameSexCounter + oppositeSexCounter == 3) {

           Player playerB = new Player();
           Player playerC = new Player();
           Player playerD = new Player();

           int counter = 0;
           outerloop: for (Player p: playerListCopy) {
                if (p.isMaleSex() == sexP1) {
                    playerC = p;
                } else if (p.isMaleSex() != sexP1) {

                  if(counter == 1) {
                      playerD = p;
                      break outerloop;
                  }
                      if (counter == 0) {
                         playerB = p;
                        counter++;
                      }
                  }
                }

           double playerBstrength = playerB.getPlayerStrength();
           double playerCstrength = playerC.getPlayerStrength();
           double playerDstrength = playerD.getPlayerStrength();

           double scenarioA1 = (player1Strength + playerBstrength) - (playerCstrength + playerDstrength);
           double scenarioA2 = (player1Strength + playerDstrength) - (playerCstrength + playerBstrength);

           if (scenarioA1 <= scenarioA2) {
               assignMixPlayer(playerB, courtId, 3, assignedPlayersToCourtsList, playerList); // partner
               assignMixPlayer(playerC, courtId, 2 , assignedPlayersToCourtsList, playerList); // opponent opposite sex
               assignMixPlayer(playerD, courtId, 4, assignedPlayersToCourtsList, playerList);// opponent same sex
           } else {
               assignMixPlayer(playerD, courtId, 3, assignedPlayersToCourtsList, playerList); // partner
               assignMixPlayer(playerC, courtId, 2, assignedPlayersToCourtsList, playerList);  // opponent opposite sex
               assignMixPlayer(playerB, courtId, 4, assignedPlayersToCourtsList, playerList);// opponent same sex
           }
       }

        /**
         * Scenario B
         * many configurations possible.
         * 1 OPPONENT is Fixed.
         * 1 VARIABLE PARTNER
         * 1 VARIABLE OPPONENT
         *
         *
          */
        else if (sameSexCounter == 1){
            for (Player p: playerListCopy) {
                if (p.isMaleSex() == sexP1) {
                    assignMixPlayer(p, courtId, 2, assignedPlayersToCourtsList, playerList);
                }
            }
        double player2Strength = assignedPlayersToCourtsList.get(assignedPlayersToCourtsList.size()).getStrength();
        double average2Strength = (player1Strength + player2Strength) / 2;
        findPlayer(!sexP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 3, strengthDifference, average2Strength);

        double player3Strength = assignedPlayersToCourtsList.get(assignedPlayersToCourtsList.size()).getStrength();
        double average4Strength = player1Strength + player3Strength - player2Strength;
        findPlayer(!sexP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 4, strengthDifference, average4Strength);
        }
        /**
         * Scenario C
         * initially only 2 configurations possible. later on many more possible.
         * PARTNER can be chosen from 2 options
         * (A+B)vs(C) --or-- (A+C)vs(B)
         * start off with the least strength difference and find player D
         *
         */
       else if (oppositeSexCounter == 2) {
           Player playerB = new Player();
           Player playerC = new Player();

           int counter = 0;
           outerloop:
           for (Player p : playerListCopy) {
               if (p.isMaleSex() == !sexP1) {
                   if (counter == 1) {
                       playerC = p;
                       break outerloop;
                   }
                   if (counter == 0) {
                       playerB = p;
                       counter++;
                   }
               }

               double playerBstrength = playerB.getPlayerStrength();
               double playerCstrength = playerC.getPlayerStrength();

               double scenarioC1 = (player1Strength + playerBstrength) - (playerCstrength);
               double scenarioC2 = (player1Strength + playerCstrength) - (playerBstrength);

               if (scenarioC1 <= scenarioC2) {
                   assignMixPlayer(playerB, courtId, 3, assignedPlayersToCourtsList, playerList); // partner opposite sex
                   assignMixPlayer(playerC, courtId, 4, assignedPlayersToCourtsList, playerList); // opponent opposite sex
                   findPlayer(sexP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 2, strengthDifference, scenarioC1);

               } else {
                   assignMixPlayer(playerC, courtId, 3, assignedPlayersToCourtsList, playerList); // partner opposite sex
                   assignMixPlayer(playerB, courtId, 4, assignedPlayersToCourtsList, playerList);  // opponent opposite sex
                   findPlayer(sexP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 2, strengthDifference, scenarioC2);
               }
           }
       }
        /**
         * Exception
         */
        else {
            //throw exception
        }

        return true;
    }
}
