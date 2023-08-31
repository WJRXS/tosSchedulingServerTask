package com.citybridge.tos.schedulingServerTask.court;

public enum TypeOfCourt {

   // #TODO add integer numbers to the enums
        CARPET(0),
        CLAY(1),
        GRASS(2),
        HARD(3),
        SMASH(4);

     private final int value;

     TypeOfCourt(final int newValue) {
          value = newValue;
     }

     public int getValue() { return value; }
}


