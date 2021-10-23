package com.imc.workshop.datamodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Trade implements MarketEvent {
    private final String theInstrument;
    private final long theTimestamp;
    private final long theSequenceNumber;
    private final int theVolume;
    private final double thePrice;

    @JsonCreator
    public Trade(@JsonProperty("instrument") String aInstrument,
            @JsonProperty("volume") int aVolume,
            @JsonProperty("price") double aPrice,
            @JsonProperty("timestamp") long aTimestamp,
            @JsonProperty("sequenceNumber") long aSequenceNumber) {
        theVolume = aVolume;
        thePrice = aPrice;
        theInstrument = aInstrument;
        theTimestamp = aTimestamp;
        theSequenceNumber = aSequenceNumber;
    }

    @Override
    public String getInstrument() {
        return theInstrument;
    }

    @Override
    public String prettyPrint() {
        return "Trade: " + theInstrument + ": " + theVolume + " @ " + thePrice;
    }

    @Override
    public MarketEvent withSeqNum(long aSeqNum) {
        return new Trade(theInstrument, theVolume, thePrice, theTimestamp, aSeqNum);
    }

    @Override
    public long getTimestamp() {
        return theTimestamp;
    }

    @Override
    public long getSequenceNumber() {
        return theSequenceNumber;
    }

    public int getVolume() {
        return theVolume;
    }

    public double getPrice() {
        return thePrice;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) {
            return true;
        }
        if (aO == null || getClass() != aO.getClass()) {
            return false;
        }
        final Trade myTrade = (Trade) aO;
        return theTimestamp == myTrade.theTimestamp &&
               theSequenceNumber == myTrade.theSequenceNumber &&
               theVolume == myTrade.theVolume &&
               Double.compare(myTrade.thePrice, thePrice) == 0 &&
               theInstrument.equals(myTrade.theInstrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theInstrument, theTimestamp, theSequenceNumber, theVolume, thePrice);
    }
}
