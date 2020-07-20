/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
module competition {
    requires transitive gameapi;

    exports game;
    exports utils;
    
    uses gameapi.GameProvider;
    
    uses gameapi.TournamentType;
    provides gameapi.TournamentType with game.League, game.Knockout;
}
