/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class Dribble extends SoccerEvent {
    
    // At the simplest, if successful dribbling moves towards the other teams goal
    public Dribble(){
        
        super();
        
    }
    
    public String toString() {
        return "Dribble ";
    }
    
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = { new Shoot(), new Pass(), new FreeKickToAttackingSide()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return false;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
}
