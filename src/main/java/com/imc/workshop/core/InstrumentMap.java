package com.imc.workshop.core;

import java.util.*;

public class InstrumentMap {
    private final Map<String, Integer> theInstrumentMap = new HashMap<>();
    private int theNextInstrumentId = 0;

    public int get(String aInstrument) {
        return Optional.ofNullable(theInstrumentMap.get(aInstrument))
            .orElseThrow();
    }

    public int getOrCreate(String aInstrument) {
        return theInstrumentMap.computeIfAbsent(aInstrument, aS -> theNextInstrumentId++);
    }

    public Set<String> getInstrumentNames() {
        return theInstrumentMap.keySet();
    }
}
