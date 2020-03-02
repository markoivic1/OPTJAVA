package hr.fer.zemris.optjava.dz12.terminal;

import hr.fer.zemris.optjava.dz12.Ant;
import hr.fer.zemris.optjava.dz12.Node;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class TRotateRight extends TerminalNode {
    @Override
    public void execute(Ant ant, int[][] map) {
        ant.rotateRight();
    }

    @Override
    public Node copy() {
        TRotateRight copy = new TRotateRight();
        copy.setNodeNumber(nodeNumber);
        copy.setNodeDepth(nodeDepth);
        return copy;
    }

    @Override
    public String toString() {
        return "Right";
    }
}
