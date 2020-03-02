package hr.fer.zemris.optjava.dz12.terminal;

import hr.fer.zemris.optjava.dz12.Node;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public abstract class TerminalNode implements Node {
    int nodeNumber;
    int nodeDepth;
    private int foodEaten;
    private double fitness;


    @Override
    public void setNodeDepth(int nodeDepth) {
        this.nodeDepth = nodeDepth;
    }

    @Override
    public void setNodeNumber(int n) {
        this.nodeNumber = n;
    }

    @Override
    public Node getNodeByNumber(int n) {
        return n == this.nodeNumber ? this.copy() : null;
    }

    @Override
    public int getNodeDepth() {
        return nodeDepth;
    }

    @Override
    public int getChildrenSize() {
        return 0;
    }

    @Override
    public int getDescendentSize() {
        return 0;
    }

    @Override
    public int getMaxDepth(int maxDepth) {
        return maxDepth - nodeDepth;
    }

    @Override
    public void replace(Node node, int nodeNumber) {
    }

    @Override
    public int getTreeDepth() {
        return nodeDepth;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    @Override
    public int calculateNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
        return nodeNumber;
    }

    @Override
    public void calculateNodeDepth(int nodeDepth) {
        this.nodeDepth = nodeDepth;
    }
}
