package hr.fer.zemris.optjava.dz12.terminal;

import hr.fer.zemris.optjava.dz12.Ant;
import hr.fer.zemris.optjava.dz12.Node;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class TMove extends TerminalNode {

    @Override
    public void execute(Ant ant, int[][] map) {
        ant.move(map);
    }

    @Override
    public Node copy() {
        TMove copy = new TMove();
        copy.setNodeNumber(nodeNumber);
        copy.setNodeDepth(nodeDepth);
        return copy;
    }

    @Override
    public String toString() {
        return "Move";
    }
}
