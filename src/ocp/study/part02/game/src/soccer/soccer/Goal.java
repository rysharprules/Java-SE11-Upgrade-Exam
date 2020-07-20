/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package soccer;

public class Goal extends SoccerEvent  {
    
    public Goal(){
       
    }
    public String toString() {
        return "GOAL! ";
    }
    

    public SoccerEvent[] getNextFailEvent(){
        SoccerEvent theEvent[] = {new Kickoff()};
        return theEvent;
    }
    public SoccerEvent[] getNextEvents() {
        SoccerEvent theEvent[] = {new Kickoff()};
        return theEvent;
    }
    
    public boolean changePlayer() {
        return false;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
    
    // Little bit of a hack maybe as ballPos not used.
    public void setBallPos(int ballPos) {
       //super.setBallPos(100);
        super.ballPos = 100;
    }
    
    public boolean isGoal() {
        return true;
    }
     
}
