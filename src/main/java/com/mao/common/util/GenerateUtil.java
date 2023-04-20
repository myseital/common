package com.mao.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author myseital
 * @date 2022/9/5
 */
public class GenerateUtil {

    //长度15
    private static final SimpleDateFormat _FORMAT = new SimpleDateFormat("yyMMddHHmmss");
    private static final char[] NUMBERS_AND_LETTERS = ("0123456789QWERTYUIOPASDFGHJKLZXCVBNM").toCharArray();

    public static synchronized String generateSn(String prefix) {
        return generateSn(prefix, 5);
    }

    public static synchronized String generateSn(String prefix, int randomStrLen) {
        StringBuilder builder = new StringBuilder();
        return builder.append(prefix).append(_FORMAT.format(new Date()) + createRandomString(randomStrLen)).toString();
    }


    /**
     * 获取指定位数的随机字符串
     *
     * @param length 位数
     * @return 随机字符串
     */
    public synchronized static String createRandomString(int length) {
        if (length < 1) {
            return "";
        }
        int nextIntMax = NUMBERS_AND_LETTERS.length - 1;
        Random random = new Random();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = NUMBERS_AND_LETTERS[random.nextInt(nextIntMax)];
        }
        return new String(randBuffer);
    }


}
