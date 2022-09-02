package com;

import com.encryption.imagebitmap.ImagePixels;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //try {
        BufferedImage baseImage = ImageIO.read(new File(Constants.INPUT_FILE_PATH));
        ImagePixels imageBitMap = new ImagePixels(baseImage);

        int[][] pixels = imageBitMap.getPixels();
        ImagePixels pixelsBitMap = new ImagePixels(pixels);
        BufferedImage restoredImage = pixelsBitMap.getImage();

        boolean same = true;
        for (int i = 0; i < restoredImage.getHeight(); i++) {
            for (int n = 0; n < restoredImage.getWidth(); n++) {
                if(restoredImage.getRGB(n,i)!=baseImage.getRGB(n,i)){
                    System.out.println(restoredImage.getRGB(n,i)+" vs "+baseImage.getRGB(n,i));
                    same = false;
                }
            }
        }
        System.out.println(same);

//            PPKeyImageEncryptorBitMapWrapper imageEncryptor = new PPKeyImageEncryptorBitMapWrapper(2048);
//
//            System.out.println("symbol capacity: " + imageEncryptor.getSymbolCapacity(imageBitMap));
//
//            BufferedImage encryptedImage = imageEncryptor.encrypt(Constants.MESSAGE.getBytes(), imageBitMap).getImage();
//
//            //decryption part
//            PPKeyImageEncryptorBitMapWrapper imageEncryptor1 = new PPKeyImageEncryptorBitMapWrapper
//                    (imageEncryptor.getModulus()
//                            , imageEncryptor.getPublicExponent()
//                            , imageEncryptor.getPrivateExponent());
//
//            byte[] decryptedMessage = imageEncryptor1.decrypt(encryptedImage);
//            System.out.println(new String(decryptedMessage));
//        }catch (TooManyBitsException e){
//            e.printStackTrace();
//        }
    }
}
