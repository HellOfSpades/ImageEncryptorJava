package com.encryption.image_encoder;

import java.awt.image.BufferedImage;

/**
 * author: HellOfSpades
 * encrypts a message so that one pixel corresponds to one bit
 */
public class PerPixelEncoder extends ImageEncoder {

    public PerPixelEncoder(BufferedImage image) {
        super(image);
    }

    /**
     * @param bits
     * @throws TooManyBitsException
     */
    @Override
    public void encryptBits(boolean[] bits) throws TooManyBitsException {

        //throw an exception if there are too many bits in the message
        if (bits.length > getBitCapacity())
            throw new TooManyBitsException(
                    String.format("There are %d bits in your message, however the image can only hold %d"
                            , bits.length, getBitCapacity()));

        //the image that will be encoded
        BufferedImage image = super.getImage();

        int bitsIndex = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if(bitsIndex<bits.length) {
                    image.setRGB(j, i, newColour(image.getRGB(j, i), bits[bitsIndex]));
                    bitsIndex++;
                }else{
                    image.setRGB(j, i, newColour(image.getRGB(j, i)));
                }
            }
        }
    }


    @Override
    public boolean[] decryptToBits() {
        //the image that will be decoded
        BufferedImage image = super.getImage();

        boolean[] bits = new boolean[image.getWidth()*image.getHeight()];

        int bitsIndex = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                bits[bitsIndex] = getColourBit(image.getRGB(j,i));
                bitsIndex++;
            }
        }
        return bits;
    }

    /**
     * this algorithm stores one bit in each pixel
     * @return
     */
    @Override
    public int getBitCapacity() {
        BufferedImage image = getImage();
        return image.getHeight() * image.getWidth();
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
     * minimally changes the colour of the pixel to have it represent the bit, if needed
     * @param oldColour
     * @param bit
     * @return
     */
    private int newColour(int oldColour, boolean bit){
        if(bit == getColourBit(oldColour)){
            return oldColour;
        }
        else return changeColourByOne(oldColour);
    }

    /**
     * changes the colour as if the input bit is 0
     * @param oldColour
     * @return
     */
    private int newColour(int oldColour){
        return newColour(oldColour,false);
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
