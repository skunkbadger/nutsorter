package fi.badgerworks.nutsorter;

import java.util.Collections;
import java.util.List;

public interface Scrambler {

    static void scrambleNutsAndBolts(final List<Nut> nuts,
                                     final List<Bolt> bolts) {
        scramble(nuts);
        scramble(bolts);
    }

    static void scramble(final List list) {
        Collections.shuffle(list);
    }
}
