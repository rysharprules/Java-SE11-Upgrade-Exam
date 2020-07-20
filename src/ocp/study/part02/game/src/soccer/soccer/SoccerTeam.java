/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;

import gameapi.GameEvent;
import gameapi.Player;
import gameapi.Team;


import java.util.ArrayList;
import java.util.Arrays;

public class SoccerTeam implements Team{
    
    private String teamName;
    private Player[] playerArray;
    //@JsonProperty(access = Access.READ_WRITE)
    private int pointsTotal;
    //@JsonProperty(access = Access.READ_WRITE)
    private int goalsTotal;
    private boolean detailAvailable = false;
    private int id = 0;
    private String detailType = "Team";

    // this is temporary to total leagues won for testing.
    public int leaguesWon = 0;


    @Override
    public int compareTo(Team theTeam){
        int returnValue = -1;
        if (this.getPointsTotal()< theTeam.getPointsTotal()) {
            returnValue = 1;
        } else if (this.getPointsTotal() == theTeam.getPointsTotal()){
            if (this.getGoalsTotal()< ((SoccerTeam)theTeam).getGoalsTotal()) {
                returnValue = 1;
            } 
        }
        return returnValue;
    }
    
    @Override
    public void incGoalsTotal(int goals){
        this.setGoalsTotal(this.getGoalsTotal() + goals);
    }

    @Override
    public void incPointsTotal(int points){
        this.pointsTotal += points;
    }
    
    public SoccerTeam(String teamName) {
        this.teamName = teamName;
    }
    
    public SoccerTeam(String teamName, Player[] players) {
        this(teamName);
        this.playerArray = players;
    }
    
    public SoccerTeam() {}

    /**
     * @return the teamName
     */
    @Override
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName the teamName to set
     */
    @Override
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * @return the playerArray
     */
    @Override
    public Player[] getPlayerArray() {
        return playerArray;
    }

    /**
     * @param playerArray the playerArray to set
     */
    @Override
    public void setPlayerArray(Player[] playerArray) {
        this.playerArray = playerArray;
    }

    /**
     * @return the pointsTotal
     */
    @Override
    public int getPointsTotal() {
        return pointsTotal;
    }

    /**
     * @param pointsTotal the pointsTotal to set
     */
    @Override
    public void setPointsTotal(int pointsTotal) {
        this.pointsTotal = pointsTotal;
    }

    /**
     * @return the goalsTotal
     */
    @Override
    public int getGoalsTotal() {
        return goalsTotal;
    }

    /**
     * @param goalsTotal the goalsTotal to set
     */
    @Override
    public void setGoalsTotal(int goalsTotal) {
        this.goalsTotal = goalsTotal;
    }
    
    @Override
    public String toString(){
        return teamName;
    }
    
    // Remainder is displayDetailStuff
    
    @Override
    public String getDisplayDetail(){
        return teamName;
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
    @Override
    public void setDetailAvailable(boolean detailAvailable) {
        this.detailAvailable = detailAvailable;
    }



    /**
     * @param id the id to set
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the getDetailType
     */
    @Override
    public String getGetDetailType() {
        return detailType;
    }

    /**
     * @param getDetailType the getDetailType to set
     */
    @Override
    public void setGetDetailType(String detailType) {
        this.detailType = detailType;
    }
    
    
    @Override
    public GameEvent getNextPlayAttempt(GameEvent currEvent){
        

	int ballPos = currEvent.getBallPos();
    GameEvent[] possEvents = currEvent.getNextEvents();



		if (teamName.equals("xxxxxxxxx")) {	// Are we a particular team we want to advantage? (for testing)
			for (GameEvent thisEvent: possEvents ) {
				if ( thisEvent instanceof Shoot) { // Is Shoot valid here?
					if (ballPos < 60 ) {			// Are we a long way from goal? If so remove Shoot
						GameEvent tempEvent = thisEvent;
						ArrayList<GameEvent> eventList = new ArrayList<GameEvent>(Arrays.asList(possEvents));
						eventList.remove(tempEvent);
						possEvents = new SoccerEvent[eventList.size()];
						eventList.toArray(possEvents);

						//System.out.print("SHOOT removed ");
						for (GameEvent theEvent: possEvents) {
							//System.out.print(theEvent + " - ");
						}
						//System.out.println();


					} else if (ballPos < 80) { 
						GameEvent[] newEvents = {new Pass()}; // Currently just for testing
						//possEvents = newEvents;
					} else { // Shoot!!
						GameEvent[] newEvents = {new Shoot()};
						possEvents = newEvents;
						//System.out.print("SHOOT only ");
						for (GameEvent theEvent: possEvents) {
							//System.out.print(theEvent + " - ");
						}
						//System.out.println();
					}
				}		
			 }
		}

		// All events equally likely
        currEvent = possEvents[(int) (Math.random() * (possEvents.length))];
			//System.out.println(currEvent + " - Not a Shoot, cos ball pos is " + ballPos);
			//System.out.println(teamName + " : " + currEvent + " - Not a Shoot, cos ball pos is " + ballPos);
        return currEvent;
		//return new Shoot();
    }
    
    
}


