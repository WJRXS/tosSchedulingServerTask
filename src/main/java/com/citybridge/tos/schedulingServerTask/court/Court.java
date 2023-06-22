package com.citybridge.tos.schedulingServerTask.court;

public class Court {

    Long courtId;
    TypeOfCourt typeOfCourt;

    public Court() {
    }

    public Court(Long courtId, TypeOfCourt typeOfCourt) {
        this.courtId = courtId;
        this.typeOfCourt = typeOfCourt;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public TypeOfCourt getTypeOfCourt() {
        return typeOfCourt;
    }

    public void setTypeOfCourt(TypeOfCourt typeOfCourt) {
        this.typeOfCourt = typeOfCourt;
    }
}
