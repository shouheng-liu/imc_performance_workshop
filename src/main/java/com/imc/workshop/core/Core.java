package com.imc.workshop.core;

import com.imc.workshop.datamodel.Covariance;
import com.imc.workshop.datamodel.MarketUpdate;
import com.imc.workshop.datamodel.Result;
import com.imc.workshop.datamodel.Trade;

import java.util.List;

public interface Core {
    List<Result> handleTrade(Trade aTrade);

    List<Result> handleMarketUpdate(MarketUpdate aMarketUpdate);

    void handleCovariance(Covariance aCovariance);
}
