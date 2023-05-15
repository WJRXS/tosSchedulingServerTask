package com.citybridge.tos.schedulingServerTask.player;

public class Player {

    private Long playerId;
    private double playerStrength;
    private boolean Benched;


    public Player() {
    }

    public Player(Long playerId, double playerStrength, boolean benched) {
        this.playerId = playerId;
        this.playerStrength = playerStrength;
        Benched = benched;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public double getPlayerStrength() {
        return playerStrength;
    }

    public void setPlayerStrength(double playerStrength) {
        this.playerStrength = playerStrength;
    }

    public boolean isBenched() {
        return Benched;
    }

    public void setBenched(boolean benched) {
        Benched = benched;
    }
}
