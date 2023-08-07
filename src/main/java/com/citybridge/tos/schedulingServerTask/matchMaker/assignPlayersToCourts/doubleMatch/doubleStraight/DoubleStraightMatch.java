package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleStraight;

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
public class DoubleStraightMatch {
    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param typeOfMatch
     * @param isMatchTypeLast
     * @return
     */
    public boolean createDoubleStraightMatch(List<Player> playerList,
                                             List<AssignedPlayer> assignedPlayersToCourtsList,
                                             TypeOfMatch typeOfMatch,
                                             boolean isMatchTypeLast,
                                             Event event){

        if (!isMatchTypeLast) {
            createDoubleStraightMatchPlenty(playerList, assignedPlayersToCourtsList, event);
        }
        else {
            createDoubleStraightMatchLast(playerList, assignedPlayersToCourtsList, typeOfMatch);
        }

        return true;
    }

    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean createDoubleStraightMatchPlenty(List<Player> playerList,
                                                    List<AssignedPlayer> assignedPlayersToCourtsList,
                                                    Event event) {
     int index = assignedPlayersToCourtsList.size();
     AssignedPlayer player1 = assignedPlayersToCourtsList.get(index);
     boolean isSexMale = player1.getSexMale();
     double strengthDifference = event.getStrengthDifference();

     // Partner request event setting.
     boolean partnerConnection = false; // event.isPartnerConnection(); #TODO implement later
     if (partnerConnection) {
         if (findPartnerPlayer3()) {
             findOpponentPlayer2();
             findOpponentPlayer4();
         } else {
           findPlayer234(isSexMale, playerList, assignedPlayersToCourtsList, strengthDifference );
         }
     } else {
         findPlayer234(isSexMale, playerList, assignedPlayersToCourtsList, strengthDifference );

     }
    return true;
    }

    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean createDoubleStraightMatchLast(List<Player> playerList,
                                                  List<AssignedPlayer> assignedPlayersToCourtsList,
                                                  TypeOfMatch typeOfMatch) {

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

    private void findPlayer234(Boolean isSexMale,
                               List<Player> playerList,
                               List<AssignedPlayer> assignedPlayersToCourtsList,
                               double strengthDifference) {

        int index = assignedPlayersToCourtsList.size() -1;
        Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
        Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
        Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
        TypeOfMatch typeOfMatch;

        if (isSexMale) {typeOfMatch = TypeOfMatch.DOUBLE_MALE;
        } else {
            typeOfMatch = TypeOfMatch.DOUBLE_FEMALE;
        }

        /**
         * 2
         */
        findPlayer2(isSexMale, playerList, assignedPlayersToCourtsList, player1Id, courtId, strengthDifference, player1Strength, typeOfMatch);

        int index2 = assignedPlayersToCourtsList.size() -1;
        Double player2Strength = assignedPlayersToCourtsList.get(index2).getStrength();
        Double average3Strength = (player1Strength + player2Strength) /2;

        /**
         * 3
          */
        findPlayer3(isSexMale, playerList, assignedPlayersToCourtsList, player1Id, courtId, strengthDifference, average3Strength, typeOfMatch);


        int index3 = assignedPlayersToCourtsList.size() -1;
        Double player3Strength = assignedPlayersToCourtsList.get(index3).getStrength();
        Double average4Strength = player1Strength + player3Strength - player2Strength;

        /**
         * 4
         */
        findPlayer4(isSexMale, playerList, assignedPlayersToCourtsList, player1Id, courtId, strengthDifference, average4Strength, typeOfMatch);
    }

    /**
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     * @param p2Sex
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param player1Id
     * @param courtId
     * @param strengthDifference
     * @param player1Strength
     * @param typeOfMatch
     */
    private void findPlayer2(Boolean p2Sex,
                             List<Player> playerList,
                             List<AssignedPlayer> assignedPlayersToCourtsList,
                             Long player1Id,
                             Long courtId,
                             Double strengthDifference,
                             Double player1Strength,
                             TypeOfMatch typeOfMatch) {

           int position2 = 2;




            List<Player> player2List = new ArrayList<>();
            /**
             * make list of potentials. sort on strength. and next pick the closest strength.
             * ADD any friend
             * ADD close in strength
             */

            outerloop: for(Player p2:playerList){
                // ADD any friend
                if(p2.getFriendIdList().contains(player1Id) && (p2.isMaleSex() == p2Sex)) {
                    player2List.add(p2);
                } else if(p2.getPlayerStrength() <= (player1Strength +strengthDifference) && p2.getPlayerStrength() >= (player1Strength -strengthDifference)&& (p2.isMaleSex() == p2Sex)) {
                    player2List.add(p2);
                }
            }

            // Sort list. last = smallest strength difference.
            if (player2List.size() > 0) {

                Collections.sort(player2List, new Comparator<Player>() {
                    @Override
                    public int compare(Player pA, Player pB) {
                        return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                    }
                });

                int index2 = player2List.size() -1;


                assignPlayer(player2List.get(index2), courtId, position2, assignedPlayersToCourtsList, playerList, typeOfMatch);

            } else {
                // player2list is empty. ZERO str difference requirement!

                outerloop: for(Player p2:playerList){
                    // ADD any player
                     if(p2.isMaleSex() == p2Sex) {
                        player2List.add(p2);
                    }
                }

                if (player2List.size() > 0) {

                    Collections.sort(player2List, new Comparator<Player>() {
                        @Override
                        public int compare(Player pA, Player pB) {
                            return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                        }
                    });

                    int index2 = player2List.size() -1;


                    assignPlayer(player2List.get(index2), courtId, position2, assignedPlayersToCourtsList, playerList, typeOfMatch);

            } else {
                    // throw exception, no list of potential opponents could be made.
                }
        }
    }


    /**
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     * @param p3Sex
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param player1Id
     * @param courtId
     * @param strengthDifference
     * @param average3Strength
     * @param typeOfMatch
     */
    private void findPlayer3(Boolean p3Sex,
                             List<Player> playerList,
                             List<AssignedPlayer> assignedPlayersToCourtsList,
                             Long player1Id,
                             Long courtId,
                             Double strengthDifference,
                             Double average3Strength,
                             TypeOfMatch typeOfMatch) {

        int position3 = 3;

        List<Player> player3List = new ArrayList<>();
        /**
         * make list of potentials. sort on strength. and next pick the closest strength.
         * ADD any friend
         * ADD close in strength
         */

        outerloop: for(Player p3:playerList){
            // ADD any friend
            if(p3.getFriendIdList().contains(player1Id) && (p3.isMaleSex() == p3Sex)) {
                player3List.add(p3);
            } else if(p3.getPlayerStrength() <= (average3Strength +strengthDifference) && p3.getPlayerStrength() >= (average3Strength -strengthDifference)&& (p3.isMaleSex() == p3Sex)) {
                player3List.add(p3);
            }
        }

        // Sort list. last = smallest strength difference.
        if (player3List.size() > 0) {

            Collections.sort(player3List, new Comparator<Player>() {
                @Override
                public int compare(Player pA, Player pB) {
                    return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                }
            });

            int index3 = player3List.size() -1;

            assignPlayer(player3List.get(index3), courtId, position3, assignedPlayersToCourtsList, playerList, typeOfMatch);

        } else {
            // player3list is empty. ZERO str difference requirement!

            outerloop: for(Player p3:playerList){
                // ADD any player
                if(p3.isMaleSex() == p3Sex) {
                    player3List.add(p3);
                }
            }

            if (player3List.size() > 0) {

                Collections.sort(player3List, new Comparator<Player>() {
                    @Override
                    public int compare(Player pA, Player pB) {
                        return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                    }
                });

                int index3 = player3List.size() -1;


                assignPlayer(player3List.get(index3), courtId, position3, assignedPlayersToCourtsList, playerList, typeOfMatch);

            } else {
                // throw exception, no list of potential opponents could be made.
            }
       }
    }

    /**
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     * @param p4Sex
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param player1Id
     * @param courtId
     * @param strengthDifference
     * @param average4Strength
     * @param typeOfMatch
     */
    private void findPlayer4(Boolean p4Sex,
                             List<Player> playerList,
                             List<AssignedPlayer> assignedPlayersToCourtsList,
                             Long player1Id,
                             Long courtId,
                             Double strengthDifference,
                             Double average4Strength,
                             TypeOfMatch typeOfMatch) {

        int position4 = 4;

        List<Player> player4List = new ArrayList<>();
        /**
         * make list of potentials. sort on strength. and next pick the closest strength.
         * ADD any friend
         * ADD close in strength
         */

        outerloop: for(Player p4:playerList){
            // ADD any friend
            if(p4.getFriendIdList().contains(player1Id) && (p4.isMaleSex() == p4Sex)) {
                player4List.add(p4);
            } else if(p4.getPlayerStrength() <= (average4Strength +strengthDifference) && p4.getPlayerStrength() >= (average4Strength -strengthDifference)&& (p4.isMaleSex() == p4Sex)) {
                player4List.add(p4);
            }
        }

        // Sort list. last = smallest strength difference.
        if (player4List.size() > 0) {

            Collections.sort(player4List, new Comparator<Player>() {
                @Override
                public int compare(Player pA, Player pB) {
                    return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                }
            });

            int index4 = player4List.size() -1;


            assignPlayer(player4List.get(index4), courtId, position4, assignedPlayersToCourtsList, playerList, typeOfMatch);

        } else {
            // player3list is empty. ZERO str difference requirement!

            outerloop: for(Player p4:playerList){
                // ADD any player
                if(p4.isMaleSex() == p4Sex) {
                    player4List.add(p4);
                }
            }

            if (player4List.size() > 0) {

                Collections.sort(player4List, new Comparator<Player>() {
                    @Override
                    public int compare(Player pA, Player pB) {
                        return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                    }
                });

                int index4 = player4List.size() -1;

                assignPlayer(player4List.get(index4), courtId, position4, assignedPlayersToCourtsList, playerList, typeOfMatch);

            } else {
                // throw exception, no list of potential opponents could be made.
            }
        }
    }

    /**
     * Assign LAST MATCH players 2, 3 4
     */
    private void assignLastPlayer234(Player p2, Player p3, Player p4, Long courtId, List<AssignedPlayer> assignedPlayersToCourtsList, List<Player> playerList, TypeOfMatch typeOfMatch) {

        assignPlayer(p2, courtId, 2, assignedPlayersToCourtsList,  playerList, typeOfMatch);
        assignPlayer(p3, courtId, 3, assignedPlayersToCourtsList,  playerList, typeOfMatch);
        assignPlayer(p4, courtId, 4, assignedPlayersToCourtsList,  playerList, typeOfMatch);

    }

    /**
     * #TODO exception handling
     * @param player
     * @param courtId
     * @param position
     * @param assignedPlayersToCourtsList
     * @param playerList
     * @param typeOfMatch
     */
    private void assignPlayer(Player player, Long courtId, int position, List<AssignedPlayer> assignedPlayersToCourtsList, List<Player> playerList, TypeOfMatch typeOfMatch) {


        AssignedPlayer doublePlayer =new AssignedPlayer(
                player.getPlayerId(),
                player.isMaleSex(),
                courtId,
                position,
                player.getRoll(),
                typeOfMatch
        );

       assignedPlayersToCourtsList.add(doublePlayer);
       playerList.remove(player);
   }
}
