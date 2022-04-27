package com.encryption.image_encoder;

import com.encryption.adaptors.BitByteAdaptor;

import java.awt.image.BufferedImage;

/**
 * author: HellOfSpades
 * base interface for an imageEncryptor
 */
public abstract class ImageEncoder {

    private BufferedImage image;

    ImageEncoder(BufferedImage image){
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void encryptString(String string) throws TooManyBitsException {encryptBytes(string.getBytes());}

    public void encryptBytes(byte[] bytes) throws TooManyBitsException {encryptBits(BitByteAdaptor.byteArrayToBitArray(bytes));}

    abstract public void encryptBits(boolean[] bits) throws TooManyBitsException;


    public String decryptToString() {
        return new String(decryptToBytes());
    }

    public byte[] decryptToBytes(){
        return BitByteAdaptor.bitArrayToByteArray(decryptToBits());
    }

    abstract public boolean[] decryptToBits();

    abstract public int getBitCapacity();
}
