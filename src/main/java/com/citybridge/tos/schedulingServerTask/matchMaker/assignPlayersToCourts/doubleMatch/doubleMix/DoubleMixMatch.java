package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleMix;

import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoubleMixMatch {

    public boolean createDoubleMixMatch(List<Player> playerList,
                                        List<AssignedPlayer> assignedPlayersToCourtsList,
                                        TypeOfMatch typeOfMatch,
                                        boolean isMatchTypeLast){
        return true;
    }

}
