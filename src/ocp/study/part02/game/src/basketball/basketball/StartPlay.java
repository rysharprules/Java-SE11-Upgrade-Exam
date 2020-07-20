/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;


class StartPlay extends BasketballEvent  {  // Really it's a special Pass
    
    public StartPlay() {

    }

    public String toString() {
        return "Start play "; // This will cause a problem with the first StartPlay
    }
    
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = {new ReceivePass()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return true;
    }
    
    public boolean changeTeam() {
        return true;
    }

    public void setBallPos(int ballPos) {

       super.ballPos = 50;
    }
    
}
