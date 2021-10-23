package com.imc.workshop.datamodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PositionLimits implements Result {
    private final String theInstrument;
    private final long theLastSequenceNumber;
    private final double theMaxAllowedBuyVolume;
    private final double theMaxAllowedSellVolume;

    @JsonCreator
    public PositionLimits(@JsonProperty("instrument") String aInstrument,
            @JsonProperty("lastSequenceNumber") long aLastSequenceNumber,
            @JsonProperty("maxAllowedBuyVolume") double aMaxAllowedBuyVolume,
            @JsonProperty("maxAllowedSellVolume") double aMaxAllowedSellVolume) {
        theInstrument = aInstrument;
        theLastSequenceNumber = aLastSequenceNumber;
        theMaxAllowedBuyVolume = aMaxAllowedBuyVolume;
        theMaxAllowedSellVolume = aMaxAllowedSellVolume;
    }

    @Override
    public String getInstrument() {
        return theInstrument;
    }

    @Override
    public long getLastSequenceNumber() {
        return theLastSequenceNumber;
    }

    public double getMaxAllowedBuyVolume() {
        return theMaxAllowedBuyVolume;
    }

    public double getMaxAllowedSellVolume() {
        return theMaxAllowedSellVolume;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) {
            return true;
        }
        if (aO == null || getClass() != aO.getClass()) {
            return false;
        }
        final PositionLimits myLimits = (PositionLimits) aO;
        return theLastSequenceNumber == myLimits.theLastSequenceNumber &&
               Double.compare(myLimits.theMaxAllowedBuyVolume, theMaxAllowedBuyVolume) == 0 &&
               Double.compare(myLimits.theMaxAllowedSellVolume, theMaxAllowedSellVolume) == 0 &&
               theInstrument.equals(myLimits.theInstrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theInstrument, theLastSequenceNumber, theMaxAllowedBuyVolume, theMaxAllowedSellVolume);
    }

    @Override
    public String toString()
    {
        return "PositionLimits{" +
               "theInstrument='" + theInstrument + '\'' +
               ", theLastSequenceNumber=" + theLastSequenceNumber +
               ", theMaxAllowedBuyVolume=" + theMaxAllowedBuyVolume +
               ", theMaxAllowedSellVolume=" + theMaxAllowedSellVolume +
               '}';
    }
}
