package com.citybridge.tos.schedulingServerTask.matchMaker.playerListChecker;

import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PlayerListChecker {

    List<Player> playerList;
    List<Player> playerBinList;
    int nrOfCourts;

        public List<Player> checkForBadConfiguration(List<Player> playerList, int nrOfCourts) {
            this.playerList = playerList;
            this.nrOfCourts = nrOfCourts;

            int nrOfPlayers = playerList.size();
            int maxNrOfPlayers = nrOfCourts * 4;

            // if too many players
            if (nrOfPlayers > maxNrOfPlayers) {
                moveSurplusPlayersToTrashBin();
                fixBadSexDistribution();
            }

            // if too few players and 1 leftover (no mexican possible)
            if (nrOfPlayers < maxNrOfPlayers) {
                int leftOver = (nrOfPlayers + 4) % 4;
                if (leftOver == 1) {
                    checkFemaleMaleAndRemoveLowestRoll();
                }
            }
            return playerBinList;
    }

    private void moveSurplusPlayersToTrashBin() {
        /**
         * #TODO Check if you have even number of sexes. put the removed player rolls in
         * a trash bin if sexes are uneven, check1 what sex is bottom list? is there an
         * opposite sex available in the trash bin? yes -> remove bottom list, and add
         * highest roll opposite wanted sex to make good. no -> what sex is bottom
         * list+1 --> is in trash bin? -- yes done, no -> bottom list+2 exception: 1
         * woman? she'll always be removed. lets make a safety. if just benched, you can
         * never be removed.
         *
         * if the available courts are NOT filled, - and there is modulus4= 1 player
         * leftover. the lowest roll will be benched! +Fixing sex distribution! - and
         * there is modulus4= 2 players leftover there will be a single. +Fixing sex
         * distribution! - and there is modulus4= 3 players leftover there will be a
         * mexican. +Fixing sex distribution!
         */

        // since we removed from bottom up, we want to sort the benchedPlayerList with
        // the highest roll on index = 0
        int maxPlayersIndex = (nrOfCourts * 4) - 1; // 10*4 = 40 -1 = 39
        int sizeIndex = playerList.size() - 1; // size 42 -1 = 41, en dan gaan er dus 2 players, op i = 41 en i = 40, de trashbin in?

        for (int i = sizeIndex; i > maxPlayersIndex; i--) {
            System.out.println(
                    "removing " + playerList.get(i).getPlayerId() + " with roll: " + playerList.get(i).getRoll());

            playerBinList.add(playerList.get(i));
            playerList.remove(i);
        }
        Collections.sort(playerBinList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p1.getRoll() > p2.getRoll() ? -1 : p1.getRoll() == p2.getRoll() ? 0 : 1;
            }
    });
}

    private void fixBadSexDistribution() {

        /** [MALE - FEMALE]
         * [0-4] [4-0] sameSex ---OR--- [2-2] mix
         * leftovers are good.
         *
         * but what if you have
         * [3-1] ----OR---- [1-3]
         * is the lowest roll player a [3]? can you find an OPPOSITE SEX in the BIN? to make a MIX?
         * SOLUTION1 yes? fix it.                                     ----->  [2-2]
         * SOLUTION2 no? find SAMESEX in the BIN and bench the [1].              ----->  [4-0] or [0 4]
         *
         * is the lowest roll player a [1]? can you find an OPPOSITE SEX in the BIN? to make a SAMESEX?
         * SOLUTION1 yes? fix it.                                     ---->   [4-0] or [0-4]
         * SOLUTION2 no? find SAMESEX in the BIN and bench the lowest of the [3]. ----->  [2-2]
         *
         */


        int nrOfmalesCounter = 0;
        int nrOfFemalesCounter = 0;
        int nrOfPlayers = playerList.size();
        for (Player p : playerList) {
            if (p.isMaleSex() == true) {
                nrOfmalesCounter++;
            } else {
                nrOfFemalesCounter++;
            }
        }

        if (nrOfmalesCounter % 2 == 0) { // if sex are good? do nothing
            System.out.println(
                    "all good nrOfmalesCounter%2= " + nrOfmalesCounter % 2 + " nrOfmalesCounter = " + nrOfmalesCounter);
            System.out.println("all good nrOfFemalesCounter%2= " + nrOfFemalesCounter % 2 + " nrOfFemalesCounter = "
                    + nrOfFemalesCounter);
        } else {
            System.out.println("Bad setup nrOfmalesCounter%2= " + nrOfmalesCounter % 2 + " nrOfmalesCounter = "
                    + nrOfmalesCounter);
            System.out.println("Bad setup nrOfFemalesCounter%2= " + nrOfFemalesCounter % 2 + " nrOfFemalesCounter = "
                    + nrOfFemalesCounter);

            /**
             * Determine the sex of the lowest ROLL
             */
            boolean lowestSex = playerList.get(nrOfPlayers - 1).isMaleSex();
            System.out.println("lowest roll sex isMaleSex =" + lowestSex);

            boolean solution1 = false;
            boolean solution2 = false;

            outerloop: for (Player trash : playerBinList) {  // FIND OPPOSITE SEX
                if (trash.isMaleSex() == !lowestSex) { // solution 1
                    System.out.println("solution1: Removed lowest roll in signUpList with sex isMale= "
                            + playerList.get(nrOfPlayers - 1).isMaleSex());
                    System.out.println("solution1: Added highest roll trash with sex isMale= " + trash.isMaleSex());
                    playerBinList.add(playerList.get(nrOfPlayers - 1));
                    playerList.remove(nrOfPlayers - 1);
                    playerList.add(trash);
                    playerBinList.remove(trash);
                    solution1 = true;
                    break outerloop;
                } else { // solution2
                    for (int i = nrOfPlayers - 2; i > -1; i--) { /// -1 for index && -1 to skip lowest roll = -2


                        if (playerList.get(i).isMaleSex() == !lowestSex) {
                            System.out.println("went up the list and found & removed player with opposite sex isMale = "
                                    + playerList.get(i).isMaleSex());
                            playerBinList.add(playerList.get(i));
                            playerList.remove(i);


                            for (Player p: playerBinList) {
                                if (p.isMaleSex() == lowestSex) {
                                    playerList.add(p);
                                    playerBinList.remove(p);
                                    solution2 = true;
                                    System.out.println("got the highest player from benchedPlayerList with same sex isMale = "+p.isMaleSex());
                                    break outerloop;
                                }
                            }
                        }
                    }
                }
            }

            if (solution1 == false && solution2 == false) {
                System.out.println("ERROR inside fix badSexDistribution");
                System.out.println("total nr of court spots =" + nrOfCourts * 4);
                System.out.println("total nr of players signed up =" + nrOfPlayers);
                System.out.println("but something went wrong because solution 1 & 2 are false.");
            }

        }


        Collections.sort(playerBinList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p1.getRoll() > p2.getRoll() ? -1 : p1.getRoll() == p2.getRoll() ? 0 : 1;
            }
        });

        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p1.getRoll() > p2.getRoll() ? -1 : p1.getRoll() == p2.getRoll() ? 0 : 1;
            }
        });

        /**
         * TEST: final configuration of playerList
         */
        nrOfmalesCounter = 0;
        nrOfFemalesCounter = 0;

        for (Player p : playerList) {
            if (p.isMaleSex() == true) {
                nrOfmalesCounter++;
            } else {
                nrOfFemalesCounter++;
            }
        }
        System.out.println(
                "FINAL setup nrOfmalesCounter%2= " + nrOfmalesCounter % 2 + " nrOfmalesCounter = " + nrOfmalesCounter);
        System.out.println("FINAL setup nrOfFemalesCounter%2= " + nrOfFemalesCounter % 2 + " nrOfFemalesCounter = "
                + nrOfFemalesCounter);
    }


    /** scenario: TOO FEW PLAYERS and leftover = 1
     * check if a female or male need to be removed to fix configuration.
     * remove lowest rolled female or male.
     */
    private void checkFemaleMaleAndRemoveLowestRoll() {
        int nrOfmalesCounter = 0;
        int nrOfFemalesCounter = 0;

        for (Player p : playerList) {
            if (p.isMaleSex() == true) {
                nrOfmalesCounter++;
            } else {
                nrOfFemalesCounter++;
            }
        }


        int nrOfPlayers = playerList.size();
        int sizeIndex = nrOfPlayers - 1;

        if (nrOfmalesCounter % 2 == 1) {
            System.out.println("trying to remove leftover 1 with sex isMale = true");
            for (int i = sizeIndex; i > 0; i--) {
                if (playerList.get(i).isMaleSex() == true) {
                    System.out.println("removing leftover 1 " + playerList.get(i).getPlayerId() + " with roll: "
                            + playerList.get(i).getRoll() + " and sex isMale =" + playerList.get(i).isMaleSex());
                    playerBinList.add(playerList.get(i));
                    playerList.remove(i);
                    break;
                }
            }
        } else if (nrOfFemalesCounter % 2 == 1) {
            System.out.println("trying to remove leftover 1 with sex isMale = false");
            for (int i = sizeIndex; i > 0; i--) {
                if (playerList.get(i).isMaleSex() == false) {
                    System.out.println("removing leftover 1 " + playerList.get(i).getPlayerId() + " with roll: "
                            + playerList.get(i).getRoll() + " and sex isMale =" + playerList.get(i).isMaleSex());
                    playerBinList.add(playerList.get(i));
                    playerList.remove(i);
                    break;
                }
            }
        }
    }



}
