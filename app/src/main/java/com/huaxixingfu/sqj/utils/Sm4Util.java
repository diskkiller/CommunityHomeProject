package com.huaxixingfu.sqj.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.net.URI;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

public class Sm4Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";
    // PKCS5Padding  NoPadding 补位规则，PKCS5Padding缺位补0，NoPadding不补
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    public static final String KEY = "FA17113405706F714D7B973DB89F0B47";

    /**
     * ECB加密模式，无向量
     * @explain ECB
     * @param algorithmName
     * @param mode
     * @param key
     * @return
     * @throws Exception
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }
    /**
     * sm4加密
     * @explain 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
     * @param hexKey 16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        String cipherText = "";
        // 16进制字符串-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // String-->byte[]
        //当加密数据为16进制字符串时使用这行
//	        byte[] srcData = ByteUtils.fromHexString(hexKey);
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
        // byte[]-->hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }

    /**
     * 加密模式之Ecb
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);//声称Ecb暗号,通过第二个参数判断加密还是解密
        return cipher.doFinal(data);
    }

    //解密****************************************
    /**
     * sm4解密
     * @explain 解密模式：采用ECB
     * @param hexKey 16进制密钥
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptEcb(String hexKey, String cipherText) throws Exception {
        // 用于接收解密后的字符串
        String decryptStr = "";
        // hexString-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // hexString-->byte[]
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        // byte[]-->String
        decryptStr = new String(srcData, ENCODING);
        return decryptStr;
    }

    /**
     * 解密
     * @explain
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     */
    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);//生成Ecb暗号,通过第二个参数判断加密还是解密
        return cipher.doFinal(cipherText);
    }
    public static boolean verifyEcb(String hexKey, String cipherText, String paramStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // hexString-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // 将16进制字符串转换成数组
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] decryptData = decrypt_Ecb_Padding(keyData, cipherData);
        // 将原字符串转换成byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 判断2个数组是否一致
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }

    public static void main(String[] args) {
        try {
            /*System.out.println("开始****************************");
            String json = "123456";
            System.out.println("加密前："+json);
            //自定义的32位16进制秘钥
            String key = "FA17113405706F714D7B973DB89F0B47";
            String cipher = encryptEcb(key,json);//sm4加密
            System.out.println("加密后："+cipher);
            System.out.println("校验："+verifyEcb(key,cipher,json));//校验加密前后是否为同一数据

            //cipher = "jiami";
            json =decryptEcb(key,cipher);//解密
            System.out.println("解密后："+json);
            System.out.println("结束****************************");*/

            String server = "wss://jm.shequj.cn/api/im/ws/?token=c930f9c4-ecd5-482b-a15a-0b044e637773";
            URI uri;
            uri = URI.create(server);
            String host = uri.getHost();
            int port = uri.getPort();
            System.out.println("uri=========："+uri.toString());
            System.out.println("host=========："+host);
            System.out.println("port=========："+port);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}



