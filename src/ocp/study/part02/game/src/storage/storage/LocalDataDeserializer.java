/* Copyright © 2017 Oracle and/or its affiliates. All rights reserved. */

package storage;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LocalDateDeserializer extends StdDeserializer<LocalDateTime> {//StdDeserializer

    private static final long serialVersionUID = 1L;

    protected LocalDateDeserializer() {       
        super(LocalDateTime.class);
        //System.out.println("I've instantiated custom deserialization!");
    }


    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        //System.out.println("I'm in the deserialize method!");
        return LocalDateTime.parse(jp.readValueAs(String.class),DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
