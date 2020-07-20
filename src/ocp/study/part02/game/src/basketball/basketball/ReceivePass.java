/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;


class ReceivePass extends BasketballEvent  {
    
    public ReceivePass(){
        
        super();
        
    }    
    public String toString() {
        return "Receive pass ";
    }
    
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = { new Dribble(), new Shoot(), new Pass(), new FreeThrowToAttackingSide()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
}
