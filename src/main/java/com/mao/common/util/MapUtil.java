package com.mao.common.util;

import java.util.Map;

/**
 * map工具类
 *
 * @author myseital
 * @date 22023/1/29
 */
public class MapUtil {

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }
}