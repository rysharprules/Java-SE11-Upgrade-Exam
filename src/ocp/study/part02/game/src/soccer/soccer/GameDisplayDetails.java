/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class GameDisplayDetails {
    
    private String homeTeam;
    private String awayTeam;
    private String score;
    
    GameDisplayDetails() {}
    
    public GameDisplayDetails(String homeTeam, String awayTeam, String score){
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = score;
        
    }

    /**
     * @return the homeTeam
     */
    public String getHomeTeam() {
        return homeTeam;
    }

    /**
     * @return the awayTeam
     */
    public String getAwayTeam() {
        return awayTeam;
    }

    /**
     * @return the score
     */
    public String getScore() {
        return score;
    }
    
    
    
}
