package com.luxx.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecureUtil {

    public static String encryptByAES(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] encryptBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptBytes);
    }

    public static String decryptByAES(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] encryptBytes = Base64.getDecoder().decode(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static String encryptByDES(String plain, String encryptKey) throws Exception {
        //获取密码实例对象，参数格式为"算法/模式/填充"
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        //使用Key作为DES密钥的密钥内容，创建一个 DESKeySpec对象
        DESKeySpec desKeySpec = new DESKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8));
        //返回DES算法的SecretKeyFactory对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        //生成SecretKey对象
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        //使用密钥构造一个IvParameterSpec对象。
        IvParameterSpec iv = new IvParameterSpec(encryptKey.getBytes());
        //用密钥和一组算法参数初始化密码实例对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        //加密，使用Base64密码
        return Base64.getEncoder().encodeToString(cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decryptByDES(String encryptString, String encryptKey) throws Exception {
        //使用密钥构造IV对象
        IvParameterSpec iv = new IvParameterSpec(encryptKey.getBytes());
        //根据密钥和DES算法构造一个SecretKeySpec
        SecretKeySpec skeySpec = new SecretKeySpec(encryptKey.getBytes(), "DES");
        //返回实现了指定转换的Cipher对象
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        //解密初始化
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        //解码返回
        byte[] byteMi = Base64.getDecoder().decode(encryptString);
        byte[] decryptedData = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }

    public static void genKeyPair() throws NoSuchAlgorithmException {
        //KeyPairGenerator类，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        //初始化密钥对生成器
        keyPairGen.initialize(1024, new SecureRandom());
        //生成一个密钥对，保存在keyPair对象中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //得到私钥对象
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //得到公钥对象
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        //公钥字符串
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println("public key: ");
        System.out.println(publicKeyString);
        //私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("private key: ");
        System.out.println(privateKeyString);
    }

    public static String encryptByPublicKey(String plain, String publicKey) throws Exception {
        //从公钥数据中得到KeySpec对象
        byte[] key = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        //根据RSA算法构造一个KeyFactory
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        //获取密码实例对象 参数格式为"算法/模式/填充"
        Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cp.doFinal(plain.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decryptByPrivateKey(String encrypted, String privateKey) throws Exception {
        //从私钥数据中得到KeySpec对象
        byte[] key = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        //获取密码实例对象，参数格式为"算法/模式/填充"
        Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] encryptBytes = Base64.getDecoder().decode(encrypted);
        byte[] decryptBytes = cp.doFinal(encryptBytes);
        return new String(decryptBytes);
    }
}
