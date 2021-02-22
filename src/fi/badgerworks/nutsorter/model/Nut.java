package fi.badgerworks.nutsorter.model;

public class Nut extends AbstractFastener {

    Bolt bolt;

    public Nut(final int size,
               final String name) {
        super(size, name);
    }

    public ComparisonValue compareToBolt(final Bolt bolt) {
        final int nutSize = super.getSize();
        final int boltSize = bolt.getSize();
        if (nutSize < boltSize) {
            return ComparisonValue.SMALLER;
        } else if (nutSize > boltSize) {
            return ComparisonValue.LARGER;
        }
        return ComparisonValue.EQUAL;
    }

    public boolean hasPair() {
        return bolt != null;
    }

    public void setBolt(final Bolt bolt) {
        this.bolt = bolt;
    }

    public String toString() {
        return "Nut name: " + super.getName() + ", Size: " + super.getSize();
    }
}
