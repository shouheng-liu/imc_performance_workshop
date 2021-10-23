package com.imc.workshop.integrationtest;

import com.imc.workshop.core.limits.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.concurrent.*;

public class MathUtilBenchmark {
    
    @State(Scope.Benchmark)
    public static class MathUtilBenchmarkState {
        public Matrix theFirstMatrix;
        public Matrix theSecondMatrix;

        public MathUtilBenchmarkState() {
            int myNbRows = 20;
            int myNbColumns = 20;
            theFirstMatrix = new Matrix(myNbRows, myNbColumns);

            for (int i = 0; i < myNbRows; ++i) {
                for (int j = 0; j < myNbColumns; ++j) {
                    double myValue = i * j * 0.5;
                    theFirstMatrix.set(i, j, myValue);
                }
            }

            theSecondMatrix = theFirstMatrix;
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 0)
    @Warmup(iterations=1)
    @Measurement(iterations = 2)
    @BenchmarkMode(value = Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void multiplyBenchmark(MathUtilBenchmarkState aBenchmarkState) {
        MathUtil.multiply(aBenchmarkState.theFirstMatrix, aBenchmarkState.theSecondMatrix);
    }
}
