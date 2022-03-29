package com.encryption.adaptors;

/**
 * author: HellOfSpades
 */
public class BitByteAdaptor {

    /**
     * converts an array of bytes into an array of bits
     * @param bytes
     * @return bits
     */
    public static boolean[] byteArrayToBitArray(byte[] bytes){
        boolean[] bits = new boolean[bytes.length*8];
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i]+128;
            for (int j = 7; j >= 0; j--) {
                bits[(i+1)*8-j-1] = (int)(b/Math.pow(2,j))>=1;
                b-=(bits[(i+1)*8-j-1])?Math.pow(2,j):0;
            }
        }
        return bits;
    }

    /**
     * converts an array of bits, into an array of bytes
     * @param bits
     * @return bytes
     */
    public static byte[] bitArrayToByteArray(boolean[] bits){
        byte[] bytes = new byte[bits.length/8];
        for (int i = 0; i < bytes.length; i++) {
            byte b = -128;
            for (int j = 7; j >= 0; j--) {
                b+=(bits[8*(i+1)-j-1])?Math.pow(2,j):0;
            }
            bytes[i] = b;
        }

        return bytes;
    }
}
