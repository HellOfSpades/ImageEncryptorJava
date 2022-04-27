package com.encryption;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

public interface ImageEncryptor {
    public BufferedImage encrypt(byte[] message, BufferedImage image);
    public byte[] decrypt(BufferedImage image);
}
