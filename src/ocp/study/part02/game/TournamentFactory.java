package game;

import gameapi.Team;
import gameapi.TournamentType;
import java.util.ServiceLoader;

/*
07_02
module competition {
    requires transitive gameapi;

    exports game;
    exports utils;

    uses gameapi.GameProvider;

    uses gameapi.TournamentType;
    provides gameapi.TournamentType with game.League, game.Knockout;
}
 */
public class TournamentFactory {

  public static TournamentType getTournament(String tourneyType, String gameType, Team[] theTeams) {

    TournamentType theTourney = getTournament(tourneyType);
    theTourney.populate(gameType, theTeams);

    return theTourney;

  }

  public static TournamentType getTournament(String name) {

    TournamentType theTourney = null;
    ServiceLoader<TournamentType> sl = ServiceLoader.load(TournamentType.class);

    for ( TournamentType currTournament: sl) {

      if (currTournament.getName().equalsIgnoreCase(name)) {
        theTourney = currTournament;
        break;
      }

    }

    if (theTourney == null) {
      throw new RuntimeException("No suitable service provider found!");
    }

    return theTourney;
  }

}
