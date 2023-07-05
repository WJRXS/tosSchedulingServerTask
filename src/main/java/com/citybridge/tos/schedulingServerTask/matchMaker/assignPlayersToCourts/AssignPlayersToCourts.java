package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.event.TosType;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatchListCreator;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.mexicanMatch.MexicanMatchCreator;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.singleMatch.SingleMatchCreator;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;




@Service
public class AssignPlayersToCourts {

    private final SingleMatchCreator singleMatchCreator;
    private final MexicanMatchCreator mexicanMatchCreator;
    private final TypeOfMatchListCreator typeOfMatchListCreator;

    @Autowired
    public AssignPlayersToCourts(SingleMatchCreator singleMatchCreator, MexicanMatchCreator mexicanMatchCreator, TypeOfMatchListCreator typeOfMatchListCreator) {
        this.singleMatchCreator = singleMatchCreator;
        this.mexicanMatchCreator = mexicanMatchCreator;
        this.typeOfMatchListCreator = typeOfMatchListCreator;
    }


    /**
     * Action I:
     * Many factors have to be considered.
     * first of all what kind of TOS?
     TosType tosType =

     * Action II:
     * Load event settings
     * what %MIX has the organiser set? does the organiser want as many mixes as pissible (100%)?
     * or the most straight matches (0 %)
     *
     *
     *
     * Check1:
     * 1A) Nr courts [vs] Nr players?
     * players < courts: create single / mexican (possible 1 on bench)
     * players = courts: perfect, might end up with weird MFFF or FMMM double
     * players > courts: standard scenario: benched players.
     *
     * #Old Notes 1A
     * female/male divisions: The Double matches have priority, and thus if needed,
     * - the single will consist out of a man and a female: BattleOfTheSexes.
     * - odd total players. If there is 1 female/male leftover, the lowest sex roll
     *   will be benched in processing.
     *  - If there are 3 leftovers, there is no bench ,
     *   and thus a 3 man match will be assigned to the single court.
     *
     *
     * 1B) Nr Male & Nr Female - Mix & Non-Mix
     * Create a list of match type(mix double, f double, m double)
     *
     * 1c)
     * start creating those matches and add them to the assignedplayercourtlist
     *
     *

     *
     *
     *
     */

    public List<AssignedPlayer> getAssignedPlayersToCourtsList(Event event, List<Court> courtIdList, List<Player> playerList, List<Player> playerBinList) {
        List<AssignedPlayer> assignedPlayersToCourtsList = new ArrayList<AssignedPlayer>();

        /**
         * #TODO implement different tostypes, like all singles.
         */
        TosType tosType = event.getTosType();
        //if (tosType == TosType.DOUBLE) {}
        /**
         * Check 0
         */
        createLeftOverMatch(event, playerList, assignedPlayersToCourtsList, courtIdList);

        /**
         * Action 1
         * get the Mix % from event
         * determine the number of Mix Doubles, and thus, Male doubles and Female Doubles.
         * Make a list of the MatchTypes.
         *
         *
         * // EVENT: Type of TOS setting
         * // private TosType tosType = TosType.DOUBLE;  // focus on double.
         * // private int mixVersusStraight = 0; // 0 most mix matches, 100 for most straight matches.
         */
        List<TypeOfMatch> typeOfMatchList = typeOfMatchListCreator.createTypeOfMatchList(event, playerList);

        /**
         * Action2
         * use the matchlist to create the actual doubles.
         */


        /**
         * Action 3
         * return the assigned player list.
         */
        return assignedPlayersToCourtsList;
        }








    /**
     * --------------------   CREATE COURT LIST --------------
     * #TODO only tosType setting = DOUBLE
     * @param event
     *
     *
     * Step by Step by Step plan given variables:
     *  number of Players (dont need nr of courts, you know that when you look at nr players)
     *
     *  CHeck0: LEFTOVER MATCH
     *  - Single / Mexican
     *  - assign players
     *  (add assigned players, add assigned courts?)
     *  (remove from lists: court id, playerid )
     *
     *
     *
     *
     *  Check0_1: is there going to be a SINGLE match?
     *  if [single court] go through list who wants to play single. -> make list ->
     *  highest gets +if friends who wants single +else similar strength.
     *  if none found --> bottom roll gets single. get sex. get partner = same sex &
     *  similarstrength and semi-low roll.
     *
     *  Check0_2: is there going to be a MEXICAN match?
     *
     *
     *  Check3: Mix or same Sex Matchsettings [Mix]-[Same Sex] % get lowest number
     *  from list - woman or men. max mix courts = number /2 number of mix courts =
     *  max number of mix courts * % same sex courts = nrofcourts - mix courts
     *  courtlist[] = add same sex - add mix --add same sex -- add mix --add same sex
     *  --->untill both are at zero.
     *
     *  check 3 first court same sex: Highest roll & wants to do court type: Does
     *  player have mutual friends. add highest roll friend as opponent or partner.
     *
     *  No mutual friends: friends - strength %roll
     */

    private void createLeftOverMatch(Event event,
                                     List<Player> playerList,
                                     List<AssignedPlayer> assignedPlayersToCourtsList,
                                     List<Court> courtList) {

        int nrOfPlayers = playerList.size();
        int leftover = nrOfPlayers%4;


        if(leftover == 1) {
            //throw exception
        } else if(leftover == 2) {
            // SINGLE
            singleMatchCreator.createSingleMatch(playerList, assignedPlayersToCourtsList, courtList);

        } else if (leftover == 3) {
            // MEXICAN
            mexicanMatchCreator.createMexicanMatch(playerList, assignedPlayersToCourtsList, courtList);
        } else if (leftover == 0 | leftover == 4) {
            // nothing
        } else {
            // throw exception
        }
  }


    /**
     *
     */








        }

/**
 * #TODO incorporate settings
 */
// MATCH SETTING set by [ORGANIZER]
// used by CreateDoubleMatch {}
// private int friendRequestChance;
// MATCH SETTING set by [ORGANIZER]
// used by CreateStandardRandomDoubleCourtList {}
// private double mixPercentage = 50;
// MATCH SETTING set by [ORGANIZER]
// set to 0 to get minimum mix matches, set to 100 to get maximum mix matches.
// used by CreateStandardRandomDoubleCourtList {}


