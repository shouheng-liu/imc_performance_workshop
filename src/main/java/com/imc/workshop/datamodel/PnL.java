package com.imc.workshop.datamodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PnL implements Result {
    private final String theInstrument;
    private final long theLastSequenceNumber;
    private final int thePosition;
    private final double thePnL;
    private final double thePositionCashValue;

    @JsonCreator
    public PnL(@JsonProperty("instrument") String aInstrument,
            @JsonProperty("lastSequenceNumber") long aLastSequenceNumber,
            @JsonProperty("position") int aPosition,
            @JsonProperty("pnL") double aPnL,
            @JsonProperty("positionCashValue") double aPositionCashValue) {
        theInstrument = aInstrument;
        theLastSequenceNumber = aLastSequenceNumber;
        thePosition = aPosition;
        thePnL = aPnL;
        thePositionCashValue = aPositionCashValue;
    }

    @Override
    public String getInstrument() {
        return theInstrument;
    }

    @Override
    public long getLastSequenceNumber() {
        return theLastSequenceNumber;
    }

    public int getPosition() {
        return thePosition;
    }

    public double getPnL() {
        return thePnL;
    }

    public double getPositionCashValue() {
        return thePositionCashValue;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) {
            return true;
        }
        if (aO == null || getClass() != aO.getClass()) {
            return false;
        }
        final PnL myPnL = (PnL) aO;
        return theLastSequenceNumber == myPnL.theLastSequenceNumber &&
               thePosition == myPnL.thePosition &&
               Double.compare(myPnL.thePnL, thePnL) == 0 &&
               theInstrument.equals(myPnL.theInstrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theInstrument, theLastSequenceNumber, thePosition, thePnL);
    }
}
