package com;

import com.encryption.ImageEncoder;
import com.encryption.PerColourEncoder;
import com.encryption.TooManyBitsException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedImage baseImage = ImageIO.read(new File(Constants.INPUT_FILE_PATH));

        ImageEncoder encryptor = new PerColourEncoder(baseImage);
        System.out.println(encryptor.getBitCapacity());

        try {
            encryptor.encryptString(Constants.MESSAGE);
            ImageIO.write(encryptor.getImage(), "png",new File(Constants.OUTPUT_FILE_PATH));
        } catch (TooManyBitsException e) {
            e.printStackTrace();
        }

        BufferedImage codedImage = ImageIO.read(new File(Constants.OUTPUT_FILE_PATH));
        ImageEncoder decryptor = new PerColourEncoder(codedImage);
        System.out.println(decryptor.decryptToString());

    }
}
