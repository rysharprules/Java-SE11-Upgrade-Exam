/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;



public class GainPossession extends SoccerEvent {
    
    public GainPossession(){
        
        super();
        
    }
    
    public String toString() {
        return "WON possession ";
    }
    
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = { new Pass(), new Dribble(), new Shoot(), new FreeKickToDefendingSide()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return true;
    }
    
}
