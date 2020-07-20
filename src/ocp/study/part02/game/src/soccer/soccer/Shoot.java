/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class Shoot extends SoccerEvent {
    
    public Shoot(){
        
    }
   

    public int getPercentChanceSuccess() {
		// ternary operator below for ballPos
		//int odds = (int)(Math.pow((isHomeTeam?ballPos:reverseBallPos()),2)/200);
		// Below indicate 100% chance if ball at 100 yards!
		int odds = (int)(Math.pow(ballPos,2)/100);
		//System.out.println(ballPos + " : " + odds);
		//System.err.println(this + " : " + ballPos + " : " + odds);
		return odds;
    }    
    public String toString() {
        return "SHOOTS ";
    }
    
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = { new Goal()};
        return theEvent;
    }
    public SoccerEvent[] getNextFailEvent(){
        SoccerEvent theEvent[] = { new Kickout() };
        return theEvent;
    }
    
    public boolean changePlayer() {
        return false;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
        /**
     * @param ballPos the ballPos to set
     */
    public void setBallPos(int currBallPos) {
 
        super.ballPos = currBallPos;

    }
    
}
