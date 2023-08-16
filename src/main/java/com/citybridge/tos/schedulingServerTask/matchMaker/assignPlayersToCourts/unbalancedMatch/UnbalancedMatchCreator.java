package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.unbalancedMatch;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.court.TypeOfCourt;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UnbalancedMatchCreator {

    /**
     * LEFTOVER MATCH
     *
     * This method fill assignedPlayersToCourtsList  with 4 players on 1 court.
     * sex is unbalanced and mixed: MFFF or FMMM
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param courtList
     */
    public void createUnbalancedMatch(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList, List<Court> courtList) {

        // determine solosex  M (MFFFF) OR F (FMMM)
        boolean soloSex = getUnbalancedSex(playerList);

        // FIND player 1
       findPlayer1(soloSex, playerList,  courtList, assignedPlayersToCourtsList);

        // Find player 2, 3, 4, opposite sex.
        findPlayer234(!soloSex, playerList, assignedPlayersToCourtsList);
    }

    /**
     * --------- 1 --------------------
     * check sex distribution for the single sex. F(-MMM) or M(-FFF)
     * can be found my modulus %4 = 1
     */
    private Boolean getUnbalancedSex(List<Player> playerList) {

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
 *
 */
private boolean findPlayer1(Boolean isSexMale, List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {


        if (findVoluntaryPlayer1(isSexMale, playerList,  courtList, assignedPlayersToCourtsList)) {
            return true;
        } else if (findInVoluntaryPlayer1(isSexMale, playerList,  courtList, assignedPlayersToCourtsList)) {
        return false;
    } else {
        // #TODO throw exception
        return false;
    }
}

    /**
     * FIND Voluntary Player
     */
private boolean findVoluntaryPlayer1(Boolean isSexMale, List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {

    outerloop: for (Player unbalancedPlayer1: playerList) {
        if (unbalancedPlayer1.isMaleSex() == isSexMale && unbalancedPlayer1.isWantToPlayUnbalanced()) {
            List<TypeOfCourt> courtPreference = unbalancedPlayer1.getCourtPreference();
            Long courtId = findCourtId(courtPreference,courtList);
            int position1 = 1;

         assignUnbalancedPlayer(unbalancedPlayer1, courtId, position1,assignedPlayersToCourtsList, playerList);
         return true;
        }
    }
   return false;
}

    /**
     * FIND Involuntary Player
     */
    private boolean findInVoluntaryPlayer1(Boolean isSexMale, List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        int indexLowestRoll = playerList.size() -1;
        Player unbalancedPlayer1 = new Player();

        outerloop: for (int i = indexLowestRoll; i >= 0; i--) {
            if (playerList.get(i).isMaleSex() == isSexMale) {
                unbalancedPlayer1 = playerList.get(i);

                List<TypeOfCourt> courtPreference=unbalancedPlayer1.getCourtPreference();
                Long courtId=findCourtId(courtPreference,courtList);
                int position1 = 1;
                assignUnbalancedPlayer(unbalancedPlayer1, courtId, position1, assignedPlayersToCourtsList, playerList);
                return true;
            }
        }

        return false;
    }


private void findPlayer234(Boolean isSexMale, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

    int player1Index = assignedPlayersToCourtsList.size() -1;
    double player1Strength = assignedPlayersToCourtsList.get(player1Index).getStrength();
    long courtId = assignedPlayersToCourtsList.get(player1Index).getCourtId();


    Player playerB = new Player();
    Player playerC = new Player();
    Player playerD = new Player();

    int counter = 0;

    // find voluntary players
    outerloop: for (Player p : playerList) {
        if (p.isWantToPlayUnbalanced() && p.isMaleSex() == isSexMale ) {
            if (counter == 0) {
                playerB = p;
                counter++;
                break;
            } else if (counter == 1) {
                playerC = p;
                counter++;
                break;
            } else if (counter == 2) {
                playerD = p;
                break outerloop;
            }
        }
        }

    // reverse loop to find involuntary players.
    if (counter <= 1){
        outerloop: for (int i = playerList.size() - 1; i >= 0; i--) {
            if (!playerList.get(i).isWantToPlayUnbalanced() && playerList.get(i).isMaleSex() == isSexMale) {
                if (counter == 0) {
                    playerB = playerList.get(i);
                    counter++;
                    break;
                } else if (counter == 1) {
                    playerC = playerList.get(i);
                    counter++;
                    break;
                } else if (counter == 2) {
                    playerD = playerList.get(i);
                    break outerloop;
                }
            }
        }
    }

    double playerBstrength = playerB.getPlayerStrength();
    double playerCstrength = playerC.getPlayerStrength();
    double playerDstrength = playerD.getPlayerStrength();

    double scenarioA1 = (player1Strength + playerBstrength) - (playerCstrength + playerDstrength);
    double scenarioA2 = (player1Strength + playerCstrength) - (playerBstrength + playerDstrength);
    double scenarioA3 = (player1Strength + playerDstrength) - (playerBstrength + playerCstrength);



    if (scenarioA1 <= scenarioA2 && scenarioA1 <= scenarioA3) {
        assignUnbalancedPlayer(playerB, courtId, 3, assignedPlayersToCourtsList, playerList); // partner
        assignUnbalancedPlayer(playerC, courtId, 2, assignedPlayersToCourtsList, playerList); // opponent
        assignUnbalancedPlayer(playerD, courtId, 4, assignedPlayersToCourtsList, playerList);// opponent
    } else if (scenarioA2 <= scenarioA1 && scenarioA2 <= scenarioA3) {
        assignUnbalancedPlayer(playerC, courtId, 3, assignedPlayersToCourtsList, playerList); // partner
        assignUnbalancedPlayer(playerB, courtId, 2, assignedPlayersToCourtsList, playerList);  // opponent
        assignUnbalancedPlayer(playerD, courtId, 4, assignedPlayersToCourtsList, playerList);// opponent
    } else {
        assignUnbalancedPlayer(playerD, courtId, 3, assignedPlayersToCourtsList, playerList); // partner
        assignUnbalancedPlayer(playerB, courtId, 2, assignedPlayersToCourtsList, playerList);  // opponent
        assignUnbalancedPlayer(playerC, courtId, 4, assignedPlayersToCourtsList, playerList);// opponent

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
    private void assignUnbalancedPlayer(Player player, Long courtId, int position, List<AssignedPlayer> assignedPlayersToCourtsList, List<Player> playerList) {
        AssignedPlayer unbalancedPlayer =new AssignedPlayer(
                player.getPlayerId(),
                player.isMaleSex(),
                courtId,
                position,
                player.getRoll(),
                TypeOfMatch.DOUBLE_UNBALANCED
        );

        assignedPlayersToCourtsList.add(unbalancedPlayer);
        playerList.remove(player);
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

  }

