package com.citybridge.tos.schedulingServerTask.matchMaker.benchUpdate;


import com.citybridge.tos.schedulingServerTask.player.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BenchUpdate {

    public void updateBenchPlay(List<Player> playerList, List<Player> playerBinList) {

        for (Player matchPlayer: playerList) {
            putPlayMatchPlayer(matchPlayer);
        }

        for (Player benchedPlayer: playerBinList) {
            putBenchPlayer(benchedPlayer);
        }
    }

    /**
     * Method conacts DATABASE[PLAYER] with playerId and modifies bench  status
     * #TODO PUTMAPPING
     */
    private void putPlayMatchPlayer(Player matchPlayer) {

        matchPlayer.setBenched(false);
        matchPlayer.setHasNotPlayedAMatchThisTos(false);

    }


    /**
     * Method conacts DATABASE[PLAYER] with playerId and modifies bench status
     * #TODO PUTMAPPING
     */
    private void putBenchPlayer(Player benchedPlayer) {

        benchedPlayer.setBenched(true);
    }


}
