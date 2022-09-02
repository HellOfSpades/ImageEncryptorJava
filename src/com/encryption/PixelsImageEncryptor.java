package com.encryption;

import com.encryption.image_encoder.TooManyBitsException;
import com.encryption.imagebitmap.ImagePixels;

public interface PixelsImageEncryptor {
    public ImagePixels encrypt(byte[] message, ImagePixels image) throws TooManyBitsException;
    public byte[] decrypt(ImagePixels image);
    public int getSymbolCapacity(ImagePixels image);
}
