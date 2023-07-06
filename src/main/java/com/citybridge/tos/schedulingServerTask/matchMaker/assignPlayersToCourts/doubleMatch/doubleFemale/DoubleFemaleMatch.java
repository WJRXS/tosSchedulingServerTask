package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleFemale;

import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoubleFemaleMatch {

    public boolean createDoubleFemaleMatch(List<Player> playerList,
                                           List<AssignedPlayer> assignedPlayersToCourtsList,
                                           TypeOfMatch typeOfMatch,
                                           boolean isMatchTypeLast){
        return true;
    }

}
