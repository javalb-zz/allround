package com.allround.algorithm.rsa;

import static org.junit.Assert.*;

import com.allround.algorithm.des.Coder;
import org.junit.Before;
import org.junit.Test;


import java.util.Map;

/**
 *
 * @author lb
 * @version 1.0
 * @since 1.0
 */
public class RSACoderTest {
    private String publicKey;
    private String privateKey;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> keyMap = RSACoder.initKey();

        publicKey = RSACoder.getPublicKey(keyMap);
        privateKey = RSACoder.getPrivateKey(keyMap);
        System.err.println("公钥: \n\r" + publicKey);
        System.err.println("私钥： \n\r" + privateKey);
    }

    @Test
    public void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String inputStr = "小明回家吃饭";
        byte[] data = inputStr.getBytes();

        byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);

        byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData,
                privateKey);
        System.out.println(Coder.decryptBASE64(new String(encodedData)));
        String outputStr = new String(decodedData,"gbk");
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        assertEquals(inputStr, outputStr);

    }

    @Test
    public void test1() throws Exception {
        System.out.println("公钥加密");
        String inputStr = "小明回家吃饭";
        byte[] data = inputStr.getBytes();
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzmUcdpdAWVYOUzAbFdj7IAWf0FXekXcHrikU/wFBtJJTVykC8PGLHNYaRep6uWdtFwpsqDlxRvs3wEM/uMERDQ8MGBdiYhn1Y6VcafvnrEfUjx6cwPAURBUR8gC15wWRU7OosgCcQEDjVNEy28+VnbHxYAcx5gZvqFxIJvMJOrQIDAQAB";
        byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);



        String outputStr = new String(encodedData);
        System.out.println(Coder.decryptBASE64(outputStr));

    }

    @Test
    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();

        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);

        byte[] decodedData = RSACoder
                .decryptByPublicKey(encodedData, publicKey);

        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        assertEquals(inputStr, outputStr);

        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = RSACoder.sign(encodedData, privateKey);
        System.err.println("签名:\r" + sign);

        // 验证签名
        boolean status = RSACoder.verify(encodedData, publicKey, sign);
        System.err.println("状态:\r" + status);
        assertTrue(status);

    }

}
