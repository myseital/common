package com.mao.common.util;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * BigDecimal工具类
 *
 * @author myseital
 * @date 2022/10/28
 */
public class BigDecimalUtil {

    /**
     * 保留几位小数
     */
    private static final Integer BIG_DECIMAL_SCALE = 4;
    private static final Integer ZERO_INT = 0;


    /**
     * 数值相加
     *
     * @param number1      数值1
     * @param number2      数值2
     * @param newScale     保留小数位
     * @param roundingMode 小数的保留模式 BigDecimal.ROUND_UP java.math.BigDecimal.ROUND_HALF_UP
     * @return 相加后的数值
     */
    public static BigDecimal add(BigDecimal number1, BigDecimal number2, int newScale, int roundingMode) {
        return setScale(number1.add(number2), newScale, roundingMode);
    }

    /**
     * 数值相加 - 带默认截取
     *
     * @param number1
     * @param number2
     * @return
     */
    public static BigDecimal add(BigDecimal number1, BigDecimal number2) {
        return setScale(number1.add(number2), BIG_DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 数值相减
     *
     * @param number1      数值1
     * @param number2      数值2
     * @param newScale     保留小数位
     * @param roundingMode 小数的保留模式 BigDecimal.ROUND_UP java.math.BigDecimal.ROUND_HALF_UP ....
     * @return 结果
     */
    public static BigDecimal subtract(BigDecimal number1, BigDecimal number2, int newScale, int roundingMode) {
        return setScale(number1.subtract(number2), newScale, roundingMode);
    }


    /**
     * 数值相减 - 待默认截取
     *
     * @param number1
     * @param number2
     * @return
     */
    public static BigDecimal subtract(BigDecimal number1, BigDecimal number2) {
        return subtract(number1, number2, BIG_DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 数值相乘
     *
     * @param number1      数值1
     * @param number2      数值2
     * @param newScale     保留小数位
     * @param roundingMode 小数的保留模式 BigDecimal.ROUND_UP java.math.BigDecimal.ROUND_HALF_UP ....
     * @return 结果
     */
    public static BigDecimal multiply(BigDecimal number1, BigDecimal number2, int newScale, int roundingMode) {
        return setScale(number1.multiply(number2), newScale, roundingMode);
    }

    /**
     * 数值相乘
     *
     * @param number1 数值1
     * @param number2 数值2
     * @return 结果
     */
    public static BigDecimal multiply(BigDecimal number1, BigDecimal number2) {
        return setScale(number1.multiply(number2), BIG_DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 数值相除
     *
     * @param number1      数值1
     * @param number2      数值2
     * @param newScale     保留小数位
     * @param roundingMode 小数的保留模式 BigDecimal.ROUND_UP java.math.BigDecimal.ROUND_HALF_UP ....
     * @return 结果
     */
    public static BigDecimal divide(BigDecimal number1, BigDecimal number2, int newScale, int roundingMode) {
        return number1.divide(number2, newScale, roundingMode);

    }

    public static BigDecimal divide(BigDecimal number1, BigDecimal number2) {
        return divide(number1, number2, BIG_DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置小数位
     *
     * @param number
     * @param newScale
     * @param roundingMode
     * @return
     */
    public static BigDecimal setScale(BigDecimal number, int newScale, int roundingMode) {
        return number.setScale(newScale, roundingMode);
    }

    public static BigDecimal setScaleWithDefaul(BigDecimal number) {
        return number.setScale(BIG_DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 数值转换
     *
     * @param number       数字
     * @param newScale
     * @param roundingMode
     * @return
     */
    public static String setScale(String number, int newScale, int roundingMode) {
        if (number == null || number.length() == 0) {
            number = ZERO_INT.toString();
        }
        return new BigDecimal(number).setScale(newScale, roundingMode).toPlainString();
    }

    /**
     * 比较
     *
     * @param num1
     * @param num2
     * @return
     */
    public static int compare(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2);
    }

    /**
     * 判断是否为0
     *
     * @param var
     * @return
     */
    public static boolean isZero(BigDecimal var) {
        return !Objects.isNull(var) && var.compareTo(BigDecimal.ZERO) == 0;
    }


}