/**
 * Step by Step by Step plan given variables: number of Players (dont need nr of
 * courts, you know that when you look at nr players)
 *
 * Check1: are there anough players? is there going to be a single match? if
 * [single court] go through list who wants to play single. -> make list ->
 * highest gets +if friends who wants single +else similar strength. if none
 * found..bottom roll gets single. get sex. get partner = same sex &
 * similarstrength.
 *
 * Check2: Mix or same Sex Matchsettings [Mix]-[Same Sex] % get lowest number
 * from list - woman or men. max mix courts = number /2 number of mix courts =
 * max number of mix courts * % same sex courts = nrofcourts - mix courts
 * courtlist[] = add same sex - add mix --add same sex -- add mix --add same sex
 * --->untill both are at zero.
 *
 * check 3 first court same sex: Highest roll & wants to do court type: Does
 * player have mutual friends. add highest roll friend as opponent or partner.
 *
 * No mutual friends: friends - strength %roll
 */


/**
 * PROCESS THE RAW SIGNUP LIST INTO playerList and benchList
 */
  //      ProcessSignUpList processSignUpList = new ProcessSignUpList(signUpList, benchedPlayerList, nrOfCourts);
 //       System.out.println("test process signup list after execution: signuplist size = "+ signUpList.size()+" benched list size = " +benchedPlayerList.size());

  //      CreateStandardRandomDoubleCourtList createCourtList = new CreateStandardRandomDoubleCourtList(signUpList, typeOfCourStringList, nrOfCourts);


    /*    for (String type : typeOfCourStringList) {
        switch (type) {
        case "MixDouble":
        System.out.println("test making mix court");
        CreateDoubleMatch createDoubleMatch = new CreateDoubleMatch(signUpList, assignedPlayerRollList, "MixDouble");
        //	assignPlayersToMixMatch();
        break;
        case "FemaleDouble":
        System.out.println("test making female court");
        CreateDoubleMatch createFemaleDoubleMatch = new CreateDoubleMatch(signUpList, assignedPlayerRollList, "FemaleDouble");
        //	assignPlayersToFemaleOrMaleMatch(false);
        break;
        case "MaleDouble":
        System.out.println("test making male court");
        CreateDoubleMatch createMaleDoubleMatch = new CreateDoubleMatch(signUpList, assignedPlayerRollList, "MaleDouble");
        //	assignPlayersToFemaleOrMaleMatch(true);
        break;
        case "FemaleSingle":
        System.out.println("test making Female Single court");
        CreateSingleMatch createSingleMatchFemale = new CreateSingleMatch(signUpList, assignedPlayerRollList, "FemaleSingle");
        //leftOverPlayerRollList = createSingleMatchFemale.getAssignedSinglePlayerRollList();
        break;
        case "MaleSingle":
        System.out.println("test making Male Single court");
        CreateSingleMatch createSingleMatchMale = new CreateSingleMatch(signUpList, assignedPlayerRollList, "MaleSingle");
        //leftOverPlayerRollList = createSingleMatchMale.getAssignedSinglePlayerRollList();
        break;
        case "MixSingle":
        System.out.println("test making SingleMix court");
        CreateSingleMatch createSingleMatchMix = new CreateSingleMatch(signUpList, assignedPlayerRollList, "MixSingle");
        //leftOverPlayerRollList = createSingleMatchMix.getAssignedSinglePlayerRollList();
        break;
        case "MexicanMMM":
        System.out.println("test making MexicanMMM court");
        CreateMexicanMatch createMexicanMatchMMM = new CreateMexicanMatch(signUpList, assignedPlayerRollList, "MexicanMMM");
        break;
        case "MexicanFFF":
        System.out.println("test making MexicanFFF court");
        CreateMexicanMatch createMexicanMatchFFF = new CreateMexicanMatch(signUpList, assignedPlayerRollList, "MexicanFFF");
        break;
        case "MexicanMFF":
        System.out.println("test making MexicanMFF court");
        CreateMexicanMatch createMexicanMatchMFF = new CreateMexicanMatch(signUpList, assignedPlayerRollList, "MexicanMFF");
        break;
        case "MexicanMMF":
        System.out.println("test making MexicanMMF court");
        CreateMexicanMatch createMexicanMatchMMF = new CreateMexicanMatch(signUpList, assignedPlayerRollList, "MexicanMMF");
        break;
        case "FemaleMix":
        System.out.println("test making FemaleMix court");
        CreateBadMixDoubleMatch createBadMixMatchMFFF = new CreateBadMixDoubleMatch(signUpList, assignedPlayerRollList, "FemaleMix");
        break;
        case "MaleMix":
        System.out.println("test making MaleMix court");
        CreateBadMixDoubleMatch createBadMixMatchMMMF = new CreateBadMixDoubleMatch(signUpList, assignedPlayerRollList, "MaleMix");
        break;
        }
        }

        }
public ArrayList<UserData> getBenchedPlayerList() {
        // TODO Auto-generated method stub
        return benchedPlayerList;
        }

public ArrayList<String> getStringTypeOfCourtList() {
        // TODO Auto-generated method stub
        return typeOfCourStringList;
        }

        }*/

/*
private void assignPlayersToMixMatch() {
	// #TODO load friend--strength
	// #TODO load if partner or opponent friend
	int indexFirstPlayer = getHighestRollFreeIndex();
	int firstPlayerStrength = signUpList.get(indexFirstPlayer).getStrength();
	boolean firstPlayerSex = signUpList.get(indexFirstPlayer).getIsSexMale();
	signUpList.get(indexFirstPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexFirstPlayer));

	int indexSecondPlayer = getIndexSpecificSexAfterComparingStrength(firstPlayerStrength, firstPlayerSex);
	int secondPlayerStrength = signUpList.get(indexSecondPlayer).getStrength();
	signUpList.get(indexSecondPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexSecondPlayer));

	int teamAStrength = (firstPlayerStrength + secondPlayerStrength) / 2;
	int indexThirdPlayer = getIndexSpecificSexAfterComparingStrength(teamAStrength, !firstPlayerSex);
	int thirdPlayerStrength = signUpList.get(indexThirdPlayer).getStrength();
	signUpList.get(indexThirdPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexThirdPlayer));

	int strengthLeftover = (firstPlayerStrength + thirdPlayerStrength) - secondPlayerStrength;
	// [2 player team A - 1 player Team B]
	int indexFourthPlayer = getIndexSpecificSexAfterComparingStrength(strengthLeftover, !firstPlayerSex);
	signUpList.get(indexFourthPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexFourthPlayer));
}

private void assignPlayersToFemaleOrMaleMatch(boolean sexNeeded) {

	boolean sex = sexNeeded;
	int indexFirstPlayer = getIndexHighestRollSpecificSex(sex);
	int firstPlayerStrength = signUpList.get(indexFirstPlayer).getStrength();
	signUpList.get(indexFirstPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexFirstPlayer));

	int indexSecondPlayer = getSecondPlayerIndexSpecificSexAfterComparingStrength(firstPlayerStrength, sex);
	int secondPlayerStrength = signUpList.get(indexSecondPlayer).getStrength();
	signUpList.get(indexSecondPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexSecondPlayer));

	// int teamAStrength = (firstPlayerStrength + secondPlayerStrength) / 2;
	int indexThirdPlayer = getThirdPlayerIndexSpecificSexAfterComparingStrength(firstPlayerStrength,
			secondPlayerStrength, sex);
	int thirdPlayerStrength = signUpList.get(indexThirdPlayer).getStrength();
	signUpList.get(indexThirdPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexThirdPlayer));

	int strengthLeftover = (firstPlayerStrength + thirdPlayerStrength) - secondPlayerStrength;
	// [2 player team A - 1 player Team B]
	int indexFourthPlayer = getIndexSpecificSexAfterComparingStrength(strengthLeftover, sex);
	signUpList.get(indexFourthPlayer).setIsAssigned(true);
	assignedPlayerRollList.add(signUpList.get(indexFourthPlayer));
}

/**
 * gets INDEX of highest roll of who -[a] is not assigned yet.
 *
 * @return index
 */
