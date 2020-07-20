/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package gameapi;

public interface Team extends Comparable<Team> {


    int compareTo(Team theTeam);

    String getDetailType();

    // Remainder is displayDetailStuff
    String getDisplayDetail();

    /**
     * @return the getDetailType
     */
    String getGetDetailType();

    /**
     * @return the goalsTotal
     */
    int getGoalsTotal();

    int getID();

    GameEvent getNextPlayAttempt(GameEvent currEvent);

    /**
     * @return the playerArray
     */
    Player[] getPlayerArray();

    /**
     * @return the pointsTotal
     */
    int getPointsTotal();

    /**
     * @return the teamName
     */
    String getTeamName();

    void incGoalsTotal(int goals);

    void incPointsTotal(int points);

    boolean isDetailAvailable();

    /**
     * @param detailAvailable the detailAvailable to set
     */
    void setDetailAvailable(boolean detailAvailable);

    /**
     * @param getDetailType the getDetailType to set
     */
    void setGetDetailType(String detailType);

    /**
     * @param goalsTotal the goalsTotal to set
     */
    void setGoalsTotal(int goalsTotal);

    /**
     * @return the id
     */
    //public int getId() {
    //    return id;
    //}
    /**
     * @param id the id to set
     */
    void setId(int id);

    /**
     * @param playerArray the playerArray to set
     */
    void setPlayerArray(Player[] playerArray);

    /**
     * @param pointsTotal the pointsTotal to set
     */
    void setPointsTotal(int pointsTotal);

    /**
     * @param teamName the teamName to set
     */
    void setTeamName(String teamName);

    String toString();
    
}
