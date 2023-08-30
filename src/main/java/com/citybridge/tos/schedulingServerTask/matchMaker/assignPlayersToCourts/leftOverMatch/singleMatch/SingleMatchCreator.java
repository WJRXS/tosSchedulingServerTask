package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.leftOverMatch.singleMatch;

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
public class SingleMatchCreator {


    /**
     * #TODO
     * GLOBAL METHOD: findplayer (matchtype matchtype, numberPlayer numberPlayer, playerlist playerlist.  assignedplayerlist assignedplayerlist, boolean voluntary)
     *
     */


    // 1) FIND player 1
    // VOLUNTARY: High roll player who likes single (court)
    // or
    // INVOLUNTARY: Lowest roll player(court)
    // add p1 to assignedPlayerList
    // remove p1 from playerList
    // find court &= remove court!!! from courtList


    // 2) FIND player 2
    // check: when P1 = voluntary, strength of opponent P2 should be considered.
    // VOLUNTARY: Highest roll friend (who likes & X & S+)
    //          : Highest roll player (who likes & X & S)
    // INVOLUNTARY: Lowest roll player (who likes & X &S)
    // add p2 to assingedPlayerList
    // remove p2 from playerList


    /**
     * This method fills the assignedPlayersToCourtsList with 2 players on 1 court.
     * Diagram_SingleMatchCreator.png
     *
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param courtList
     */
    public void createSingleMatch(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList, List<Court> courtList) {

        // 0) check for sex distribution: is the single going to be mixed sex or non_mixed sex
        /**
         * #TODO klopt nog niet. wat als je eigen wilt dat 2 mannen single doen, of 2 vrouwen?
         * #TODO het kan namelijk het aantal Mix Doubles beinvloeden.
         */

        boolean isSingleMixed = checkSexDistribution(playerList);

        // 1) FIND player 1
        findPlayer1(playerList, courtList, assignedPlayersToCourtsList);

        // 2) FIND player 2
        findPlayer2(playerList, courtList, assignedPlayersToCourtsList, isSingleMixed);

    }

    /**
     * --------- 0 --------------------
     * check for sex distribution.
     *
     * @param playerList
     * @return
     */
    private boolean checkSexDistribution(List<Player> playerList) {
        boolean isSexMix = false;
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
        return isSexMix;
    }

    /**
     * @param playerList
     * @param courtList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean findPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        if (findVoluntaryPlayer1(playerList, courtList, assignedPlayersToCourtsList)) {
            return true;
        } else if (findInvoluntaryPlayer1(playerList, courtList, assignedPlayersToCourtsList)) {
            return false;
        } else {
            // #TODO throw exception
            return false;
        }
    }


    /**
     * VOLUNTARY: High roll player who likes single (court)
     *
     * @param playerList
     * @param courtList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean findVoluntaryPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        outerloop:
        for (Player p : playerList) {
            if (p.isWantToPlaySingle()) {
                boolean singlePlayerIsMaleSex = p.isMaleSex();


                List<TypeOfCourt> courtPreference = p.getCourtPreference();
                Long courtId = findCourtId(courtPreference, courtList);
                int position1 = 1;

                assignSinglePlayer(p, courtId, position1, assignedPlayersToCourtsList, playerList);


                return true;
            }
        }
        return false;
    }

    /**
     * INVOLUNTARY: Lowest roll player (with or without strength check)
     *
     * @param playerList
     * @param courtList
     * @param assignedPlayersToCourtsList
     * @return
     */
    private boolean findInvoluntaryPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {
        int indexLowestRoll = playerList.size() - 1;
        Player p = playerList.get(indexLowestRoll);

        List<TypeOfCourt> courtPreference = p.getCourtPreference();
        Long courtId = findCourtId(courtPreference, courtList);
        int position1 = 1;

        assignSinglePlayer(p, courtId, position1, assignedPlayersToCourtsList, playerList);

        return true;
    }


    private boolean findPlayer2(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList, boolean isSingleMixed) {

        boolean player2Sex = getPlayer2Sex(isSingleMixed, assignedPlayersToCourtsList);

        if (findVoluntaryPlayer2(playerList, assignedPlayersToCourtsList, player2Sex)) {
            return true;
        } else if (findInvoluntaryPlayer2(playerList, assignedPlayersToCourtsList, player2Sex)) {
            return false;
        } else {
            // #TODO throw exception
            return false;
        }


    }


