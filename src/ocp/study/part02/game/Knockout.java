package game;


import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import java.util.stream.Collectors;

import utils.Settings;
import gameapi.Game;
import gameapi.GameEvent;
import gameapi.GameResult;
import gameapi.Player;
import gameapi.Team;
import gameapi.TournamentType;

/*
module competition {
    requires transitive gameapi;

    exports game;
    exports utils;

    uses gameapi.GameProvider;

    uses gameapi.TournamentType;
    provides gameapi.TournamentType with game.League, game.Knockout;
}
 */
public class Knockout implements TournamentType {

  public Knockout(){};

  @Override
  public void populate( String gameType, Team[] theTeams) {
    // Check if correct number of teams to create a knockout tournament
    if (Math.log(theTeams.length) / Math.log(2) != (int) (Math.log(theTeams.length) / Math.log(2)) || theTeams.length < 2) {
      System.out.println("Number of teams for knockout tournament should be > 3 and a power of 2 (e.g. 4, 8, 16... etc.");
      System.exit(0);
      //throw new Exception("");
    }

    this.teams = theTeams;
    this.gameType = gameType;
  }


  public Knockout(Game[] allGames) {
    games = this.flatArrayTo2DList(allGames);
  }


  @Override
  public Team[] getTeams() {
    return this.teams;
  }

  private String gameType;

  private List<List<Game>> games;
  private Team[] teams;

  // TODO - tidy this method
  // Maybe this should be half of showBestTeam
  // Do we really need teams here? Can we get from the Games?
  public void setTeamStats(Team[] theTeams, Game[] theGames) {

    // zero all Team scores and Player scores
    for (Team currTeam : theTeams) {
      currTeam.setGoalsTotal(0);
      currTeam.setPointsTotal(0);
      // zero all Player scores
      for (Player currPlayer : currTeam.getPlayerArray()) {
        currPlayer.setGoalsScored(0);
      }
    }

    // Repopulate goalsTotal and pointsTotal on each Team object
    //
    // Note all that is needed from GameResult is:
    // 1. Is the game drawn?
    // 2. Who won the game? (no winner equals drawn? but not good to pass null).
    // 3. What did homeTeam score, what did awayTeam score?
    //
    // Should be possible to get homeTeam and awayTeam from currGame
    for (Game currGame : theGames) {

      GameResult theResult = currGame.getGameResult(); //
      //IGameResult theResult = new GameResult(currGame); // Maybe above is better

      // Increment pointsTotal on Team
      if (theResult.isDrawnGame()) {
        currGame.getHomeTeam().incPointsTotal(Settings.DRAWN_GAME_POINTS);   // Another way to do it currGame vs. theResult
        theResult.getAwayTeam().incPointsTotal(Settings.DRAWN_GAME_POINTS);
      } else {
        theResult.getWinner().incPointsTotal(Settings.WINNER_GAME_POINTS);
      }

      // Increment goalsTotal in Team
      theResult.getHomeTeam().incGoalsTotal(theResult.getHomeTeamScore());
      theResult.getAwayTeam().incGoalsTotal(theResult.getAwayTeamScore());

    }
  }

  public void showBestTeam(Team[] theTeams) {

    // Note below method puts last first and first last if no scoring at all!
    Arrays.sort(theTeams);
    Team currBestTeam = theTeams[0];

    // Increment league counter
    //currBestTeam.leaguesWon++;
    //System.out.println("\nTeam Points");
    for (Team currTeam : theTeams) {
      //System.out.println(currTeam.getTeamName() + " : " + currTeam.getPointsTotal() + " : " + currTeam.getGoalsTotal());

    }
    // XX Temp commented out - wd be better returning it anyway.
    System.out.println("Winner of the competition is " + currBestTeam.getTeamName());

  }

  public String getLeagueAnnouncement(Game[] theGames) {

    Period thePeriod = Period.between(theGames[0].getTheDateTime().toLocalDate(),
        theGames[theGames.length - 1].getTheDateTime().toLocalDate());

    return "The league is scheduled to run for "
        + thePeriod.getMonths() + " month(s), and "
        + thePeriod.getDays() + " day(s)\n";
  }

