package fi.badgerworks.nutsorter.sorters;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import fi.badgerworks.nutsorter.model.Bolt;
import fi.badgerworks.nutsorter.model.Nut;

public interface NutSorter {

    ConcurrentHashMap<Nut, Bolt> matchMyNutsAndBolts(final List<Nut> nuts,
                                                     final List<Bolt> bolts);
}
