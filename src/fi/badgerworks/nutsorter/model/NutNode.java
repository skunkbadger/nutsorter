package fi.badgerworks.nutsorter.model;

import java.util.List;

public class NutNode {

    private List<Nut> unorderedNuts;

    private NutNode leftChildNode;
    private NutNode rightChildNode;

    private List<Nut> nuts;

    public NutNode(final List<Nut> unorderedNuts) {
        this.unorderedNuts = unorderedNuts;
    }

    public NutNode getLeftChildNode() {
        return leftChildNode;
    }

    public void setLeftChildNode(final NutNode leftChildNode) {
        this.leftChildNode = leftChildNode;
    }

    public boolean hasLeftChildNode() {
        return leftChildNode != null;
    }

    public NutNode getRightChildNode() {
        return rightChildNode;
    }

    public void setRightChildNode(final NutNode rightChildNode) {
        this.rightChildNode = rightChildNode;
    }

    public boolean hasRightChildNode() {
        return rightChildNode != null;
    }

    public Nut getUnusedNut() {
        if (nuts != null) {
            for (final Nut nut : nuts) {
                if (!nut.hasPair()) {
                    return nut;
                }
            }
        }
        return null;
    }

    public void setNuts(final List<Nut> nuts) {
        this.nuts = nuts;
    }

    public List<Nut> getUnorderedNuts() {
        return unorderedNuts;
    }

    public void resetNuts() {
        unorderedNuts = null;
    }
}
