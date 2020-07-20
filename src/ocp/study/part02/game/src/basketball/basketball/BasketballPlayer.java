/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;

import gameapi.Player;

public class BasketballPlayer implements Player {
    
    private String playerName;
    private int goalsScored;
    
    // Needed to identify this concrete class with ServiceLoader
    public String getDetailType () {
        return "Player";
    }
    
    @Override
    public void incGoalsScored() {
        this.goalsScored++;
    }
    
    public BasketballPlayer(String playerName) {
        this.playerName = playerName;
    }
    
    public BasketballPlayer() {}

    /**
     * @return the playerName
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName the playerName to set
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @return the goalsScored
     */
    @Override
    public int getGoalsScored() {
        return goalsScored;
    }

    /**
     * @param goalsScored the goalsScored to set
     */
    @Override
    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }
    
    
    
    
    // TODO - possibly remove this so can sort based on Lambda expression
    // If so, need to use 1.8 Java for GlassFish
    @Override
    public int compareTo(Player thePlayer){

        if (this.getGoalsScored() < thePlayer.getGoalsScored()){
            return 1;
        }
        else {
            return -1;

        }
    }

    // TODO (8/2016)
    // Add code here to have player return suggested Event
    //
    // Add playerPosition int
    // Add various playerSkill values
    //
    // Add method for Players to move when off the ball
    //
    
}
