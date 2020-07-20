/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;


public class GainPossession extends BasketballEvent {
    
    public GainPossession(){
        
        super();
        
    }
    
    public String toString() {
        return "WON possession ";
    }
    
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = { new Pass(), new Dribble(), new Shoot(), new FreeThrowToDefendingSide()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return true;
    }
    
}
