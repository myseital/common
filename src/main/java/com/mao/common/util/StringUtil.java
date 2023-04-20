package com.mao.common.util;

import com.google.common.base.Joiner;

import java.util.List;
import java.util.Random;

/**
 * @author myseital
 * @date 2023/3/1
 */
public class StringUtil extends CharSequenceUtil {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final String EMPTY = "";
    private static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';

    public static String getRandomString(int length) {
        String base = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for (int i = 0; i < length; ++i) {
                if (!isBlankChar(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 字符串连接
     *
     * @param strList   原集合
     * @param separator 分隔符
     * @return
     */
    public static String join(List<String> strList, String separator) {
        return Joiner.on(separator).join(strList);
    }

    public static String one(String one, String two) {
        if (isNotEmpty(one)) {
            return one;
        }
        if (isNotEmpty(two)) {
            return two;
        }
        return null;
    }
}
