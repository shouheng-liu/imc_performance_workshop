package com.imc.workshop.integrationtest;

import com.fasterxml.jackson.databind.*;
import com.imc.workshop.core.*;
import com.imc.workshop.core.limits.*;
import com.imc.workshop.core.pnl.*;
import com.imc.workshop.datamodel.*;

import java.io.*;
import java.util.*;

public class LimitsGenerator
{
    public static final String MARKET_EVENTS_FILENAME = "marketevents.json";
    private static final double MAXIMUM_RISK = 1000.;
    public static final String OUTPUT_FILENAME = "positionlimits.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException
    {
        List<MarketEvent> myMarketEvents = loadMarketEvents(MARKET_EVENTS_FILENAME);
        List<PositionLimits> myPositionLimits = replayMarketEvents(myMarketEvents, Covariances.getCovariances());

        FileOutputStream myFileOutputStream = new FileOutputStream(OUTPUT_FILENAME);

        OBJECT_MAPPER.writeValue(myFileOutputStream, new PositionLimitsList(myPositionLimits));
    }

    static List<MarketEvent> loadMarketEvents(String aMarketEventFilename) throws IOException
    {
        List<MarketEvent> myMarketEvents = new ArrayList<>();
        new MarketEventsReader().read(aMarketEventFilename).forEachRemaining(myMarketEvents::add);

        return myMarketEvents;
    }

    static List<PositionLimits> replayMarketEvents(List<MarketEvent> aMarketEvents, List<Covariance> aCovarianceList)
    {
        LimitsCalculator myLimitsCalculator = new LimitsCalculator(MAXIMUM_RISK);
        Core myCore = new SimpleCore(new PnLCalculator(), myLimitsCalculator);

        aCovarianceList.forEach(myCore::handleCovariance);

        List<PositionLimits> myPositionLimits = new ArrayList<>();
        aMarketEvents.forEach(aMarketEvent -> handleMarketEvent(myCore, aMarketEvent, myLimitsCalculator, myPositionLimits));

        return myPositionLimits;
    }

    private static class PositionLimitsList extends ArrayList<PositionLimits> {
        PositionLimitsList(List<PositionLimits> aPositionLimits) {
            super(aPositionLimits);
        }
    }

    private static void handleMarketEvent(Core aCore, MarketEvent aMarketEvent, LimitsCalculator aLimitsCalculator, List<PositionLimits> aPositionLimits)
    {
        if (aMarketEvent instanceof MarketUpdate) {
            aCore.handleMarketUpdate((MarketUpdate) aMarketEvent);
        } else {
            aCore.handleTrade((Trade) aMarketEvent);
        }

        aPositionLimits.addAll(aLimitsCalculator.getLimits());
    }
}