/*
private int getHighestRollFreeIndex() {
	int index = 0;

	for (UserData player : signUpList) {
		if (!player.getIsAssigned()) {
			break;
		}
		index++;
	}
	return index;
}

/**
 * gets INDEX of highest roll of who - [a] is not assigned yet. - [b] with
 * specific sex
 *
 * @return index
 */
/*
private int getIndexHighestRollSpecificSex(boolean sexNeeded) {
	int index = 0;

	for (UserData player : signUpList) {
		if (!player.getIsAssigned() && player.getIsSexMale() == sexNeeded) {
			break;
		}
		index++;
	}
	return index;
}

/**
 * gets INDEX of a player who: -[a] has the least strength difference. enabled
 * by UserData class implements Comparable<UserData>
 *
 * @param compareList
 * @return index
 */
/*
private int getIndexWithMinimumDifferenceInStrength(ArrayList<Integer> compareList) {
	return compareList.indexOf(Collections.min(compareList));
}

/**
 * get INDEX of highest roll of who -[a] is not assigned yet - [b] similar
 * strength as per strength needed to balance the match.
 *
 * @param strength
 * @return playerRollIndex
 */
/*
private int getIndexSpecificSexAfterComparingStrength(int strength, boolean sexNeeded) {
	int compareStrength = strength;
	boolean sex = sexNeeded;

/*
	ArrayList<UserData> playerOptionsList = getSameSexOptionList(sex);
	int index = 0;

	if (playerOptionsList.size() > 1) {
		ArrayList<Integer> compareList = new ArrayList<Integer>();
		for (UserData player : playerOptionsList) {
			compareList.add(Math.abs(compareStrength - player.getStrength()));
		}
		index = getIndexWithMinimumDifferenceInStrength(compareList);
	} else if (playerOptionsList.size() == 1) { // nothing to compare, no such element, just get the zero
		index = 0;
	}

	int playerNumber = playerOptionsList.get(index).getNumber();
	int playerRollIndex = getPlayerRollIndexFromID(playerNumber);

	return playerRollIndex;
}

/**
 * For the last match to be assigned of it's type, it is vital to be aware of
 * that fact before assigning the SECOND player. Otherwise you could end up with
 * strength differences: p1 = 7... compare.. p2=7...but next is just fill up
 * with whatever is left. p3=5 and p4=9. 7+9 vs 7+5. while it should have been
 * 9+5 vs 7+7!!!!!!!
 *
 * get INDEX of highest roll of who - [a] is not assigned yet - [b] similar
 * strength as per strength needed to balance the match.
 *
 * @param strength
 * @return playerRollIndex
 */
/*
private int getSecondPlayerIndexSpecificSexAfterComparingStrength(int strength, boolean sexNeeded) {
	int firstPlayerStrength = strength;
	int compareStrength = strength;
	boolean sex = sexNeeded;

/*
	ArrayList<UserData> playerOptionsList = getSameSexOptionList(sex);
	int index = 0;

/*
	if (playerOptionsList.size() > 3) { // plenty of choice, this is not the last female/or/male match
		System.out.println("TEST SECOND INDEX. optionlist.size() =" + playerOptionsList.size());
		ArrayList<Integer> compareList = new ArrayList<Integer>();
		for (UserData player : playerOptionsList) {
			compareList.add(Math.abs(compareStrength - player.getStrength()));
		}
		index = getIndexWithMinimumDifferenceInStrength(compareList);
	} else if (playerOptionsList.size() == 3) { // last female/or/male match
		System.out.println("TEST SECOND INDEX. optionlist.size() =" + playerOptionsList.size());
		// index UserData works: TeamA --TeamB--- TeamA---TeamB
		// options fp+0 vs 1+2 (list order: fp 1 0 2)----or--- fp+1 vs 0+2 (list order:
		// fp 0 1 2)----or---
		// fp+2 vs 0+1 (list order: fp 0 2 1)
		// what setup has the least difference in strength
		// just have to set 1 correct player in the opposing team,
		// other methods will do the rest.
		// it's just that by this way you do not put the best option team mate in the
		// opposing team.
		int option1 = Math.abs(firstPlayerStrength + playerOptionsList.get(0).getStrength()
				- playerOptionsList.get(1).getStrength() - playerOptionsList.get(2).getStrength());
		int option2 = Math.abs(firstPlayerStrength + playerOptionsList.get(1).getStrength()
				- playerOptionsList.get(0).getStrength() - playerOptionsList.get(2).getStrength());
		int option3 = Math.abs(firstPlayerStrength + playerOptionsList.get(2).getStrength()
				- playerOptionsList.get(0).getStrength() - playerOptionsList.get(1).getStrength());

		if (option1 <= option2 && option1 <= option3) {
			index = 1; // or =2 but that doesn't matter... thirdplayer will handle that.
			System.out.println("TEST option1 is smallest! option1 =" + option1 + " option2=" + option2 + " option3="
					+ option3);
		} else if (option2 <= option1 && option2 <= option3) {
			index = 0; // or =2 but that doesn't matter... thirdplayer will handle that.
			System.out.println("TEST option2 is smallest! option2 =" + option2 + " option1=" + option1 + " option3="
					+ option3);
		} else if (option3 <= option1 && option3 <= option2) {
			index = 0; // or =2 but that doesn't matter... thirdplayer will handle that.
			System.out.println("TEST option3 is smallest! option3 =" + option3 + " option1=" + option1 + " option2="
					+ option2);
		}
	}

	int playerNumber = playerOptionsList.get(index).getNumber();
	int playerRollIndex = getPlayerRollIndexFromID(playerNumber);

	return playerRollIndex;
}

/**
 *
 * @param firstPlayerS
 * @param secondPlayerS
 * @param sexNeeded
 * @return index
 */
