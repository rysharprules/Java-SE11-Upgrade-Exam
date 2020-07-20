/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class Kickoff extends SoccerEvent  {  // Really it's a special Pass
    
    public Kickoff() {

    }

    public String toString() {
        return "Kickoff "; // This will cause a problem with the first Kickoff
    }
    
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = {new ReceivePass()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return true;
    }
    
   
    public void setBallPos(int ballPos) {
 
       super.ballPos = 50;
    }
    
}
