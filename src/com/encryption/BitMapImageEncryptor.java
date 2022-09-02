package com.encryption;

import com.encryption.image_encoder.TooManyBitsException;
import com.encryption.imagebitmap.ImageBitMap;

public interface BitMapImageEncryptor{
    public ImageBitMap encrypt(byte[] message, ImageBitMap image) throws TooManyBitsException;
    public byte[] decrypt(ImageBitMap image);
    public int getSymbolCapacity(ImageBitMap image);
}