/*
private int getThirdPlayerIndexSpecificSexAfterComparingStrength(int firstPlayerS, int secondPlayerS,
		boolean sexNeeded) {
	int firstPlayerStrength = firstPlayerS;
	int secondPlayerStrength = secondPlayerS;
	int compareStrength = (firstPlayerStrength + secondPlayerStrength) / 2;
	boolean sex = sexNeeded;

	ArrayList<UserData> playerOptionsList = getSameSexOptionList(sex);
	int index = 0;

	if (playerOptionsList.size() > 2) { // plenty of choice, this is not the last female/or/male match
		ArrayList<Integer> compareList = new ArrayList<Integer>();
		for (UserData player : playerOptionsList) {
			compareList.add(Math.abs(compareStrength - player.getStrength()));
		}
		index = getIndexWithMinimumDifferenceInStrength(compareList);
	} else if (playerOptionsList.size() == 2) { // last female/or/male match
		// index UserData works: TeamA p1 --TeamB p2--- TeamA p3---TeamB p4
		// options fp+0 vs sp+1 (lirder order: fp sp 0 1)----or--- fp+1 vs sp+0 (list
		// order: fp sp 1 0)
		// what setup has the least difference in strength

		int option1 = Math.abs(firstPlayerStrength + playerOptionsList.get(0).getStrength()
				- playerOptionsList.get(1).getStrength() - secondPlayerStrength);
		int option2 = Math.abs(firstPlayerStrength + playerOptionsList.get(1).getStrength()
				- playerOptionsList.get(0).getStrength() - secondPlayerStrength);
		if (option1 <= option2) {
			index = 0;
		} else {
			index = 1;
		}
	}

	int playerNumber = playerOptionsList.get(index).getNumber();
	int playerRollIndex = getPlayerRollIndexFromID(playerNumber);

	return playerRollIndex;
}

/**
 * gets INDEX of player with similar strength.
 *
 * @param optionsList
 * @param strength
 * @return playerRollIndex
 */
/*
private int getPlayerRollIndexAfterComparingStrength(ArrayList<UserData> optionsList, int strength) {
	int compareStrength = strength;
	ArrayList<Integer> compareList = new ArrayList<Integer>();

	for (UserData player : optionsList) {
		compareList.add(Math.abs(compareStrength - player.getStrength()));
	}

	int index = getIndexWithMinimumDifferenceInStrength(compareList);
	int playerNumber = optionsList.get(index).getNumber();
	int playerRollIndex = getPlayerRollIndexFromID(playerNumber);

	return playerRollIndex;
}

/**
 * gets INDEX of player with the same ID number as playerNumber
 *
 * @param playerNumber
 * @return index of player with a matching ID number
 */
/*
private int getPlayerRollIndexFromID(int playerNumber) {
	int playerRollIndex = 0;
	for (UserData player : signUpList) {
		if (player.getNumber() == playerNumber) {
			break;
		}
		playerRollIndex++;
	}

	return playerRollIndex;

}

/**
 * get a LIST of all players who: (In order of likely hood to speed up loop) -
 * [a] have same sex - [b] need to be assigned.
 *
 * @return playerOptionsSameSexList
 */
/*
private ArrayList<UserData> getSameSexOptionList(boolean sexNeeded) {
	boolean sex = sexNeeded;
	ArrayList<UserData> playerOptionsSameSexList = new ArrayList<UserData>();

	for (UserData player : signUpList) {
		if (player.getIsSexMale() == sex && !player.getIsAssigned()) {
			playerOptionsSameSexList.add(player);
		}
	}
	return playerOptionsSameSexList;
}


	/**
	 * Leftover Assigned Players are returned in an ArrayList<UserData> these need
	 * to be unpacked and process into the lists in this class.
	 *
	 * @param type
	 */
/*	private void processLeftOverPlayerRollList(String type) {
		String leftOverType = type;
		switch (leftOverType) {
		case "Single":
			for (UserData player : leftOverPlayerRollList) {
				System.out.println("Test processleftover: number " + player.getNumber() + " sexismale "
						+ player.getIsSexMale() + " isAssigned= " + player.getIsAssigned());
			}
			// int playerOneIDNumber = leftOverPlayerRollList.get(0).getNumber();
			// int playerTwoIDNumber = leftOverPlayerRollList.get(0).getNumber();
			// int playerOneIndex = getPlayerRollIndexFromID(playerOneIDNumber);
			// int playerTwoIndex =getPlayerRollIndexFromID(playerTwoIDNumber);
			// signUpList.get(playerOneIndex).setIsAssigned(true);
			// signUpList.get(playerTwoIndex).setIsAssigned(true);
			signUpList.get(getPlayerRollIndexFromID(leftOverPlayerRollList.get(0).getNumber())).setIsAssigned(true);
			signUpList.get(getPlayerRollIndexFromID(leftOverPlayerRollList.get(1).getNumber())).setIsAssigned(true);
			assignedPlayerRollList.add(leftOverPlayerRollList.get(0));
			assignedPlayerRollList.add(leftOverPlayerRollList.get(1));
			for (UserData player : signUpList) {
				if (player.getIsAssigned() == true)
					System.out
							.println("Test processleftover: signUpList & assigned = true. number: " + player.getNumber()
									+ " sexismale " + player.getIsSexMale() + " isAssigned= " + player.getIsAssigned());
			}

			for (UserData player : assignedPlayerRollList) {
				System.out.println("Test processleftover: assignedPlayerRollList number: " + player.getNumber()
						+ " sexismale " + player.getIsSexMale() + " isAssigned= " + player.getIsAssigned());
			}
		case "Mexican":
			// signUpList.get(getPlayerRollIndexFromID(leftOverPlayerRollList.get(0).getNumber())).setIsAssigned(true);
			// signUpList.get(getPlayerRollIndexFromID(leftOverPlayerRollList.get(1).getNumber())).setIsAssigned(true);
			// signUpList.get(getPlayerRollIndexFromID(leftOverPlayerRollList.get(2).getNumber())).setIsAssigned(true);
			// assignedPlayerRollList.add(leftOverPlayerRollList.get(0));
			// assignedPlayerRollList.add(leftOverPlayerRollList.get(1));
			// assignedPlayerRollList.add(leftOverPlayerRollList.get(2));
		}
	}

}


/*
 * private ArrayList<Integer> getLastOneOrThreePlayerIndex(int
 * firstPlayerStrength, boolean firstPlayerSex) { // only 3 persons remaining
 * with assigned = false. ArrayList<Integer> LastOneOrThreePlayerIndex = new
 * ArrayList<Integer>(); int indexLastSecondPlayer = -1; // Step 1: make
 * arraylist with 1 or 3 perons. optionlist ArrayList<UserData> lastOptions =
 * getSameSexOptionList(firstPlayerSex); // Step 2: if mix match: only one setup
 * possible. if (lastOptions.size() == 1) {
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(0).
 * getNumber())); } else if (lastOptions.size() == 3) { // Step 3: if non mix
 * match: // index UserData works: TeamA --TeamB--- TeamA---TeamB // options
 * fp+0 vs 1+2 (fp- 1 0 2)----or--- fp+1 vs 0+2 (fp- 0 1 2)----or--- // fp+2 vs
 * 0+1 (fp- 0 2 1) // what setup has the least difference in strength int
 * option1 = Math.abs(firstPlayerStrength + lastOptions.get(0).getStrength() -
 * lastOptions.get(1).getStrength() - lastOptions.get(2).getStrength()); int
 * option2 = Math.abs(firstPlayerStrength + lastOptions.get(1).getStrength() -
 * lastOptions.get(0).getStrength() - lastOptions.get(2).getStrength()); int
 * option3 = Math.abs(firstPlayerStrength + lastOptions.get(2).getStrength() -
 * lastOptions.get(0).getStrength() - lastOptions.get(1).getStrength()); if
 * (option1 <= option2 && option1 <= option3) {
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(1).
 * getNumber()));
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(0).
 * getNumber()));
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(2).
 * getNumber())); } else if (option2 <= option1 && option2 <= option3) {
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(0).
 * getNumber()));
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(1).
 * getNumber()));
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(2).
 * getNumber())); } else {
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(0).
 * getNumber()));
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(2).
 * getNumber()));
 * LastOneOrThreePlayerIndex.add(getPlayerRollIndexFromID(lastOptions.get(1).
 * getNumber())); } }
 *
 * return LastOneOrThreePlayerIndex; }
 */
