package fi.badgerworks.nutsorter;

import java.util.List;

class NutNode {

    private List<Nut> unorderedNuts;

    private NutNode leftChildNode;
    private NutNode rightChildNode;

    private List<Nut> nuts;

    NutNode(final List<Nut> unorderedNuts) {
        this.unorderedNuts = unorderedNuts;
    }

    NutNode getLeftChildNode() {
        return leftChildNode;
    }

    void setLeftChildNode(final NutNode leftChildNode) {
        this.leftChildNode = leftChildNode;
    }

    boolean hasLeftChildNode() {
        return leftChildNode != null;
    }

    NutNode getRightChildNode() {
        return rightChildNode;
    }

    void setRightChildNode(final NutNode rightChildNode) {
        this.rightChildNode = rightChildNode;
    }

    boolean hasRightChildNode() {
        return rightChildNode != null;
    }

    Nut getUnusedNut() {
        if (nuts != null) {
            for (final Nut nut : nuts) {
                if (!nut.hasPair()) {
                    return nut;
                }
            }
        }
        return null;
    }

    void setNuts(final List<Nut> nuts) {
        this.nuts = nuts;
    }

    List<Nut> getUnorderedNuts() {
        return unorderedNuts;
    }

    void resetNuts() {
        unorderedNuts = null;
    }
}
