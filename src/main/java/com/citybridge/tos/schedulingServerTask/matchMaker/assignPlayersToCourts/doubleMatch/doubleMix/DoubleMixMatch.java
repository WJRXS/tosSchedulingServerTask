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
            createDoubleMixMatchLast(playerList, assignedPlayersToCourtsList);
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
         * Player 2, opposite sex
         */
        findPlayer(!isSexMaleP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 2, strengthDifference, player1Strength);

        int index2 = assignedPlayersToCourtsList.size() -1;
        Double player2Strength = assignedPlayersToCourtsList.get(index2).getStrength();
        Double average3Strength = (player1Strength + player2Strength) /2;

        /**
         * Player 3, same sex
         */
        findPlayer(isSexMaleP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 3, strengthDifference, average3Strength);

        int index3 = assignedPlayersToCourtsList.size() -1;
        Double player3Strength = assignedPlayersToCourtsList.get(index3).getStrength();
        Double average4Strength = player1Strength + player3Strength - player2Strength;

        /**
         * Player 4, opposite sex
         */
        findPlayer(!isSexMaleP1, playerList, assignedPlayersToCourtsList, player1Id, courtId, 4, strengthDifference, average4Strength);
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
                                             List<AssignedPlayer> assignedPlayersToCourtsList) {

        int index = assignedPlayersToCourtsList.size() -1;
        Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
        Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
        Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
        TypeOfMatch typeOfMatch = TypeOfMatch.DOUBLE_MIX;
        boolean sexP1 = assignedPlayersToCourtsList.get(index).getSexMale();

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
         * Scenario A
         *  --ONLY 1 CONFIGURATION POSSIBLE--
         *
         *  #TODO MAKE HARD COPY OF PLAYERLIST CAUSE I AM MODIFYING AND USING LIST IN LOOP!!!!!!!!!!!
          */
        if (sameSexCounter + oppositeSexCounter == 3) {
            int position = 2;
            for (Player p: playerList) {
                if (p.isMaleSex() == sexP1) {
                    // assign P3
                    assignMixPlayer(p, courtId, 3, assignedPlayersToCourtsList, playerList);
                } else if (p.isMaleSex() != sexP1) {
                    // assign P2 & P4
                        position=+2;
                }
            }



        }
        /**
         * Scenario B
         * --VARIABLE OPPONENT--
          */
        else if (sameSexCounter == 1){

        }
        /**
         * Scenario C
         * --VARIABLE PARTNER--
         */
        else if (oppositeSexCounter == 2) {

        }
        /**
         * Exception
         */
        else {
            //throw exception
        }



        int index = assignedPlayersToCourtsList.size();
        AssignedPlayer player1 = assignedPlayersToCourtsList.get(index);
        boolean isSexMale = player1.getSexMale();
        double p1Strength = player1.getStrength();
        long courtId = player1.getCourtId();

        // get the 3 leftovers, put in list
        List<Player> player234List = new ArrayList<>();
        for(Player p:playerList) {
            // ADD players (3!) with sex required
            if (p.isMaleSex() == isSexMale) {
                player234List.add(p);
            }
        }
        if (player234List.size() == 3) {
            // #TODO proceed
        } else {
            // throw exception
        }

        double pX2Strength = player234List.get(0).getPlayerStrength();
        double pX3Strength = player234List.get(1).getPlayerStrength();
        double pX4Strength = player234List.get(2).getPlayerStrength();

        // secenario A: 12-34
        double scenarioA = (p1Strength + pX2Strength) - (pX3Strength + pX4Strength);
        // scenario 2: 13-24
        double scenarioB = (p1Strength + pX3Strength) - (pX2Strength + pX4Strength);
        // scenario 3: 14-23
        double scenarioC = (p1Strength + pX4Strength) - (pX2Strength + pX3Strength);

        // try all configurations for best strength balance
        if (scenarioA <= scenarioB && scenarioA <= scenarioB) {
            assignLastPlayer234(player234List.get(0), player234List.get(1), player234List.get(2), courtId, assignedPlayersToCourtsList, playerList, typeOfMatch);
        }
        else if(scenarioB <= scenarioA && scenarioB <= scenarioC) {
            assignLastPlayer234(player234List.get(1), player234List.get(0), player234List.get(2), courtId, assignedPlayersToCourtsList, playerList, typeOfMatch);
        }
        else if (scenarioC <= scenarioA && scenarioC <= scenarioB) {
            assignLastPlayer234(player234List.get(2), player234List.get(0), player234List.get(1), courtId, assignedPlayersToCourtsList, playerList, typeOfMatch);
        }
        else { //throw exception
        }

        return true;
    }




}
