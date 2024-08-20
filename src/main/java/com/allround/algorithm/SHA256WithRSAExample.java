package com.allround.algorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.SignatureException;


public class SHA256WithRSAExample {

    public  void test() {
        try {
            // 生成密钥对
            KeyPair keyPair = generateKeyPair();
            // 创建签名
            byte[] signature = createSignature("Hello, World!".getBytes(), keyPair.getPrivate());
            // 验证签名
            boolean isValid = verifySignature("Hello, World!".getBytes(), signature, keyPair.getPublic());
            System.out.println("Signature is valid: " + isValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // 设置密钥长度
        return keyGen.generateKeyPair();
    }

    private static byte[] createSignature(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    private static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signatureObj = Signature.getInstance("SHA256withRSA");
        signatureObj.initVerify(publicKey);
        signatureObj.update(data);
        return signatureObj.verify(signature);
    }
}