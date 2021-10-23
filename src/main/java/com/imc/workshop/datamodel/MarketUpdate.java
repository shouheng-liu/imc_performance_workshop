package com.imc.workshop.datamodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MarketUpdate implements MarketEvent {
    private final String theInstrument;
    private final long theSequenceNumber;
    private final long theTimestamp;
    private final MarketSide theBid;
    private final MarketSide theAsk;

    @JsonCreator
    public MarketUpdate(@JsonProperty("instrument") String aInstrument,
            @JsonProperty("sequenceNumber") long aSequenceNumber,
            @JsonProperty("timestamp") long aTimestamp,
            @JsonProperty("bid") MarketSide aBid,
            @JsonProperty("ask") MarketSide aAsk) {
        theInstrument = aInstrument;
        theSequenceNumber = aSequenceNumber;
        theTimestamp = aTimestamp;
        theBid = aBid;
        theAsk = aAsk;
    }

    public MarketSide getBid() {
        return theBid;
    }

    public MarketSide getAsk() {
        return theAsk;
    }

    @Override
    public long getTimestamp() {
        return theTimestamp;
    }

    @Override
    public long getSequenceNumber() {
        return theSequenceNumber;
    }

    @Override
    public String getInstrument() {
        return theInstrument;
    }

    public static class MarketSide {
        private final double theBestPrice;
        private final int theVolume;

        @JsonCreator
        public MarketSide(@JsonProperty("bestPrice") double aBestPrice, @JsonProperty("volume") int aVolume) {
            theBestPrice = aBestPrice;
            theVolume = aVolume;
        }

        public double getBestPrice() {
            return theBestPrice;
        }

        public int getVolume() {
            return theVolume;
        }

        @Override
        public boolean equals(Object aO) {
            if (this == aO) {
                return true;
            }
            if (aO == null || getClass() != aO.getClass()) {
                return false;
            }
            final MarketSide myUpdate = (MarketSide) aO;
            return Double.compare(myUpdate.theBestPrice, theBestPrice) == 0
                   && theVolume == myUpdate.theVolume;
        }

        public String prettyPrint() {
            return theVolume + " @ " + theBestPrice;
        }

        @Override
        public int hashCode() {
            return Objects.hash(theBestPrice, theVolume);
        }
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) {
            return true;
        }
        if (aO == null || getClass() != aO.getClass()) {
            return false;
        }
        final MarketUpdate myUpdate = (MarketUpdate) aO;
        return theSequenceNumber == myUpdate.theSequenceNumber &&
               theTimestamp == myUpdate.theTimestamp &&
               theInstrument.equals(myUpdate.theInstrument) &&
               theBid.equals(myUpdate.theBid) &&
               theAsk.equals(myUpdate.theAsk);
    }

    @Override
    public String prettyPrint() {
        return theInstrument + ":" + theBid.prettyPrint() + " | " + theAsk.prettyPrint();
    }

    @Override
    public MarketEvent withSeqNum(long aSeqNum) {
        return new MarketUpdate(theInstrument, aSeqNum, theTimestamp, theBid, theAsk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theInstrument, theSequenceNumber, theTimestamp, theBid, theAsk);
    }
}
