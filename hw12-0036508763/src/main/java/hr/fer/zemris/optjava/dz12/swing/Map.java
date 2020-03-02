package hr.fer.zemris.optjava.dz12.swing;

import hr.fer.zemris.optjava.dz12.Ant;

import javax.swing.*;
import java.awt.*;

import static hr.fer.zemris.optjava.dz12.Orientation.*;
import static hr.fer.zemris.optjava.dz12.Orientation.NORTH;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Map extends JComponent {
    private int width;
    private int height;
    private int[][] map;
    private Ant ant;

    public Map(int width, int height, int[][] map, Ant ant) {
        this.width = width;
        this.height = height;
        this.map = map;
        this.ant = ant;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = new Dimension();
        preferredSize.height = height * 30;
        preferredSize.width = width * 30;
        return preferredSize;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        int cellWidth = getWidth() / width;
        int cellHeight = getHeight() / height;

        for (int vert = 0; vert < width; vert++) {
            for (int horz = 0; horz < height; horz++) {
                if (map[vert][horz] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(horz * cellWidth, vert * cellHeight, cellWidth, cellHeight);
                } else {
                    g.setColor(Color.YELLOW);
                    g.fillRect(horz * cellWidth, vert * cellHeight, cellWidth, cellHeight);
                }
                g.setColor(Color.BLACK);
                g.drawRect(horz * cellWidth, vert * cellHeight, cellWidth, cellHeight);
                if (horz == ant.getxPosition() && vert == ant.getyPosition()) {
                    g.setColor(Color.GRAY);
                    g.fillOval(horz * cellWidth, vert * cellHeight, cellWidth, cellHeight);
                    g.setColor(Color.BLACK);
                    switch (ant.getOrientation()) {
                        case NORTH -> g.fillRect(horz * cellWidth + cellWidth / 2 - 2, vert * cellHeight, 5, cellHeight / 2);
                        case SOUTH -> g.fillRect(horz * cellWidth + cellWidth / 2 - 2, vert * cellHeight + cellHeight / 2, 5, cellHeight / 2);
                        case WEST -> g.fillRect(horz * cellWidth, vert * cellHeight + cellHeight / 2 - 2, cellWidth / 2, 5);
                        case EAST -> g.fillRect(horz * cellWidth + cellWidth / 2, vert * cellHeight + cellHeight / 2 - 2, cellWidth / 2, 5);
                    }
                }
            }
        }
        g2d.dispose();
    }
}
