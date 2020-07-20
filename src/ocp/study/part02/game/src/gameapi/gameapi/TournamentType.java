/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package gameapi;


public interface TournamentType {

    void createAndPlayAllGames();

    Game[] getGames();

    Team[] getTeams();
    
    String getName();
    
    void populate(String gameType, Team[] theTeam);
}
