package fi.badgerworks.nutsorter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface NutSorter {

    ConcurrentHashMap<Nut, Bolt> matchMyNutsAndBolts(final List<Nut> nuts,
                                                     final List<Bolt> bolts);
}
