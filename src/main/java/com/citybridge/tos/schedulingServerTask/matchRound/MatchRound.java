package com.citybridge.tos.schedulingServerTask.matchRound;

import java.time.LocalDateTime;

public class MatchRound {

    private Long matchRoundId;
    private Long eventId;
    private int roundNr;
    private LocalDateTime createTime;

    public MatchRound() {
    }

    public MatchRound(Long matchRoundId, Long eventId, int roundNr, LocalDateTime createTime) {
        this.matchRoundId = matchRoundId;
        this.eventId = eventId;
        this.roundNr = roundNr;
        this.createTime = createTime;
    }

    public Long getMatchRoundId() {
        return matchRoundId;
    }

    public void setMatchRoundId(Long matchRoundId) {
        this.matchRoundId = matchRoundId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getRoundNr() {
        return roundNr;
    }

    public void setRoundNr(int roundNr) {
        this.roundNr = roundNr;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


}
