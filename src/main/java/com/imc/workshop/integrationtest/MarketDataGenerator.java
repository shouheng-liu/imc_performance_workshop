package com.imc.workshop.integrationtest;

import com.imc.workshop.datamodel.MarketEvent;
import com.imc.workshop.datamodel.MarketUpdate;
import com.imc.workshop.datamodel.MarketUpdate.MarketSide;
import com.imc.workshop.datamodel.Trade;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.LongSupplier;

public class MarketDataGenerator {
    private final String theInstrumentName;
    private final double theVolatility;
    private final double theSpread;
    private final int theAverageVolume;
    private final ZonedDateTime theStartTime;
    private final ZonedDateTime theEndDateTime;
    private final long theUpdateIntervalNanos;
    private final LongSupplier theSequenceNumberSource;
    private final Random theRandom;
    private final List<Consumer<MarketEvent>> theOutputHandlers;
    private final double theStartingPrice;
    private double theLastPrice;
    private int thePosition = 0;

    public MarketDataGenerator(String aInstrumentName,
            double aStartingPrice,
            double aVolatility,
            double aSpread,
            int aAverageVolume,
            ZonedDateTime aStartTime,
            ZonedDateTime aEndDateTime,
            long aUpdateIntervalNanos,
            LongSupplier aSequenceNumberSource) {
        theInstrumentName = aInstrumentName;
        theVolatility = aVolatility;
        theSpread = aSpread;
        theAverageVolume = aAverageVolume;
        theStartTime = aStartTime;
        theEndDateTime = aEndDateTime;
        theUpdateIntervalNanos = aUpdateIntervalNanos;
        theSequenceNumberSource = aSequenceNumberSource;
        theRandom = new Random();
        theOutputHandlers = new ArrayList<>();
        theLastPrice = aStartingPrice;
        theStartingPrice = aStartingPrice;
    }

    public void generateData() {
        ZonedDateTime myCurrentTime = theStartTime;
        while (myCurrentTime.isBefore(theEndDateTime)) {
            double myBid = round(theLastPrice);
            double myAsk = round(theLastPrice + theSpread);
            int myBidVolume = getVolume();
            int myAskVolume = getVolume();
            MarketUpdate myUpdate = new MarketUpdate(theInstrumentName,
                    theSequenceNumberSource.getAsLong(),
                    getTimestamp(myCurrentTime),
                    new MarketSide(myBid, myBidVolume),
                    new MarketSide(myAsk, myAskVolume));
            write(myUpdate);
            if (myBid > theStartingPrice + theVolatility * 2 - thePosition * theVolatility) {
                thePosition -= myBidVolume;
                write(new Trade(theInstrumentName,
                        -myBidVolume,
                        myBid,
                        getTimestamp(myCurrentTime),
                        theSequenceNumberSource.getAsLong()));
            } else if (myAsk < theStartingPrice - theVolatility * 2 - thePosition * theVolatility) {
                thePosition += myAskVolume;
                write(new Trade(theInstrumentName,
                        myAskVolume,
                        myAsk,
                        getTimestamp(myCurrentTime),
                        theSequenceNumberSource.getAsLong()));
            }

            myCurrentTime = myCurrentTime.plusNanos(theUpdateIntervalNanos);
            theLastPrice = Math.max(theLastPrice + theRandom.nextGaussian() * theVolatility, 1);
        }
    }

    public void addHandler(Consumer<MarketEvent> aHandler) {
        theOutputHandlers.add(aHandler);
    }

    private int getVolume() {
        return Math.max(theAverageVolume + theRandom.nextInt(theAverageVolume) - theAverageVolume / 2, 0);
    }

    private static long getTimestamp(ZonedDateTime aTime) {
        return TimeUnit.SECONDS.toNanos(aTime.toEpochSecond()) + (long) aTime.getNano();
    }

    private void write(MarketEvent aEvent) {
        theOutputHandlers.forEach(c -> c.accept(aEvent));
    }

    private double round(double aPrice) {
        return ((int) (aPrice / theSpread)) * theSpread;
    }

    public static void main(String[] args) {
        ZonedDateTime myNow = ZonedDateTime.now();
        MarketDataGenerator myGenerator =
                new MarketDataGenerator("AAPL", 100, 0.1, 0.1, 10, myNow, myNow.plusMinutes(1), 1000000000L, () -> 1);
        myGenerator.addHandler(u -> System.out.println(u.prettyPrint()));
        myGenerator.generateData();
    }
}
