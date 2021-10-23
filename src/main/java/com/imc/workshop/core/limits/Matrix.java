package com.imc.workshop.core.limits;

import java.util.*;
import java.util.stream.*;

public class Matrix {
    private final List<List<Double>> theMatrix = new ArrayList<>();

    public Matrix(int aNumberOfRows, int aNumberOfColumns) {
        assert aNumberOfRows >= 0 && aNumberOfColumns >= 0;
        for (int i = 0; i < aNumberOfRows; ++i) {
            List<Double> myRow = new ArrayList<>();
            for (int j = 0; j < aNumberOfColumns; ++j) {
                myRow.add(0.);
            }
            theMatrix.add(myRow);
        }
    }

    public Matrix(Matrix aMatrix) {
        theMatrix.addAll(aMatrix.theMatrix);
    }

    @Override
    public String toString()
    {
        return theMatrix.toString();
    }

    public Matrix(double[][] aMatrix) {
        for (int i = 0; i < aMatrix.length; ++i) {
            List<Double> myRow = Arrays.stream(aMatrix[i]).boxed().collect(Collectors.toList());
            theMatrix.add(myRow);
        }
    }

    public double get(int aRowIndex, int aColumnIndex) {
        return theMatrix.get(aRowIndex).get(aColumnIndex);
    }

    public void set(int aRowIndex, int aColumnIndex, double aValue) {
        theMatrix.get(aRowIndex).set(aColumnIndex, aValue);
    }

    public int getNumberOfRows() {
        return theMatrix.size();
    }

    @Override
    public boolean equals(Object myaO)
    {
        if (this == myaO)
        {
            return true;
        }
        if (myaO == null || getClass() != myaO.getClass())
        {
            return false;
        }
        final Matrix mymyMatrix = (Matrix) myaO;
        return theMatrix.equals(mymyMatrix.theMatrix);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(theMatrix);
    }

    public int getNumberOfColumns() {
        return theMatrix.size() == 0
            ? 0
            : theMatrix.get(0).size();
    }
}
