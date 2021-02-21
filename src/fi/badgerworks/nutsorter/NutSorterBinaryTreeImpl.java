package fi.badgerworks.nutsorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static fi.badgerworks.nutsorter.ComparisonValue.EQUAL;
import static fi.badgerworks.nutsorter.LoggingUtils.logError;

public class NutSorterBinaryTreeImpl implements NutSorter {

    private final static String ALGORITHM_NAME_BINARY = "binary tree";
    private final ConcurrentHashMap<Nut, Bolt> sortedNutsAndBolts;
    private Long iteration;

    NutSorterBinaryTreeImpl() {
        sortedNutsAndBolts = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Nut, Bolt> matchMyNutsAndBolts(final List<Nut> nuts,
                                                            final List<Bolt> bolts) {
        iteration = 1L;
        final NutNode masterNode = new NutNode(nuts);
        bolts.forEach(bolt -> {
            doSort(masterNode, bolt);
        });
        LoggingUtils.logSorted(sortedNutsAndBolts, iteration, ALGORITHM_NAME_BINARY);
        return sortedNutsAndBolts;
    }

    boolean doSort(final NutNode node,
                   final Bolt pivotBolt) {
        ++iteration;
        boolean hasMatch = false;
        boolean didSorting = false;
        final List<Nut> smallerNuts = new ArrayList<>();
        final List<Nut> largerNuts = new ArrayList<>();
        final List<Nut> nodeNuts = node.getNuts();
        if (nodeNuts != null) {
            for (final Nut nut : nodeNuts) {
                final ComparisonValue comparison = nut.compareToBolt(pivotBolt);
                switch (comparison) {
                    case EQUAL:
                        node.setMatchingBolt(pivotBolt);
                        node.setNut(nut);
                        sortedNutsAndBolts.put(nut, pivotBolt);
                        hasMatch = true;
                        break;
                    case SMALLER:
                        smallerNuts.add(nut);
                        break;
                    case LARGER:
                        largerNuts.add(nut);
                        break;
                    default:
                        logError("Guru meditation, nut comparison gone bad with pivot bolt: " + pivotBolt);
                }
            }
            didSorting = true;
        } else if (node.getNut() != null &&     node.getNut().compareToBolt(pivotBolt) == EQUAL) {
            hasMatch = true;
        }
        if (!smallerNuts.isEmpty()) {
            final NutNode leftNode = new NutNode(smallerNuts);
            node.setLeftChildNode(leftNode);
        }
        if (!largerNuts.isEmpty()) {
            final NutNode rightNode = new NutNode(largerNuts);
            node.setRightChildNode(rightNode);
        }
        if (didSorting) {
            node.resetNuts();
        } else {
            if (node.hasLeftChildNode()) {
                hasMatch = doSort(node.getLeftChildNode(), pivotBolt);
            }
            if (!hasMatch && node.hasRightChildNode()) {
                hasMatch = doSort(node.getRightChildNode(), pivotBolt);
            }
        }
        return hasMatch;
    }
}
