package com.encryption;

import com.encryption.image_encoder.ImageEncoder;
import com.encryption.image_encoder.PerColourEncoder;
import com.encryption.image_encoder.TooManyBitsException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PPKeyImageEncryptor implements ImageEncryptor{

    RSAPrivateKey privateKey;
    RSAPublicKey publicKey;

    public PPKeyImageEncryptor(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
            this.publicKey = (RSAPublicKey) keyPair.getPublic();

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    public PPKeyImageEncryptor(BigInteger modulus, BigInteger publicExponent) {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            this.publicKey = (RSAPublicKey) factory.generatePublic(new RSAPublicKeySpec(modulus,publicExponent));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public PPKeyImageEncryptor(BigInteger modulus, BigInteger publicExponent, BigInteger privateExponent) {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            this.publicKey = (RSAPublicKey) factory.generatePublic(new RSAPublicKeySpec(modulus,publicExponent));
            this.privateKey = (RSAPrivateKey) factory.generatePrivate(new RSAPrivateKeySpec(modulus,privateExponent));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage encrypt(byte[] message, BufferedImage image) {

        try{
            AesEncryptor aesEncryptor = new AesEncryptor();
            //the message encrypted with the AES algorithm
            byte[] encryptedMessage = aesEncryptor.encrypt(message);
            byte[] messageLength = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(encryptedMessage.length).array();

            byte[] aesParameters = aesEncryptor.getParameters();

            //putting the message length and aes parameters into a single byte array so they can be encoded together
            byte[] encodedWithRsa = new byte[aesParameters.length+messageLength.length];
            for (int i = 0; i < aesParameters.length; i++) {
                encodedWithRsa[i] = aesParameters[i];
            }
            for (int i = 0; i < messageLength.length; i++) {
                encodedWithRsa[i+aesParameters.length] = messageLength[i];
            }


            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            //encrypted AES cipher parameters with the RSA algorithm
            byte[] encryptedAesParameters = rsaCipher.doFinal(encodedWithRsa);
            //combined message that will be encoded into the image
            //first comes the encrypted AES parameters (48 bytes)
            //then comes the encrypted message
            byte[] combinedMessage = new byte[encryptedMessage.length+encryptedAesParameters.length];
            for (int i = 0; i < encryptedAesParameters.length; i++) {
                combinedMessage[i] = encryptedAesParameters[i];
            }
            for (int i = 0; i < encryptedMessage.length; i++) {
                combinedMessage[i+encryptedAesParameters.length] = encryptedMessage[i];
            }

            ImageEncoder encoder = new PerColourEncoder(image);
            encoder.encryptBytes(combinedMessage);
            return encoder.getImage();


        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (TooManyBitsException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] decrypt(BufferedImage image) {
        if(privateKey==null)throw new UnsupportedOperationException();
        try {
            ImageEncoder imageEncoder = new PerColourEncoder(image);
            byte[] decodedMessage = imageEncoder.decryptToBytes();

            byte[] encryptedAesParametersAndMessageLength = new byte[publicKey.getModulus().toByteArray().length-1];


            for (int i = 0; i < encryptedAesParametersAndMessageLength.length; i++) {
                encryptedAesParametersAndMessageLength[i] = decodedMessage[i];
            }


            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, this.privateKey);


            byte[] rsaDecoded = rsaCipher.doFinal(encryptedAesParametersAndMessageLength);

            byte[] aesParameters = new byte[48];
            byte[] messageLength = new byte[4];

            for (int i = 0; i < aesParameters.length; i++) {
                aesParameters[i] = rsaDecoded[i];
            }
            for (int i = 0; i < messageLength.length; i++) {
                messageLength[i] = rsaDecoded[i+aesParameters.length];
            }

            int length = ByteBuffer.wrap(messageLength).order(ByteOrder.LITTLE_ENDIAN).getInt();

            AesEncryptor aesEncryptor = new AesEncryptor(aesParameters);

            byte[] encryptedMessage = new byte[length];

            for (int i = 0; i < length; i++) {
                encryptedMessage[i] = decodedMessage[i+encryptedAesParametersAndMessageLength.length];
            }

            return aesEncryptor.decrypt(encryptedMessage);

        }catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BigInteger getPrivateExponent() {
        return (privateKey!=null)?privateKey.getPrivateExponent():null;
    }

    public BigInteger getPublicExponent() {
        return publicKey.getPublicExponent();
    }

    public BigInteger getModulus(){
        return publicKey.getModulus();
    }


    /**
     * This class is used to code the message itself
     * then the generated iv and key are encrypted with the rsa algorithm
     */
    private class AesEncryptor{

        private IvParameterSpec iv;
        private SecretKey key;

        public AesEncryptor(IvParameterSpec iv, SecretKey key) {
            this.iv = iv;
            this.key = key;
        }

        public AesEncryptor(byte[] parameters){
            //TODO implement this
            byte[] keyBytes = new byte[32];
            byte[] ivBytes = new byte[16];
            for (int i = 0; i < keyBytes.length; i++) {
                keyBytes[i] = parameters[i];
            }
            for (int i = keyBytes.length; i < parameters.length; i++) {
                ivBytes[i-keyBytes.length] = parameters[i];
            }
            this.key = new SecretKeySpec(keyBytes,0,keyBytes.length,"AES");
            this.iv = new IvParameterSpec(ivBytes);
        }

        public byte[] getParameters(){
            byte[] bytes = new byte[48];
            byte[] keyBytes = this.key.getEncoded();
            byte[] ivBytes = this.iv.getIV();

            for (int i = 0; i < keyBytes.length; i++) {
                bytes[i] = keyBytes[i];
            }
            for (int i = 0; i < ivBytes.length; i++) {
                bytes[i+keyBytes.length] = ivBytes[i];
            }
            return bytes;
        }



        public AesEncryptor(){
            this.iv = generateAesIv();
            this.key = generateAesKey();
        }

        public byte[] decrypt(byte[] bytes){
            //TODO make the method decrypt the input
            try{
                Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                aesCipher.init(Cipher.DECRYPT_MODE, key,iv);
                return aesCipher.doFinal(bytes);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }

            return bytes;
        }

        public byte[] encrypt(byte[] bytes){
            try {
                Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                aesCipher.init(Cipher.ENCRYPT_MODE, key, iv);
                byte[] encryptedMessage = aesCipher.doFinal(bytes);
                return encryptedMessage;

            }catch (NoSuchAlgorithmException | NoSuchPaddingException e){
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            return null;
        }

        private SecretKey generateAesKey(){
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256);
                return keyGenerator.generateKey();
            }catch (NoSuchAlgorithmException e){
                return null;
            }
        }

        private IvParameterSpec generateAesIv() {
            byte[] bytes = new byte[16];
            new SecureRandom().nextBytes(bytes);
            return new IvParameterSpec(bytes);
        }
    }
}