/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package gameapi;

public interface Player extends Comparable<Player> {

    String getDetailType();


    int compareTo(Player thePlayer);


    /**
     * @return the goalsScored
     */
    int getGoalsScored();

    /**
     * @return the playerName
     */
    String getPlayerName();

    void incGoalsScored();

    /**
     * @param goalsScored the goalsScored to set
     */
    void setGoalsScored(int goalsScored);

    /**
     * @param playerName the playerName to set
     */
    void setPlayerName(String playerName);
    
}
