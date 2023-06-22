package com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts;

public class AssignedPlayer {

   Long playerId;
   Boolean isSexMale;
   Double strength;
   Long courtId;
   int position;
   int roll;
   TypeOfMatch typeOfMatch;

    public AssignedPlayer() {
    }

    public AssignedPlayer(Long playerId, Boolean isSexMale, Long courtId, int position, int roll, TypeOfMatch typeOfMatch) {
        this.playerId = playerId;
        this.courtId = courtId;
        this.position = position;
        this.roll = roll;
        this.typeOfMatch = typeOfMatch;
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

    public TypeOfMatch getTypeOfMatch() {
        return typeOfMatch;
    }

    public void setTypeOfMatch(TypeOfMatch typeOfMatch) {
        this.typeOfMatch = typeOfMatch;
    }

    public Boolean getSexMale() {
        return isSexMale;
    }

    public void setSexMale(Boolean sexMale) {
        isSexMale = sexMale;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }


}
