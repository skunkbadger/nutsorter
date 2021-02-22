package fi.badgerworks.nutsorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import fi.badgerworks.nutsorter.model.Bolt;
import fi.badgerworks.nutsorter.model.Nut;
import fi.badgerworks.nutsorter.sorters.BinaryTreeNutSorterImpl;
import fi.badgerworks.nutsorter.sorters.ListSplitterNutSorterImpl;
import fi.badgerworks.nutsorter.sorters.NutSorter;
import static fi.badgerworks.nutsorter.util.LoggingUtils.logDuration;
import static fi.badgerworks.nutsorter.util.Scrambler.scrambleNutsAndBolts;

public class Main {

    public final static boolean LIST_SPLITTER_PARALLELISM_ENABLED = false;
    public final static boolean ENABLE_DEBUG_LOGGING = false;
    public final static boolean PRINT_RESULTS = true;

    private final static int COUNT = 10;
    private final static int MIN_SIZE = 4;
    private final static int SIZE_COUNT = 4; // How many different sizes are there for bolts and nuts
    private final static boolean UNIQUE_BOLTS_AND_NUTS = false;

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
            final int size = getBoltSize(i);
            bolts.add(new Bolt(size, "Bolt " + i));
            nuts.add(new Nut(size, "Nut " + i));
        }
    }

    private static int getBoltSize(final int uniqueSize) {
        if (UNIQUE_BOLTS_AND_NUTS) {
            return uniqueSize;
        } else {
            return ThreadLocalRandom.current().nextInt(MIN_SIZE, MIN_SIZE + SIZE_COUNT + 1);
        }
    }
}