/**
 * get index of highest roll of who - is not assigned yet - similar strength as
 * per strength needed to balance the match.
 *
 * @param strength
 * @return playerRollIndex
 */

//	public int getIndexSameSexAfterComparingStrength(int strength, boolean isMale) {
//	int compareStrength = strength;
//

//	ArrayList<UserData> playerOptionsList = getOptionList();
//	ArrayList<Integer> compareList = new ArrayList<Integer>();
//	for (UserData player : playerOptionsList) {
//		compareList.add(Math.abs(compareStrength - player.getStrength()));
//	}

//	int index = getIndexWithMinimumDifferenceInStrength(compareList);
//	int playerNumber = playerOptionsList.get(index).getNumber();
//	int playerRollIndex = getPlayerRollIndexFromID(playerNumber);

//	return playerRollIndex;
//}
/**
 * get a list of all players who: - need to be assigned.
 *
 * @return playerOptionList
 */
//	private ArrayList<UserData> getOptionList() {
//		ArrayList<UserData> playerOptionsList = new ArrayList<UserData>();
//
//		for (UserData player : signUpList) {
//			if (!player.getIsAssigned()) {
//				playerOptionsList.add(player);
//			}
//		}
//		return playerOptionsList;
//	}


/*
 * int indexLastFirstPlayer = getHighestRollFreeIndex(); firstPlayerStrength =
 * signUpList.get(indexLastFirstPlayer).getStrength(); firstPlayerSex =
 * signUpList.get(indexLastFirstPlayer).getIsSexMale();

 * signUpList.get(indexLastFirstPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexLastFirstPlayer));
 *
 * ArrayList<Integer> lastOneOrThreePlayerIndex =
 * getLastOneOrThreePlayerIndex(firstPlayerStrength, firstPlayerSex);
 *
 * if (lastOneOrThreePlayerIndex.size() == 1) { // Mix int indexLastSecondPlayer
 * = lastOneOrThreePlayerIndex.get(0);
 * signUpList.get(indexLastSecondPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexLastSecondPlayer));
 *
 * int indexThirdPlayer = getHighestRollFreeIndex();
 * signUpList.get(indexThirdPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexThirdPlayer));
 *
 * int indexFourthPlayer = getHighestRollFreeIndex(); // check if this works
 * when only 1 person is left with // assigned = false.
 * signUpList.get(indexFourthPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexFourthPlayer));
 * System.out.println( "TEST courtcouter =" + courtCounter +
 * " total players assigned = " + assignedPlayerRollList.size()); } if
 * (lastOneOrThreePlayerIndex.size() == 3) { // Non-Mix int
 * indexLastSecondPlayer = lastOneOrThreePlayerIndex.get(0);
 * signUpList.get(indexLastSecondPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexLastSecondPlayer));
 *
 * int indexLastThirdPlayer = lastOneOrThreePlayerIndex.get(1);
 * signUpList.get(indexLastThirdPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexLastThirdPlayer));
 *
 * int indexLastFourthPlayer = lastOneOrThreePlayerIndex.get(2);
 * signUpList.get(indexLastFourthPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexLastFourthPlayer));
 * System.out.println( "TEST courtcouter =" + courtCounter +
 * " total players assigned = " + assignedPlayerRollList.size()); }
 */
/**
 * Determines the number of courts to be used for mix matches. takes in to
 * account: - the match setting Mix% set by the organizer of the match.
 *
 * @return number of mix courts
 *
 *         private int setNrOfMixCourts() {
 *
 *         double nrOfMixCourtsDouble = 0; int maximumNrOfMixCourts = 0;
 *
 *         int maleCounter = 0; int femaleCounter = 0;
 *
 *         for (UserData player : signUpList) { if (player.getIsSexMale() ==
 *         true && (!player.getIsAssigned())) { maleCounter++; } else if
 *         (player.getIsSexMale() == false && (!player.getIsAssigned())) {
 *         femaleCounter++; } }
 *
 *         /* Check 1: if minimum Sex Counter == 0 --> nrOfMixCourts = 0 Check
 *         2: if minmum Sex Counter == 2 --> nrOfMixCourts = 1; Check 3: if
 *         minimumSexCounter > 4 --> do calculation with match setting.
 *
 *         if (femaleCounter == 0 | maleCounter == 0) { nrOfMixCourtsInt = 0; }
 *         else if (femaleCounter == 2 | maleCounter == 2) { nrOfMixCourtsInt =
 *         1; } else { /** adding 0.5 to round 1,5 up to integer =2. else this
 *         would happen: (integer cast) 1,999 = 1. if organiser has a Mix
 *         setting of 100% or 0% things gets messy with 0,5. because - 0+0.5 = 1
 *         - max +0.5 = max + 1 to void that, 0.49999 is used.
 *
 *
 *         if (femaleCounter <= maleCounter) { maximumNrOfMixCourts =
 *         (femaleCounter / 2); nrOfMixCourtsDouble = ((maximumNrOfMixCourts *
 *         mixPercentage) / 100);
 *
 *         } else { maximumNrOfMixCourts = (maleCounter / 2);
 *         nrOfMixCourtsDouble = ((maximumNrOfMixCourts * mixPercentage) / 100);
 *         } // double calc = ; System.out.println("nrOfMixCourtsDouble=" +
 *         nrOfMixCourtsDouble); nrOfMixCourtsInt = (int) (nrOfMixCourtsDouble +
 *         0.49999);
 *
 *         Choice made:
 *         After the number of mix courts have been picked, a check has to be made because
 *         have to leave over a number of female and male that can be
 *         divided by 4. so after having decided how many mix courts.. a modulus
 *         %4 has to be done. if it fails... you gotta do mix court +1 or -1.
 *         let the rounding decide. if it was rounded up.... mix court -1 if it
 *         was rounded down.... mix court +1 (has to be below max nr of
 *         mixcourtspossible).
 *
 */

