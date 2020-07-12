package basketball;

import gameapi.Game;
import gameapi.GameProvider;
import gameapi.Player;
import gameapi.Team;
import java.time.LocalDateTime;

/*
module basketball {
    requires gameapi;
    requires java.logging;
    opens basketball to jackson.databind;

    provides gameapi.GameProvider with basketball.BasketballProvider;
}
 */
public class BasketballProvider implements GameProvider {

  @Override
  public Game getGame(Team homeTeam, Team awayTeam,
      LocalDateTime plusDays) {
    return new Basketball(homeTeam, awayTeam, plusDays);
  }

  @Override
  public Player getPlayer(String playerName) {
    return new BasketballPlayer(playerName);
  }

  @Override
  public Team getTeam(String teamName, Player[] players) {
    return new BasketballTeam(teamName, players);
  }

  @Override
  public String getType() {
    return "basketball";
  }

}
