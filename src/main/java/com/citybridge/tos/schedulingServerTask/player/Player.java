package com.citybridge.tos.schedulingServerTask.player;

import com.citybridge.tos.schedulingServerTask.court.TypeOfCourt;
import com.citybridge.tos.schedulingServerTask.matchMaker.assignPlayersToCourts.typeOfMatch.TypeOfMatch;

import java.util.List;

public class Player { //} implements Comparable<Player>{

    private Long playerId;
    private double playerStrength;

    private boolean benched;
    private boolean maleSex;

    // more elegant procedure: Long lastPlayedEvent = x. cross check this with event and you know if player has played
    // a match this event.
    private boolean hasNotPlayedAMatchThisTos;
    private boolean hasPlayedLeftOverMatch;
    private boolean wantToPlaySingle;
    private boolean wantToPlayMexican;
    private boolean wantToPlayUnbalanced;
    private int roll = 0;

    // 1 CARPET,
    // 2 CLAY,
    // 3 GRASS,
    // 4 HARD,
    // 5 SMASH
    private List<TypeOfCourt> courtPreference;

    private List<TypeOfMatch> typeOfMatchPreference;

    private List<Long> friendIdList;


    public Player() {
    }

    public Player(Long playerId, double playerStrength, boolean benched, boolean maleSex) {
        this.playerId = playerId;
        this.playerStrength = playerStrength;
        this.benched = benched;
        this.maleSex = maleSex;
        courtPreference = List.of(TypeOfCourt.GRASS, TypeOfCourt.HARD, TypeOfCourt.CLAY, TypeOfCourt.SMASH, TypeOfCourt.CARPET);

        friendIdList = List.of(5L, 6L);
        dummyFillTypeOfMatchPreference(maleSex);
    }

 //   @Override
 //   public int compareTo(Player anotherPlayer) {
  //      return this.roll.compareTo(anotherPlayer.getRoll());
 //   }

    private void dummyFillTypeOfMatchPreference(Boolean maleSex) {

        if (maleSex) {
            typeOfMatchPreference = List.of(TypeOfMatch.DOUBLE_MIX, TypeOfMatch.DOUBLE_MALE);
        }
        else {
            typeOfMatchPreference = List.of(TypeOfMatch.DOUBLE_MIX, TypeOfMatch.DOUBLE_FEMALE);
        }

    }

    public boolean isHasNotPlayedAMatchThisTos() {
        return hasNotPlayedAMatchThisTos;
    }

    public void setHasNotPlayedAMatchThisTos(boolean hasNotPlayedAMatchThisTos) {
        this.hasNotPlayedAMatchThisTos = hasNotPlayedAMatchThisTos;
    }


    public void setMaleSex(boolean maleSex) {
        this.maleSex = maleSex;
    }

    public List<TypeOfMatch> getTypeOfMatchPreference() {
        return typeOfMatchPreference;
    }

    public void setTypeOfMatchPreference(List<TypeOfMatch> typeOfMatchPreference) {
        this.typeOfMatchPreference = typeOfMatchPreference;
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

    public boolean isMaleSex() {
        return maleSex;
    }

    public List<TypeOfCourt> getCourtPreference() {
        return courtPreference;
    }

    public void setCourtPreference(List<TypeOfCourt> courtPreference) {
        this.courtPreference = courtPreference;
    }

    public boolean isWantToPlaySingle() {
        return wantToPlaySingle;
    }

    public void setWantToPlaySingle(boolean wantToPlaySingle) {
        this.wantToPlaySingle = wantToPlaySingle;
    }

    public boolean isWantToPlayMexican() {
        return wantToPlayMexican;
    }

    public void setWantToPlayMexican(boolean wantToPlayMexican) {
        this.wantToPlayMexican = wantToPlayMexican;
    }

    public boolean isWantToPlayUnbalanced() {
        return wantToPlayUnbalanced;
    }

    public void setWantToPlayUnbalanced(boolean wantToPlayUnbalanced) {
        this.wantToPlayUnbalanced = wantToPlayUnbalanced;
    }

    public List<Long> getFriendIdList() {
        return friendIdList;
    }

    public void setFriendIdList(List<Long> friendIdList) {
        this.friendIdList = friendIdList;
    }
}
