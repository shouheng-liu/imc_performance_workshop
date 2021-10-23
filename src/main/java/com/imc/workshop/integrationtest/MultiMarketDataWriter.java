package com.imc.workshop.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imc.workshop.datamodel.MarketEvent;

import java.io.FileOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MultiMarketDataWriter {
    private final List<MarketDataGenerator> theGenerators;
    private final String theFileName;
    private final ObjectMapper theObjectMapper;

    public MultiMarketDataWriter(List<MarketDataGenerator> aGenerators, String aFileName) {
        theGenerators = aGenerators;
        theFileName = aFileName;
        theObjectMapper = new ObjectMapper();
    }

    public void write() throws Exception {
        UniqueSequenceNumberSource myUniqueSequenceNumberSource = new UniqueSequenceNumberSource();
        SortedSet<MarketEvent> theEvents = new TreeSet<>(Comparator.comparingLong(MarketEvent::getTimestamp).thenComparing(MarketEvent::getInstrument).thenComparing(
                (MarketEvent aMarketEvent) -> aMarketEvent.getClass().getSimpleName()));
        for (MarketDataGenerator myGenerator : theGenerators) {
            myGenerator.addHandler(theEvents::add);
            myGenerator.generateData();
        }

        List<MarketEvent> myEvents =
                theEvents.stream().map(e -> e.withSeqNum(myUniqueSequenceNumberSource.getAsLong())).collect(Collectors.toList());
        FileOutputStream myFileOutputStream = new FileOutputStream(theFileName);
        theObjectMapper.writeValue(myFileOutputStream, new MarketEventList(myEvents));
        myFileOutputStream.close();


    }

    private static final class InstrumentConfiguration {
        private final String theInstrumentName;
        private final double thePrice;
        private final double theSpread;
        private final double theVolatility;

        private InstrumentConfiguration(String aInstrumentName, double aPrice, double aSpread, double aVolatility) {
            theInstrumentName = aInstrumentName;
            thePrice = aPrice;
            theSpread = aSpread;
            theVolatility = aVolatility;
        }
    }

    public static void main(String[] args) throws Exception {
        List<MarketDataGenerator> myGenerators = new ArrayList<>();
        var myStart = ZonedDateTime.of(2019, 7, 15, 9, 0, 0, 0, ZoneId.systemDefault());
        var myEnd = ZonedDateTime.of(2019, 7, 15, 9, 5, 0, 0, ZoneId.systemDefault());
        var mySeqNumSource = new UniqueSequenceNumberSource();

        List<InstrumentConfiguration> myConfigurations =
                Arrays.asList(new InstrumentConfiguration("AAPL", 200, 0.1, 0.1),
                        new InstrumentConfiguration("NFLX", 100, 0.1, 0.1),
                        new InstrumentConfiguration("TSLA", 230, 0.1, 0.2),
                        new InstrumentConfiguration("GOOG", 230, 0.1, 0.1),
                        new InstrumentConfiguration("BP", 13, 0.1, 0.1),
                        new InstrumentConfiguration("SAP", 20, 0.1, 0.1),
                        new InstrumentConfiguration("BNP", 50, 0.1, 0.1),
                        new InstrumentConfiguration("BARC", 123, 0.1, 0.1),
                        new InstrumentConfiguration("MSFT", 34, 0.1, 0.1),
                        new InstrumentConfiguration("AMZN", 40, 0.1, 0.1),
                        new InstrumentConfiguration("FESX", 3500, 0.1, 0.1),
                        new InstrumentConfiguration("FDAX", 12000, 0.1, 0.1),
                        new InstrumentConfiguration("FTSE", 5000, 0.1, 0.1),
                        new InstrumentConfiguration("ES", 3000, 0.1, 0.1),
                        new InstrumentConfiguration("BAC", 10, 0.1, 0.1));

        for (InstrumentConfiguration myConfiguration : myConfigurations) {
            myGenerators.add(new MarketDataGenerator(myConfiguration.theInstrumentName,
                    myConfiguration.thePrice,
                    myConfiguration.theVolatility,
                    myConfiguration.theSpread,
                    10,
                    myStart,
                    myEnd,
                    150000000,
                    mySeqNumSource));
        }
        new MultiMarketDataWriter(myGenerators, "test.json").write();
    }

    private static class MarketEventList extends ArrayList<MarketEvent> {

        public MarketEventList(List<MarketEvent> aEvents) {
            super(aEvents);
        }
    }
}
