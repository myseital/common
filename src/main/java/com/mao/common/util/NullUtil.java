
package com.mao.common.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Optional;

public class NullUtil {

    private static final String EMPTY_STRING = "";

    /**
     * 字符串null -> ""
     */
    public static String ofNullable(String str) {
        return Optional.ofNullable(str).orElse(EMPTY_STRING);
    }

    public static BigDecimal ofNullable(BigDecimal bigDecimal) {
        return Optional.ofNullable(bigDecimal).orElse(BigDecimal.ZERO);
    }

    public static Integer ofNullable(Integer a) {
        return Optional.ofNullable(a).orElse(0);
    }

    public static Long ofNullable(Long a) {
        return Optional.ofNullable(a).orElse(0L);
    }

    public static Double ofNullable(Double a) {
        return Optional.ofNullable(a).orElse(0.00);
    }
}