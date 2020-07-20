/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class FreeKickToDefendingSide extends SoccerEvent {
    
        // At the simplest, if successful dribbling moves towards the other teams goal
    public FreeKickToDefendingSide(){
        
        super();
        
    }
    
    public String toString() {
        return "Fouled. Free kick to defending side.";
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
    
}