    /**
     * find opponent gender
     */
    private boolean getPlayer2Sex(Boolean isSingleMixed, List<AssignedPlayer> assignedPlayersToCourtsList) {

        int index = assignedPlayersToCourtsList.size() - 1;
        boolean p1Sex = assignedPlayersToCourtsList.get(index).getSexMale();

        boolean p2Sex;
        if (isSingleMixed) {
            p2Sex = !p1Sex;
        } else {
            p2Sex = p1Sex;
        }

        return p2Sex;
    }


    /**
     * FIND VOLUNTARY Player2
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     */
    private boolean findVoluntaryPlayer2(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList, Boolean p2Sex) {

        int index = assignedPlayersToCourtsList.size() - 1;
        Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
        Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
        Double strengthDifference = 1.0; // #TODO get from event
        Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
        int position2 = 2;


        List<Player> player2List = new ArrayList<>();
        /**
         * make list of potentials. sort on strength. and next pick the closest strength.
         */

        outerloop:
        for (Player p2 : playerList) {
            // ADD any likePlaySingle & friend & P2Sex
            if (p2.isWantToPlaySingle() && p2.getFriendIdList().contains(player1Id) && p2.isMaleSex() == p2Sex) {
                player2List.add(p2);
            }
            // ADD any likePlaySingle & similar strength & p2Sex.
            else if (p2.isWantToPlaySingle()
                    && (p2.getPlayerStrength() <= (player1Strength + strengthDifference) && p2.getPlayerStrength() >= (player1Strength - strengthDifference))
                    && p2.isMaleSex() == p2Sex) {

                player2List.add(p2);
            }
        }

        if (player2List.size() == 0) {
            return false;
        } else if (player2List.size() > 0) { // Sort list. last = smallest strength difference.
            Collections.sort(player2List, new Comparator<Player>() {
                @Override
                public int compare(Player pA, Player pB) {
                    return pA.getPlayerStrength() > pB.getPlayerStrength() ? -1 : pA.getPlayerStrength() == pB.getPlayerStrength() ? 0 : 1;
                }
            });

            int index2 = player2List.size() - 1;

            assignSinglePlayer(player2List.get(index2), courtId, position2, assignedPlayersToCourtsList, playerList);

            return true;
        }

        return false;
    }

    /**
     * FIND INVOLUNTARY Player2
     */
    private boolean findInvoluntaryPlayer2(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList, Boolean p2Sex) {

        int indexP1 = assignedPlayersToCourtsList.size() - 1;
        Long courtId = assignedPlayersToCourtsList.get(indexP1).getCourtId();
        int position2 = 2;

        int lastIndex = playerList.size() - 1;


        for (int i = lastIndex; i >= 0; i--) {

            if (playerList.get(i).isMaleSex() == p2Sex) {
                assignSinglePlayer(playerList.get(i), courtId, position2, assignedPlayersToCourtsList, playerList);

                return true;
            } else {
                // throw exception. no possible partners for single. #TODO set minimum event players?
            }
        }
        return true;
    }


    /**
     * Util
     * find a preferred court
     */
    private Long findCourtId(List<TypeOfCourt> courtPreference, List<Court> courtList) {
        int sizeCourtList = courtList.size();
        outerLoop:
        for (TypeOfCourt typeOfCourt : courtPreference) {
            innerLoop:
            for (int i = 0; i < sizeCourtList; i++) {
                if (typeOfCourt == courtList.get(i).getTypeOfCourt()) {
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
     *
     * @param player
     * @param courtId
     * @param position
     * @param assignedPlayersToCourtsList
     * @param playerList
     */
    private void assignSinglePlayer(Player player, Long courtId, int position, List<AssignedPlayer> assignedPlayersToCourtsList, List<Player> playerList) {
        AssignedPlayer unbalancedPlayer =new AssignedPlayer(
                player.getPlayerId(),
                player.isMaleSex(),
                courtId,
                position,
                player.getRoll(),
                TypeOfMatch.SINGLE
        );

        assignedPlayersToCourtsList.add(unbalancedPlayer);
        playerList.remove(player);
    }

}
