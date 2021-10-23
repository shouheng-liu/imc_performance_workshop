package com.imc.workshop.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({@Type(value = MarketUpdate.class, name = "marketUpdate"),
        @Type(value = Trade.class, name = "trade")
})
public interface MarketEvent {
    long getTimestamp();

    long getSequenceNumber();

    String getInstrument();

    @JsonIgnore
    String prettyPrint();

    MarketEvent withSeqNum(long aSeqNum);
}
