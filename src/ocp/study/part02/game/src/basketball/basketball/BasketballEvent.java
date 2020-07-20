/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;

import gameapi.GameEvent;

import gameapi.Player;
import gameapi.Team;


public abstract class BasketballEvent implements GameEvent{
    
     
    
    private Team theTeam;
    private Player thePlayer;
    private double theTime;
    int ballPos;
    int AbsBallPos;
    //boolean isHomeTeam = true; // Will start off being home team

    
    public BasketballEvent(){
      
    }


    @Override
    public int getPercentChanceSuccess() {
		int odds = 92;

	return odds;  // Default value
    }    

    
    /**
     * @return the theTeam
     */
    @Override
    public Team getTheTeam() {
        return theTeam;
    }


    /**
     * @param theTeam the theTeam to set
     */
    @Override
    public void setTheTeam(Team theTeam) {
        this.theTeam = theTeam;
    }

    /**
     * @return the thePlayer
     */
    @Override
    public Player getThePlayer() {
        return thePlayer;
    }

    /**
     * @param thePlayer the thePlayer to set
     */
    @Override
    public void setThePlayer(Player thePlayer) {
        this.thePlayer = thePlayer;
    }

    /**
     * @return the theTime
     */
    @Override
    public double getTheTime() {
        return theTime;
    }

    /**
     * @param theTime the theTime to set
     */
    @Override
    public void setTheTime(double theTime) {
        this.theTime = theTime;
    }
    
    @Override
    public BasketballEvent[] getNextFailEvent(){
        BasketballEvent theEvent[] = { new GainPossession() };
        return theEvent;
    }


    /**
     * @return the ballPos
     */
    @Override
    public int getBallPos() {
        return ballPos;
    }

    /**
     * @param ballPos the ballPos to set
     */


    @Override
    public void setBallPos(int ballPos) {
        this.ballPos = ballPos + (basketball.util.GameSettings.PITCH_LENGTH - ballPos)/8;
    }
    
    
    @Override
    public void reverseBallPos(){
        this.ballPos = basketball.util.GameSettings.PITCH_LENGTH - this.ballPos;
    }


    public boolean isGoal() {
        return false;
    }
    
}
