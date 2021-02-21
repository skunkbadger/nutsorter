package fi.badgerworks.nutsorter;

class Bolt extends AbstractFastener {

    Bolt(final int size,
         final String name) {
        super(size, name);
    }

    ComparisonValue compareToNut(final Nut nut) {
        final int boltSize = super.getSize();
        final int nutSize = nut.getSize();
        if (nutSize < boltSize) {
            return ComparisonValue.SMALLER;
        } else if (nutSize > boltSize) {
            return ComparisonValue.LARGER;
        }
        return ComparisonValue.EQUAL;
    }

    public String toString() {
        return "Bolt name: " + super.getName() + ", Size: " + super.getSize();
    }
}
