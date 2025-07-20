package com.xianmao.common.core.utils;

public class HexUtil {

    public static String byteArrayToHexString(byte[] data) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            hexString.append(String.format("%02X ", b));
        }
        return hexString.toString().trim();
    }

    public static byte[] hexStringToByteArray(String s) {
        s = s.replaceAll("\\s", ""); // 去除所有空白字符
        int len = s.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string length");
        }

        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将16进制字符串转换为ASCII字符串
     * @param hexString
     * @return
     */
    public static String hexStringToAscii(String hexString) {
        byte[] bytes = hexStringToByteArray(hexString);
        return new String(bytes);
    }

    /**
     * 将16进制字符串转换为十进制整型
     * @param hexString
     * @return
     */
    public static long hexStringToDecimal(String hexString) {
        hexString = hexString.replaceAll("\\s", ""); // 去除所有空白字符
        long decimalValue = 0;

        for (int i = 0; i < hexString.length(); i++) {
            char c = hexString.charAt(i);
            int value = Character.digit(c, 16); // 获取当前字符的10进制值
            decimalValue = decimalValue * 16 + value;
        }

        return decimalValue;
    }


    /**
     * 将16进制字符串转换为浮点数
     * @param hexString
     * @return
     */
    public static float hexStringToFloat(String hexString) {
        hexString = hexString.replaceAll("\\s", ""); // 去除所有空白字符
        byte[] bytes = hexStringToByteArray(hexString);
        return bytesToFloat(bytes);
    }

    /**
     * 格式化16进制字符串
     * @param hexString
     * @return
     */
    public static String formatHexString(String hexString) {
        StringBuilder formattedString = new StringBuilder();
        for (int i = 0; i < hexString.length(); i += 2) {
            if (i > 0) {
                formattedString.append(" ");
            }
            formattedString.append(hexString.substring(i, i + 2));
        }

        return formattedString.toString();
    }

    private static float bytesToFloat(byte[] bytes) {
        int intBits = 0;
        for (int i = 0; i < 4; i++) {
            intBits |= (bytes[i] & 0xFF) << (24 - 8 * i);
        }
        return Float.intBitsToFloat(intBits);
    }
}
