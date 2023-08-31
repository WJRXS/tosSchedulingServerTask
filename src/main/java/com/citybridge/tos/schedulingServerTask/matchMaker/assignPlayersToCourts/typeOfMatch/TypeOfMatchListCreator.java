package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.typeOfMatch;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeOfMatchListCreator {

    /**
     * returns a list of double matches. (Mix MMFF, Male MMMM or Female FFFF)
     * taken into account is the event setting: mixVersusStraight.
     * does the administrator want many mix doubles, average or only a few mix double?
     * @param event
     * @param playerList
     * @return
     */
    public List<TypeOfMatch> createTypeOfMatchList(Event event,
                                                    List<Player> playerList) {

        List<TypeOfMatch> typeOfMatchList = new ArrayList<>();

        int nrOfPlayers = playerList.size();
        int nrOfDoubleMatches = (int) (nrOfPlayers / 4); //#TODO check for rounding problems
        int mixVersusStraight = event.getMixVersusStraight();

        int maleCounter = 0;
        int femaleCounter = 0;

        for(Player p: playerList) {
            if (p.isMaleSex()) {
                maleCounter++;
            } else {
                femaleCounter++;
            }
        }

        int maximumMixMatches;
        int minimumMixMatches;
        // MORE MALES
        if (maleCounter >= femaleCounter & femaleCounter >= 2) {
            maximumMixMatches = femaleCounter/2;

            if (femaleCounter%4 == 0) {
                minimumMixMatches = 0; }
            else if (femaleCounter%4 == 2) {
                minimumMixMatches = 1;
            } else {
                // throw exception
                minimumMixMatches = 0;
            }

            double delta = (maximumMixMatches - minimumMixMatches) * (mixVersusStraight / 100);
            int roundedDelta = (int) (delta +0.5);

            int nrOfMixMatches = minimumMixMatches + roundedDelta;

            for (int i = 0; i < nrOfDoubleMatches; i++) {
                if (nrOfMixMatches > 0) {
                    typeOfMatchList.add(TypeOfMatch.DOUBLE_MIX);
                    nrOfMixMatches--;
                }
                else if (nrOfMixMatches ==0) {
                    typeOfMatchList.add(TypeOfMatch.DOUBLE_MALE);
                }
            }
        }

        // MORE FEMALES
        else if (femaleCounter >= maleCounter & maleCounter >= 2) {
            maximumMixMatches = maleCounter / 2;

            if (maleCounter%4 == 0) {
                minimumMixMatches = 0; }
            else if (maleCounter%4 == 2) {
                minimumMixMatches = 1;
            } else {
                // throw exception
                minimumMixMatches = 0;
            }

            double delta = (maximumMixMatches - minimumMixMatches) * (mixVersusStraight / 100);
            int roundedDelta = (int) (delta +0.5);

            int nrOfMixMatches = minimumMixMatches + roundedDelta;

            for (int i = 0; i < nrOfDoubleMatches; i++) {
                if (nrOfMixMatches > 0) {
                    typeOfMatchList.add(TypeOfMatch.DOUBLE_MIX);
                    nrOfMixMatches--;
                }
                else if (nrOfMixMatches ==0) {
                    typeOfMatchList.add(TypeOfMatch.DOUBLE_FEMALE);
                }
            }

        // ONLY FEMALES
        } else if (maleCounter == 0) {
            for (int i = 0; i < nrOfDoubleMatches; i++) {
                typeOfMatchList.add(TypeOfMatch.DOUBLE_FEMALE);
            }

        // ONLY MALES
        } else if (femaleCounter == 0) {
            for (int i = 0; i < nrOfDoubleMatches; i++) {
                typeOfMatchList.add(TypeOfMatch.DOUBLE_MALE);
            }
        }

        return typeOfMatchList;
    }
}