//}

/*
 * int firstPlayerStrength = 0; boolean firstPlayerSex = false; int
 * secondPlayerStrength = 0; int thirdPlayerStrength = 0; int courtCounter = 0;
 * boolean sexNeeded = false; int savingLastFourPlayers = 4;
 */
/*
 * for (int i = 0; i < signUpList.size() - singleCourtAdjustment -
 * savingLastFourPlayers; i++) { // adding 4 to create leftovers. else we get
 * 3%4 = 0 while we want 7%4 =3 if ((i + 4) % 4 == 0) { // player multitude 1,
 * 5, 9, 13 --- index multitude of 0 4 8 MODULUS = 0 [TEAM // A] int
 * indexFirstPlayer = getHighestRollFreeIndex(); firstPlayerStrength =
 * signUpList.get(indexFirstPlayer).getStrength(); firstPlayerSex =
 * signUpList.get(indexFirstPlayer).getIsSexMale();
 * signUpList.get(indexFirstPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexFirstPlayer));
 *
 * } else if ((i + 4) % 4 == 1) { // player multitude 2, 6, 10, 14 --- index
 * multitude of 1 5 9 MODULUS =1 [TEAM // B] int indexSecondPlayer =
 * getIndexSpecificSexAfterComparingStrength(firstPlayerStrength,
 * firstPlayerSex); secondPlayerStrength =
 * signUpList.get(indexSecondPlayer).getStrength();
 * signUpList.get(indexSecondPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexSecondPlayer));
 *
 * } else if ((i + 4) % 4 == 2) { // player multitude 3, 7, 11, 15 --- index
 * multitude of 2 6 10 MODULUS = 2 [TEAM // A]
 *
 * if (booleanIsMixCourtList.get(courtCounter)) { // mix match sexNeeded =
 * !firstPlayerSex; } else { // non mix match sexNeeded = firstPlayerSex; } int
 * teamAStrength = (firstPlayerStrength + secondPlayerStrength) / 2; int
 * indexThirdPlayer = getIndexSpecificSexAfterComparingStrength(teamAStrength,
 * sexNeeded); thirdPlayerStrength =
 * signUpList.get(indexThirdPlayer).getStrength();
 * signUpList.get(indexThirdPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexThirdPlayer)); } else if ((i +
 * 4) % 4 == 3) { // player multitude 4, 8, 12, 16 --- index multitude of 3 7 11
 * MODULUS = 3 [TEAM // B] int strengthLeftover = (firstPlayerStrength +
 * thirdPlayerStrength) - secondPlayerStrength; // [2p team A - 1p Team B] int
 * indexFourthPlayer =
 * getIndexSpecificSexAfterComparingStrength(strengthLeftover, sexNeeded);
 * signUpList.get(indexFourthPlayer).setIsAssigned(true);
 * assignedPlayerRollList.add(signUpList.get(indexFourthPlayer));
 *
 * }
 *
 * if (i > 2 && (i + 1) % 4 == 0) { System.out.println("TEST courtcouter =" +
 * courtCounter + " total players assigned = " + assignedPlayerRollList.size());
 * courtCounter++; } }

/**
 * get highest roll who is not assigned yet and with similar strength as per
 * strength needed to ballance the match
 *
 * public int getHighestRollFreeIndexAndSimilarStrength(int strength) { int
 * compareStrength = strength; ArrayList<Integer> freeIndexList = getAllFree();
 *
 * int sizeFreeIndex = freeIndexList.size(); System.out.println("assigner hsstr
 * size freeindex =" + sizeFreeIndex); // make arraylist of all player not
 * assigned yet.
 *
 * ArrayList<UserData> playerOptionsList = new ArrayList<UserData>(); for
 * (int i = 0; i < sizeFreeIndex; i++) {
 * playerOptionsList.add(signUpList.get(freeIndexList.get(i))); }
 *
 * ArrayList<Integer> compareList = new ArrayList<Integer>(); for (UserData
 * player : playerOptionsList) { compareList.add(Math.abs(compareStrength -
 * player.getStrength())); }
 *
 * int index = getMinimumIndex(compareList); int playerNumber =
 * playerOptionsList.get(index).getNumber(); int playerRollIndex = 0; for
 * (UserData player : signUpList) { if (player.getNumber() == playerNumber) {
 * break; } playerRollIndex++; } return playerRollIndex; }
 *
 * /** get all who are not assigned yet.
 *
 * public ArrayList<Integer> getAllFree() { ArrayList<Integer> freeIndex = new
 * ArrayList<Integer>();
 *
 * for (int i = 0; i < signUpList.size(); i++) { if
 * (signUpList.get(i).getIsAssigned() == false) { freeIndex.add(i);
 *
 * }
 *
 * }
 *
 * return freeIndex; }
 *
 * private UserData playerRollStream(int number) { UserData pr =
 * signUpList.stream().filter(x -> x.getNumber() == number).findFirst().get();
 * System.out.println(pr.getNumber() + " " + pr.getRoll() + " " +
 * pr.getStrength()); return pr; }
 *
 *
 */



///////////////////////////////////////////////////////////////////////////////////////////////////


