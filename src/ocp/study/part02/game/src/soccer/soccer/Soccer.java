/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


import gameapi.Game;
import gameapi.GameEvent;
import gameapi.GameResult;
import gameapi.Player;
import gameapi.Team;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import soccer.util.GameSettings;
import java.util.logging.Logger;


public class Soccer implements Game {
    
    private static final Logger LOGGER = Logger.getLogger("");
    


    private Team homeTeam;
    private Team awayTeam;
    private GameEvent[] events;
    

    private LocalDateTime theDateTime;
    
    private boolean detailAvailable = false;
    private int id = 0;

    private String detailType = "soccer";


    public Soccer() {};

    public Soccer(Team homeTeam, Team awayTeam, LocalDateTime plusDays) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.theDateTime = plusDays;
    }
    
    // So Game as a service is possible
    public void populate (Team homeTeam, Team awayTeam, LocalDateTime plusDays) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.theDateTime = plusDays;
    }


    public void playGame() {

	// Set up an Event list with Kickoff as the first Event (Need an ArrayList for convenience
	// but this will be copied to an events Array at the end of the loop that creates the events
	// Choose either homeTeam or awayTeam to have possession
	// Choose a player to kickoff randomly from the team that has possession

        ArrayList<GameEvent> eventList = new ArrayList<>();
        Team currTeam;
        Player currPlayer;
        GameEvent currEvent = new Kickoff();
	GameEvent nextEvent;
        currEvent.setBallPos(50); // Maybe this should happen automatically as part of Kickoff?
        currEvent.setTheTeam(Math.random() > 0.5?homeTeam: awayTeam);
        currEvent.setThePlayer(currEvent.getTheTeam().
                getPlayerArray()[(int) (Math.random() * this.homeTeam.getPlayerArray().length)]);
        currEvent.setTheTime(0);
        
        
        eventList.add(currEvent);
        LOGGER.fine("****** New game ****** ");
        LOGGER.fine("" + currEvent.toString());

	// Loop until game is over
	// if statement ensures that the increase of the time (i) increases by random jumps for each event.

        for (int i = 1; i <=GameSettings.GAME_LENGTH; i++){
            
            //if (Math.random() > GameSettings.GAME_EVENT_FREQUENCY){
            if (true) {
                
                // First need to get currTeam, currPlayer, and currBallPos in case we need to change.
                // TODO - move Type declaration outside for loop
                currTeam = currEvent.getTheTeam();
                currPlayer = currEvent.getThePlayer();
                int currBallPos = currEvent.getBallPos(); // Need to get before setting up new Event
                

		if ((Math.random()* 100 < currEvent.getPercentChanceSuccess())){
		    ///System.err.println("Succeeded " + currEvent);
		    // ** This will replace currEvent with next event **//

		    GameEvent tempEvent = currTeam.getNextPlayAttempt(currEvent); 

		    try {
			GameEvent[] tempEvents = currEvent.getNextEvents();
			boolean isFound = false;

			    for (GameEvent thisEvent: tempEvents ) {
				    if ( thisEvent.getClass().equals( tempEvent.getClass())) { 
				    // We're good
				    isFound = true;
				    }
			    }

			    if (isFound) {
				currEvent = tempEvent;
			    } else throw new Exception("Not a valid event type.");
			} catch (Exception e) {
			    System.out.println(tempEvent + " not a valid event type for "+ currEvent);
			}
		} else {
		    //System.err.println("Failed" + currEvent);
		    currEvent = currEvent.getNextFailEvent()[0];
		}		
		    //currEvent = currTeam.getNextPlayAttempt(currEvent); 

                // This is now the new currEvent so need to know if should change player and team
                //currEvent.setTheTeam(currEvent.changeTeam()?getOtherTeam(currTeam): currTeam);
		// Instead of all this juggling with currThis and currThat, should we just have currEvent and nextEvent? (8/2016)
                
                // Now set the new ball position for this nextEvent.
                currEvent.setBallPos(currBallPos );  // 8 could be random.
                
                if (currEvent.changeTeam()) {
                    currTeam = getOtherTeam(currTeam);
                    // Reverse ball position
                    currEvent.reverseBallPos();
                    //currEvent.changeTeamPossession();
                }
                currEvent.setTheTeam(currTeam);
                
                //System.out.println(currEvent.getTheTeam().getTeamName());
               
		// Below code sets things up so not same player twice 
		// It actually doesn't need to run when there's a change in team (in that case currPlayer won't be removed
                ArrayList <Player> currPlayerList = new ArrayList<>(Arrays.asList(currEvent.getTheTeam().getPlayerArray()));
                if (currPlayerList.size() > 1 ) currPlayerList.remove(currPlayer); // Needs a check here otherwise error
                currEvent.setThePlayer(
                    currEvent.changePlayer()?
                    currPlayerList.get((int)(Math.random() * currPlayerList.size())):
                    currPlayer
                );

                currEvent.setTheTime(i);
		// Note that currTeam, currPlayer, currBallPos are all now for the previous event
		// but they will be updated at the start of the loop
                eventList.add(currEvent); 
                
                LOGGER.fine(currEvent.getThePlayer().getPlayerName() + " of the " + 
                        currEvent.getTheTeam().getTeamName() + " team -- " + currEvent.toString());
                
                //System.out.println(i);
            }

	    // Finally copy the list of created Events
            this.events = new GameEvent[eventList.size()];
            eventList.toArray(events);
 
        }
    }
    

    public String getDescription(boolean showEvents) {

        // Announce the game
        StringBuilder returnString = new StringBuilder();
        returnString.append(this.getHomeTeam().getTeamName() + " vs. " +
                this.getAwayTeam().getTeamName() + " (" + 
                this.getTheDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE) + ")");
        returnString.append("\n");
        
        // Returns result object based on the gameEvents array
        //GameResult theResult = new SoccerResult(this);
        GameResult theResult = getGameResult();
      
        // Announce result
        if (theResult.isDrawnGame()){
            returnString.append("It's a draw!");
        } else  {
            returnString.append(theResult.getWinner().getTeamName());
            returnString.append(" win!");
        }
        returnString.append(" (" + theResult.getHomeTeamScore() + " - " + theResult.getAwayTeamScore() + ") \n");
	
        
        // Add description of the events  
        if (showEvents){
            returnString.append("Ball position distance is relative to team currently in possession for the Event.\n\n");
            for (GameEvent currEvent: this.getEvents()) {
                returnString.append(currEvent.getBallPos() + " : " + currEvent + "after " + 
                currEvent.getTheTime() + " mins by " + 
                currEvent.getThePlayer().getPlayerName() + 
                " of " + currEvent.getTheTeam().getTeamName() + 
                "\n");
            }  
        }

        return returnString.toString();
    }
    
    

    public String getDescription() {
        return getDescription(false);
    }
    

    public String getScore(){
        
        String theScore;
        GameResult theResult = getGameResult();
        theScore = theResult.getHomeTeamScore() + " - " + theResult.getAwayTeamScore();
        return theScore;
        
    }
    
    // TODO - is there neater way? Ternary works.
  
    public Team getOtherTeam(Team currTeam){
        if (currTeam == homeTeam){
            currTeam = awayTeam;
        } else currTeam = homeTeam;
        return currTeam;
    }

    /**
     * @return the homeTeam
     */

    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * @param homeTeam the homeTeam to set
     */
   
    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    /**
     * @return the awayTeam
     */
 
    public Team getAwayTeam() {
        return awayTeam;
    }

    /**
     * @param awayTeam the awayTeam to set
     */

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    /**
     * @return the events
     */

    public GameEvent[] getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */

    public void setEvents(GameEvent[] events) {
        this.events = events;
    }

    /**
     * @return the localDateTime
     */
 
    public LocalDateTime getLocalDateTime() {
        return getTheDateTime();
    }

    /**
     * @param theDateTime the localDateTime to set
     */

    public void setLocalDateTime(LocalDateTime theDateTime) {
        this.setTheDateTime(theDateTime);
    }

    
    /**
     * @return the theDateTime
     */

    public LocalDateTime getTheDateTime() {
        return theDateTime;
    }

    /**
     * @param theDateTime the theDateTime to set
     */

    public void setTheDateTime(LocalDateTime theDateTime) {
        this.theDateTime = theDateTime;
    }
    
    
    
    // TODO. Perhaps better have the code here, not in constructor of SoccerResult

    public GameResult getGameResult(){
        return new SoccerResult(this);  // ???
    }
    
        // Remainder is displayDetailStuff
    

    public String getDisplayDetail(){
        return getScore();
    }
    @Override
    public boolean isDetailAvailable (){
        return detailAvailable;
    }
    @Override
    public int getID(){
        return id;
    }
    @Override
    public String getDetailType() {
        return detailType;
    }

    /**
     * @param detailAvailable the detailAvailable to set
     */

    public void setDetailAvailable(boolean detailAvailable) {
        this.detailAvailable = detailAvailable;
    }


    /**
     * @param id the id to set
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the getDetailType
     */

    public String getGetDetailType() {
        return detailType;
    }

    /**
     * @param getDetailType the getDetailType to set
     */

    public void setGetDetailType(String detailType) {
        this.detailType = detailType;
    }

    @Override
    public String getScoreDescriptionString() {
        return "Total Goals";
    }
    
      
}