  // Should this zero all players first therefore make getAllPlayers() a util method? TODO
  public void setPlayerStats(Game[] theGames) {
    for (Game currGame : theGames) {
      for (GameEvent currEvent : currGame.getEvents()) {
        if (currEvent.isGoal()) {
          currEvent.getThePlayer().incGoalsScored();
        }
      }
    }

  }

  public void showBestPlayersByLeague() {
    ArrayList<Player> thePlayers = new ArrayList<>();
    for (Team currTeam : teams) {
      thePlayers.addAll(Arrays.asList(currTeam.getPlayerArray()));
    }

    Collections.sort(thePlayers, (p1, p2) -> Double.valueOf(p2.getGoalsScored()).compareTo(Double.valueOf(p1.getGoalsScored())));

    // How to get the team the player is in? TODO.
    System.out.println("\n\nBest Players in League");
    for (Player currPlayer : thePlayers) {
      System.out.println(currPlayer.getPlayerName() + " : " + currPlayer.getGoalsScored());
    }
  }

  public void showBestPlayersByTeam() {

    for (Team currTeam : teams) {
      Arrays.sort(currTeam.getPlayerArray(), (p1, p2) -> Double.valueOf(p2.getGoalsScored()).compareTo(Double.valueOf(p1.getGoalsScored())));

      System.out.println("\n\nBest Players in " + currTeam.getTeamName());
      for (Player currPlayer : currTeam.getPlayerArray()) {
        System.out.println(currPlayer.getPlayerName() + " : " + currPlayer.getGoalsScored());
      }

    }

  }

  // Returns a round of games - is this correct? Or should it return tree with some games not
  // populated yet?
  //
  public Game[] createGames(String gameType) {
    //TODO Have it throw exception
    int daysBetweenGames = 0;

    ArrayList<Game> theGames = new ArrayList<>();

    //for (Team homeTeam: teams){
    for (int i = 0; i < teams.length; i += 2) {

      daysBetweenGames += Settings.DAYS_BETWEEN_GAMES;

      // *** This is where the concrete class instantiated
      //theGames.add(GameFactory.getGame(gameType, teams[i], teams[i + 1], LocalDateTime.now().plusDays(daysBetweenGames)));
      //System.out.println("Getting GameFactory --> " + gameType);
      //theGames.add(GameFactory.getProvider(gameType).getGame(teams[i], teams[i + 1], LocalDateTime.now().plusDays(daysBetweenGames)));
      theGames.add(Factory.createGame(gameType, teams[i], teams[i + 1], LocalDateTime.now().plusDays(daysBetweenGames)));

    }

    return theGames.toArray(new Game[1]);
  }


