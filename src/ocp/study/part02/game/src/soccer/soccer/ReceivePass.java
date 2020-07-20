/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;


public class ReceivePass extends SoccerEvent  {
    
    public ReceivePass(){
        
        super();
        
    }    
    public String toString() {
        return "Receive pass ";
    }
    
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = { new Dribble(), new Shoot(), new Pass(), new FreeKickToAttackingSide()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
}
