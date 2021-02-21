package fi.badgerworks.nutsorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static fi.badgerworks.nutsorter.ComparisonValue.EQUAL;
import static fi.badgerworks.nutsorter.LoggingUtils.logError;

public class BinaryTreeNutSorterImpl implements NutSorter {

    private final static String ALGORITHM_NAME = "binary tree";
    private final ConcurrentHashMap<Nut, Bolt> sortedNutsAndBolts;
    private AtomicInteger recursions;
    private AtomicInteger iterations;

    BinaryTreeNutSorterImpl() {
        sortedNutsAndBolts = new ConcurrentHashMap<>();
        recursions = new AtomicInteger();
        iterations = new AtomicInteger();
    }

    public ConcurrentHashMap<Nut, Bolt> matchMyNutsAndBolts(final List<Nut> nuts,
                                                            final List<Bolt> bolts) {
        iterations.set(0);
        recursions.set(0);
        final NutNode masterNode = new NutNode(nuts);
        bolts.forEach(bolt -> doSort(masterNode, bolt));
        LoggingUtils.logSorted(sortedNutsAndBolts, recursions, iterations, ALGORITHM_NAME);
        return sortedNutsAndBolts;
    }

    boolean doSort(final NutNode node,
                   final Bolt pivotBolt) {
        recursions.addAndGet(1);
        boolean hasMatch = false;
        boolean didSorting = false;
        final List<Nut> nodeNuts = node.getUnorderedNuts();
        if (nodeNuts != null) {
            final List<Nut> smallerNuts = new ArrayList<>();
            final List<Nut> largerNuts = new ArrayList<>();
            final List<Nut> equalNuts = new ArrayList<>();
            for (final Nut nut : nodeNuts) {
                iterations.addAndGet(1);
                final ComparisonValue comparison = nut.compareToBolt(pivotBolt);
                switch (comparison) {
                    case EQUAL:
                        if (!hasMatch) {
                            nut.setBolt(pivotBolt);
                            sortedNutsAndBolts.put(nut, pivotBolt);
                            hasMatch = true;
                        }
                        equalNuts.add(nut);
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
            setNodeNuts(node, smallerNuts, largerNuts, equalNuts);
            node.resetNuts();
        } else {
            final Nut unusedNut = node.getUnusedNut();
            if (unusedNut != null && unusedNut.compareToBolt(pivotBolt) == EQUAL) {
                unusedNut.setBolt(pivotBolt);
                sortedNutsAndBolts.put(unusedNut, pivotBolt);

                hasMatch = true;
            }
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
                             final List<Nut> largerNuts,
                             final List<Nut> equalNuts) {
        node.setNuts(equalNuts);
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