/*public class CreateStandardRandomDoubleCourtList {
    private double mixPercentage = 50; // MATCH SETTING set by [ORGANIZER]
    private int nrOfCourts;
    private String leftOverType = "";
    private int nrOfLeftOverCourtsInt = 0;
    private int nrOfMixCourtsInt = 0;
    private int nrOfMaleCourtsInt = 0;
    private int nrOfFemaleCourtsInt = 0;
    private int nrOfTotalCourtsInt = 0;

    private final ArrayList<UserData> signUpList; // final -> = new ArrayList<UserData>();
    private final ArrayList<String> courtTypeList; // final -> = new ArrayList<String>();

    // constructor
    CreateStandardRandomDoubleCourtList(ArrayList<UserData> signUpList, ArrayList<String> typeOfCourtsStringList,
                                        int numberOfCourts) {
        this.signUpList = signUpList;
        this.courtTypeList = typeOfCourtsStringList;
        this.nrOfCourts = numberOfCourts;
        setNrOfTypeCourts();
        setStringTypeOfCourtList();
    }*/

    /**
     * Determines the number of courts to be used for mix matches. takes in to
     * account: - the match setting Mix% set by the organizer of the match.
     *
     * @return number of mix courts
     */
 /*   private void setNrOfTypeCourts() {

        double nrOfMixCourtsDouble = 0;
        int maximumNrOfMixCourts = 0;

        int maleCounter = 0;
        int femaleCounter = 0;

        for (UserData player : signUpList) {
            if (player.getIsSexMale() == true && (!player.getIsAssigned())) {
                maleCounter++;
            } else if (player.getIsSexMale() == false && (!player.getIsAssigned())) {
                femaleCounter++;
            }
        }
        int totalNrOfPlayers = signUpList.size();
        System.out.println("TEST CreateCourtList: Total Nr Players =" + totalNrOfPlayers);
        System.out.println("TEST CreateCourtList: Nr Male =" + maleCounter);
        System.out.println("TEST CreateCourtList: Nr Female =" + femaleCounter);*/

        /**
         * Check1 Sometime it can happen that player number == available courts *4. then
         * the female/male configuration could cause trouble. a match with 1 male +3
         * females or 3 males + 1 female would be the leftover match.
         */

  /*      int nrOfPlayers = signUpList.size();
        if (nrOfCourts * 4 == nrOfPlayers) {
            if ((maleCounter + 4) % 4 == 1 && (femaleCounter + 4) % 4 == 3) {
                System.out.println("WHITE RABBIT --HIT a strange MIX");
                leftOverType = "FemaleMix";
                maleCounter--;
                femaleCounter = femaleCounter - 3;
                nrOfPlayers = nrOfPlayers - 4;
            } else if ((maleCounter + 4) % 4 == 3 && (femaleCounter + 4) % 4 == 1) {
                System.out.println("WHITE RABBIT --HIT a strange MIX");
                leftOverType = "MaleMix";
                maleCounter = maleCounter - 3;
                femaleCounter--;
                nrOfPlayers = nrOfPlayers - 4;
            }
        }*/

        /**
         * Check 2 Find out if there should be a single(type) or Mexican (type).
         */

     /*   int modulus = (nrOfPlayers + 4) % 4;

        System.out.println("TEST CreateCourtList BEFORE single/mexican assignment: modulus =" + modulus);
        System.out.println("test blanco leftOverType length =" + leftOverType.length());
        if (modulus == 2 | modulus == 3) {
            switch (modulus) {
                case 2: // single
                    if ((maleCounter + 2) % 2 == 1 && (femaleCounter + 2) % 2 == 1) {
                        leftOverType = "MixSingle";
                        maleCounter--;
                        femaleCounter--;
                        nrOfPlayers = nrOfPlayers - 2;
                        break;
                    } else if ((maleCounter + 4) % 4 == 2) {
                        leftOverType = "MaleSingle";
                        maleCounter = maleCounter - 2;
                        nrOfPlayers = nrOfPlayers - 2;
                        break;
                    } else if ((femaleCounter + 4) % 4 == 2) {
                        leftOverType = "FemaleSingle";
                        femaleCounter = femaleCounter - 2;
                        nrOfPlayers = nrOfPlayers - 2;
                        break;
                    }
                case 3:*/
                    /**
                     * mexican shootout! combining: A) male<female female<male female==male B) is
                     * the leftover male or female. (modulus% =1)
                     *
                     * switched to modulus%2, because I want to hit 1 and 3 of the %4.
                     */
/*                    if ((maleCounter + 2) % 2 == 1 && maleCounter < femaleCounter) {
                        leftOverType = "MexicanMFF";
                        maleCounter = maleCounter - 1;
                        femaleCounter = femaleCounter - 2;
                        nrOfPlayers = nrOfPlayers - 3;
                        break;
                    } else if ((maleCounter + 2) % 2 == 1 && maleCounter > femaleCounter) {
                        leftOverType = "MexicanMMM";
                        maleCounter = maleCounter - 3;
                        nrOfPlayers = nrOfPlayers - 3;
                        break;
                    } else if ((maleCounter + 2) % 2 == 1 && maleCounter == femaleCounter) {
                        int coinFlip = (int) Math.random() * 2;
                        if (coinFlip == 0) {
                            leftOverType = "MexicanMMF";
                            maleCounter = maleCounter - 2;
                            femaleCounter--;
                            nrOfPlayers = nrOfPlayers - 3;
                            break;
                        } else {
                            leftOverType = "MexicanMFF";
                            maleCounter--;
                            femaleCounter = femaleCounter - 2;
                            nrOfPlayers = nrOfPlayers - 3;
                            break;
                        }
                    } else if ((femaleCounter + 2) % 2 == 1 && femaleCounter < maleCounter) {
                        leftOverType = "MexicanMMF";
                        maleCounter = maleCounter - 2;
                        femaleCounter--;
                        nrOfPlayers = nrOfPlayers - 3;
                        break;
                    } else if ((femaleCounter + 2) % 2 == 1 && femaleCounter > maleCounter) {
                        leftOverType = "MexicanFFF";
                        femaleCounter = femaleCounter - 3;
                        nrOfPlayers = nrOfPlayers - 3;
                        break;
                    } else if ((femaleCounter + 2) % 2 == 1 && femaleCounter == maleCounter) {
                        int coinFlip = (int) Math.random() * 2;
                        if (coinFlip == 0) {
                            leftOverType = "MexicanMMF";
                            maleCounter = maleCounter - 2;
                            femaleCounter--;
                            nrOfPlayers = nrOfPlayers - 3;
                            break;
                        } else {
                            leftOverType = "MexicanMFF";
                            maleCounter--;
                            femaleCounter = femaleCounter - 2;
                            nrOfPlayers = nrOfPlayers - 3;
                            break;
                        }
                    }

            }

        }

        modulus = (nrOfPlayers + 4) % 4; // test
        System.out.println("TEST CreateCourtList AFTER single/mexican assignment: modulus =" + modulus); // test
        System.out.println("test leftOverType length =" + leftOverType.length() + " leftOverType = " + leftOverType);

        if (leftOverType.length() > 0) {
            nrOfLeftOverCourtsInt = 1;
        }*/

        /**
         * Check 2: if minimum Sex Counter == 0 --> nrOfMixCourts = 0 Check 2: if
         * minimum Sex Counter == 2 --> nrOfMixCourts = 1; Check 3: if minimumSexCounter
         * > 4 --> do calculation with [ORGANISER] match setting.
         */

