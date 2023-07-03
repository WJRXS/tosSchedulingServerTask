package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.singleMatch;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.court.TypeOfCourt;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

@Service
public class SingleMatchCreator {


    /**
     * #TODO
     * GLOBAL METHOD: findplayer (matchtype matchtype, numberPlayer numberPlayer, playerlist playerlist.  assignedplayerlist assignedplayerlist, boolean voluntary)
     *
     */
    // 0) Check
    // check for sex distribution.
    // is the single going to be mixed sex or non_mixed sex


    // 1) FIND player 1
    // VOLUNTARY: High roll player who likes single (court)
    // or
    // INVOLUNTARY: Lowest roll player(court)
    // add p1 to assignedPlayerList
    // remove p1 from playerList
    // find court &= remove court from courtList



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
     * @param playerList
     * @param assignedPlayersToCourtsList
     * @param courtList
     */
    public void createSingleMatch(List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList, List<Court> courtList) {

        // 0) Check
        // check for sex distribution.
        // is the single going to be mixed sex or non_mixed sex
        boolean isSingleMixed = checkSexDistribution(playerList);




        // 1) FIND player 1
        // VOLUNTARY: High roll player who likes single (court)
        // or
        // INVOLUNTARY: Lowest roll player(court)

        // VOLUNTARY P1
        boolean isThereAPlayer1WhoLikesSingle = findVoluntaryPlayer1(playerList,  courtList, assignedPlayersToCourtsList);
        // INVOLUNTARY P1
        if (!isThereAPlayer1WhoLikesSingle) { findInvoluntaryPlayer1(playerList,  courtList, assignedPlayersToCourtsList); }

        // 2) FIND player 2
        // VOLUNTARY: (automaticly means p1 is also voluntary = strength check)
        //
        // or
        // INVOLUNTARY: Lowest roll player (with or without strength check)

        // VOLUNTARY P2
        boolean player2Sex = getPlayer2Sex(isSingleMixed, assignedPlayersToCourtsList);

        boolean isThereAPlayer2WhoLikesSingle = false;

        if(isThereAPlayer1WhoLikesSingle) {
            isThereAPlayer2WhoLikesSingle = findVoluntaryPlayer2(player2Sex, playerList, assignedPlayersToCourtsList);
        }
            // INVOLUNTARY P2
            if (!isThereAPlayer2WhoLikesSingle) {
                findInvoluntaryPlayer2(player2Sex, playerList, assignedPlayersToCourtsList);
            }

      // #TODO make some final check if the single is really created.




            }







    /**
     * --------- 0 --------------------
     * check for sex distribution.
     * Assign opponent sex = X.
     * @param playerList
     * @return
     */
    private boolean checkSexDistribution(List<Player> playerList) {
        boolean isSexMix = false;
        int maleCounter = 0;
        int femaleCounter = 0;

        for(Player p: playerList) {
            if (p.isMaleSex()) {
                maleCounter++;
            } else {
                femaleCounter++;
            }
        }

         if(maleCounter%2 == 0 && femaleCounter%2 == 0) {
             isSexMix = false;
         }
         else if(maleCounter%2 !=0 && femaleCounter%2 !=0){
             isSexMix = true;
         }
         else {
             // throw exception
         }
       return isSexMix;
    }


