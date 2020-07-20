/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package main;

import displayDiagram.Display;
import displayDiagram.DisplayDetail;

import database.PlayerDatabaseException;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import game.Factory;
import gameapi.Game;

import database.PlayerDatabase;
import game.TournamentFactory;
import gameapi.Team;
import gameapi.TournamentType;

import utils.Settings;


public class Main {
    
    
    public static void main(String[] args) {
        
        String gameType = "soccer"; 
        String competitionType = "knockout";
        
        try {
            Handler fh = new FileHandler(Settings.dirName + "/" + gameType + ".log");
            fh.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fh);
            Logger.getLogger("").setLevel(Level.FINE);
 
        } catch (IOException ex) {
        }

        // Create the teams
   
        String teamNames = "Robins, Pelicans, Sparrows, Magpies, Crows, Falcons, Geese, Terns";
        //String teamNames = "Plovers,Ravens,Doves,Robins,Sparrows,Magpies,Crows,Falcons,Pelicans,Geese,Ducks,Eagles,Gulls,Hawks,Owls,Pigeons";
        int teamSize = 5;   // Number of players on each team
        Team[] theTeams = null;
        try {

            theTeams = createTeams(gameType, teamNames, teamSize);  
        } catch (PlayerDatabaseException e) {
            e.printStackTrace(System.err);
        }
        
        TournamentType theCompetition = TournamentFactory.getTournament(competitionType, gameType, theTeams);

        
        // Create and play all games using these teams and competition type
        theCompetition.createAndPlayAllGames();
        
        // Display results
        if (competitionType.equals("league")) {
            DisplayDetail[][] dataGrid = getDataGrid(theCompetition.getGames(), theCompetition.getTeams());
            Display.outputTextGrid(dataGrid); 
        } else if (competitionType.equals("knockout")) {
            DisplayDetail[] dataKnockout = getData(theCompetition.getGames());
            Display.printDataTree(dataKnockout);
            
        } else {
            System.out.println("Not a defined competition type.");
            
        }
        
    }

   
    private static DisplayDetail[][] getDataGrid(Game[] theGames, Team[] theTeams) {

        int numTeams = theTeams.length;

        // Size of grid allow for extra column on the left for list of Teams, and two 
        // extra columns on right for Points and Goals. Also extra column on top for list of
        // Teams.
        DisplayDetail[][] theGrid = new DisplayDetail[numTeams + 1][numTeams + 3];

        int colNum = 0;
        int rowNum = 0;

        // Starting at 0, 0, insert a blank top left corner.
        theGrid[rowNum][colNum] = new DisplayDetail("");

        // Do the first row of Teams (headings);
        for (int i = 0; i < theTeams.length; i++) {

            theTeams[i].setId(i);   // set the Id to the index
            // ternary expression below determines whether to add the full orginal Team class or a simple 
            // DisplayString class
            theGrid[rowNum][colNum + 1] = new DisplayDetail(theTeams[i].getTeamName());
            //theGrid[rowNum][colNum + 1] = theTeams[i];
            colNum++;
        }

        // Add Points and Games columns to the first row (headings)
        theGrid[rowNum][colNum + 1] = new DisplayDetail("League Pts");
        // Getting "Goals" vs "Baskets" from Game (same on all IGames - another approach?)
        theGrid[rowNum][colNum + 2] = new DisplayDetail(theGames[0].getScoreDescriptionString());

        // Add each row of Games for each home team (note all Team IDs will be set by previous for loop
        // Also note rowNum = i + 1; therefore starting on second row.
        for (int i = 0; i < theTeams.length; i++) {
            rowNum = i + 1;
            
            // Add the home Team to the first column of the current row
            colNum = 0;
            Team currHomeTeam = theTeams[i];
            //theGrid[rowNum][colNum] = currHomeTeam;
            
            theGrid[rowNum][colNum] = new DisplayDetail(currHomeTeam.getTeamName());
            //theGrid[rowNum][colNum] = theTeams[i];


            // Inner loop through all away teams on current row to add Games
            for (Team currAwayTeam : theTeams) {
                colNum++;   // Could also use traditional for loop here
                if (currHomeTeam != currAwayTeam) {
                    for (Game theGame : theGames) {
                        if (theGame.getHomeTeam().getTeamName().equals(currHomeTeam.getTeamName()) 
                                && theGame.getAwayTeam().getTeamName().equals(currAwayTeam.getTeamName())) {
                            //theGrid[rowNum][colNum] = theGame;
                            //theGrid[rowNum][colNum] = useOriginalClass?theGame:new DisplayString(theGame.getScore());
                            //theGrid[rowNum][colNum] = theGame;
                            //System.out.println("Setting the score!");
                            theGrid[rowNum][colNum] = new DisplayDetail(theGame.getScore());
                            break;
                        }
                    }
                } else {
                    theGrid[rowNum][colNum] = new DisplayDetail(" X ");    // Mark with X as team doesn't play itself
                }
            }

            // Add last two columns to current row (team points and goals)
            theGrid[rowNum][colNum + 1] = new DisplayDetail(Integer.toString(currHomeTeam.getPointsTotal()));
            theGrid[rowNum][colNum + 2] = new DisplayDetail(Integer.toString(currHomeTeam.getGoalsTotal()));
        }
        return theGrid;
    }
    


        
    public static Team[] createTeams(String gameType, String teamNames, int teamSize) throws PlayerDatabaseException {

        PlayerDatabase playerDB = new PlayerDatabase(gameType);

        StringTokenizer teamNameTokens = new StringTokenizer(teamNames, ",");
        Team[] theTeams = new Team[teamNameTokens.countTokens()];
        for (int i = 0; i < theTeams.length; i++) {
  
            theTeams[i] = Factory.createTeam(gameType, teamNameTokens.nextToken(), playerDB.getTeamPlayers(teamSize));
        }

        return theTeams;
    }
    
    // Convert array of Game into array of DisplayDetail so display-ascii library can process
    public static DisplayDetail[] getData ( Game[] gameList) {
        
        DisplayDetail[] theDetails = new DisplayDetail[gameList.length];
        
        for (int i = 0; i < gameList.length; i++) {
            theDetails[i] = new DisplayDetail(gameList[i].getHomeTeam().getTeamName() + " " + gameList[i].getAwayTeam().getTeamName());
            theDetails[i].addDetail(gameList[i].getScore());
        }
        
        return theDetails;

    }

    
}
