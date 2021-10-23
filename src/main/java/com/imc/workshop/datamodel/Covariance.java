package com.imc.workshop.datamodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Covariance {
    private final String theFirstInstrument;
    private final String theSecondInstrument;
    private final double theCovariance;

    @JsonCreator
    public Covariance(@JsonProperty("firstInstrument") String aFirstInstrument,
            @JsonProperty("secondInstrument") String aSecondInstrument,
            @JsonProperty("covariance") double aCovariance) {
        theFirstInstrument = aFirstInstrument;
        theSecondInstrument = aSecondInstrument;
        theCovariance = aCovariance;
    }

    public String getFirstInstrument() {
        return theFirstInstrument;
    }

    public String getSecondInstrument() {
        return theSecondInstrument;
    }

    public double getCovariance() {
        return theCovariance;
    }
}
