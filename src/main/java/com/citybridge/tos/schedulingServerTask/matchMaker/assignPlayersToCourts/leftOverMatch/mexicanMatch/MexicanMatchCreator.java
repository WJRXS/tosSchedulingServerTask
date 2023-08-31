package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.leftOverMatch.mexicanMatch;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.court.TypeOfCourt;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.typeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class MexicanMatchCreator {

    /**
     * This method fill assignedPlayersToCourtsList  with 3 players on 1 court.
     * Diagram_MexicanMatchCreator.png
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param courtList
     */
        public void createMexicanMatch(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList, List<Court> courtList) {

            // 0) FIND player 1
            // VOLUNTARY: High roll player who likes Mexican (court)
            // or
            // INVOLUNTARY: Lowest roll player(court)
            boolean isThereAPlayer1WhoLikesMexican = findPlayer1(playerList,  courtList, assignedPlayersToCourtsList);

            // 1) Check for sex distribution: Determine Player2Sex and player3Sex
            List<Boolean> player23Sex = checkSexDistribution(playerList, assignedPlayersToCourtsList);

            // 2) FIND player 2
            boolean isThereAPlayer2WhoLikesMexican;
            if (isThereAPlayer1WhoLikesMexican) {
            isThereAPlayer2WhoLikesMexican = findVoluntaryPlayer2(player23Sex, playerList, assignedPlayersToCourtsList);
            } else {
            isThereAPlayer2WhoLikesMexican = findInvoluntaryPlayer2(player23Sex, playerList, assignedPlayersToCourtsList); }


            // 3) FIND player 3
            if (isThereAPlayer2WhoLikesMexican) {
                findVoluntaryPlayer3(player23Sex, playerList, assignedPlayersToCourtsList);
            } else {
                findInvoluntaryPlayer3(player23Sex, playerList, assignedPlayersToCourtsList);
                }
            // #TODO make some final check if the mexican is really created.
            }


/**
 * Find Player1
 */
        private boolean findPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {

            if (findVoluntaryPlayer1(playerList,  courtList, assignedPlayersToCourtsList)) {
                return true;
           }else {
                return findInvoluntaryPlayer1(playerList,  courtList, assignedPlayersToCourtsList);

            }
        }


    /**
     * FIND VOLUNTARY Player1
     */
    private boolean findVoluntaryPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        outerloop: for(Player p:playerList){
            if(p.isWantToPlayMexican()){
                List<TypeOfCourt> courtPreference=p.getCourtPreference();
                Long courtId=findCourtId(courtPreference,courtList);
                int position1 = 1;

                assignMexicanPlayer(p, courtId, position1, assignedPlayersToCourtsList, playerList);

                 return true;
            }
        }
        return false;
    }

    /**
     * FIND Involuntary Player1
     */
    private boolean findInvoluntaryPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {
        int indexLowestRoll = playerList.size() -1;
        Player p = playerList.get(indexLowestRoll);
        boolean mexicanP1SexIsMale = p.isMaleSex();

        List<TypeOfCourt> courtPreference=p.getCourtPreference();
        Long courtId=findCourtId(courtPreference,courtList);
        int position1 = 1;

        assignMexicanPlayer(p, courtId, position1, assignedPlayersToCourtsList, playerList);
        return false;
    }




        /**
         * --------- 1 --------------------
         * check for sex distribution.
         * Assign opponent sex = X2 & X3.
         */
        private List<Boolean> checkSexDistribution(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {
            int lastIndex = assignedPlayersToCourtsList.size() -1;
            boolean player1SexIsMale = assignedPlayersToCourtsList.get(lastIndex).getSexMale();

            boolean isSexMix = true;
            boolean X2;
            boolean X3;
            int maleCounter = 0;
            int femaleCounter = 0;

            for (Player p : playerList) {
                if (p.isMaleSex()) {
                    maleCounter++;
                } else {
                    femaleCounter++;
                }
            }

            if (maleCounter % 2 == 0 && femaleCounter % 2 == 0) {
                isSexMix = false;
            } else if (maleCounter % 2 != 0 && femaleCounter % 2 != 0) {
                isSexMix = true;
            } else {
                // throw exception
            }

            // forced mix mexican: [FMF] or [MMF]
            if (isSexMix) {
                return List.of(true, false);
            } else {
                // preferred non_mix mexican, [MMM] or [FFF]
                // first: try get p1Sex.
                if (player1SexIsMale && maleCounter >= 2) {
                      return List.of(true, true);
                } else if (!player1SexIsMale && femaleCounter >= 2) {
                      return List.of(false, false);
                }
                // Cannot be helped, mexican sex distribution. will be [MFF] or [FMM]
                else if (player1SexIsMale && femaleCounter >= 2)  {
                    return List.of(false, false);
                } else if (!player1SexIsMale && maleCounter >= 2)  {
                    return List.of(true, true);
                } else    {
                    // #TODO throw exception
                    return List.of(true, true);
                  }
            }

        }





        /**
         * FIND VOLUNTARY Player2
         * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
         */
        private boolean findVoluntaryPlayer2(List<Boolean> player23Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

            boolean p2Sex = player23Sex.get(0);

            int index = assignedPlayersToCourtsList.size() -1;
            Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
            Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
            Double strengthDifference = 1.0; // #TODO get from event settings
            Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
            int position2 = 2;


            List<Player> player2List = new ArrayList<>();
            /**
             * make list of potentials. sort on strength. and next pick the closest strength.
             */

            outerloop: for(Player p2:playerList){
                // ADD any likePlayMexican & friend
                if(p2.isWantToPlayMexican() && p2.getFriendIdList().contains(player1Id)){
                    player2List.add(p2);

                }
                // ADD any likePlayMexican & similar strength.
                else if(p2.isWantToPlayMexican()
                        && (p2.getPlayerStrength() <= (player1Strength +strengthDifference) && p2.getPlayerStrength() >= (player1Strength -strengthDifference))) {

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

                assignMexicanPlayer(player2List.get(index2), courtId, position2, assignedPlayersToCourtsList, playerList);
                return true;
            } else {
                return findInvoluntaryPlayer2(player23Sex, playerList, assignedPlayersToCourtsList);
            }
        }

        /**
         * FIND INVOLUNTARY Player2
         */
        private boolean findInvoluntaryPlayer2(List<Boolean> player23Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {
            boolean p2Sex = player23Sex.get(0);
            int indexP1 = assignedPlayersToCourtsList.size() -1;
            Long courtId = assignedPlayersToCourtsList.get(indexP1).getCourtId();
            int position2 = 2;

            int lastIndex = playerList.size() -1;

            for (int i = lastIndex; i >= 0; i--) {

                if(playerList.get(i).isMaleSex() == p2Sex) {
                    assignMexicanPlayer(playerList.get(i), courtId, position2, assignedPlayersToCourtsList, playerList);
                    return false;
                }
                else {
                    // throw exception. no possible partners for single. #TODO set minimum event players?

                }

            }
            return false;
        }


    /**
     * FIND VOLUNTARY Player3
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     */
    private boolean findVoluntaryPlayer3(List<Boolean> player23Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        boolean p3Sex = player23Sex.get(1);

        int indexPlayer1 = assignedPlayersToCourtsList.size() -2;
        int indexPlayer2 = assignedPlayersToCourtsList.size() -1;

        Long player1Id = assignedPlayersToCourtsList.get(indexPlayer1).getPlayerId();
        Double player1Strength = assignedPlayersToCourtsList.get(indexPlayer1).getStrength();
        Double player2Strength = assignedPlayersToCourtsList.get(indexPlayer2).getStrength();
        Double player3AverageStrength = (player1Strength + player2Strength) / 2;
        Double strengthDifference = 1.0; // #TODO get from event settings
        Long courtId = assignedPlayersToCourtsList.get(indexPlayer1).getCourtId();
        int position3 = 3;



        List<Player> player3List = new ArrayList<>();
        /**
         * make list of potentials. sort on strength. and next pick the closest strength.
         */

        outerloop: for(Player p3:playerList){
            // ADD any likePlayMexican & friend
            if(p3.isWantToPlayMexican() && p3.getFriendIdList().contains(player1Id)){
                player3List.add(p3);

            }
            // ADD any likePlayMexican & similar strength.
            else if(p3.isWantToPlayMexican()
                    && (p3.getPlayerStrength() <= (player3AverageStrength +strengthDifference) && p3.getPlayerStrength() >= (player3AverageStrength -strengthDifference))) {

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

            assignMexicanPlayer(player3List.get(index3), courtId, position3, assignedPlayersToCourtsList, playerList);
            return true;
        } else {
            return findInvoluntaryPlayer3(player23Sex, playerList, assignedPlayersToCourtsList);
        }

    }

    /**
     * FIND INVOLUNTARY Player3
     */
    private boolean findInvoluntaryPlayer3(List<Boolean> player23Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {
        boolean p3Sex = player23Sex.get(1);
        int indexP1 = assignedPlayersToCourtsList.size() -2;
        Long courtId = assignedPlayersToCourtsList.get(indexP1).getCourtId();
        int position3 = 3;

        int lastIndex = playerList.size() -1;

        for (int i = lastIndex; i >= 0; i--) {

            if(playerList.get(i).isMaleSex() == p3Sex) {
                assignMexicanPlayer(playerList.get(i), courtId, position3, assignedPlayersToCourtsList, playerList);
                return false;
            }
            else {
                // throw exception. no possible partners for single. #TODO set minimum event players?
            }

        }
        return false;
    }


        /**
         * Util
         * find a prefered court
         */
        private Long findCourtId(List<TypeOfCourt> courtPreference, List<Court> courtList) {
            int sizeCourtList = courtList.size();
            outerLoop: for(TypeOfCourt typeOfCourt: courtPreference) {
                innerLoop: for(int i = 0; i<sizeCourtList; i++) {
                    if(typeOfCourt == courtList.get(i).getTypeOfCourt())  {
                        // MATCH!!!
                        Long courtId = courtList.get(i).getCourtId();
                        courtList.remove(i);
                        return courtId;
                    }
                }
            }
            // NO MATCH, something might have gone wrong, just take the first court.
            return courtList.get(0).getCourtId();
        }

    private void assignMexicanPlayer(Player player, Long courtId, int position, List<AssignedPlayer> assignedPlayersToCourtsList, List<Player> playerList) {

        AssignedPlayer mexicanPlayer =new AssignedPlayer(
                player.getPlayerId(),
                player.isMaleSex(),
                courtId,
                position,
                player.getRoll(),
                TypeOfMatch.MEXICAN
        );

        assignedPlayersToCourtsList.add(mexicanPlayer);
        playerList.remove(player);
    }



    }
