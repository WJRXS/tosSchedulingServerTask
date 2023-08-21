package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch;


import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.court.TypeOfCourt;
import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleStraight.DoubleStraightMatch;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleMix.DoubleMixMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoubleMatchCreator {


    private final DoubleStraightMatch doubleStraightMatch;
    private final DoubleMixMatch doubleMixMatch;

    @Autowired
    public DoubleMatchCreator(DoubleStraightMatch doubleStraightMatch, DoubleMixMatch doubleMixMatch) {
        this.doubleStraightMatch = doubleStraightMatch;
        this.doubleMixMatch = doubleMixMatch;
    }

    public void createDoubleMatch(List<AssignedPlayer> assignedPlayersToCourtsList,
                                  List<TypeOfMatch> typeOfMatchList,
                                  List<Court> courtList,
                                  List<Player> playerList,
                                  Event event
                                  ) {
    int numberOfMatches = typeOfMatchList.size();

        for (int i = 0; i < numberOfMatches; i++) {
            TypeOfMatch typeOfMatch=  findPlayer1(playerList, courtList, assignedPlayersToCourtsList, typeOfMatchList);
            boolean isMatchTypeLast = isMatchTypeLast(assignedPlayersToCourtsList, typeOfMatchList);
            createSpecificDouble(playerList, assignedPlayersToCourtsList, typeOfMatch, isMatchTypeLast, event);
        }
    }

    /**
     * FIND Player1
     */
    private TypeOfMatch findPlayer1(List<Player> playerList, List<Court> courtList, List<AssignedPlayer> assignedPlayersToCourtsList, List<TypeOfMatch> typeOfMatchList) {

        Player player1 = playerList.get(0);
        Long player1Id = player1.getPlayerId();
        boolean player1MaleSex= player1.isMaleSex();
        int position1 = 1;

        List<TypeOfMatch> typeOfMatchPreference = player1.getTypeOfMatchPreference();
        TypeOfMatch typeOfMatch = getTypeOfMatch(typeOfMatchList, typeOfMatchPreference);

        List<TypeOfCourt> courtPreference = player1.getCourtPreference();
        Long courtId = findCourtId(courtPreference, courtList);

                AssignedPlayer doublePlayer1 = new AssignedPlayer(
                        player1Id,
                        player1MaleSex,
                        courtId,
                        position1,
                        player1.getRoll(),
                        typeOfMatch
                );

                assignedPlayersToCourtsList.add(doublePlayer1);

                playerList.remove(player1);

                return typeOfMatch;
   }

    /**
     * calls the specific class needed to create the double
     0* @param typeOfMatch
     */
   private boolean createSpecificDouble(List<Player> playerList,
                                     List<AssignedPlayer> assignedPlayersToCourtsList,
                                     TypeOfMatch typeOfMatch,
                                     boolean isMatchTypeLast,
                                        Event event) {
       boolean b = false;
        if (typeOfMatch == TypeOfMatch.DOUBLE_MALE | typeOfMatch == TypeOfMatch.DOUBLE_FEMALE) {
            b = doubleStraightMatch.createDoubleStraightMatch(playerList, assignedPlayersToCourtsList, isMatchTypeLast, event);
        }  else if (typeOfMatch == TypeOfMatch.DOUBLE_MIX) {
            b = doubleMixMatch.createDoubleMixMatch(playerList, assignedPlayersToCourtsList, isMatchTypeLast, event);
        }
                else {
                    //throw exception
        }
                return b;
   }

    /**
     * Util
     * determine what kind of Match player1 will be assigned to. check his preference.
     * @param typeOfMatchList
     * @param typeOfMatchPreference
     * @return
     */
    private TypeOfMatch getTypeOfMatch(List<TypeOfMatch> typeOfMatchList, List<TypeOfMatch> typeOfMatchPreference) {


        TypeOfMatch typeOfMatch = typeOfMatchList.get(0);

        for (TypeOfMatch type: typeOfMatchPreference) {
            if (typeOfMatchList.contains(type)) {
                typeOfMatchList.remove(type);
                return type;
            } else { // #TODO redundant return, might just leave the bottom one.
               typeOfMatchList.remove(0);
                return typeOfMatch;
            }
        }
        return typeOfMatch;
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
     * find out if the MatchType is the last of its type,
     * AND
     * the number of player configuration is limited by few players remaining.
     */
    private boolean isMatchTypeLast(List<AssignedPlayer> assignedPlayersToCourtsList, List<TypeOfMatch> typeOfMatchList) {
        int index = assignedPlayersToCourtsList.size();
        TypeOfMatch typeOfMatch = assignedPlayersToCourtsList.get(index).getTypeOfMatch();

        int maleCounter = 0;
        int femaleCounter = 0;
        int mixCounter = 0;

        for (TypeOfMatch type: typeOfMatchList) {
            if (type == TypeOfMatch.DOUBLE_MALE) { maleCounter++;}
            else if (type == TypeOfMatch.DOUBLE_FEMALE) {femaleCounter++;}
                else if (type == TypeOfMatch.DOUBLE_MIX) {mixCounter++; }
            else {
                // throw exception
            }
        }

        if (    maleCounter >=2 &&
                femaleCounter >= 2 &&
                mixCounter >= 2) {
            return false;
        } else if (
                (typeOfMatch == TypeOfMatch.DOUBLE_MALE) &&
                (maleCounter >= 1 && mixCounter >= 1)
        ) {
            return false;
        } else if (
                (typeOfMatch == TypeOfMatch.DOUBLE_FEMALE) &&
                        (femaleCounter >= 1 && mixCounter >= 1)
        ) {
            return false;
        } else if (
                (typeOfMatch == TypeOfMatch.DOUBLE_MIX) &&
                        (maleCounter >= 1 && femaleCounter >= 1)
        ) {
            return false;
        } else if (maleCounter == 1 && mixCounter ==0) {
            return true;
        } else if (femaleCounter == 1 && mixCounter == 0) {
            return true;
        } else if (mixCounter == 1 && (maleCounter ==0 || femaleCounter == 0)) {
            return true;
        } else {
            return true;
                    // throw exception
        }
 }



}
