/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;


public class FreeThrowToDefendingSide extends BasketballEvent {
    
        // At the simplest, if successful dribbling moves towards the other teams goal
    public FreeThrowToDefendingSide(){
        
        super();
        
    }
    
    public String toString() {
        return "Fouled. Possession given to other side";
    }
    
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = { new GainPossession()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return true;
    }
    
}