/*        if (femaleCounter == 0 | maleCounter == 0) {
            nrOfMixCourtsInt = 0;
        } else if (femaleCounter == 2 | maleCounter == 2) {
            nrOfMixCourtsInt = 1;
        } else {*/
            /**
             * adding 0.5 to round 1,5 up to integer =2. else this would happen: (integer
             * cast) 1,999 = 1. if organiser has a Mix setting of 100% or 0% things gets
             * messy with 0,5. because - 0+0.5 = 1 - max +0.5 = max + 1 to void that,
             * 0.49999 is used.
             */

  /*          if (femaleCounter <= maleCounter) {
                maximumNrOfMixCourts = (femaleCounter / 2);
                nrOfMixCourtsDouble = ((maximumNrOfMixCourts * mixPercentage) / 100);

            } else {
                maximumNrOfMixCourts = (maleCounter / 2);
                nrOfMixCourtsDouble = ((maximumNrOfMixCourts * mixPercentage) / 100);
            }

            nrOfMixCourtsInt = (int) (nrOfMixCourtsDouble + 0.49999);

            int leftOverMaleModulusProof = maleCounter - nrOfMixCourtsInt * 2 + 4;
            int leftOverFeMaleModulusProof = femaleCounter - nrOfMixCourtsInt * 2 + 4;
            if (leftOverMaleModulusProof % 4 != 0 | leftOverFeMaleModulusProof % 4 != 0) {
                Double rounding = nrOfMixCourtsDouble - nrOfMixCourtsInt;
                if (rounding > 0 && nrOfMixCourtsInt < maximumNrOfMixCourts) {
                    nrOfMixCourtsInt++;
                } else {
                    nrOfMixCourtsInt--;
                }
            }
        }

        int nrOfMalesLeft = maleCounter - nrOfMixCourtsInt * 2;
        int nrOfFemalesLeft = femaleCounter - nrOfMixCourtsInt * 2;
        if (nrOfMalesLeft > 0) {
            nrOfMaleCourtsInt = nrOfMalesLeft / 4;
        }
        if (nrOfFemalesLeft > 0) {
            nrOfFemaleCourtsInt = nrOfFemalesLeft / 4;
        }
        nrOfTotalCourtsInt = nrOfMixCourtsInt + nrOfMaleCourtsInt + nrOfFemaleCourtsInt + nrOfLeftOverCourtsInt;
        System.out.println("TEST CreateCourtList() nrOfTotalCourtsInt = " + nrOfTotalCourtsInt + " nrOfMixCourtsInt = "
                + nrOfMixCourtsInt + " nrOfMaleCourtsInt = " + nrOfMaleCourtsInt + " nrOfFemaleCourtsInt = "
                + nrOfFemaleCourtsInt + " nrOfLeftOverCourtsInt =" + nrOfLeftOverCourtsInt);
    }*/

 /*   private void setStringTypeOfCourtList() {*/

        /**
         * WEAKNESS: if all matches are Mix... the last match will be mix. It's not
         * worth it to prevent the forced poor matchmaking choices that this will cause.
         *
         * CHOICE made:
         *  if you always put mix matches first.. .. then benched players will have
         * an increased chance of always getting a mixed match. if female courts > 1
         * --add a female (on top). if male courts >1 --add a male (on top). next... add
         * all the mix matches. next add non mix matches -> until female = 1 & male = 1
         * do the last 2 core extra care.
         *
         */

  /*      String female = ("FemaleDouble");
        String male = ("MaleDouble");
        String mix = ("MixDouble");

        int mixCourtCounter = 0;
        int maleCourtCounter = 0;
        int femaleCourtCounter = 0;*/

        /**
         * If there is only 1 court total with a female/male double it needs to be
         * caught. this is a side effect of forcing a male / female court on top and on
         * bottom of a list with multiple courts. that side effect is caught with a nrOfTotalCourtsInt+1.
         *
         * Other exception: if the courts fall through and 1 court is leftover.
         * that happens with 2 or 3 courts total.
         *
         * few courts (<4) and the i cannot hit i>1 with the female/male > 0.
         */

 /*       for (int i = 0; i < nrOfTotalCourtsInt+2; i++) { //fall through fix +1  */


            /**
             * first 2 iterations try set: priority: leftOverCourt else a male and a female
             * court ontop.
             */

    /*        if (i == 0) {
                if (nrOfLeftOverCourtsInt > 0) {
                    courtTypeList.add(leftOverType);
                }
                if ((nrOfFemaleCourtsInt - femaleCourtCounter) > 2) {
                    courtTypeList.add(female);
                    femaleCourtCounter++;

                } else if ((nrOfMaleCourtsInt - maleCourtCounter) > 2) {
                    courtTypeList.add(male);
                    maleCourtCounter++;
                } else if ((nrOfMixCourtsInt - mixCourtCounter) > 0) {
                    courtTypeList.add(mix);
                    mixCourtCounter++;
                }
            } else if (i == 1) {
                if ((nrOfMaleCourtsInt - maleCourtCounter) > 2) {
                    courtTypeList.add(male);
                    maleCourtCounter++;

                } else if ((nrOfFemaleCourtsInt - femaleCourtCounter) > 2) {
                    courtTypeList.add(female);
                    femaleCourtCounter++;

                } else if ((nrOfMixCourtsInt - mixCourtCounter) > 0) {
                    courtTypeList.add(mix);
                    mixCourtCounter++;
                }
            } else if (i > 1) {
                if ((nrOfMixCourtsInt - mixCourtCounter) > 0) {
                    courtTypeList.add(mix);
                    mixCourtCounter++;

                } else if ((nrOfMaleCourtsInt - maleCourtCounter) > 1) {
                    courtTypeList.add(male);
                    maleCourtCounter++;

                } else if ((nrOfFemaleCourtsInt - femaleCourtCounter) > 1) {
                    courtTypeList.add(female);
                    femaleCourtCounter++;

                } else if ((nrOfFemaleCourtsInt - femaleCourtCounter) > 0) {
                    courtTypeList.add(female);
                    femaleCourtCounter++;

                } else if ((nrOfMaleCourtsInt - maleCourtCounter) > 0) {
                    courtTypeList.add(male);
                    maleCourtCounter++;

                }
            }
        }*/









//	public ArrayList<String> getCourtTypeList() {
//		return this.courtTypeList;
//	}
//}