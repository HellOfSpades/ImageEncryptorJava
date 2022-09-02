package com.encryption.imagebitmap;

import com.encryption.adaptors.BitMapBufferedImageAdaptor;

import java.awt.image.BufferedImage;

/**
 * this class is used when using the library with android
 * it converts between Androids BitMap colors per pixel to java BufferedImage
 * and vice versa
 */
public class ImageBitMap{

    private BufferedImage image;
    //[y][x]
    //[height][width]
    private int[][] pixels;

    //constructors take either pixels or the image
    public ImageBitMap(int[][] pixels){
        this.pixels = pixels;
    }
    public ImageBitMap(BufferedImage image){
        this.image = image;
    }

    /**
     * returns the image, if there is no image, it initializes it using pixels
     * @return the image
     */
    public BufferedImage getImage() {
        if(image==null && pixels!=null)initializeImage();
        return image;
    }
    /**
     * returns pixels, if there is are no pixels, it initializes them using the image
     * @return pixels
     */
    public int[][] getPixels() {
        if(pixels==null && image!=null)initializePixels();
        return pixels;
    }


    /**
     * initializes the image from pixels
     */
    private void initializeImage(){
        image = BitMapBufferedImageAdaptor.bitMapToBufferedImage(pixels);
    }
    /**
     * initializes pixels from the image
     */
    private void initializePixels(){
        pixels = BitMapBufferedImageAdaptor.bufferedImageToBitMap(image);
    }
}
