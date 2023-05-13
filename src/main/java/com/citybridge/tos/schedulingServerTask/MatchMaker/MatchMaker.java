package com.citybridge.tos.schedulingServerTask.MatchMaker;

import com.citybridge.tos.schedulingServerTask.court.Court;
import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchMaker {

    // autowired bench / court / match ??

    public void makeMatchRound(List<Long> matchRoundList) {

             for (Long matchRoundId: matchRoundList) {
             planMatchRound(matchRoundId);
            }
    }


    private void planMatchRound(Long matchRoundId) {

    }





    public List<Player> executeMatchMaker(List<Integer> eventVariables, List<Player> playerList, List<Court> courtList) {
        return List.of(new Player());
    }
}
