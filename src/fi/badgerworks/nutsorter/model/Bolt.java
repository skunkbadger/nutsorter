package fi.badgerworks.nutsorter.model;

public class Bolt extends AbstractFastener {

    public Bolt(final int size,
                final String name) {
        super(size, name);
    }

    public ComparisonValue compareToNut(final Nut nut) {
        final int boltSize = super.getSize();
        final int nutSize = nut.getSize();
        if (boltSize < nutSize) {
            return ComparisonValue.SMALLER;
        } else if (boltSize > nutSize) {
            return ComparisonValue.LARGER;
        }
        return ComparisonValue.EQUAL;
    }

    public String toString() {
        return "Bolt name: " + super.getName() + ", Size: " + super.getSize();
    }
}
