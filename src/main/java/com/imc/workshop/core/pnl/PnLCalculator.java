package com.imc.workshop.core.pnl;

import com.imc.workshop.datamodel.MarketUpdate;
import com.imc.workshop.datamodel.PnL;
import com.imc.workshop.datamodel.Trade;

import java.util.HashMap;
import java.util.Map;

public class PnLCalculator {
    private final Map<String, PnlEntry> thePnlEntries;
    private long theLastSequenceNumber = 0;

    public PnLCalculator() {
        thePnlEntries = new HashMap<>();
    }

    public void onMarketUpdate(MarketUpdate marketUpdate) {
        thePnlEntries.computeIfAbsent(marketUpdate.getInstrument(), PnlEntry::new).onMarketUpdate(marketUpdate);
        theLastSequenceNumber = marketUpdate.getSequenceNumber();
    }

    public void onTrade(Trade trade) {
        thePnlEntries.computeIfAbsent(trade.getInstrument(), PnlEntry::new).onTrade(trade);
        theLastSequenceNumber = trade.getSequenceNumber();
    }

    public PnL getPnl(String instrument) {
        PnlEntry myPnlEntry = thePnlEntries.get(instrument);
        return new PnL(myPnlEntry.getInstrument(),
                theLastSequenceNumber,
                myPnlEntry.getPosition(),
                myPnlEntry.getPnL(),
                myPnlEntry.getPositionCashValue());
    }

    private static class PnlEntry {
        private final String theInstrument;
        private int thePosition;
        private double theCashBalance;
        private double theLastPrice;


        private PnlEntry(String instrument) {
            theInstrument = instrument;
            thePosition = 0;
            theCashBalance = 0;
            theLastPrice = Double.NaN;
        }

        public double getPnL() {
            if (Double.isNaN(theLastPrice)) {
                throw new IllegalStateException(
                        "Cannot calculate PnL for instrument " + theInstrument + " without last price!");
            }
            return theCashBalance + getPositionCashValue();
        }

        public void onMarketUpdate(MarketUpdate marketUpdate) {
            // throw new UnsupportedOperationException("Function not yet implemented.");
            theLastPrice = (marketUpdate.getAsk().getBestPrice() + marketUpdate.getBid().getBestPrice()) / 2.0;
        }

        public void onTrade(Trade trade) {
            // throw new UnsupportedOperationException("Function not yet implemented.");
            thePosition += trade.getVolume();
            theCashBalance += (-1.0) * trade.getPrice() * trade.getVolume();
        }

        public String getInstrument() {
            return theInstrument;
        }

        public int getPosition() {
            return thePosition;
        }

        public double getCashBalance() {
            return theCashBalance;
        }

        public double getPositionCashValue() {
            if (Double.isNaN(theLastPrice)) {
                throw new IllegalStateException(
                    "Cannot calculate position cash value for instrument " + theInstrument + " without last price!");
            }
            return thePosition * theLastPrice;
        }
    }
}
