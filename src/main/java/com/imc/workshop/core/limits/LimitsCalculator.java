package com.imc.workshop.core.limits;

import com.imc.workshop.core.InstrumentMap;
import com.imc.workshop.core.limits.Polynom.PolynomialRoots;
import com.imc.workshop.datamodel.Covariance;
import com.imc.workshop.datamodel.PnL;
import com.imc.workshop.datamodel.PositionLimits;

import java.util.*;

public class LimitsCalculator {
    private final List<Double> theCashValues = new ArrayList<>();
    private final List<Long> theLastSequenceNumbers = new ArrayList<>();
    private Matrix theCovariances = new Matrix(0, 0);
    private final InstrumentMap theInstrumentMap = new InstrumentMap();
    private final double theMaximumRisk;
    private final Map<String, PositionLimits> theLimits = new HashMap<>();

    public LimitsCalculator(double aMaximumRisk) {
        theMaximumRisk = aMaximumRisk;
    }

    public void handlePnl(PnL aPnL) {
        int myInstrumentId = theInstrumentMap.get(aPnL.getInstrument());

        theCashValues.set(myInstrumentId, aPnL.getPositionCashValue());
        theLastSequenceNumbers.set(myInstrumentId, aPnL.getLastSequenceNumber());

        computeLimits();
    }

    public void handleCovariance(Covariance aCovariance) {
        int myFirstInstrumentId = theInstrumentMap.getOrCreate(aCovariance.getFirstInstrument());
        int mySecondInstrumentId = theInstrumentMap.getOrCreate(aCovariance.getSecondInstrument());
        int myNewestInstrumentId = Math.max(myFirstInstrumentId, mySecondInstrumentId);
        int myNbKnownInstruments = theCovariances.getNumberOfRows();

        if (myNewestInstrumentId >= myNbKnownInstruments) {
            Matrix myCovariances = new Matrix(myNewestInstrumentId + 1, myNewestInstrumentId + 1);
            for (int i = 0; i < myNbKnownInstruments; ++i) {
                for (int j = 0; j < myNbKnownInstruments; ++j) {
                    myCovariances.set(i, j, theCovariances.get(i, j));
                }
            }

            theCovariances = myCovariances;

            while (myNewestInstrumentId >= theCashValues.size()) {
                theCashValues.add(0.);
                theLastSequenceNumbers.add(0L);
            }
        }

        theCovariances.set(myFirstInstrumentId, mySecondInstrumentId, aCovariance.getCovariance());

        computeLimits();
    }

    public Collection<PositionLimits> getLimits() {
        return theLimits.values();
    }

    private void computeLimits() {
        theInstrumentMap.getInstrumentNames()
                .forEach(aInstrumentName -> theLimits.put(aInstrumentName, computeLimits(aInstrumentName)));
    }

    private PositionLimits computeLimits(String aInstrument) {
        int myInstrumentId = theInstrumentMap.get(aInstrument);

        Matrix myCashValueMatrix = new Matrix(theCashValues.size(), 1);
        for (int i = 0; i < theCashValues.size(); ++i) {
            myCashValueMatrix.set(i, 0, theCashValues.get(i));
        }

        Matrix myTransposedCashValueMatrix = MathUtil.transpose(myCashValueMatrix);

        Matrix myTransposedCashValueMatrixTimesCovarianceMatrix = MathUtil.multiply(myTransposedCashValueMatrix, theCovariances);

        double myGlobalPortfolioRisk = (MathUtil.multiply(myTransposedCashValueMatrixTimesCovarianceMatrix, myCashValueMatrix)).get(0, 0);

        Matrix myCovariancesForInstrument = new Matrix(theCovariances.getNumberOfRows(), 1);

        for (int i = 0; i < theCovariances.getNumberOfRows(); ++i) {
            myCovariancesForInstrument.set(i, 0, theCovariances.get(i, myInstrumentId));
        }

        double myQuadraticTerm = theCovariances.get(myInstrumentId, myInstrumentId);
        double myLinearTerm = 2. * MathUtil.multiply(myTransposedCashValueMatrix, myCovariancesForInstrument).get(0, 0);
        double myConstantTerm = myGlobalPortfolioRisk - Math.pow(theMaximumRisk, 2);

        PolynomialRoots myRoots = Polynom.computePolynomialRoots(myQuadraticTerm, myLinearTerm, myConstantTerm);

        double myBuyLimit, mySellLimit;

        if (Double.isNaN(myRoots.theSecondRoot)) {
            myBuyLimit = Math.max(0, myRoots.theFirstRoot);
            mySellLimit = Math.max(0, -myRoots.theFirstRoot);
        } else {
            double myLargestRoot = Math.max(myRoots.theFirstRoot, myRoots.theSecondRoot);
            double mySmallestRoot = Math.min(myRoots.theFirstRoot, myRoots.theSecondRoot);

            myBuyLimit = Math.max(0, myLargestRoot);
            mySellLimit = Math.max(0, -mySmallestRoot);
        }

        return new PositionLimits(aInstrument, theLastSequenceNumbers.get(myInstrumentId), myBuyLimit, mySellLimit);
    }
}
