package com.lightcurriculum.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;


public class AESUtil {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String AES_NAME = "AES";
    // 加密模式
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    public static final String PREFIX = "JavaIsTheBestLanguageInTheWorldTTdlroWehTnIegaugnaLtseBehTsIavaJ";

    public static final String IV = "abcdefghijklmnop";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     *
     * @param pwd  actual passwd
     * @param salt take from page
     * @return AES encrypted passwd
     */
    public String encrypt(String pwd, String salt) {
        assert (salt.length() == 16);
        pwd = PREFIX + pwd;
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(pwd.getBytes(CHARSET_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 解密
     *
     * @param data encrypted passwd
     * @param salt take from page
     * @return AES decrypted passwd
     */
    public String decrypt(String data, String salt) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(salt.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)), CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}