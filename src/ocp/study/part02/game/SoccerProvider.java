package soccer;

import gameapi.Game;
import gameapi.GameProvider;
import gameapi.Player;
import gameapi.Team;
import java.time.LocalDateTime;

/*
module soccer {
    requires gameapi;
    requires java.logging;
    opens soccer to jackson.databind;

    provides gameapi.GameProvider with soccer.SoccerProvider;
}
 */
public class SoccerProvider implements GameProvider {

  @Override
  public Game getGame(Team homeTeam, Team awayTeam,
      LocalDateTime plusDays) {
    return new Soccer(homeTeam, awayTeam, plusDays);
  }

  @Override
  public Player getPlayer(String playerName) {
    return new SoccerPlayer(playerName);
  }

  @Override
  public Team getTeam(String teamName, Player[] players) {
    return new SoccerTeam(teamName, players);
  }

  @Override
  public String getType() {
    return "soccer";
  }

}
