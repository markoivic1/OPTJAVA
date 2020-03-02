package hr.fer.zemris.optjava.dz12;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public interface Node {
    void execute(Ant ant, int[][] map);

    Node copy();

    Node getNodeByNumber(int n);

    int calculateNodeNumber(int n);

    void setNodeNumber(int n);

    int getNodeDepth();

    void calculateNodeDepth(int nodeDepth);

    void setNodeDepth(int nodeDepth);

    int getChildrenSize();

    int getDescendentSize();

    int getMaxDepth(int maxDepth);

    int getTreeDepth();

    void replace(Node node, int nodeNumber);

    String toString();
}
