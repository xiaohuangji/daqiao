/**
 * $Id: DesCryptUtils.java 66922 2012-01-30 08:14:20Z shuai.ma@XIAONEI.OPI.COM $
 * Copyright 2009-2010 Oak Pacific Interactive. All rights reserved.
 */
package com.tg.service.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.CharEncoding;

/**
 * 修改日志
 * 
 * @author ji.sun
 */
public final class DesCryptUtils {

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DesCryptUtils.class);

    private static final URLCodec urlCodec = new URLCodec();;

    /**
     * encrypt with des algo
     * 
     * @param data
     * @param key
     * @param iv - initial vector
     * @return des result
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        try {
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(paddingIv(iv)));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * encrypt utf8 string to base84
     * 
     * @param data
     * @param key
     * @param iv - initial vector
     * @return des and base64 result
     */
    public static String encrpytBase64(String data, String key, String iv) {
        try {
            byte[] encryptedBytes = encrypt(data.getBytes(CharEncoding.UTF_8),
                    key.getBytes(CharEncoding.UTF_8), iv.getBytes(CharEncoding.UTF_8));
            return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * encrypt utf8 string to base84 and then url encode
     * 
     * @param data
     * @param key
     * @param iv
     * @return des base64 and urlencode
     */
    public static String encrpytURLEncode(String data, String key, String iv) {
        if (logger.isDebugEnabled()) {
            logger.debug("data=[" + data + "] key=[" + key + "] iv=[" + iv + "]");
        }
        try {
            return urlCodec.encode(encrpytBase64(data, key, iv));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * decrypt with des algo
     * 
     * @param encryptedData
     * @param key
     * @param iv - initial vector
     * @return des decrypt
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) {
        try {
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(paddingIv(iv)));
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * decrypt utf8 string from base64
     * 
     * @param encryptedString
     * @param key
     * @param iv - initial vector
     * @return des decrypt and base64
     */
    public static String decryptBase64(String encryptedString, String key, String iv) {
        byte[] encryptedData = Base64.decodeBase64(encryptedString);
        try {
            byte[] data = decrypt(encryptedData, key.getBytes(CharEncoding.UTF_8),
                    iv.getBytes(CharEncoding.UTF_8));
            return new String(data, CharEncoding.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * url decode src, and then decrpt utf8 string from base64
     * 
     * @param encryptedString
     * @param key
     * @param iv
     * @return des decrypt base64 and urldecode
     */
    public static String decryptURLDecode(String encryptedString, String key, String iv) {
        String _encryptedString;
        try {
            _encryptedString = urlCodec.decode(encryptedString);
            return decryptBase64(_encryptedString, key, iv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * padding the intial vector to 64bit
     * 
     * @param iv - initial vector
     * @return padding result
     */
    private static byte[] paddingIv(byte[] iv) {
        byte[] riv = new byte[8];
        int i = 0;
        for (; i < 8 && i < iv.length; i++) {
            riv[i] = iv[i];
        }
        for (; i < 8; i++) {
            riv[i] = 0;
        }
        return riv;
    }

    /**
     * private constructor
     */
    private DesCryptUtils() {
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        String src = "http://www.renren.com/yanzhid.mp3";
        String key = "52d456b4-df67-442d-8c65-4b18bfe57779";
        String iv = key;
        String result = DesCryptUtils.encrpytBase64(src, key, iv);
        System.out.println(result);
        String ret = DesCryptUtils.decryptBase64(result, key, iv);
        System.out.println(ret.equals(src));
        long et = System.currentTimeMillis();
        System.out.println("timeCost:" + (et - time));
    }

}
