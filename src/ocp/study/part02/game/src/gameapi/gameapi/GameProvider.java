/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package gameapi;

import java.time.LocalDateTime;


public interface GameProvider {
    
    Game getGame(Team homeTeam, Team awayTeam, LocalDateTime plusDays);

    Player getPlayer(String playerName);

    Team getTeam(String teamName, Player[] players);
    
    String getType();
    
}
