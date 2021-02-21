package fi.badgerworks.nutsorter;

abstract class AbstractFastener {

    private final int size;
    private final String name;

    AbstractFastener(final int size,
                     final String name) {
        this.size = size;
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    int getSize() {
        return this.size;
    }
}
