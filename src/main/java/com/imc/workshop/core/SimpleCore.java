package com.imc.workshop.core;

import com.imc.workshop.core.limits.LimitsCalculator;
import com.imc.workshop.core.pnl.PnLCalculator;
import com.imc.workshop.datamodel.*;

import java.util.ArrayList;
import java.util.List;

public class SimpleCore implements Core {
    private final PnLCalculator thePnLCalculator;
    private final LimitsCalculator theLimitsCalculator;

    public SimpleCore(PnLCalculator aPnLCalculator, LimitsCalculator aLimitsCalculator) {
        thePnLCalculator = aPnLCalculator;
        theLimitsCalculator = aLimitsCalculator;
    }

    @Override
    public List<Result> handleTrade(Trade aTrade) {
        thePnLCalculator.onTrade(aTrade);
        String myInstrument = aTrade.getInstrument();
        return updateAndGetResult(myInstrument);
    }

    @Override
    public List<Result> handleMarketUpdate(MarketUpdate aMarketUpdate) {
        thePnLCalculator.onMarketUpdate(aMarketUpdate);
        String myInstrument = aMarketUpdate.getInstrument();
        return updateAndGetResult(myInstrument);
    }

    @Override
    public void handleCovariance(Covariance aCovariance) {
        theLimitsCalculator.handleCovariance(aCovariance);
    }

    private List<Result> updateAndGetResult(String aInstrument) {
        List<Result> myResults = new ArrayList<>();
        final PnL myPnl = thePnLCalculator.getPnl(aInstrument);
        theLimitsCalculator.handlePnl(myPnl);
        myResults.add(myPnl);
        myResults.addAll(theLimitsCalculator.getLimits());
        return myResults;
    }
}
