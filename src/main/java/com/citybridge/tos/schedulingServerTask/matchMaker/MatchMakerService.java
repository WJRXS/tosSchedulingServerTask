package com.citybridge.tos.schedulingServerTask.matchMaker;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.lottery.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.lottery.Lottery;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MatchMakerService {

    private final Lottery lottery;

    @Autowired
    public MatchMakerService(Lottery lottery) { this.lottery = lottery;}


    public List<AssignedPlayer> getAssignedPlayerList(Event event, List<Long> courtIdList, List<Player> playerList) {


        /** ---- 1A----- Lottery
         * add roll to players
         * sort list comparable to roll.
         */

        lottery.addRoll(playerList, event);


        /**
         *  --- 1B -------------
         *  check for surplus and sexes.
         *
         *  divide into playerlist & trash list.
         *
         *  next try to convert playerlist into assigned player, and use trash list for leftover fixes.
         *
         *  last:
         *  make assigned playerlist solid, and turn trash list into bench list.
         *
         *  next create the assignedplayer list.
         */




        /** ---- 2 -----
         * Puts all these variables into the matchMakerService
         * that returns:
         * players assigned to courts
         * players assigned to bench
         * List [playerId][CourtId][Position][Roll]  CourtId -1 = benched    Position: 1234 (13vs24)
         * List [Long][Long][int][int]
         * returns List<AssignedPlayer>
         */
        List<AssignedPlayer> assignedPlayerList = new ArrayList<>();
        return assignedPlayerList;
    }



    /**
     * When there are more players then courts available, low roll players will be
     * benched. Allowances are made so abit higher rolls might be benched for the
     * greater cause: male / female distribution.
     *
     *
     * When there are more courts available than the players can use. There are no
     * benched players. And thus there is no bench to use to fix an impossible
     * female/male player signup list.
     *
     * female/male divisions: The Double matches have priority, and thus if needed,
     * the single will consist out of a man and a female: BattleOfTheSexes.
     *
     * odd total players. If there is 1 female/male leftover, the lowest sex roll
     * will be benched in processing.
     *
     * If there are 3 leftovers, there is no bench , to fill a 3 player Mexican to a
     * 4man double. and thus a 3 man match will be assigned to the single court.
     */


    // EVENT SETTINGS
    // bench gives roll bonus of XXX

    private int oneTimeCompensationForHavingPlayedALeftOverMatch = 0;//500;// MATCH SETTING set by [ORGANIZER] , set to 0 to
    // turn off.
    private boolean newPlayersGetToPlayImmediately = false; // MATCH SETTING set by [ORGANIZER]



    private int nrOfCourts;
    private int nrOfPlayers;
    private int maxNrOfPlayers;
    private ArrayList<UserData> signUpList = new ArrayList<UserData>();
    private ArrayList<UserData> benchedPlayerList = new ArrayList<UserData>();


    public void processSignupList() {


        // 1) all players get a dice ROLL

        // 2) this ROLL is modified by wasBenched bolean, and wasPlayingLeftovermatch

        // 3) SORT this list by roll (comparable)
        /**
        private void addRollToSignedUpPlayersAndSort() {
            DiceRoller dice = new DiceRoller();
            for (UserData player : signUpList) {
                int roll = dice.getDiceRoll();

                roll = roll + oneTimeCompensationForHavingPlayedALeftOverMatch;

                if (player.getWasBenched()) {
                    roll = roll + 1000;
                }

                if (newPlayersGetToPlayImmediately == true) {
                    if (player.getIsSecondOrMoreMatchOfTheEvent() == false) {
                        roll = roll + 1000;
                    }
                }

                player.setRoll(roll);
            }
            Collections.sort(signUpList);
        }
      */




        // 4) checkSignUpListForBadConfiguration();
        private void checkSignUpListForBadConfiguration() {
            nrOfPlayers = signUpList.size();
            maxNrOfPlayers = nrOfCourts * 4;

            if (nrOfPlayers > maxNrOfPlayers) {
                moveSurplusPlayersToTrashBin();
                fixBadSexDistribution();
            }
            if (nrOfPlayers < maxNrOfPlayers) {
                int leftOver = (nrOfPlayers + 4) % 4;
                if (leftOver == 1) {
                    checkFemaleMaleAndRemoveLowestRoll();
                }
            }
        }

        /**
         * 1 leftover: check if a female or male need to be removed to fix
         * configuration. remove lowest rolled female or male.
         *
         */
        private void checkFemaleMaleAndRemoveLowestRoll() {
            int nrOfmalesCounter = 0;
            int nrOfFemalesCounter = 0;

            for (UserData p : signUpList) {
                if (p.getIsSexMale() == true) {
                    nrOfmalesCounter++;
                } else {
                    nrOfFemalesCounter++;
                }
            }

            int sizeIndex = nrOfPlayers - 1;
            if (nrOfmalesCounter % 2 == 1) {
                System.out.println("trying to remove leftover 1 with sex isMale = true");
                for (int i = sizeIndex; i > 0; i--) {
                    if (signUpList.get(i).getIsSexMale() == true) {
                        System.out.println("removing leftover 1 " + signUpList.get(i).getNumber() + " with roll: "
                                + signUpList.get(i).getRoll() + " and sex isMale =" + signUpList.get(i).getIsSexMale());
                        benchedPlayerList.add(signUpList.get(i));
                        signUpList.remove(i);
                        break;
                    }
                }
            } else if (nrOfFemalesCounter % 2 == 1) {
                System.out.println("trying to remove leftover 1 with sex isMale = false");
                for (int i = sizeIndex; i > 0; i--) {
                    if (signUpList.get(i).getIsSexMale() == false) {
                        System.out.println("removing leftover 1 " + signUpList.get(i).getNumber() + " with roll: "
                                + signUpList.get(i).getRoll() + " and sex isMale =" + signUpList.get(i).getIsSexMale());
                        benchedPlayerList.add(signUpList.get(i));
                        signUpList.remove(i);
                        break;
                    }
                }
            }
        }

        private void moveSurplusPlayersToTrashBin() {
            /**
             * #TODO Check if you have even number of sexes. put the removed player rolls in
             * a trash bin if sexes are uneven, check1 what sex is bottom list? is there an
             * opposite sex available in the trash bin? yes -> remove bottom list, and add
             * highest roll opposite wanted sex to make good. no -> what sex is bottom
             * list+1 --> is in trash bin? -- yes done no -> bottom list+2 exception: 1
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
            // the
            // highest roll on index = 0
            int maxPlayersIndex = (nrOfCourts * 4) - 1; // 10*4 = 40 -1 = 39
            int sizeIndex = signUpList.size() - 1; // size 38 -1 = 37

            for (int i = sizeIndex; i > maxPlayersIndex; i--) {
                System.out.println(
                        "removing " + signUpList.get(i).getNumber() + " with roll: " + signUpList.get(i).getRoll());

                benchedPlayerList.add(signUpList.get(i));
                signUpList.remove(i);
            }
            Collections.sort(benchedPlayerList);
        }

        private void fixBadSexDistribution() {
            /**
             * FIX BAD SEX DISTRIBUTION if sex are good? do nothing if sex are bad
             * ---Solution1: check who lowest roll is. // check if benchedPlayerList has
             * opposite sex // yes? get sex // no? solution2 -- solution2: check who lowest
             * roll is. --get sex // go up the list for the first opposite sex. // delete
             * that person with opposite sex // get highest same sex from benchedPlayerList.
             */

            int nrOfmalesCounter = 0;
            int nrOfFemalesCounter = 0;
            int signUpListSize = signUpList.size();
            for (UserData p : signUpList) {
                if (p.getIsSexMale() == true) {
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

                boolean lastSex = signUpList.get(signUpListSize - 1).getIsSexMale();
                System.out.println("lowest roll sex isMale =" + lastSex);

                boolean solution1 = false;
                boolean solution2 = false;
                outerloop: for (UserData trash : benchedPlayerList) {
                    if (trash.getIsSexMale() == !lastSex) { // solution 1
                        System.out.println("solution1: Removed lowest roll in signUpList with sex isMale= "
                                + signUpList.get(signUpListSize - 1).getIsSexMale());
                        System.out.println("solution1: Added highest roll trash with sex isMale= " + trash.getIsSexMale());
                        benchedPlayerList.add(signUpList.get(signUpListSize - 1));
                        signUpList.remove(signUpListSize - 1);
                        signUpList.add(trash);
                        benchedPlayerList.remove(trash);
                        solution1 = true;
                        break outerloop;
                    } else { // solution2
                        for (int i = signUpListSize - 2; i > -1; i--) {
                            if (signUpList.get(i).getIsSexMale() == !lastSex) {
                                System.out.println("went up the list and found & removed player with sex isMale = "
                                        + signUpList.get(i).getIsSexMale());

                                benchedPlayerList.add(signUpList.get(i));
                                signUpList.remove(i);
                                System.out.println("got player from benchedPlayerList with sex isMale = "
                                        + benchedPlayerList.get(0).getIsSexMale());

                                signUpList.add(benchedPlayerList.get(0));
                                benchedPlayerList.remove(0);
                                solution2 = true;
                                break outerloop;
                            }
                        }
                    }

                }
                if (solution1 == false && solution2 == false) {
                    System.out.println("total nr of court spots =" + nrOfCourts * 4);
                    System.out.println("total nr of players signed up =" + signUpListSize);
                }

            }
            Collections.sort(benchedPlayerList);

            nrOfmalesCounter = 0;
            nrOfFemalesCounter = 0;

            for (UserData p : signUpList) {
                if (p.getIsSexMale() == true) {
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

        private void setBenchBoolean() {
            for (UserData player : signUpList) {
                player.setWasBenched(false);
            }
            for (UserData bencher : benchedPlayerList) {
                bencher.setWasBenched(true);
            }

        }

        private void resetAssignedBoolean() {
            for (UserData player : signUpList) {
                player.setIsAssigned(false);
            }
            for (UserData bencher : benchedPlayerList) {
                bencher.setIsAssigned(false);
            }

        }
        //setBenchBoolean();
        //resetAssignedBoolean();

    }



}
