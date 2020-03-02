package hr.fer.zemris.optjava.dz3.util;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Util {
    public static boolean[] convertToBool(byte[] bits) {
        boolean[] bool = new boolean[bits.length * 8];
        StringBuilder stringBool = new StringBuilder();
        for (int i = 0; i < bits.length; i++) {
            stringBool.append(String.format("%8s", Integer.toBinaryString(bits[i] & 0xFF)).replace(' ', '0'));
        }
        for (int i = 0; i < stringBool.length(); i++) {
            bool[i] = stringBool.charAt(i) == '1';
        }
        return bool;
    }

    public static byte[] convertToBits(boolean[] bool) {
        byte[] bits = new byte[bool.length / 8];
        for (int i = 0; i < bool.length / 8; i++) {
            StringBuilder stringRepresentation = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                stringRepresentation.append(bool[8 * i + j] ? "1" : "0");
            }
            bits[i] = (byte) Integer.parseInt(stringRepresentation.toString(), 2);
        }
        return bits;
    }
}
