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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class Main {

    public static void main(String[] args) throws IOException {


        BufferedImage baseImage = ImageIO.read(new File(Constants.INPUT_FILE_PATH));

        PPKeyImageEncryptor imageEncryptor = new PPKeyImageEncryptor(2048);
        BufferedImage encryptedImage = imageEncryptor.encrypt(Constants.MESSAGE.getBytes(), baseImage);

        //decryption part
        ImageEncryptor imageEncryptor1 = new PPKeyImageEncryptor
                (imageEncryptor.getModulus()
                        , imageEncryptor.getPublicExponent()
                        ,imageEncryptor.getPrivateExponent());

        byte[] decryptedMessage = imageEncryptor1.decrypt(encryptedImage);
        System.out.println(new String(decryptedMessage));
    }
}
