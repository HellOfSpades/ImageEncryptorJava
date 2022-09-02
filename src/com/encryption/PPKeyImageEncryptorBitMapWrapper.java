package com.encryption;

import com.encryption.image_encoder.TooManyBitsException;
import com.encryption.imagebitmap.ImageBitMap;

import java.awt.image.BufferedImage;
import java.math.BigInteger;

/**
 * a wrapper for the PPKeyImageEncryptor class that takes a BitMap instead of BufferedImage
 * BufferedImage doesn't exist on Android, so this class is necessary for the libraries use in that environment
 */
public class PPKeyImageEncryptorBitMapWrapper extends PPKeyImageEncryptor implements BitMapImageEncryptor{


    public PPKeyImageEncryptorBitMapWrapper(int keySize) {
        super(keySize);
    }

    public PPKeyImageEncryptorBitMapWrapper(BigInteger modulus, BigInteger publicExponent) {
        super(modulus, publicExponent);
    }

    public PPKeyImageEncryptorBitMapWrapper(BigInteger modulus, BigInteger publicExponent, BigInteger privateExponent) {
        super(modulus, publicExponent, privateExponent);
    }


    @Override
    public ImageBitMap encrypt(byte[] message, ImageBitMap image) throws TooManyBitsException {
        BufferedImage bufferedImage = super.encrypt(message,image.getImage());
        return new ImageBitMap(bufferedImage);
    }

    @Override
    public byte[] decrypt(ImageBitMap image) {
        return super.decrypt(image.getImage());
    }

    @Override
    public int getSymbolCapacity(ImageBitMap image) {
        return super.getSymbolCapacity(image.getImage());
    }
}
