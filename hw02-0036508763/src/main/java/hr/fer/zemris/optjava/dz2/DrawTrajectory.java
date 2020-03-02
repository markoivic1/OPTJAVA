package hr.fer.zemris.optjava.dz2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class DrawTrajectory extends JFrame {
    private static java.util.List<Double> xCoordiantes;
    private java.util.List<Double> yCoordiantes;
    public static String name;

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    public DrawTrajectory() throws HeadlessException {
        initGUI();
        xCoordiantes = new ArrayList<>();
        yCoordiantes = new ArrayList<>();
    }

    private void initGUI() {
        this.setLayout(new BorderLayout());
        this.setSize(WIDTH, HEIGHT);
    }

    public void addPoint(double[] point) {
        xCoordiantes.add(point[0]);
        yCoordiantes.add(point[1]);
    }

    public void drawPoints() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);

        int[] xCoor = new int[xCoordiantes.size()];
        int[] yCoor = new int[yCoordiantes.size()];

        for (int i = 0; i < xCoor.length; i++) {
            xCoor[i] = (int) Math.round((xCoordiantes.get(i) + 5) * 100);
            yCoor[i] = (int) Math.round((yCoordiantes.get(i) + 5) * 100);
        }
        for (int i = 0; i < xCoor.length - 1; i++) {
            g.drawPolyline(xCoor, yCoor, xCoor.length);
        }
        try {
            ImageIO.write(image, "png", new File("./"+ name +".png"));
        } catch (IOException e) {
            System.out.println("Unable to save image.");
        }
        g.dispose();
    }
}
