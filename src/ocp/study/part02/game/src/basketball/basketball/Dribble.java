/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;


public class Dribble extends BasketballEvent {
    
    // At the simplest, if successful dribbling moves towards the other teams goal
    public Dribble(){
        
        super();
        
    }
    
    public String toString() {
        return "Dribble ";
    }
    
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = { new Shoot(), new Pass(), new FreeThrowToAttackingSide()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return false;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
}
