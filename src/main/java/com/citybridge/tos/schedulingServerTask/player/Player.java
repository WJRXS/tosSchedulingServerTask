package com.citybridge.tos.schedulingServerTask.player;

public class Player { //} implements Comparable<Player>{

    private Long playerId;
    private double playerStrength;

    private boolean benched;

    // more elegant procedure: Long lastPlayedEvent = x. cross check this with event and you know if player has played
    // a match this event.
    private boolean hasNotPlayedAMatchThisTos;

    private boolean hasPlayedLeftOverMatch;
    private int roll = 0;

    public Player() {
    }

    public Player(Long playerId, double playerStrength, boolean benched) {
        this.playerId = playerId;
        this.playerStrength = playerStrength;
        this.benched = benched;
    }

 //   @Override
 //   public int compareTo(Player anotherPlayer) {
  //      return this.roll.compareTo(anotherPlayer.getRoll());
 //   }

    public boolean isHasNotPlayedAMatchThisTos() {
        return hasNotPlayedAMatchThisTos;
    }

    public void setHasNotPlayedAMatchThisTos(boolean hasNotPlayedAMatchThisTos) {
        this.hasNotPlayedAMatchThisTos = hasNotPlayedAMatchThisTos;
    }

    public boolean isHasPlayedLeftOverMatch() {
        return hasPlayedLeftOverMatch;
    }

    public void setHasPlayedLeftOverMatch(boolean hasPlayedLeftOverMatch) {
        this.hasPlayedLeftOverMatch = hasPlayedLeftOverMatch;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
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
        return benched;
    }

    public void setBenched(boolean benched) {
        this.benched = benched;
    }
}
