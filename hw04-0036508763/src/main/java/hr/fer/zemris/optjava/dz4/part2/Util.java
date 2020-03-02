package hr.fer.zemris.optjava.dz4.part2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {

    public static void printContainer(List<Column> container) {
        int largest = 0;
        for (Column column : container) {
            if (column.getSize() > largest) {
                largest = column.getSize();
            }
        }
        int index = largest - 1;
        while (true) {
            boolean hasMore = false;
            for (Column column : container) {
                try {
                    if (column.getAtIndex(index) / 10 == 0) {
                        System.out.print(" ");
                    }
                    System.out.print(column.getAtIndex(index) + " ");
                    hasMore = true;
                } catch (Exception e) {
                    System.out.print("   ");
                }
            }
            index--;
            System.out.println();
            if (!hasMore) {
                break;
            }
        }
    }

    public static void scramble(Box box) {
        Collections.shuffle(box.getSticks());
    }

    public static int count(Box box, int number) {
        int count = 0;
        for (int i = 0; i < box.getSticks().size(); i++) {
            for (int j = 0; j < box.getSticks().get(i).getColumn().size(); j++) {
                if (box.getSticks().get(i).getColumn().get(j) == number) {
                    count++;
                }
            }
        }
        return count;
    }
}
