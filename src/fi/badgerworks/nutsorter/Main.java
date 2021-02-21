package fi.badgerworks.nutsorter;

import java.util.ArrayList;
import java.util.List;

import static fi.badgerworks.nutsorter.LoggingUtils.logDuration;
import static fi.badgerworks.nutsorter.Scrambler.scrambleNutsAndBolts;

public class Main {

    final static boolean ENABLE_DEBUG_LOGGING = false;
    final static boolean PRINT_RESULTS = true;

    private final static int COUNT = 10;

    public static void main(String[] args) {
        final List<Nut> nuts = new ArrayList<>();
        final List<Bolt> bolts = new ArrayList<>();
        initializeNutsAndBolts(nuts, bolts, COUNT);
        scrambleNutsAndBolts(nuts, bolts);
        listSplitterSort(new ArrayList<>(nuts), new ArrayList<>(bolts));
        binaryTreeSort(new ArrayList<>(nuts), new ArrayList<>(bolts));
    }

    private static void listSplitterSort(final List<Nut> nuts,
                                         final List<Bolt> bolts) {
        final NutSorter nutSorter = new ListSplitterNutSorterImpl();
        long startTime = System.nanoTime();
        nutSorter.matchMyNutsAndBolts(nuts, bolts);
        long endTime = System.nanoTime();
        logDuration(startTime, endTime);
    }

    private static void binaryTreeSort(final List<Nut> nuts,
                                       final List<Bolt> bolts) {
        final NutSorter nutSorter = new BinaryTreeNutSorterImpl();
        long startTime = System.nanoTime();
        nutSorter.matchMyNutsAndBolts(nuts, bolts);
        long endTime = System.nanoTime();
        logDuration(startTime, endTime);
    }

    private static void initializeNutsAndBolts(final List<Nut> nuts,
                                               final List<Bolt> bolts,
                                               final int count) {
        for (int i = 1; i <= count; i++) {
            bolts.add(new Bolt(i, "Bolt " + i));
            nuts.add(new Nut(i, "Nut " + i));
        }
    }
}
