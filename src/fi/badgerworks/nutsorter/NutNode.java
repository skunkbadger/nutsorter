package fi.badgerworks.nutsorter;

import java.util.List;

class NutNode {

    private List<Nut> nuts;

    private NutNode leftChildNode;
    private NutNode rightChildNode;

    private Nut nut;
    private Bolt matchingBolt;

    NutNode(final List<Nut> nuts) {
        this.nuts = nuts;
    }

    void setMatchingBolt(final Bolt nut) {
        matchingBolt = nut;
    }

    boolean hasMatch() {
        return matchingBolt != null;
    }

    public NutNode getLeftChildNode() {
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

    Nut getNut() {
        return nut;
    }

    public void setNut(final Nut nut) {
        this.nut = nut;
    }

    public List<Nut> getNuts() {
        return nuts;
    }

    public void resetNuts() {
        nuts = null;
    }
}
