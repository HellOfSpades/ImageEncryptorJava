package com;

import com.encryption.ImageEncryptor;
import com.encryption.PPKeyImageEncryptor;
import com.encryption.image_encoder.ImageEncoder;
import com.encryption.image_encoder.PerColourEncoder;
import com.encryption.image_encoder.TooManyBitsException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) throws IOException {


        BufferedImage baseImage = ImageIO.read(new File(Constants.INPUT_FILE_PATH));

        PPKeyImageEncryptor imageEncryptor = new PPKeyImageEncryptor();
        BufferedImage encryptedImage = imageEncryptor.encrypt(Constants.MESSAGE.getBytes(), baseImage);

        //decryption part
        ImageEncryptor imageEncryptor1 = new PPKeyImageEncryptor(imageEncryptor.getPrivateKey(),imageEncryptor.getPublicKey());

        byte[] decryptedMessage = imageEncryptor1.decrypt(encryptedImage);
        System.out.println(new String(decryptedMessage));

    }
}