  @Override
  public void createAndPlayAllGames() {   // TODO return something to indicate success? winner? list?

    List<Game> gameRound = null;
    GameResult thisGame = null;

    List<List<Game>> allGames = new ArrayList<List<Game>>();

    int round = 0;

    while (teams.length > 1) {

      round++;
      // Create knockout rounds - this will repeat until only one team left (the winner)
      // Maybe should use ArrayLists and not arrays?
      Game[] theGames = this.createGames(gameType); // Each round of games

      // Now that theGames are created, we need a new array for the teams that will go forward
      teams = new Team[teams.length / 2];
      //IGame[][] allGames = new Game[theGames.length][(int)(Math.log(theGames.length)/Math.log(2))]; // Find number of rounds
      //System.out.println(allGames.length + " rounds");

      //gameRound = Arrays.asList(theGames);
      //ArrayList<IGame>
      gameRound = new ArrayList<>(Arrays.asList(theGames));

      // Populate first round of the allGames ArrayList
      allGames.add(gameRound);

      //for(int i=0;i<1000;i++) { // for loop to play the league x times for testing
      //System.out.println(theLeague.getLeagueAnnouncement(theGames));
      // Play all the games in each round
      int i = 0;
      for (Game currGame : gameRound) {
        currGame.playGame();
        while (currGame.getGameResult().isDrawnGame()) {
          //thisGame = currGame.getGameResult();
          //System.out.println("Replaying " + thisGame.getHomeTeam() + "/"  + thisGame.getAwayTeam() );
          currGame.playGame(); // Or penalties?
        }
        thisGame = currGame.getGameResult();
        //System.out.print("Round " + round + ":" + thisGame.getHomeTeam() + "/"  + thisGame.getAwayTeam() );
        //System.out.println("  (" + thisGame.getWinner()+ ")" );
        // Teams to promote to next round
        teams[i] = currGame.getGameResult().getWinner();

        i++;
      }

    }
    // TODO: to set stats on knockout teams, players,
    // Need to convert to flat array, then call below,
    // then convert back
    //
    //this.setTeamStats(teams, theGames);
    //this.setPlayerStats(theGames);
    //this.showBestTeam(teams); // Sets team order in the GRID
    games = allGames;
  }

//    public void saveGamesToJSONFile(String dirName, String filename) {
//            // Call util method to create flat array
//            Game[] allGames = this.flatten2DListToArray(games);
//
//            // Call util method to save as JSON string
//            JacksonUtil.saveToJSONFile(dirName, filename, allGames);
//        }
//
//    // Unlike in League this method doesn't need to restore teams as no
//    // useful data like points and goals are stored there
//    // Could be added if deemed useful
//    public void getGamesFromJSONFile(String dirName, String filename) {
//        //System.out.println("Reading knockout games from JSON");  // TODO maybe log
//        Game[] allGames = JacksonUtil.getGamesFromJSONFile(dirName, filename);
//
//        // Call util method to turn flat file back into 2DList
//        games = this.flatArrayTo2DList(allGames);
//
//    }

  // TODO This is same code as saveGamesToJSONFile so DRY it!
  @Override
  public Game[] getGames(){
    Game[] allGames = this.flatten2DListToArray(this.games);
    return allGames;
  }


  // ** private utility methods for converting a Game[] to List<List<IGame>> and vice versa
  // Convert  to Game[]
  private Game[] flatten2DListToArray(List<List<Game>> knockoutGames) {
    Game[][] arrayAllGames = knockoutGames.stream().map(u -> u.toArray(new Game[0])).toArray(Game[][]::new);

    // First find out what size to make the new array
    int numGames = 0;
    for (Game[] outerArray : arrayAllGames) {
      for (Game theGame : outerArray) {
        //System.out.println(theGame.getDescription());
        numGames++;
      }
    }

    // Convert 2D array to 1D array
    Game[] flatArrayGames = new Game[numGames];
    numGames = 0;
    for (Game[] outerArray : arrayAllGames) {
      for (Game theGame : outerArray) {
        flatArrayGames[numGames] = theGame;
        numGames++;
      }
    }
    return flatArrayGames;
  }

  private List<List<Game>> flatArrayTo2DList(Game[] tempGames) {

    int numGamesInFirstRound = (tempGames.length + 1) / 2;
    //int numRounds = (int)(Math.log(teams.length) / Math.log(2));
    int numRounds = (int) (Math.log(numGamesInFirstRound * 2) / Math.log(2));
    //System.out.println("Number of Rounds " + numRounds);
    //System.out.println("Number game in 1 " + numGamesInFirstRound);
    // Create outer array
    Game[][] outerArray = new Game[numRounds][];

    int roundNum = 0;
    int indexNum = 0;
    for (int i = numGamesInFirstRound; i > 0; i = i / 2) {

      // Create innerArray
      Game[] innerArray = new Game[i];
      for (int j = 0; j < i; j++) {

        innerArray[j] = tempGames[indexNum];
        indexNum++;

      }
      // Add innerArray to outerArray
      outerArray[roundNum] = innerArray;
      roundNum++;
    }

    List<List<Game>> newKnGames = Arrays.stream(outerArray).map(Arrays::asList).collect(Collectors.toList());
    return newKnGames;
  }

  public String getName() {
    return "knockout";
  }

}
