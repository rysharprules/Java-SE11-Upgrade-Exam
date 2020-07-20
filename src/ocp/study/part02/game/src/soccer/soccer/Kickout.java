/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class Kickout extends SoccerEvent  { // Special type of Pass
    
    public Kickout(){
     
    }
    
    public String toString() {
        return "Saved. Kickout ";
    }
    
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = { new ReceivePass()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return true;
    }
    

    public void setBallPos(int ballPos) {

       super.ballPos = 95;
    }
    
}
