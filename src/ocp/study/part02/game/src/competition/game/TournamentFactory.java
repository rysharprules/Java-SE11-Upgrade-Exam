/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import gameapi.Team;
import gameapi.TournamentType;
import java.util.ServiceLoader;

/**
 *
 * @author kenny
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
