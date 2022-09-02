package com.encryption.adaptors;

import java.awt.image.BufferedImage;

public class PixelsBufferedImageAdaptor {
    public static BufferedImage pixelsToBufferedImage(int[][] pixels){
        BufferedImage image = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < pixels.length; i++) {
            for (int n = 0; n < pixels[i].length; n++) {
                image.setRGB(n, i, pixels[i][n]);
            }
        }
        return image;
    }

    public static int[][] bufferedImageToPixels(BufferedImage image){
        int[][] pixels = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int n = 0; n < image.getWidth(); n++) {
                pixels[i][n] = image.getRGB(n, i);
            }
        }
        return pixels;
    }
}
