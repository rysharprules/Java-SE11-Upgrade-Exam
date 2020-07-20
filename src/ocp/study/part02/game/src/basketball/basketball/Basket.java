/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;


class Basket extends BasketballEvent  {
    
    public Basket(){
       
    }
    public String toString() {
        return "Basket! ";
    }
    

    public BasketballEvent[] getNextFailEvent(){
        BasketballEvent theEvent[] = {new StartPlay()};
        return theEvent;
    }
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = {new StartPlay()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return false;
    }
    
    public boolean changeTeam() {
        return false;
    }
    

    public void setBallPos(int ballPos) {

        super.ballPos = 100;
    }
    
    public boolean isGoal() {
        return true;
    }
     
}
