package com.huaxixingfu.sqj.utils;
import android.util.Log;

import org.bouncycastle.jcajce.provider.symmetric.SM4;

public class Encryption {


    /**
     *  hexString转byte[]数组
     * @param str 字符串，原串为“112233”
     * @return 转换后的数据， 转换后为 0x11,0x22,0x33
     */
    public static byte[] HexStrToByteArray(String str) {
        int charIndex, value;
        int datalen = str.length() / 2;
        byte[] data = new byte[datalen];
        for (int i = 0; i < datalen; i++) {
            charIndex = i * 2;
            value = Integer.parseInt(str.substring(charIndex, charIndex + 2), 16);
            data[i] = (byte) value;
        }

        return data;
    }

    /**
     * byte[]数组转hexString
     * @param data 需转换的数组 0x11,0x22,0x33
     * @param offset 需转换开始位置
     * @param len 需转换长度
     * @return 转换后的字符传为“112233”
     */
    public static String ByteArrayToHexStr(byte[] data, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++)
            sb.append(String.format("%02X", data[i + offset] & 0xff));
        return sb.toString();
    }

    public static String ByteArrayToHexStr(byte[] bytes) {
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f'};
        // 一个字节对应两个16进制数，所以长度为字节数组乘2
        char[] resultCharArray = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            resultCharArray[index++] = hexDigits[b>>>4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

}



