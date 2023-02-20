package com.example.mycomic.utils;


import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class CryptUtil {
    private static String key = "babf73a94156d1d931e87f87";
    public static String decode(String str){
        byte[] bArr=base64decode(str);
        try {
            byte[] result = ees3DecodeECB(key.getBytes(StandardCharsets.UTF_8), bArr);
            return new String(result);
        }catch (Exception e){
            return null;
        }
    }
    public static byte[] ees3DecodeECB(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKey generateSecret = SecretKeyFactory.getInstance("desede").generateSecret(new DESedeKeySpec(bArr));
        Cipher instance = Cipher.getInstance("desede/ECB/PKCS5Padding");
        instance.init(2, generateSecret);
        return instance.doFinal(bArr2);
    }

    public static String ees3EncodeECB2Str(byte[] bArr, byte[] bArr2) {
        try {
            return base64encode(des3EncodeECB(bArr, bArr2));
        } catch (Exception unused) {
            return "";
        }
    }

    public static byte[] des3EncodeECB(byte[] bArr, byte[] bArr2) throws Exception {
        SecretKey generateSecret = SecretKeyFactory.getInstance("desede").generateSecret(new DESedeKeySpec(bArr));
        Cipher instance = Cipher.getInstance("desede/ECB/PKCS5Padding");
        instance.init(1, generateSecret);
        return instance.doFinal(bArr2);
    }

    public static String base64encode(byte[] barr) {
        byte[] data= Base64.encode(barr,0);
        return new String(data);
    }
    public static byte[] base64decode(String content) {
        return Base64.decode(content,0);
    }

}
