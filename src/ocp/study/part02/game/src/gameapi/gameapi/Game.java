/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package gameapi;

import java.time.LocalDateTime;


public interface Game {

    
    String getScoreDescriptionString();
    
    // A way to populate Game
    // Using a provider overcomes this so may not be necessary
    void populate(Team homeTeam, Team awayTeam, LocalDateTime dateTime);
    /**
     * @return the awayTeam
     */
    Team getAwayTeam();

    String getDescription(boolean showEvents);

    String getDescription();

    // This returns "Soccer", "Basketball", etc. to identify the game
    // Also was used in DisplayDataItem to identify how to print on web page
    // Maybe overloading its function?
    String getDetailType();


    String getDisplayDetail();

    /**
     * @return the events
     */
    GameEvent[] getEvents();


    GameResult getGameResult();

    /**
     * @return the getDetailType
     */
    String getGetDetailType();

    /**
     * @return the homeTeam
     */
    Team getHomeTeam();

    int getID();

    /**
     * @return the localDateTime
     */

    LocalDateTime getLocalDateTime();


    Team getOtherTeam(Team currTeam);

    String getScore();

    /**
     * @return the theDateTime
     */
    LocalDateTime getTheDateTime();

    boolean isDetailAvailable();

    void playGame();

    /**
     * @param awayTeam the awayTeam to set
     */
    void setAwayTeam(Team awayTeam);

    /**
     * @param detailAvailable the detailAvailable to set
     */
    void setDetailAvailable(boolean detailAvailable);

    /**
     * @param events the events to set
     */
    void setEvents(GameEvent[] events);

    /**
     * @param detailType
     */
    void setGetDetailType(String detailType);

    /**
     * @param homeTeam the homeTeam to set
     */
    void setHomeTeam(Team homeTeam);


    /**
     * @param id the id to set
     */
    void setId(int id);

    /**
     * @param theDateTime the localDateTime to set
     */
    void setLocalDateTime(LocalDateTime theDateTime);

    /**
     * @param theDateTime the theDateTime to set
     */
    void setTheDateTime(LocalDateTime theDateTime);
    
}
