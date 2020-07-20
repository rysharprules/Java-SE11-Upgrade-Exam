/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


import gameapi.Player;

public class SoccerPlayer implements Player {
    
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
    
    public SoccerPlayer(String playerName) {
        this.playerName = playerName;
    }
    
    public SoccerPlayer() {}

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
    

    @Override
    public int compareTo(Player thePlayer){

        if (this.getGoalsScored() < thePlayer.getGoalsScored()){
            return 1;
        }
        else {
            return -1;

        }
    }


    
}
