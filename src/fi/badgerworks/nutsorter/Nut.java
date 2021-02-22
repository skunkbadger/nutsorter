package fi.badgerworks.nutsorter;

class Nut extends AbstractFastener {

    Bolt bolt;

    Nut(final int size,
        final String name) {
        super(size, name);
    }

    ComparisonValue compareToBolt(final Bolt bolt) {
        final int nutSize = super.getSize();
        final int boltSize = bolt.getSize();
        if (nutSize < boltSize) {
            return ComparisonValue.SMALLER;
        } else if (nutSize > boltSize) {
            return ComparisonValue.LARGER;
        }
        return ComparisonValue.EQUAL;
    }

    boolean hasPair() {
        return bolt != null;
    }

    void setBolt(final Bolt bolt) {
        this.bolt = bolt;
    }

    public String toString() {
        return "Nut name: " + super.getName() + ", Size: " + super.getSize();
    }
}
