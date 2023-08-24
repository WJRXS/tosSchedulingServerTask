package com.citybridge.tos.schedulingServerTask.matchMaker.lottery;

/**
 * #todo for player config, mockery
 */
public class DiceRoller {

    //constructor
    public DiceRoller() {

    }



    public int getDiceRoll() {
        int max = 1000;
        int min = 1;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;

        return rand;
    }


    public double getPlayerStrength() {

        // define the range
        int max = 90;
        int min = 50;
        int range = max - min + 1;


        int strengthInt = (int) (Math.random() * range + min);
        double roundedStrength = (double) strengthInt/10;
        //		double strengthInt =  (Math.random() * range + min);
        //		double strengthDouble = (double) (strengthInt / 10);

        // math round returns a long. no decimals. so we ramp it up...round..next back 1 step for 1 decimal.
        //		double roundedStrength = Math.round(strengthDouble*10)/10.0;

        return roundedStrength;
    }
}
