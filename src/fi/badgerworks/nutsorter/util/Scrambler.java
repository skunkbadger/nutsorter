package fi.badgerworks.nutsorter.util;

import java.util.Collections;
import java.util.List;

import fi.badgerworks.nutsorter.model.Bolt;
import fi.badgerworks.nutsorter.model.Nut;

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
