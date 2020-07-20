/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package database;

import utils.*;
import game.Factory;
import gameapi.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;


public class PlayerDatabase {
    
    private ArrayList <Player> players;
    
    public PlayerDatabase(String gameType){
        String authorNames = readNames();
        StringTokenizer authorTokens = new StringTokenizer(authorNames, ",");
        players = new ArrayList<Player>();
        while (authorTokens.hasMoreTokens()){

            players.add(Factory.createPlayer(gameType, authorTokens.nextToken()));

        }
        //System.out.println("Last player in file is " + (players.get(players.size() - 1)).getPlayerName());
    }
    
    public Player[] getTeamPlayers(int numberOfPlayers) throws PlayerDatabaseException {
        Player[] teamPlayers = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++){
            int playerIndex = (int) (Math.random() * players.size());
            try {
            teamPlayers[i] = players.get(playerIndex);
            players.remove(playerIndex);
            }
            catch(IndexOutOfBoundsException ie){
                throw new PlayerDatabaseException("Not enough players in the database for the teams requested.");
            }
        }
        return teamPlayers;
        
    }
    
    public static String readNames()  {  
    String authors = "";
    Charset charset = Charset.forName("UTF-8");
    try (BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().
            getPath(Settings.dirName,Settings.fileName), charset)) {
        String line = "";
        while ((line = reader.readLine()) != null) {
            //System.out.println(line);
            authors += line + ",";
        }
    } catch (IOException x) {
        System.err.format("IOException: %s%n", x);
    }
    return authors;
}
    
    
        
String authorList = 
"Agatha Christie," + 
"Alan Patton," +
"Alexander Solzhenitsyn," +
"Arthur Conan Doyle," +
"Aldous Huxley" +
"Anthony Trollope," +
"Baroness Orczy," +          
"Brendan Behan," +      
"Brian Moore," +
"Boris Pasternak," +
"Charles Dickens," +    
"Charlotte Bronte," +
"Dorothy Parker," +
"Emile Zola," +
"Ernest Hemmingway" +
"Frank O'Connor," +        
"Geoffrey Chaucer," +
"George Eliot," +
"G. K. Chesterton," +
"Graham Green," +
"Henry James," +
"James Joyce," +        
"J. M. Synge," + 
"J. R. Tolkien," +
"Jane Austin," +
"Leo Tolstoy," +
"Liam O'Flaherty," +
"Marcel Proust," +
"Mark Twain," +
"Oscar Wilde," +
"O. Henry," +
"Samuel Beckett," +
"Sean O'Casey," +
"William Shakespeare," +        
"William Makepeace Thackeray," +
"W. B. Yeats," +
"Wilkie Collins";
    
}
