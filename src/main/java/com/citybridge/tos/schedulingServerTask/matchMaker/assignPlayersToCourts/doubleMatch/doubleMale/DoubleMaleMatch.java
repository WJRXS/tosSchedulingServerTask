package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleMale;

import com.citybridge.tos.schedulingServerTask.event.Event;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoubleMaleMatch {

    public boolean createDoubleMaleMatch(List<Player> playerList,
                                         List<AssignedPlayer> assignedPlayersToCourtsList,
                                         TypeOfMatch typeOfMatch,
                                         boolean isMatchTypeLast,
                                         Event event){
            return true;
        }

    }

