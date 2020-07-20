/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import gameapi.Game;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



public class JacksonUtil {
    
    static ObjectMapper getMapper() {
        LocalDateDeserializer deserializer = new LocalDateDeserializer();
        LocalDateSerializer serializer = new LocalDateSerializer();
        
        SimpleModule module = new SimpleModule("LongDeserializerModule", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(LocalDateTime.class, deserializer);
        module.addSerializer(LocalDateTime.class, serializer);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.enableDefaultTyping();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        
        return mapper;
    }
    
    public static void saveToJSONFile(String dirName, String filename, Game[] allGames) {
        
        
        try {
            getMapper().writeValue(new File(dirName, filename), allGames);
        } catch (IOException ioe) {
            System.out.println("trouble writing");
            //ioe.printStackTrace();
            System.out.println(ioe.getMessage());
        }
        
    }
    
    public static Game[] getGamesFromJSONFile(String dirName, String filename ) {
        
        Game[] allGames = null;
        try {
    
            allGames = getMapper().readValue(new File(dirName, filename), Game[].class);

        } catch (IOException ioe) {
            System.out.println("Trouble reading");
            //ioe.printStackTrace();
            System.out.println(ioe.getMessage());
        }
        return allGames;
        
    }
    public static String getJSONListOfGames(ArrayList<List<Game>> allResults ) {
        
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        String theResults = null;

        try {
            
            theResults = mapper.writeValueAsString(allResults);
        } catch (IOException ioe) {
            System.out.println("trouble writing");
            //ioe.printStackTrace();
            System.out.println(ioe.getMessage());
        }

        return theResults;
        
    }
    

    
}
