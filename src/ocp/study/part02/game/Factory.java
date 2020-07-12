package game;


import gameapi.Team;
import gameapi.Game;
import gameapi.Player;

import gameapi.GameProvider;
import java.time.LocalDateTime;
import java.util.ServiceLoader;

/*
07_01
module competition {
    requires transitive gameapi;

    exports game;
    exports utils;

    uses gameapi.GameProvider;
}
 */
public class Factory {

  static GameProvider theProvider = null;

  public static GameProvider getProvider(String gameType) {

    if ((theProvider != null)
        && theProvider.getType().equals(gameType)) {
      return theProvider;
    }


    ServiceLoader<GameProvider> loader = ServiceLoader.load(GameProvider.class);

    for (GameProvider currProvider: loader){
      //System.out.println(currProvider.getClass() + " : " + currProvider.hashCode());
      if (currProvider.getType().equalsIgnoreCase(gameType)){
        theProvider = currProvider;

        break;
      }

    }

    if (theProvider == null) {
      throw new RuntimeException("No suitable service provider found !");
    }

    return theProvider;

  }

  public static Team createTeam(String gameType, String teamName, Player[] thePlayers) {

    theProvider = getProvider(gameType);
    return theProvider.getTeam(teamName.trim(), thePlayers);

  }

  public static Game createGame(String gameType, Team homeTeam, Team awayTeam, LocalDateTime dateOfGame) {

    theProvider = getProvider(gameType);
    return theProvider.getGame(homeTeam, awayTeam, dateOfGame);

  }

  public static Player createPlayer(String gameType, String playerName) {

    theProvider = getProvider(gameType);
    return theProvider.getPlayer(playerName);

  }
}
