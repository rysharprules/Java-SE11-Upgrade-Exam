/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;


public class FreeThrowToAttackingSide extends BasketballEvent {

    public FreeThrowToAttackingSide(){
        
        super();
        
    }
    
    public String toString() {
        return "Fouled. Free throw.";
    }
    
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = { new Pass(), new Shoot() };
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
}
