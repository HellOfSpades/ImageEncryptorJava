package com.encryption;

import com.encryption.image_encoder.TooManyBitsException;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

public interface ImageEncryptor {
    public BufferedImage encrypt(byte[] message, BufferedImage image) throws TooManyBitsException;
    public byte[] decrypt(BufferedImage image);
    public int getSymbolCapacity(BufferedImage image);
}
