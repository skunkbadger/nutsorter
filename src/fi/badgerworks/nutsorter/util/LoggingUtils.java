package fi.badgerworks.nutsorter.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import fi.badgerworks.nutsorter.model.Bolt;
import fi.badgerworks.nutsorter.model.Nut;
import static fi.badgerworks.nutsorter.Main.ENABLE_DEBUG_LOGGING;
import static fi.badgerworks.nutsorter.Main.PRINT_RESULTS;

public interface LoggingUtils {

    static void logDuration(final long startTime,
                            final long endTime) {
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Duration: " + duration + " ms");
    }

    static void logSorted(final Map<Nut, Bolt> sortedNutsAndBolts,
                          final AtomicInteger recursions,
                          final AtomicInteger iterations,
                          final String algoritm) {
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n--");
        stringBuffer.append("\n\n");
        stringBuffer.append("Matched results with algorithm (" + algoritm + "):");
        if (ENABLE_DEBUG_LOGGING || PRINT_RESULTS) {
            sortedNutsAndBolts.entrySet().forEach(entry -> stringBuffer.append("\nNut: " + entry.getKey().getName() + ", Bolt: " + entry.getValue().getName() + " Sizes: [" + entry.getKey().getSize() + "/" + entry.getValue().getSize() + "]"));
            stringBuffer.append("\n\n--");
        }
        stringBuffer.append("\n\n");
        stringBuffer.append("Recursions: " + recursions);
        stringBuffer.append("\n");
        stringBuffer.append("Iterations: " + iterations);
        stringBuffer.append("\n");
        stringBuffer.append("Sorted count: " + sortedNutsAndBolts.size());
        System.out.println(stringBuffer.toString());
    }

    static void logNutsAndBolts(final Nut pivotNut,
                                final Bolt pivotBolt,
                                final List nuts,
                                final List bolts,
                                long iteration) {
        if (ENABLE_DEBUG_LOGGING) {
            final int count = nuts.size();
            final StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("\n--");
            stringBuffer.append("\n\n");
            stringBuffer.append("Iteration [" + iteration + "] with " + count + " items!");
            stringBuffer.append("\n\n");
            stringBuffer.append("Nuts:");
            nuts.forEach(nut -> stringBuffer.append("\n" + nut.toString()));
            stringBuffer.append("\n\n");
            stringBuffer.append("Bolts:");
            bolts.forEach(bolt -> stringBuffer.append("\n" + bolt.toString()));
            if (pivotNut != null && pivotBolt != null) {
                stringBuffer.append("\n\n");
                stringBuffer.append("Matched: " + pivotNut + " / " + pivotBolt);
            }
            System.out.println(stringBuffer.toString());
        }
    }

    static void logSingleMatch(final Nut pivotNut,
                               final Bolt pivotBolt) {
        if (ENABLE_DEBUG_LOGGING) {
            final StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("\n--");
            stringBuffer.append("\n\n");
            stringBuffer.append("Logging single match!");
            stringBuffer.append("\n\n");
            stringBuffer.append("Matched: " + pivotNut + " / " + pivotBolt);
            System.out.println(stringBuffer.toString());
        }
    }

    static void logError(final String errorMessage) {
        System.out.println("ERROR: " + errorMessage);
    }
}
