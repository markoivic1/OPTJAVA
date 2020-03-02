package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.swing.Map;

import javax.swing.*;
import java.awt.*;

/**
 * Arguments: 13-SantaFeAntTrail.txt 100 500 89 program.txt
 * @author Marko Ivić
 * @version 1.0.0
 */
public class AntTrailGA extends JFrame {
    private JComponent mapGrid;
    private Ant ant;
    private int[][] map;
    private int maxIter;
    private int popSize;
    private int minFitness;
    private String savePath;
    private static final int ACTIONS_LEFT = 600;
    private JButton singleAction;
    private JButton autoAction;
    private JProgressBar progressBar;
    private boolean nextStep;
    private Node solution;
    private Listener terminalListener;
    public AntTrailGA(String[] args) throws HeadlessException {
        map = Util.loadMap(args[0]);
        maxIter = Integer.parseInt(args[1]);
        popSize = Integer.parseInt(args[2]);
        minFitness = Integer.parseInt(args[3]);
        savePath = args[4];
        ant = new Ant(Orientation.EAST, map[0].length, map.length, ACTIONS_LEFT);
        initGUI();
        GP.registerListener((iter) -> progressBar.setValue(iter));
        initButtons(args[0]);
    }

    private void initButtons(String mapPath) {
        nextStep = false;
        singleAction.addActionListener(l -> {
            synchronized (this) {
                nextStep = true;
            }
        });
        Runnable job = () -> {
            while (!ant.noActionsLeft()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                synchronized (this) {
                    nextStep = true;
                }
            }
        };
        Thread thread = new Thread(job);
        thread.setDaemon(true);
        autoAction.addActionListener(l -> {
            autoAction.setEnabled(false);
            singleAction.setEnabled(false);
            thread.start();
        });
    }

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Invalid number of arguments");
            return;
        }
        AntTrailGA antTrailGA = new AntTrailGA(args);
        SwingUtilities.invokeLater(() -> antTrailGA.setVisible(true));
        antTrailGA.execute();
    }
    private void execute() {
        ant.reset();
        autoAction.setEnabled(false);
        singleAction.setEnabled(false);

        solution = GP.calculateAnt(map, maxIter, popSize, minFitness, savePath, ant);
        ant.reset();
        terminalListener = () -> {
            while (!nextStep) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
            synchronized (this) {
                nextStep = false;
            }
            repaint();
        };
        ant.addListener(terminalListener);

        singleAction.setEnabled(true);
        autoAction.setEnabled(true);
        do {
            solution.execute(ant, map);
        } while (!ant.noActionsLeft());
    }

    private void initGUI() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        mapGrid = new Map(map[0].length,map.length, map, ant);
        add(mapGrid, BorderLayout.CENTER);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        singleAction = new JButton("Step");
        buttonPanel.add(singleAction);
        autoAction = new JButton("Auto");
        buttonPanel.add(autoAction);
        infoPanel.add(buttonPanel);
        progressBar = new JProgressBar(0, maxIter);
        infoPanel.add(progressBar);
        add(infoPanel, BorderLayout.SOUTH);
        setResizable(false);
        pack();
    }
}
