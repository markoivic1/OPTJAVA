package hr.fer.zemris.optjava.dz12.function;

import hr.fer.zemris.optjava.dz12.Ant;
import hr.fer.zemris.optjava.dz12.Node;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Prog3 implements Node {

    private Node first;
    private Node second;
    private Node third;
    private int nodeNumber;
    private int nodeDepth;

    public Prog3(Node first, Node second, Node third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public void execute(Ant ant, int[][] map) {
        first.execute(ant, map);
        if (ant.noActionsLeft()) {
            return;
        }
        second.execute(ant, map);
        if (ant.noActionsLeft()) {
            return;
        }
        third.execute(ant, map);
    }

    @Override
    public Node copy() {
        Node first = this.first.copy();
        Node second = this.second.copy();
        Node third = this.third.copy();
        Prog3 copy = new Prog3(first, second, third);
        copy.setNodeDepth(nodeDepth);
        copy.setNodeNumber(nodeNumber);
        return copy;
    }

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
        Node nodeFromThird = third.getNodeByNumber(n);
        if (nodeFromThird != null) {
            return nodeFromThird;
        }

        return null;
    }

    @Override
    public int getNodeDepth() {
        return nodeDepth;
    }

    @Override
    public int getChildrenSize() {
        return 3;
    }

    @Override
    public int getDescendentSize() {
        return first.getDescendentSize() + second.getDescendentSize() + third.getDescendentSize() + 3;
    }

    @Override
    public int getMaxDepth(int maxDepth) {
        return maxDepth - nodeDepth;
    }

    @Override
    public int getTreeDepth() {
        return Math.max(Math.max(first.getTreeDepth(), second.getTreeDepth()), third.getTreeDepth());
    }

    @Override
    public int calculateNodeNumber(int n) {
        this.nodeNumber = n;
        return third.calculateNodeNumber(second.calculateNodeNumber(first.calculateNodeNumber(n + 1) + 1) + 1);
    }

    @Override
    public void calculateNodeDepth(int nodeDepth) {
        first.calculateNodeDepth(nodeDepth + 1);
        second.calculateNodeDepth(nodeDepth + 1);
        third.calculateNodeDepth(nodeDepth + 1);
        this.nodeDepth = nodeDepth;
    }

    @Override
    public void replace(Node node, int nodeNumber) {
        if (this.nodeNumber + 1 == nodeNumber) {
            this.first = node;
        } else if (this.nodeNumber + 2 == nodeNumber) {
            this.second = node;
        } else if (this.nodeNumber + 3 == nodeNumber) {
            this.third = node;
        } else {
            this.first.replace(node, nodeNumber);
            this.second.replace(node, nodeNumber);
        }
    }

    @Override
    public String toString() {
        return "Prog3(" + first.toString() + ", " + second.toString() + ", " + third.toString() + ")";
    }
}
