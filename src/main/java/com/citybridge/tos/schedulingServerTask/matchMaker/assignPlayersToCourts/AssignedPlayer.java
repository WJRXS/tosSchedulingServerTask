package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts;

public class AssignedPlayer {

   Long playerId;
   Long courtId;
   int position;
   int roll;

    public AssignedPlayer() {
    }

    public AssignedPlayer(Long playerId, Long courtId, int position, int roll) {
        this.playerId = playerId;
        this.courtId = courtId;
        this.position = position;
        this.roll = roll;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }
}
