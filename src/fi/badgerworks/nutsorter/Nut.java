package fi.badgerworks.nutsorter;

class Nut extends AbstractFastener {

    Nut(final int size,
        final String name) {
        super(size, name);
    }

    ComparisonValue compareToBolt(final Bolt bolt) {
        final int nutSize = super.getSize();
        final int boltSize = bolt.getSize();
        if (boltSize < nutSize) {
            return ComparisonValue.SMALLER;
        } else if (boltSize > nutSize) {
            return ComparisonValue.LARGER;
        }
        return ComparisonValue.EQUAL;
    }

    public String toString() {
        return "Nut name: " + super.getName() + ", Size: " + super.getSize();
    }
}
