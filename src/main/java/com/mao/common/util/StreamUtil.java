package com.mao.common.util;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author myseital
 * @date 2022/8/25
 */
@UtilityClass
public class StreamUtil {

    /**
     * 过滤数据
     * @param sourceList 源数据
     * @param predicate 过滤条件
     */
    public static <T, R> List<T> filter(List<T> sourceList, Predicate<? super T> predicate) {
        return Optional.ofNullable(sourceList).orElseGet(ArrayList::new).stream()
                .filter(predicate).collect(Collectors.toList());
    }

    /**
     * 转换成Map对象
     *
     * @param list 源数据
     * @param function 转换方式
     */
    public static <T, R> Map<R, T> transformToMap(List<T> list, Function<T, R> function) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .filter(Objects::nonNull)
                .filter(DistinctFilter.distinctByKey(function))
                .collect(Collectors.toMap(function, e -> e));
    }

    /**
     * 转换成Map对象
     *
     * @param list 源数据
     * @param function 转换方式
     * @param filter 过滤方式
     */
    public static <T, R> Map<R, T> transformToMap(List<T> list, Function<T, R> function, Predicate<T> filter) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .filter(Objects::nonNull)
                .filter(filter)
                .filter(DistinctFilter.distinctByKey(function))
                .collect(Collectors.toMap(function, e -> e));
    }


    /**
     * 获取对象中某个值
     *
     * @param list 源数据
     * @param function 获取方式
     */
    public static <T, R> List<R> collectKeyList(List<T> list, Function<T, R> function) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .map(function)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取对象中某个值,会去重
     *
     * @param list 源数据
     * @param function 获取方式
     */
    public static <T, R> List<R> collectDistinctKeyList(List<T> list, Function<T, R> function) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .map(function)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 分组
     * @param list 源数据
     * @param function 分组方式
     */
    public static <T, R> Map<R, List<T>> group(List<T> list, Function<T, R> function) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .collect(Collectors.groupingBy(function));
    }

    /**
     * 分组
     * @param list 源数据
     * @param function 分组方式
     * @param mapping 映射方式
     */
    public static <T, R, V> Map<R, List<V>> group(List<T> list, Function<T, R> function, Function<T, V> mapping) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .collect(Collectors.groupingBy(function, Collectors.mapping(mapping, Collectors.toList())));
    }

    /**
     * 分组
     * @param list 源数据
     * @param function 分组方式
     * @param mapping 映射方式
     */
    public static <T, R, V> Map<R, Set<V>> groupSet(List<T> list, Function<T, R> function, Function<T, V> mapping) {
        return Optional.ofNullable(list).orElseGet(ArrayList::new).stream()
                .collect(Collectors.groupingBy(function, Collectors.mapping(mapping, Collectors.toSet())));
    }
}