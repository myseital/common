package com.mao.common.util;

/**
 * @author myseital
 * @date 2023/4/20
 */
public class CharSequenceUtil {

    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234 || c == 0;
    }
}
