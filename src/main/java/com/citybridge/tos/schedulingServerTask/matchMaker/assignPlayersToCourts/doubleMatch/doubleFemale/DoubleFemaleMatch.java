package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleFemale;

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
public class DoubleFemaleMatch {
    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param typeOfMatch
     * @param isMatchTypeLast
     * @return
     */
    public boolean createDoubleFemaleMatch(List<Player> playerList,
                                           List<AssignedPlayer> assignedPlayersToCourtsList,
                                           TypeOfMatch typeOfMatch,
                                           boolean isMatchTypeLast,
                                           Event event){

        if (!isMatchTypeLast) {
            createDoubleFemaleMatchPlenty(playerList, assignedPlayersToCourtsList, event);
        }
        else {
            createDoubleFemaleMatchLast(playerList, assignedPlayersToCourtsList);
        }




        return true;
    }

    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean createDoubleFemaleMatchPlenty(List<Player> playerList,
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
           findPlayer234(isSexMale, );
         }
     } else {
         findPlayer234(isSexMale, );

     }
    return true;
    }

    /**
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean createDoubleFemaleMatchLast(List<Player> playerList,
                                                List<AssignedPlayer> assignedPlayersToCourtsList) {

        // get the 3 leftovers, put in list

        // try all configurations for best strength ballance

        // select p2 p3 p4

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
        Double avarage3Strength = (player1Strength + player2Strength) /2;

        /**
         * 3
          */
        findPlayer3(isSexMale, playerList, assignedPlayersToCourtsList, player1Id, courtId, strengthDifference, avarage3Strength, typeOfMatch);


        int index3 = assignedPlayersToCourtsList.size() -1;
        Double player3Strength = assignedPlayersToCourtsList.get(index3).getStrength();
        Double avarage4Strength = player1Strength + player3Strength - player2Strength;

        /**
         * 4
         */
        findPlayer4(isSexMale, playerList, assignedPlayersToCourtsList, player1Id, courtId, strengthDifference, avarage4Strength, typeOfMatch);


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
    private Double findPlayer2(Boolean p2Sex,
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


                /**
                 * #TODO deze move herhaald zich, misschien een method voor maken?
                 */
                AssignedPlayer doublePlayer2=new AssignedPlayer(player2List.get(index2).getPlayerId(),
                        p2Sex,
                        courtId,
                        position2,
                        player2List.get(index2).getRoll(),
                        typeOfMatch
                );
                assignedPlayersToCourtsList.add(doublePlayer2);
                playerList.remove(player2List.get(index2));
                return true;
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


                    /**
                     * #TODO deze move herhaald zich, misschien een method voor maken?
                     */
                    AssignedPlayer doublePlayer2=new AssignedPlayer(player2List.get(index2).getPlayerId(),
                            p2Sex,
                            courtId,
                            position2,
                            player2List.get(index2).getRoll(),
                            typeOfMatch
                    );
                    assignedPlayersToCourtsList.add(doublePlayer2);
                    playerList.remove(player2List.get(index2));
                    return true;


            } else {
                    // throw exception, no list of potential opponents could be made.
                }





            return false;
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
     * @param avarage3Strength
     * @param typeOfMatch
     */
    private void findPlayer3(Boolean p3Sex,
                             List<Player> playerList,
                             List<AssignedPlayer> assignedPlayersToCourtsList,
                             Long player1Id,
                             Long courtId,
                             Double strengthDifference,
                             Double avarage3Strength,
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
            } else if(p3.getPlayerStrength() <= (avarage3Strength +strengthDifference) && p3.getPlayerStrength() >= (avarage3Strength -strengthDifference)&& (p3.isMaleSex() == p3Sex)) {
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


            /**
             * #TODO deze move herhaald zich, misschien een method voor maken?
             */
            AssignedPlayer doublePlayer3=new AssignedPlayer(player3List.get(index3).getPlayerId(),
                    p3Sex,
                    courtId,
                    position3,
                    player3List.get(index3).getRoll(),
                    typeOfMatch
            );
            assignedPlayersToCourtsList.add(doublePlayer3);
            playerList.remove(player3List.get(index3));
            return true;
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


                /**
                 * #TODO deze move herhaald zich, misschien een method voor maken?
                 */
                AssignedPlayer doublePlayer3=new AssignedPlayer(player3List.get(index3).getPlayerId(),
                        p3Sex,
                        courtId,
                        position3,
                        player3List.get(index3).getRoll(),
                        typeOfMatch
                );
                assignedPlayersToCourtsList.add(doublePlayer3);
                playerList.remove(player3List.get(index3));
                return true;


            } else {
                // throw exception, no list of potential opponents could be made.
            }





            return false;
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
     * @param avarage4Strength
     * @param typeOfMatch
     */
    private void findPlayer4(Boolean p4Sex,
                             List<Player> playerList,
                             List<AssignedPlayer> assignedPlayersToCourtsList,
                             Long player1Id,
                             Long courtId,
                             Double strengthDifference,
                             Double avarage4Strength,
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
            } else if(p4.getPlayerStrength() <= (avarage4Strength +strengthDifference) && p4.getPlayerStrength() >= (avarage4Strength -strengthDifference)&& (p4.isMaleSex() == p3Sex)) {
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


            /**
             * #TODO deze move herhaald zich, misschien een method voor maken?
             */
            AssignedPlayer doublePlayer4=new AssignedPlayer(player4List.get(index4).getPlayerId(),
                    p4Sex,
                    courtId,
                    position4,
                    player4List.get(index4).getRoll(),
                    typeOfMatch
            );
            assignedPlayersToCourtsList.add(doublePlayer4);
            playerList.remove(player4List.get(index4));
            return true;
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


                /**
                 * #TODO deze move herhaald zich, misschien een method voor maken?
                 */
                AssignedPlayer doublePlayer4=new AssignedPlayer(player4List.get(index4).getPlayerId(),
                        p4Sex,
                        courtId,
                        position4,
                        player4List.get(index4).getRoll(),
                        typeOfMatch
                );
                assignedPlayersToCourtsList.add(doublePlayer4);
                playerList.remove(player4List.get(index4));
                return true;


            } else {
                // throw exception, no list of potential opponents could be made.
            }





            return false;
        }

    }






}
