package com.allround.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Example {

    public static void main(String[] args) {
        String input = "123456";
        String md5Hash = getMD5Hash(input);
        System.out.println("Original Input: " + input);
        System.out.println("MD5 Hash: " + md5Hash);
    }

    public static String getMD5Hash(String input) {
        try {
            // 获取 MessageDigest 实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 计算输入字符串的 MD5 散列值
            byte[] messageDigest = md.digest(input.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
