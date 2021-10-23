package com.imc.workshop.integrationtest;

import com.imc.workshop.core.limits.*;
import com.imc.workshop.datamodel.PnL;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.*;

public class PnLCalculatorBenchmark {
    
    @State(Scope.Benchmark)
    public static class PnLCalculatorBenchmarkState {
        public LimitsCalculator theLimitsCalculator;
        public final List<PnL> thePnLs = new ArrayList<>();

        private static final int LOOP_COUNT = 100;
        public PnLCalculatorBenchmarkState() {
            Random myRandom = new Random(0L);

            for (int i = 0; i < LOOP_COUNT; ++i) {
                List<String> myInstruments = Covariances.TEST_INSTRUMENTS;
                for (String myInstrument : myInstruments) {
                    int mySideMultiplier = myRandom.nextBoolean() ? 1 : -1;
                    int myPosition = mySideMultiplier * myRandom.nextInt(1000);
                    thePnLs.add(new PnL(myInstrument, 0, myPosition, 0, myPosition * 0.8));
                }
            }
        }

        @Setup
        public void setUp() {
            theLimitsCalculator = new LimitsCalculator(1000.);
            Covariances.getCovariances().forEach(theLimitsCalculator::handleCovariance);
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 0)
    @Warmup(iterations=1)
    @Measurement(iterations = 2)
    @BenchmarkMode(value = Mode.AverageTime)
    public void pnlCalculatorBenchmark(PnLCalculatorBenchmarkState aBenchmarkState) {
        for (PnL myPnL : aBenchmarkState.thePnLs) {
            aBenchmarkState.theLimitsCalculator.handlePnl(myPnL);
        }
    }
}
