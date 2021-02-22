package fi.badgerworks.nutsorter.model;

abstract class AbstractFastener {

    private final int size;
    private final String name;

    AbstractFastener(final int size,
                     final String name) {
        this.size = size;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }
}
