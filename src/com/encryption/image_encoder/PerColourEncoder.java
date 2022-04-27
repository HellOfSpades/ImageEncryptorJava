package com.encryption.image_encoder;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class PerColourEncoder extends ImageEncoder {

    public PerColourEncoder(BufferedImage image) {
        super(image);
    }

    @Override
    public void encryptBits(boolean[] bits) throws TooManyBitsException {
        //throw an exception if there are too many bits in the message
        if (bits.length > getBitCapacity())
            throw new TooManyBitsException(
                    String.format("There are %d bits in your message, however the image can only hold %d"
                            , bits.length, getBitCapacity()));


        //the image that will be encoded
        WritableRaster image = super.getImage().getRaster();

        int bitsIndex = 0;

        outer_loop:
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {

                int[] rgb = image.getPixel(j,i,new int[4]);

                for (int k = 0; k < rgb.length-1 && bitsIndex<bits.length; k++) {
                    rgb[k] = newColour(rgb[k],bits[bitsIndex]);
                    bitsIndex++;
                }

                image.setPixel(j,i,rgb);
                if(bitsIndex>=bits.length)break outer_loop;
            }
        }
    }

    @Override
    public boolean[] decryptToBits() {
        //the image that will be decoded
        WritableRaster image = super.getImage().getRaster();

        boolean[] bits = new boolean[image.getWidth()*image.getHeight()*3];

        int bitsIndex = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int[] rgb = image.getPixel(j,i,new int[4]);
                for (int k = 0; k < rgb.length-1; k++) {
                    bits[bitsIndex] = getColourBit(rgb[k]);
                    bitsIndex++;
                }
            }
        }
        return bits;
    }

    @Override
    public int getBitCapacity() {
        return getImage().getHeight()*getImage().getWidth()*3;
    }


    private int newColour(int oldColour, boolean bit){
        if(bit == getColourBit(oldColour)){
            return oldColour;
        }
        else return changeColourByOne(oldColour);
    }
    /**
     * returns the bit that corresponds to the colour
     * even = 0/false
     * odd = 1/true
     * @param colour
     * @return
     */
    private boolean getColourBit(int colour) {
        return colour % 2 != 0;
    }
    /**
     * changes the colour provided by 1
     * odd numbers become even
     * even become odd
     * @param oldColour
     * @return
     */
    private int changeColourByOne(int oldColour){
        int newColour;
        if(oldColour>0){
            newColour = oldColour-1;
        }else{
            newColour = oldColour+1;
        }
        return newColour;
    }
}
