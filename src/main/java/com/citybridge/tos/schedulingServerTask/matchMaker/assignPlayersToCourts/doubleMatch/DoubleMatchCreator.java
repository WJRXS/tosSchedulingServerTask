package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch;


import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.AssignedPlayer;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.TypeOfMatch.TypeOfMatch;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleFemale.DoubleFemaleMatch;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleMale.DoubleMaleMatch;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.doubleMatch.doubleMix.DoubleMixMatch;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoubleMatchCreator {


    private final DoubleFemaleMatch doubleFemaleMatch;
    private final DoubleMaleMatch doubleMaleMatch;
    private final DoubleMixMatch doubleMixMatch;

    @Autowired
    public DoubleMatchCreator(DoubleFemaleMatch doubleFemaleMatch, DoubleMaleMatch doubleMaleMatch, DoubleMixMatch doubleMixMatch) {
        this.doubleFemaleMatch = doubleFemaleMatch;
        this.doubleMaleMatch = doubleMaleMatch;
        this.doubleMixMatch = doubleMixMatch;
    }


    public void createDoubleMatch(List<AssignedPlayer> assignedPlayersToCourtsList,
                                  List<TypeOfMatch> typeOfMatchList,
                                  List<Court> courtIdList,
                                  List<Player> playerList
                                  ) {
    }





}
