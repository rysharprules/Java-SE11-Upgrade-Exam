/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


import gameapi.Game;
import gameapi.GameEvent;
import gameapi.GameResult;
import gameapi.Team;


public class SoccerResult implements GameResult {
    
 
    private final Team homeTeam;
    private final Team awayTeam;
    private Team winner;
    private int homeTeamGoals;
    private int awayTeamGoals;
    private boolean isDrawn;
    
    // Could we have list of scorers here? But why not get that from game.
    // Only advantage of SoccerResult is that it has result and winners,
    // hence easier to get totalPoints. (Could also be useful for goal difference
    // home/away goals etc.
    
    // Should SoccerResult be a member of Soccer? Seems not, as it'd then have to update 
    // automatically.
    
    
 
    @Override
    public boolean isDrawnGame() {
        return isDrawn;
    } 
    
    // Possibly throw Exception here for game not played or drawn game
    @Override
    public Team getWinner()  {
        return this.winner;
    }  
    @Override
    public int getHomeTeamScore(){
        return this.homeTeamGoals;
    }
    @Override
    public int getAwayTeamScore(){
        return this.awayTeamGoals;
    }
   
   // Constructor 
   public SoccerResult(Game currGame) {
        this.homeTeam = currGame.getHomeTeam();  // This might change in later version
        this.awayTeam = currGame.getAwayTeam();
        setGoals(currGame);       
         if (homeTeamGoals == awayTeamGoals){
            this.isDrawn = true;       
        } else if (homeTeamGoals > awayTeamGoals) {
            this.winner = this.homeTeam;
        } else {
            this.winner = this.awayTeam;
        }

    }
    
    // This is really part of the constructor!!
    // As I did away with Goal!!! ... need to check for Kickoffs instead (but there'll be one too many!
    private void setGoals(Game currGame) {
       for (GameEvent currEvent: currGame.getEvents()) {
            if (currEvent instanceof Goal) {
                //if (currEvent.getTheTeam()==currGame.getHomeTeam()){    // Needs to compare team names here as deserialization means can't compare object references
                if (currEvent.getTheTeam().getTeamName().equals(currGame.getHomeTeam().getTeamName())){
                    //System.out.println("I'm a " + this.homeTeam);
                    this.homeTeamGoals++;
                    
                } else {
                    //System.out.println("I'm a " + this.awayTeam);
                    this.awayTeamGoals++;
                    
                }
                // Increment player's goals here? Seems not correct. TODO
                // NOT correct because GameResults get created all over the place.
                //currEvent.getThePlayer().incGoalsScored();
            }
       }
    } 
    
    @Override
    public String getScore() {
        return homeTeamGoals + " - " + awayTeamGoals;
    }

    /**
     * @return the homeTeam
     */
    @Override
    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * @return the awayTeam
     */
    @Override
    public Team getAwayTeam() {
        return awayTeam;
    }
 
}
