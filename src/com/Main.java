package com;

import com.encryption.ImageEncryptor;
import com.encryption.PerPixelEncryptor;
import com.encryption.TooManyBitsException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedImage baseImage = ImageIO.read(new File(Constants.INPUT_FILE_PATH));

        ImageEncryptor encryptor = new PerPixelEncryptor(baseImage);

        try {
            encryptor.encryptString(Constants.MESSAGE);
            ImageIO.write(encryptor.getImage(), "png",new File(Constants.OUTPUT_FILE_PATH));
        } catch (TooManyBitsException e) {
            e.printStackTrace();
        }

        BufferedImage codedImage = ImageIO.read(new File(Constants.OUTPUT_FILE_PATH));
        ImageEncryptor decryptor = new PerPixelEncryptor(codedImage);
        System.out.println(decryptor.decryptToString());

    }
}
