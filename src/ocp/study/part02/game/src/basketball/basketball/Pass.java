/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package basketball;

class Pass extends BasketballEvent  {
    
    public Pass(){
        
        super();
        
    }
    
    public String toString() {
        return "Pass attempt ";
    }
    
    public BasketballEvent[] getNextEvents() {
        BasketballEvent theEvent[] = { new ReceivePass() };
        return theEvent;
    }
    
    public boolean changePlayer() {
        return false;
    }
    
    public boolean changeTeam() {
        return false;
    }
    
}
