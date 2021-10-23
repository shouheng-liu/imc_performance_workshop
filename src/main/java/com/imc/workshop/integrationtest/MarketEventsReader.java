package com.imc.workshop.integrationtest;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imc.workshop.datamodel.MarketEvent;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class MarketEventsReader {
    private final ObjectMapper theObjectMapper;

    public MarketEventsReader() {
        theObjectMapper = new ObjectMapper();
    }

    public Iterator<MarketEvent> read(String aFileName) throws IOException {
        return theObjectMapper.readerFor(MarketEvent.class).readValues(this.getClass().getClassLoader().getResourceAsStream(aFileName));
    }

    public static void main(String[] args) throws IOException {
        new MarketEventsReader().read("marketevents.json").forEachRemaining(System.out::println);
    }
}
