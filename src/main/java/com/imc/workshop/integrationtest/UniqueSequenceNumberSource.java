package com.imc.workshop.integrationtest;

import java.util.function.LongSupplier;

public class UniqueSequenceNumberSource implements LongSupplier {
    private long theNumber = 0;

    @Override
    public long getAsLong() {
        return theNumber++;
    }
}
