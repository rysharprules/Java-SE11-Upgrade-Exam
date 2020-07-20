/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
module soccer {
    requires gameapi;
    requires java.logging;
    exports soccer;
    opens soccer to jackson.databind;
    
    provides gameapi.GameProvider with soccer.SoccerProvider;
    
}
