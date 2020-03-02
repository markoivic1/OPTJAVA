package hr.fer.zemris.trisat;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Clause {

    private int[] indexes;

    public Clause(int[] indexes) {
        this.indexes = indexes;
    }

    // vraća broj literala koji čine klauzulu
    public int getSize() {
        return indexes.length;
    }

    // vraća indeks varijable koja je index-ti član ove klauzule
    public int getLiteral(int index) {
        return indexes[index];
    }

    // vraća true ako predana dodjela zadovoljava ovu klauzulu
    public boolean isSatisfied(BitVector assignment) {
        for (int i = 0; i < indexes.length; i++) {
            if ((indexes[i] > 0) == assignment.get(Math.abs(indexes[i]) - 1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int index : indexes) {
            sb.append(index + " ");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
