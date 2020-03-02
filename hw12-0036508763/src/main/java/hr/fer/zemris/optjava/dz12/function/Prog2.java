package hr.fer.zemris.optjava.dz12.function;

import hr.fer.zemris.optjava.dz12.Ant;
import hr.fer.zemris.optjava.dz12.Node;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Prog2 implements Node {
    private Node first;
    private Node second;
    private int nodeNumber;
    private int nodeDepth;

    public Prog2(Node first, Node second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void execute(Ant ant, int[][] map) {
        first.execute(ant, map);
        if (ant.noActionsLeft()) {
            return;
        }
        second.execute(ant, map);
    }

    @Override
    public void setNodeDepth(int nodeDepth) {
        this.nodeDepth = nodeDepth;
    }

    @Override
    public Node copy() {
        Node first = this.first.copy();
        Node second = this.second.copy();
        Prog2 copy = new Prog2(first, second);
        copy.setNodeNumber(nodeNumber);
        copy.setNodeDepth(nodeDepth);
        return copy;
    }

    @Override
    public Node getNodeByNumber(int n) {
        if (this.nodeNumber == n) {
            return this.copy();
        }
        Node nodeFromFirst = first.getNodeByNumber(n);
        if (nodeFromFirst != null) {
            return nodeFromFirst;
        }
        Node nodeFromSecond = second.getNodeByNumber(n);
        if (nodeFromSecond != null) {
            return nodeFromSecond;
        }

        return null;
    }

    @Override
    public int getNodeDepth() {
        return nodeDepth;
    }

    @Override
    public int getChildrenSize() {
        return 2;
    }

    @Override
    public void setNodeNumber(int n) {
        this.nodeNumber = n;
    }

    @Override
    public int getDescendentSize() {
        return first.getDescendentSize() + second.getDescendentSize() + 2;
    }

    @Override
    public int getMaxDepth(int maxDepth) {
        return maxDepth - nodeDepth;
    }

    @Override
    public int getTreeDepth() {
        int firstNodeDepth = first.getTreeDepth();
        int secondNodeDepth = second.getTreeDepth();
        return Math.max(firstNodeDepth, secondNodeDepth);
    }

    @Override
    public int calculateNodeNumber(int n) {
        this.nodeNumber = n;
        return second.calculateNodeNumber(first.calculateNodeNumber(n + 1) + 1);
    }

    @Override
    public void calculateNodeDepth(int nodeDepth) {
        first.calculateNodeDepth(nodeDepth + 1);
        second.calculateNodeDepth(nodeDepth + 1);
        this.nodeDepth = nodeDepth;
    }

    @Override
    public void replace(Node node, int nodeNumber) {
        if (this.nodeNumber + 1 == nodeNumber) {
            this.first = node;
        } else if (this.nodeNumber + 2 == nodeNumber) {
            this.second = node;
        } else {
            this.first.replace(node, nodeNumber);
            this.second.replace(node, nodeNumber);
        }
    }

    @Override
    public String toString() {
        return "Prog2(" + first.toString() + ", " + second.toString() + ")";
    }
}
