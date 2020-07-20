/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class FreeKickToAttackingSide extends SoccerEvent {
    
        // At the simplest, if successful dribbling moves towards the other teams goal
    public FreeKickToAttackingSide(){
        
        super();
        
    }
    
    public String toString() {
        return "Fouled. Free kick to attacking side.";
    }
    
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = { new ReceivePass(), new Goal(), new Kickout()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return false;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
}
