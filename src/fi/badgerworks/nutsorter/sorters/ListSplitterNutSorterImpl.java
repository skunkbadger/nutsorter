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
    final private AtomicInteger recursions;
    final private AtomicInteger iterations;

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

    private void doSort(final List<Nut> nutsToSplit,
                        final List<Bolt> boltsToSplit) {
        final int currentIteration;
        currentIteration = recursions.getAndAdd(1);
        final Bolt pivotBolt;
        // Picking first "random" bolt as pivot bolt
        pivotBolt = boltsToSplit.get(0);
        if (pivotBolt == null) {
            logError("Epic fail, our pivot bolt was null!");
        }
        final List<Nut> smallerNuts = new ArrayList<>();
        final List<Nut> largerNuts = new ArrayList<>();
        final Nut pivotNut = splitNuts(nutsToSplit, smallerNuts, largerNuts, pivotBolt);
        if (pivotNut == null) {
            logError("Epic fail, our pivot nut was null!");
        }
        final List<Bolt> smallerBolts = new ArrayList<>();
        final List<Bolt> largerBolts = new ArrayList<>();
        splitBolts(boltsToSplit, smallerBolts, largerBolts, pivotBolt, pivotNut);
        LoggingUtils.logNutsAndBolts(pivotNut, pivotBolt, nutsToSplit, boltsToSplit, currentIteration);
        sortedNutsAndBolts.put(pivotNut, pivotBolt);

        processNutsAndBolts(smallerNuts, smallerBolts, largerNuts, largerBolts);
    }

    private Nut splitNuts(final List<Nut> nutsToSplit,
                          final List<Nut> smallerNuts,
                          final List<Nut> largerNuts,
                          final Bolt pivotBolt) {
        Nut pivotNut = null;
        boolean matchFound = false;
        for (final Nut nut : nutsToSplit) {
            iterations.addAndGet(1);
            final ComparisonValue comparison = nut.compareToBolt(pivotBolt);
            switch (comparison) {
                case EQUAL:
                    if (!matchFound) {
                        pivotNut = nut;
                        matchFound = true;
                    } else {
                        // Adding duplicate equal sized nuts to largerNuts
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
        return pivotNut;
    }

    private void splitBolts(final List<Bolt> boltsToSplit,
                            final List<Bolt> smallerBolts,
                            final List<Bolt> largerBolts,
                            final Bolt pivotBolt,
                            final Nut pivotNut) {
        for (final Bolt bolt : boltsToSplit) {
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
                    // Adding equal sized bolts to largerBolts
                    largerBolts.add(bolt);
                    break;
                default:
                    logError("Guru meditation at bolt loop: " + comparison);
            }
        }
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