    /**
     * FIND VOLUNTARY Player1
     */
    private boolean findVoluntaryPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        outerloop: for(Player p:playerList){
            if(p.isWantToPlaySingle()){
                boolean singlePlayerIsMaleSex=p.isMaleSex();


                List<TypeOfCourt> courtPreference=p.getCourtPreference();
                Long courtId=findCourtId(courtPreference,courtList);
                int position1 = 1;

                AssignedPlayer singlePlayer1=new AssignedPlayer(p.getPlayerId(),
                false,
                courtId,
                position1,
                p.getRoll(),
                TypeOfMatch.SINGLE
                );

                assignedPlayersToCourtsList.add(singlePlayer1);

                playerList.remove(p);


                return true;
                }
            }
    return false;
    }

    /**
     * FIND INvoluntary Player1
     */
        private void findInvoluntaryPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList) {
            int indexLowestRoll = playerList.size() -1;
            Player p = playerList.get(indexLowestRoll);

            List<TypeOfCourt> courtPreference=p.getCourtPreference();
            Long courtId=findCourtId(courtPreference,courtList);
            int position1 = 1;

            AssignedPlayer singlePlayer1=new AssignedPlayer(p.getPlayerId(),
                    false,
                    courtId,
                    position1,
                    p.getRoll(),
                    TypeOfMatch.SINGLE
            );
            assignedPlayersToCourtsList.add(singlePlayer1);

            playerList.remove(p);
        }

    /**
     * find opponent gender
     */
    private boolean getPlayer2Sex(Boolean isSingleMixed, List<AssignedPlayer> assignedPlayersToCourtsList) {

        int index = assignedPlayersToCourtsList.size() -1;
        boolean p1Sex = assignedPlayersToCourtsList.get(index).getSexMale();

        boolean p2Sex;
        if (isSingleMixed) {p2Sex = !p1Sex;}
        else {p2Sex = p1Sex;}

        return p2Sex;
    }


    /**
     * FIND VOLUNTARY Player2
     * #TODO consider dividing strength difference of friends by 2, to compete with non friend str difference.
     */
    private boolean findVoluntaryPlayer2(Boolean p2Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

    int index = assignedPlayersToCourtsList.size() -1;
    Long player1Id = assignedPlayersToCourtsList.get(index).getPlayerId();
    Double player1Strength = assignedPlayersToCourtsList.get(index).getStrength();
    Double strengthDifference = 1.0;
    Long courtId = assignedPlayersToCourtsList.get(index).getCourtId();
    int position2 = 2;


    List<Player> player2List = new ArrayList<>();
        /**
         * make list of potentials. sort on strength. and next pick the closest strength.
         */

        outerloop: for(Player p2:playerList){
        // ADD any likePlaySingle & friend
        if(p2.isWantToPlaySingle() && p2.getFriendIdList().contains(player1Id)){
               player2List.add(p2);

            }
        // ADD any likePlaySingle & similar strength.
        else if(p2.isWantToPlaySingle()
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
           AssignedPlayer singlePlayer2=new AssignedPlayer(player2List.get(index2).getPlayerId(),
                   p2Sex,
                   courtId,
                   position2,
                   player2List.get(index2).getRoll(),
                   TypeOfMatch.SINGLE
           );
           assignedPlayersToCourtsList.add(singlePlayer2);
           playerList.remove(player2List.get(index2));
           return true;
       }

    return false;
    }

    /**
     * FIND VOLUNTARY Player2
     */
    private void findInvoluntaryPlayer2(Boolean p2Sex, List<Player> playerList, List<AssignedPlayer> assignedPlayersToCourtsList) {

        int indexP1 = assignedPlayersToCourtsList.size() -1;
        Long courtId = assignedPlayersToCourtsList.get(indexP1).getCourtId();
        int position2 = 2;

        int lastIndex = playerList.size() -1;

        for (int i = lastIndex; i >= 0; i--) {

           if(playerList.get(i).isMaleSex() == p2Sex) {
               AssignedPlayer singlePlayer2=new AssignedPlayer(playerList.get(i).getPlayerId(),
                       p2Sex,
                       courtId,
                       position2,
                       playerList.get(i).getRoll(),
                       TypeOfMatch.SINGLE
               );
               assignedPlayersToCourtsList.add(singlePlayer2);
               playerList.remove(i);
            }
           else {
               // throw exception. no possible partners for single. #TODO set minimum event players?
           }

        }
       }

    // 0)
    // check for sex distribution.
    // Assign opponent sex = X.

    // 1)
    // check for friends (X) who like single

    // 2)
    // check for anybody (X) who likes single

    // 3)
    // for lowest roll (X) similar strength.

    // 4)
    // assign


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
