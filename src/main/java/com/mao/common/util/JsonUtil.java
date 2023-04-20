package com.mao.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Jackson工具类
 * @author myseital
 * @date 2022/11/24
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper;

    /**
     * 时间默认格式
     */
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final Set<JsonReadFeature> JSON_READ_FEATURES_ENABLED = Sets.newHashSet(
            //允许在JSON中使用Java注释
            JsonReadFeature.ALLOW_JAVA_COMMENTS,
            //允许 json 存在没用双引号括起来的 field
            JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES,
            //允许 json 存在使用单引号括起来的 field
            JsonReadFeature.ALLOW_SINGLE_QUOTES,
            //允许 json 存在没用引号括起来的 ascii 控制字符
            JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS,
            //允许 json number 类型的数存在前导 0 (例: 0001)
            JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS,
            //允许 json 存在 NaN, INF, -INF 作为 number 类型
            JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS,
            //允许 只有Key没有Value的情况
            JsonReadFeature.ALLOW_MISSING_VALUES,
            //允许数组json的结尾多逗号
            JsonReadFeature.ALLOW_TRAILING_COMMA
    );

    static {
        try {
            //初始化
            mapper = initMapper();
        } catch (Exception e) {
            log.error("jackson config error", e);
        }
    }

    public static ObjectMapper initMapper() {
        JsonMapper.Builder builder = JsonMapper.builder().enable(JSON_READ_FEATURES_ENABLED.toArray(new JsonReadFeature[0]));
        return initMapperConfig(builder.build());
    }

    public static ObjectMapper initMapperConfig(ObjectMapper objectMapper) {
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT));
        //配置序列化级别
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //配置JSON缩进支持
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        //允许单个数值当做数组处理
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        //禁止重复键, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        //禁止使用int代表Enum的order()來反序列化Enum, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        //有属性不能映射的时候不报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //对象为空时不抛异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //时间格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //允许未知字段
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        //序列化BigDecimal时之间输出原始数字还是科学计数, 默认false, 即是否以toPlainString()科学计数方式来输出
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        //识别Java8时间
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule);
        //识别Guava包的类
        objectMapper.registerModule(new GuavaModule());
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * JSON反序列化
     */
    public static <V> V toBean(String json, Class<V> type) {
        return toBean(json, (Type) type);
    }

    /**
     * JSON反序列化
     */
    public static <V> V toBean(String json, TypeReference<V> type) {
        return toBean(json, type.getType());
    }

    /**
     * JSON反序列化
     */
    public static <V> V toBean(String json, Type type) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("jackson to list error, data: {}, type: {}", json, type, e);
            throw new RuntimeException("jackson to bean error", e);
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> toList(String json, Class<V> type) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(json, collectionType);
        } catch (IOException e) {
            log.error("jackson to list error, data: {}", json, e);
            throw new RuntimeException("jackson to list error", e);
        }
    }

    /**
     * JSON反序列化（Map）
     */
    public static Map<String, Object> toMap(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
            return mapper.readValue(json, mapType);
        } catch (IOException e) {
            log.error("jackson to map error, data: {}", json, e);
            throw new RuntimeException("jackson to map error", e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String toJsonStr(List<V> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("jackson format json error, data: {}", list, e);
            throw new RuntimeException("jackson format json error", e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String toJsonStr(V v) {
        try {
            return mapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            log.error("jackson format json error, data: {}", v, e);
            throw new RuntimeException("jackson format json error", e);
        }
    }

    /**
     * 格式化Json(美化)
     * @return json
     */
    public static String prettyJson(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException e) {
            log.error("jackson format json error, data: {}", json, e);
            throw new RuntimeException("jackson format json error", e);
        }
    }

    /**
     * 判断字符串是否是json
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}