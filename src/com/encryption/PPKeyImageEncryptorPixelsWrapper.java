package com.encryption;

import com.encryption.image_encoder.TooManyBitsException;
import com.encryption.imagebitmap.ImagePixels;

import java.awt.image.BufferedImage;
import java.math.BigInteger;

/**
 * a wrapper for the PPKeyImageEncryptor class that takes a BitMap instead of BufferedImage
 * BufferedImage doesn't exist on Android, so this class is necessary for the libraries use in that environment
 */
public class PPKeyImageEncryptorPixelsWrapper extends PPKeyImageEncryptor implements PixelsImageEncryptor {


    public PPKeyImageEncryptorPixelsWrapper(int keySize) {
        super(keySize);
    }

    public PPKeyImageEncryptorPixelsWrapper(BigInteger modulus, BigInteger publicExponent) {
        super(modulus, publicExponent);
    }

    public PPKeyImageEncryptorPixelsWrapper(BigInteger modulus, BigInteger publicExponent, BigInteger privateExponent) {
        super(modulus, publicExponent, privateExponent);
    }


    @Override
    public ImagePixels encrypt(byte[] message, ImagePixels image) throws TooManyBitsException {
        BufferedImage bufferedImage = super.encrypt(message,image.getImage());
        return new ImagePixels(bufferedImage);
    }

    @Override
    public byte[] decrypt(ImagePixels image) {
        return super.decrypt(image.getImage());
    }

    @Override
    public int getSymbolCapacity(ImagePixels image) {
        return super.getSymbolCapacity(image.getImage());
    }
}
