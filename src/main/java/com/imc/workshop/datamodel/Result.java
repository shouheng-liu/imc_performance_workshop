package com.imc.workshop.datamodel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({@Type(value = PositionLimits.class, name = "positionLimits"),
        @Type(value = PnL.class, name = "pnl")
})
public interface Result {
    String getInstrument();

    long getLastSequenceNumber();
}
