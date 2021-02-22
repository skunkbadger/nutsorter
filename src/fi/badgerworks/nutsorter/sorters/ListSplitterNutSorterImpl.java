package fi.badgerworks.nutsorter.sorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import fi.badgerworks.nutsorter.model.Bolt;
import fi.badgerworks.nutsorter.model.ComparisonValue;
import fi.badgerworks.nutsorter.model.Nut;
import fi.badgerworks.nutsorter.util.LoggingUtils;
import static fi.badgerworks.nutsorter.Main.LIST_SPLITTER_PARALLELISM_ENABLED;
import static fi.badgerworks.nutsorter.model.ComparisonValue.EQUAL;
import static fi.badgerworks.nutsorter.util.LoggingUtils.*;

public class ListSplitterNutSorterImpl implements NutSorter {

    private final String ALGORITHM_NAME = "list splitter";

    private final ConcurrentHashMap<Nut, Bolt> sortedNutsAndBolts;
    private AtomicInteger recursions;
    private AtomicInteger iterations;

    public ListSplitterNutSorterImpl() {
        sortedNutsAndBolts = new ConcurrentHashMap<>();
        recursions = new AtomicInteger();
        iterations = new AtomicInteger();
    }

    public ConcurrentHashMap<Nut, Bolt> matchMyNutsAndBolts(final List<Nut> nuts,
                                                            final List<Bolt> bolts) {
        recursions.set(0);
        iterations.set(0);
        checkForRecursion(nuts, bolts);
        logSorted(sortedNutsAndBolts, recursions, iterations, ALGORITHM_NAME);
        return sortedNutsAndBolts;
    }

    private void checkForRecursion(final List<Nut> nuts,
                                   final List<Bolt> bolts) {
        if (nuts.size() > 0 && bolts.size() > 0) {
            if (nuts.size() == 1 && bolts.size() == 1) {
                final Bolt pivotBolt = bolts.get(0);
                final Nut pivotNut = nuts.get(0);
                if (pivotNut.compareToBolt(pivotBolt) == EQUAL) {
                    logSingleMatch(pivotNut, pivotBolt);
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
        final int currentIteration;
        currentIteration = recursions.getAndAdd(1);
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
            iterations.addAndGet(1);
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
            iterations.addAndGet(1);
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
                    break;
                default:
                    logError("Guru meditation at bolt loop: " + comparison);
            }
        }
        LoggingUtils.logNutsAndBolts(pivotNut, pivotBolt, nuts, bolts, currentIteration);
        sortedNutsAndBolts.put(pivotNut, pivotBolt);

        processNutsAndBolts(smallerNuts, smallerBolts, largerNuts, largerBolts);
    }

    private void processNutsAndBolts(final List<Nut> smallerNuts,
                                     final List<Bolt> smallerBolts,
                                     final List<Nut> largerNuts,
                                     final List<Bolt> largerBolts) {
        if (LIST_SPLITTER_PARALLELISM_ENABLED) {
            final Map<List<Nut>, List<Bolt>> parallelProcessingMap = new HashMap<>();
            parallelProcessingMap.put(smallerNuts, smallerBolts);
            parallelProcessingMap.put(largerNuts, largerBolts);
            parallelProcessingMap.entrySet().parallelStream().forEach(entry -> checkForRecursion(entry.getKey(), entry.getValue()));
        } else {
            checkForRecursion(smallerNuts, smallerBolts);
            checkForRecursion(largerNuts, largerBolts);
        }
    }
}
