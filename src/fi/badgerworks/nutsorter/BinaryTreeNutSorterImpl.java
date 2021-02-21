package fi.badgerworks.nutsorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static fi.badgerworks.nutsorter.ComparisonValue.EQUAL;
import static fi.badgerworks.nutsorter.LoggingUtils.logError;

public class BinaryTreeNutSorterImpl implements NutSorter {

    private final static String ALGORITHM_NAME = "binary tree";
    private final ConcurrentHashMap<Nut, Bolt> sortedNutsAndBolts;
    private Long recursions;
    private Long iterations;

    BinaryTreeNutSorterImpl() {
        sortedNutsAndBolts = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Nut, Bolt> matchMyNutsAndBolts(final List<Nut> nuts,
                                                            final List<Bolt> bolts) {
        iterations = 0L;
        recursions = 0L;
        final NutNode masterNode = new NutNode(nuts);
        bolts.forEach(bolt -> doSort(masterNode, bolt));
        LoggingUtils.logSorted(sortedNutsAndBolts, recursions, iterations, ALGORITHM_NAME);
        return sortedNutsAndBolts;
    }

    boolean doSort(final NutNode node,
                   final Bolt pivotBolt) {
        recursions++;
        boolean hasMatch = false;
        boolean didSorting = false;
        final List<Nut> nodeNuts = node.getNuts();
        if (nodeNuts != null) {
            final List<Nut> smallerNuts = new ArrayList<>();
            final List<Nut> largerNuts = new ArrayList<>();
            for (final Nut nut : nodeNuts) {
                iterations++;
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
            setNodeNuts(node, smallerNuts, largerNuts);
        } else if (node.getNut() != null && node.getNut().compareToBolt(pivotBolt) == EQUAL) {
            sortedNutsAndBolts.put(node.getNut(), pivotBolt);
            hasMatch = true;
        }
        if (!didSorting && !hasMatch) {
            if (node.hasLeftChildNode()) {
                hasMatch = doSort(node.getLeftChildNode(), pivotBolt);
            }
            if (!hasMatch && node.hasRightChildNode()) {
                hasMatch = doSort(node.getRightChildNode(), pivotBolt);
            }
        }
        return hasMatch;
    }

    private void setNodeNuts(final NutNode node,
                             final List<Nut> smallerNuts,
                             final List<Nut> largerNuts) {
        if (!smallerNuts.isEmpty()) {
            final NutNode leftNode = new NutNode(smallerNuts);
            node.setLeftChildNode(leftNode);
        }
        if (!largerNuts.isEmpty()) {
            final NutNode rightNode = new NutNode(largerNuts);
            node.setRightChildNode(rightNode);
        }
    }
}
