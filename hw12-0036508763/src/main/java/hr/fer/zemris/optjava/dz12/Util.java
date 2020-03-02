package hr.fer.zemris.optjava.dz12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    public static int[][] loadMap(String path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.out.println("Unable to load map.");
            return null;
        }
        String[] dimensions = lines.get(0).split("x");
        int[][] map = new int[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
        for (int i = 0; i < map.length; i++) {
            String[] line = lines.get(i + 1).split("");
            for (int j = 0; j < map[i].length; j++) {
                String character = line[j];
                if (character.equals("0") || character.equals(".")) {
                    map[i][j] = 0;
                } else
                    map[i][j] = 1;
            }
        }
        return map;
    }

    public static void saveProgram(Node node, String path) {
        try {
            Files.write(Paths.get(path), node.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Couldn't save program.");
        }
    }

    public static int[] getMapSize(String path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.out.println("Unable to load map.");
            return null;
        }
        String[] dimensions = lines.get(0).split("x");
        int[] map = new int[2];
        map[0] = Integer.parseInt(dimensions[0]);
        map[1] = Integer.parseInt(dimensions[1]);
        return map;
    }
}
