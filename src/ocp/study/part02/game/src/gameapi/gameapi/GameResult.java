/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package gameapi;


public interface GameResult {

    /**
     * @return the awayTeam
     */
    Team getAwayTeam();

    int getAwayTeamScore();

    /**
     * @return the homeTeam
     */
    Team getHomeTeam();

    int getHomeTeamScore();

    String getScore();

    Team getWinner();

    boolean isDrawnGame();
    
}
