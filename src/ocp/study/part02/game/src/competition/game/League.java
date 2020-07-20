/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package game;

import gameapi.GameEvent;
import gameapi.GameResult;
import gameapi.Team;
import gameapi.Player;
import gameapi.Game;
import gameapi.TournamentType;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import utils.Settings;


public class League implements TournamentType{
    
    public League() {};

     public League(Game[] allGames) {
 this.games = allGames;
 this.teams = getTeamsFromGames(allGames);
 } 
    /**
     * @return the games
     */
    public Game[] getGames() {
        return games;
    }

    /**
     * @return the teams
     */
    public Team[] getTeams() {
        return teams;
    }
    
    static String dirName = "data";
    private String gameType;
    private Game[] games;
    private Team[] teams;

    
    public League(String gameType, Team[] theTeams) {
        if ( theTeams.length < 2 ) {
            System.out.println("Number of teams for a League tournament should be 2 or greater.");
            System.exit(0);
            //throw new Exception("");
        }
        this.teams = theTeams;
        this.gameType = gameType;
    }  
    
    public void populate (String gameType, Team[] theTeams) {
        if ( theTeams.length < 2 ) {
            System.out.println("Number of teams for a League tournament should be 2 or greater.");
            System.exit(0);
            //throw new Exception("");
        }
        this.teams = theTeams;
        this.gameType = gameType;
    } 
    

    public void createGames() {
        int daysBetweenGames = 0;
        
        ArrayList<Game> theGames = new ArrayList<>();
        
        for (Team homeTeam: getTeams()){
            for (Team awayTeam: getTeams()){
               if (homeTeam != awayTeam) {
                   daysBetweenGames += Settings.DAYS_BETWEEN_GAMES;
                   
                   // *** this is where the Soccer game is instantiated
                   theGames.add(Factory.createGame(gameType, homeTeam, awayTeam, LocalDateTime.now().plusDays(daysBetweenGames)));

               } 
            
            }
        }
        
        
        
        this.games = theGames.toArray(new Game[1]);
    }
    

    public void setTeamStats() {

        
        // zero all Team scores and Player scores
        for (Team currTeam: getTeams()){
            currTeam.setGoalsTotal(0);
            currTeam.setPointsTotal(0);
                    // zero all Player scores
            for (Player currPlayer: currTeam.getPlayerArray()){
                currPlayer.setGoalsScored(0);
            }
        }
        
        // Repopulate goalsTotal and pointsTotal on each Team object
        //
        // Note all that is needed from GameResult is:
        // 1. Is the game drawn?
        // 2. Who won the game? (no winner equals drawn? but not good to pass null).
        // 3. What did homeTeam score, what did awayTeam score?
        //
        
        for (Game currGame: getGames()){
            
            GameResult theResult = currGame.getGameResult(); // 
            //IGameResult theResult = new GameResult(currGame); // Maybe above is better
            
            // Increment pointsTotal on Team
            if (theResult.isDrawnGame()) {
                currGame.getHomeTeam().incPointsTotal(Settings.DRAWN_GAME_POINTS);   // Another way to do it currGame vs. theResult
                theResult.getAwayTeam().incPointsTotal(Settings.DRAWN_GAME_POINTS);
            }

            else {
                theResult.getWinner().incPointsTotal(Settings.WINNER_GAME_POINTS);
            }
            
            // Increment goalsTotal in Team
            theResult.getHomeTeam().incGoalsTotal(theResult.getHomeTeamScore());
            theResult.getAwayTeam().incGoalsTotal(theResult.getAwayTeamScore());
            
        }
    }
    

    public void showBestTeam(Team[] theTeams) {
        
	// Note below method puts last first and first last if no scoring at all!
        Arrays.sort(theTeams);
        Team currBestTeam = theTeams[0];  

      
        // Comment out as method now used more for ordering than showing
        // Showing handled by display-ascii-0.1b.jar library
        for (Team currTeam: theTeams){
            //System.out.println(currTeam.getTeamName() + " : " + currTeam.getPointsTotal() + " : " + currTeam.getGoalsTotal());

        }

        
    }
    
    public String getLeagueAnnouncement(Game[] theGames){
        
        Period thePeriod = Period.between(theGames[0].getTheDateTime().toLocalDate(), 
        theGames[theGames.length - 1].getTheDateTime().toLocalDate());
        
        return "The league is scheduled to run for " +
        thePeriod.getMonths() + " month(s), and " +
        thePeriod.getDays() + " day(s)\n";
    }
    
    

    public void setPlayerStats() {
        for (Game currGame : getGames()) {
            for (GameEvent currEvent : currGame.getEvents()) {
                if (currEvent.isGoal()) {
                    currEvent.getThePlayer().incGoalsScored();
                }
            }
        }

    }
    
    public void showBestPlayers(Team[] theTeams){
        ArrayList <Player> thePlayers = new ArrayList<>();
        for (Team currTeam: theTeams){
            thePlayers.addAll(Arrays.asList(currTeam.getPlayerArray()));
        }
        
        Collections.sort(thePlayers, (p1, p2) -> Double.valueOf(p2.getGoalsScored()).compareTo(Double.valueOf(p1.getGoalsScored())));
        
        // How to get the team the player is in? TODO.
        System.out.println("\n\nBest Players in League");
        for (Player currPlayer: thePlayers){
            System.out.println(currPlayer.getPlayerName() + " : " + currPlayer.getGoalsScored());
        }
    }    
    
    public void showBestPlayersByTeam(Team[] theTeams){

        for (Team currTeam: theTeams){
            Arrays.sort(currTeam.getPlayerArray(), (p1, p2) -> Double.valueOf(p2.getGoalsScored()).compareTo(Double.valueOf(p1.getGoalsScored())));

            System.out.println("\n\nBest Players in " + currTeam.getTeamName());
            for (Player currPlayer: currTeam.getPlayerArray()){
                System.out.println(currPlayer.getPlayerName() + " : " + currPlayer.getGoalsScored());
            }
        
        }
     
    }
    



    private Team[] getTeamsFromGames(Game[] theGames) {

                
        int numTeams = (1 + (int)Math.sqrt(1 + 4*theGames.length))/2; // Quadratic equation to get num teams from num games
        Team[] theTeams = new Team[numTeams];

        for (int i = 0; i < numTeams; i++) {
            theTeams[i] = theGames[i].getAwayTeam();
        }

        this.showBestTeam(theTeams); // Need to order before returning as this isn't saved
        return theTeams;
    }
    
    public void createAndPlayAllGames() {
        this.createGames();
        for (Game currGame : this.getGames()) {
            currGame.playGame();

        }
        this.setTeamStats();
        this.setPlayerStats();
        this.showBestTeam(getTeams()); // Sets team order in the GRID

    }
    
    public String getName() {
         return "league";
    } 
    
    
}


