package fi.badgerworks.nutsorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static fi.badgerworks.nutsorter.ComparisonValue.EQUAL;
import static fi.badgerworks.nutsorter.LoggingUtils.logError;

public class ListSplitterNutSorterImpl implements NutSorter {

    private final String ALGORITHM_NAME = "list splitter";

    private final ConcurrentHashMap<Nut, Bolt> sortedNutsAndBolts;
    private Long recursions;
    private Long iterations;

    ListSplitterNutSorterImpl() {
        sortedNutsAndBolts = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Nut, Bolt> matchMyNutsAndBolts(final List<Nut> nuts,
                                                            final List<Bolt> bolts) {
        recursions = 0L;
        iterations = 0L;
        checkForRecursion(nuts, bolts);
        LoggingUtils.logSorted(sortedNutsAndBolts, recursions, iterations, ALGORITHM_NAME);
        return sortedNutsAndBolts;
    }

    private void checkForRecursion(final List<Nut> nuts,
                                   final List<Bolt> bolts) {
        if (nuts.size() > 0 && bolts.size() > 0) {
            if (nuts.size() == 1 && bolts.size() == 1) {
                final Bolt pivotBolt = bolts.get(0);
                final Nut pivotNut = nuts.get(0);
                if (pivotNut.compareToBolt(pivotBolt) == EQUAL) {
                    LoggingUtils.logSingleMatch(pivotNut, pivotBolt);
                    sortedNutsAndBolts.put(pivotNut, pivotBolt);
                } else {
                    logError("Epic nuts and bolts sorting mismatch! Fire some Chinese personnel and cut wages!");
                }
            } else {
                doSort(nuts, bolts);
            }
        }
    }

    private void doSort(final List<Nut> nuts,
                        final List<Bolt> bolts) {
        final long currentIteration;
        synchronized (recursions) {
            currentIteration = ++recursions;
        }
        final Bolt pivotBolt;
        Nut pivotNut = null;
        pivotBolt = bolts.get(0);
        if (pivotBolt == null) {
            logError("Epic fail, our pivot bolt was null!");
        }
        final List smallerNuts = new ArrayList<Nut>();
        final List largerNuts = new ArrayList<Nut>();
        boolean matchFound = false;
        for (final Nut nut : nuts) {
            iterations++;
            final ComparisonValue comparison = nut.compareToBolt(pivotBolt);
            switch (comparison) {
                case EQUAL:
                    if (!matchFound) {
                        pivotNut = nut;
                        matchFound = true;
                    } else {
                        largerNuts.add(nut);
                    }
                    break;
                case SMALLER:
                    smallerNuts.add(nut);
                    break;
                case LARGER:
                    largerNuts.add(nut);
                    break;
                default:
                    logError("Guru meditation at nut loop: " + comparison);
            }
        }
        if (pivotNut == null) {
            logError("Epic fail, our pivot nut was null!");
        }
        final List smallerBolts = new ArrayList<Bolt>();
        final List largerBolts = new ArrayList<Bolt>();
        for (final Bolt bolt : bolts) {
            iterations++;
            if (bolt.equals(pivotBolt)) {
                continue;
            }
            final ComparisonValue comparison = bolt.compareToNut(pivotNut);
            switch (comparison) {
                case SMALLER:
                    smallerBolts.add(bolt);
                    break;
                case LARGER:
                    largerBolts.add(bolt);
                    break;
                case EQUAL:
                    largerBolts.add(bolt);
                    // Do nothing, not looking for equal sized bolts;
                    break;
                default:
                    logError("Guru meditation at bolt loop: " + comparison);
            }
        }
        LoggingUtils.logNutsAndBolts(pivotNut, pivotBolt, nuts, bolts, currentIteration);
        sortedNutsAndBolts.put(pivotNut, pivotBolt);

        parallelProcessNutsAndBolts(smallerNuts, smallerBolts, largerNuts, largerBolts);
    }

    private void parallelProcessNutsAndBolts(final List<Nut> smallerNuts,
                                             final List<Bolt> smallerBolts,
                                             final List<Nut> largerNuts,
                                             final List<Bolt> largerBolts) {
        final Map<List<Nut>, List<Bolt>> parallelProcessingMap = new HashMap<>();
        parallelProcessingMap.put(smallerNuts, smallerBolts);
        parallelProcessingMap.put(largerNuts, largerBolts);
        parallelProcessingMap.entrySet().parallelStream().forEach(entry -> checkForRecursion(entry.getKey(), entry.getValue()));
    }
}
