package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.unbalancedMatch;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.court.TypeOfCourt;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class UnbalancedMatchCreator {

    /**
     * This method fill assignedPlayersToCourtsList  with 4 players on 1 court.
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param courtList
     */
    public void createUnbalancedMatch(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList, List<Court> courtList) {


        // 0) FIND INVOLUNTARY player 1  M (MFFFF) OR F (FMMM)
        boolean soloSex = getUnbalancedSexDistribution(playerList);
        findInvoluntaryPlayer1(soloSex, playerList,  courtList, assignedPlayersToCourtsList);




        // 1) Check
        // check for sex distribution.
        // Determine Player2Sex and player3Sex
        int lastIndex = assignedPlayersToCourtsList.size();
        boolean player1SexIsMale = assignedPlayersToCourtsList.get(lastIndex).getSexMale();
        List<Boolean> player23Sex = checkSexDistribution(playerList, player1SexIsMale);

        // 2) FIND player 2
        // VOLUNTARY: (automaticly means p1 is also voluntary = strength check)
        // or
        // INVOLUNTARY: Lowest roll player (with or without strength check)

        // VOLUNTARY P2
        boolean player2Sex = player23Sex.get(0);
        boolean isThereAPlayer2WhoLikesMexican = false;
        if (isThereAPlayer1WhoLikesMexican) {
            isThereAPlayer2WhoLikesMexican = findVoluntaryPlayer2(player2Sex, playerList, assignedPlayersToCourtsList);
        }
        // INVOLUNTARY P2
        if(!isThereAPlayer2WhoLikesMexican) { findInvoluntaryPlayer2(player2Sex, playerList, assignedPlayersToCourtsList); }


        // 3) FIND player 3
        // VOLUNTARY: (automaticly means p1 is also voluntary = strength check)
        // or
        // INVOLUNTARY: Lowest roll player (with or without strength check)

        // VOLUNTARY P3
        boolean player3Sex = player23Sex.get(1);
        boolean isThereAPlayer3WhoLikesMexican = false;
        if (isThereAPlayer1WhoLikesMexican) {
            isThereAPlayer3WhoLikesMexican = findVoluntaryPlayer3(player3Sex, playerList, assignedPlayersToCourtsList);
        }
        // INVOLUNTARY P3
        if(!isThereAPlayer3WhoLikesMexican) { findInvoluntaryPlayer3(player3Sex, playerList, assignedPlayersToCourtsList); }


        // #TODO make some final check if the mexican is really created.





    }





    /**
     * FIND INvoluntary Player1
     */
    private void findInvoluntaryPlayer1(Boolean soloSex, List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {


        int indexLowestRoll = playerList.size() -1;
        Player unbalancedPlayer1 = new Player();

        outerloop: for (int i = indexLowestRoll; i >= 0; i--) {
            if (playerList.get(i).isMaleSex() == soloSex) {
                unbalancedPlayer1 = playerList.get(i);
                break outerloop;
            }
      }

        List<TypeOfCourt> courtPreference=unbalancedPlayer1.getCourtPreference();
        Long courtId=findCourtId(courtPreference,courtList);
        int position1 = 1;

        AssignedPlayer player1=new AssignedPlayer(unbalancedPlayer1.getPlayerId(),
                soloSex,
                courtId,
                position1,
                unbalancedPlayer1.getRoll(),
                TypeOfMatch.DOUBLE_UNBALANCED
        );
        assignedPlayersToCourtsList.add(player1);
        playerList.remove(unbalancedPlayer1);
        deleteCourt(courtList, courtId);
    }




    /**
     * --------- 1 --------------------
     * check sex distribution for the single sex. F(-MMM) or M(-FFF)
     * can be found my modulus %4 = 1
     */
    private Boolean getUnbalancedSexDistribution(List<Player> playerList) {

        int maleCounter = 0;
        int femaleCounter = 0;

        for (Player p : playerList) {
            if (p.isMaleSex()) {
                maleCounter++;
            } else {
                femaleCounter++;
            }
        }

        if (maleCounter % 4 == 1) {
            return true;
        } else if(femaleCounter % 4 == 1) {
            return false;
        } else {
            // #TODO throw exception
            return true;
        }
    }


/**
 * #TODO HIER GELEVEN!!!!!!!!!!!!!!!!!!!!!!!!!!!******************
 */

    /**
     * FIND VOLUNTARY Player2
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     */
    private boolean findVoluntaryPlayer2(Boolean p2Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

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


            /**
             * #TODO deze move herhaald zich, misschien een method voor maken?
             */
            AssignedPlayer mexicanPlayer2=new AssignedPlayer(player2List.get(index2).getPlayerId(),
                    p2Sex,
                    courtId,
                    position2,
                    player2List.get(index2).getRoll(),
                    TypeOfMatch.MEXICAN
            );
            assignedPlayersToCourtsList.add(mexicanPlayer2);
            playerList.remove(player2List.get(index2));
            return true;
        }

        return false;
    }

    /**
     * FIND INVOLUNTARY Player2
     */
    private void findInvoluntaryPlayer2(Boolean p2Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        int indexP1 = assignedPlayersToCourtsList.size() -1;
        Long courtId = assignedPlayersToCourtsList.get(indexP1).getCourtId();
        int position2 = 2;

        int lastIndex = playerList.size() -1;

        for (int i = lastIndex; i >= 0; i--) {

            if(playerList.get(i).isMaleSex() == p2Sex) {
                AssignedPlayer mexicanPlayer2=new AssignedPlayer(playerList.get(i).getPlayerId(),
                        p2Sex,
                        courtId,
                        position2,
                        playerList.get(i).getRoll(),
                        TypeOfMatch.MEXICAN
                );
                assignedPlayersToCourtsList.add(mexicanPlayer2);
                playerList.remove(i);
            }
            else {
                // throw exception. no possible partners for single. #TODO set minimum event players?
            }

        }
    }


    /**
     * FIND VOLUNTARY Player3
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     */
    private boolean findVoluntaryPlayer3(Boolean p3Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        int index = assignedPlayersToCourtsList.size() -1;
        Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
        Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
        Double strengthDifference = 1.0; // #TODO get from event settings
        Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
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
                    && (p3.getPlayerStrength() <= (player1Strength +strengthDifference) && p3.getPlayerStrength() >= (player1Strength -strengthDifference))) {

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
            AssignedPlayer mexicanPlayer3=new AssignedPlayer(player3List.get(index3).getPlayerId(),
                    p3Sex,
                    courtId,
                    position3,
                    player3List.get(index3).getRoll(),
                    TypeOfMatch.MEXICAN
            );
            assignedPlayersToCourtsList.add(mexicanPlayer3);
            playerList.remove(player3List.get(index3));
            return true;
        }

        return false;
    }

    /**
     * FIND INVOLUNTARY Player3
     */
    private void findInvoluntaryPlayer3(Boolean p3Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        int indexP1 = assignedPlayersToCourtsList.size() -1;
        Long courtId = assignedPlayersToCourtsList.get(indexP1).getCourtId();
        int position3 = 3;

        int lastIndex = playerList.size() -1;

        for (int i = lastIndex; i >= 0; i--) {

            if(playerList.get(i).isMaleSex() == p3Sex) {
                AssignedPlayer mexicanPlayer3=new AssignedPlayer(playerList.get(i).getPlayerId(),
                        p3Sex,
                        courtId,
                        position3,
                        playerList.get(i).getRoll(),
                        TypeOfMatch.MEXICAN
                );
                assignedPlayersToCourtsList.add(mexicanPlayer3);
                playerList.remove(i);
            }
            else {
                // throw exception. no possible partners for single. #TODO set minimum event players?
            }

        }
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

    /**
     * Util
     * remove a selected court from the court list
     * #TODO might be able to directly delete with courtId? courtList.remove(courtId)?
     */
    private void deleteCourt(List<Court> courtList, Long courtId) {
        for (Court c: courtList) {
            if(c.getCourtId() == courtId) {
                courtList.remove(c);
                break;
            }
        }

    }



    }
}
