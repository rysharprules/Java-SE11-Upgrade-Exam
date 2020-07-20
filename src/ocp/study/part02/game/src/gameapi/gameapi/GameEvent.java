/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package gameapi;


public interface GameEvent {

    boolean changePlayer();

    boolean changeTeam();

    /**
     * @return the ballPos
     */
    int getBallPos();

    GameEvent[] getNextEvents();

    GameEvent[] getNextFailEvent();


    int getPercentChanceSuccess();

    /**
     * @return the thePlayer
     */
    Player getThePlayer();

    /**
     * @return the theTeam
     */
    Team getTheTeam();


    /**
     * @return the theTime
     */
    double getTheTime();


    void reverseBallPos();

    void setBallPos(int ballPos);

    /**
     * @param thePlayer the thePlayer to set
     */
    void setThePlayer(Player thePlayer);

    /**
     * @param theTeam the theTeam to set
     */
    void setTheTeam(Team theTeam);

    /**
     * @param theTime the theTime to set
     */
    void setTheTime(double theTime);
    
    boolean isGoal();
    
}
