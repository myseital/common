package com.mao.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author myseital
 * @date 2022/9/5
 */
@Slf4j
public class OKHttpUtil {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FORM = MediaType.get("application/x-www-form-urlencoded");
    private static final String ENCODING = "UTF-8";

    /**
     * okhttp客户端
     */
    private static final OkHttpClient okHttpClient;

    private static final int CONNECT_TIMEOUT = 120;
    private static final int WRITE_TIMEOUT = 120;
    private static final int READ_TIMEOUT = 200;
    private static final int MAX_IDLE_CONNECTION = 5;
    private static final long KEEP_ALIVE_DURATION = 5;

    static {
        // 支持https请求，绕过验证
        X509TrustManager manager = SSLSocketClientUtil.getX509TrustManager();
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                // 忽略校验
                .sslSocketFactory(SSLSocketClientUtil.getSocketFactory(manager), manager)
                // 忽略校验
                .hostnameVerifier(SSLSocketClientUtil.getHostnameVerifier())
                .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTION, KEEP_ALIVE_DURATION, TimeUnit.MINUTES))
                .build();
    }

    /**
     * 发送get请求，并返回结果字符串
     *
     * @param url 请求url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, null, null);
    }

    /**
     * 发送get请求，并返回结果字符串
     *
     * @param url           请求url
     * @param params        get请求参数
     * @param headersParams 请求头
     * @return 请求响应字符串
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headersParams) throws IOException {
        Request request = new Request.Builder()
                .url(getHttpUrl(url, params))
                .headers(setHeaders(headersParams))
                .build();

        return doHttpRequest(request);
    }

    /**
     *
     * @param url
     * @param params get请求参数 存在body
     * @param headersParams
     * @return
     * @throws IOException
     */
    public static String getWithBody(String url, RequestBody params, Map<String, String> headersParams) throws IOException {
        //构造请求
        Request request = new Request.Builder()
                .url(url)
                .post(params)
                .headers(setHeaders(headersParams))
                .build();
        //发起请求
        return doHttpRequest(request);
    }

    /**
     * 发送get请求，并返回结果字符串
     *
     * @param url           请求url
     * @param params        get请求参数
     * @param headersParams 请求头
     * @param keyword 请求头关键信息
     * @return 请求响应字符串
     * @throws IOException
     */
    public static Map<String, String> getWithHeader(String url, Map<String, String> params,
                                                    Map<String, String> headersParams, String keyword)
            throws IOException {
        //构造请求
        Request request = new Request.Builder()
                .url(getHttpUrl(url, params))
                .headers(setHeaders(headersParams))
                .build();
        //发起请求
        return doHttpRequest(request, keyword);
    }

    /**
     * 携带json数据体，发送post请求，并返回结果字符串
     *
     * @param url           请求url
     * @param jsonParams    json数据体
     * @param headersParams 请求头
     * @return 请求响应字符串
     * @throws IOException
     */
    public static String postWithJson(String url, String jsonParams, Map<String, String> headersParams) throws IOException {
        //MediaType，设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonParams);
        //构造请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(setHeaders(headersParams))
                .build();

        //发起请求
        return doHttpRequest(request);
    }

    /**
     * 携带json数据体，发送post请求，并返回结果字符串
     *
     * @param url           请求url
     * @param jsonParams    json数据体
     * @param headersParams 请求头
     * @return 请求响应字符串
     * @throws IOException
     */
    public static String postWithJson(String url, Map<String, Object> jsonParams, Map<String, String> headersParams) throws IOException {
        //将map转成json字符串
        String jsonStr = JsonUtil.toJsonStr(jsonParams);
        //MediaType，设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);

        //构造请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(setHeaders(headersParams))
                .build();

        //发起请求
        return doHttpRequest(request);
    }


    /**
     * 携带json数据体，发送post请求，并返回结果字符串
     *
     * @param url           请求url
     * @param headersParams 请求头
     * @return 请求响应字符串
     * @throws IOException
     */
    public static String postWithForm(String url, FormBody formBody, Map<String, String> headersParams) throws IOException {
        //构造请求
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .headers(setHeaders(headersParams))
                .build();
        //发起请求
        return doHttpRequest(request);
    }

    /**
     * 携带json数据体，发送put请求，并返回结果字符串
     *
     * @param url           请求url
     * @param jsonParams    json数据体
     * @param headersParams 请求头
     * @return 请求响应字符串
     * @throws IOException
     */
    public static String putWithJson(String url, Map<String, Object> jsonParams, Map<String, String> headersParams) throws IOException {
        //将map转成json字符串
        String jsonStr = JsonUtil.toJsonStr(jsonParams);
        //MediaType，设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonStr);
        //构造请求
        Request request = new Request.Builder()
                .url(url)    //请求地址
                .put(requestBody)//请求方式: PUT
                .headers(setHeaders(headersParams)) //请求头
                .build();
        //发起请求
        return doHttpRequest(request);
    }

    public static String patch(String url, Map<String, String> params, Map<String, Object> body, Map<String, String> headersParams) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, JsonUtil.toJsonStr(body));
        //构造请求
        Request request = new Request.Builder()
                .url(getHttpUrl(url, params))
                .headers(setHeaders(headersParams))
                .patch(requestBody)
                .build();
        //发起请求
        return doHttpRequest(request);
    }

    public static String doHttpRequest(Request request) throws IOException {
        try (Response response = okHttpClient.newCall(request).execute()) {
            int code = response.code();
            String result = Objects.requireNonNull(response.body()).string();
            log.info("请求url:[{}], code[{}]", request.url(), code);
            if (!response.isSuccessful()) {
                throw new RuntimeException(result);
            }
            return result;
        }
    }

    private static Map<String, String> doHttpRequest(Request request, String keyword) throws IOException {
        try (Response response = okHttpClient.newCall(request).execute()) {
            Map<String, String> data = new HashMap<>();
            String result = Objects.requireNonNull(response.body()).string();
            if (!response.isSuccessful()) {
                throw new RuntimeException(result);
            }
            String keywordStr = response.headers().get(keyword);
            data.put("data", result);
            data.put(keyword, keywordStr);

            return data;
        }
    }

    /**
     * 携带json数据体，发送post请求，并返回指定数据类型的对象
     *
     * @param url           请求url
     * @param jsonParams    json数据体
     * @param headersParams 请求头
     * @param resultType    返回数据类型
     * @param <T>           返回数据类型
     * @return 请求响应
     * @throws IOException
     */
    public static <T> T postForObjectWithJson(String url, Map<String, Object> jsonParams,
                                              Map<String, String> headersParams, Class<T> resultType)
            throws IOException {
        // 发送请求获取响应字符串
        String resultStr = postWithJson(url, jsonParams, headersParams);
        // 字符串转化为对象
        return JsonUtil.toBean(resultStr, resultType);
    }

    /**
     * 携带json数据体，发送post请求，并返回指定数据类型的对象
     *
     * @param url           请求url
     * @param jsonParams    json数据体
     * @param headersParams 请求头
     * @param resultType    返回数据类型
     * @param <T>           返回数据类型
     * @return 请求响应
     * @throws IOException
     */
    public static <T> T postForObjectWithJson(String url, Map<String, Object> jsonParams,
                                              Map<String, String> headersParams, TypeReference<T> resultType)
            throws IOException {
        // 发送请求获取响应字符串
        String resultStr = postWithJson(url, jsonParams, headersParams);
        // 字符串转化为对象
        return JsonUtil.toBean(resultStr, resultType);
    }

    /**
     * 发送get请求，并返回指定数据类型的对象
     *
     * @param url           请求url
     * @param params        json数据体
     * @param headersParams 请求头
     * @param resultType    返回数据类型
     * @param <T>           返回数据类型
     * @return 请求响应
     * @throws IOException
     */
    public static <T> T getForObject(String url, Map<String, String> params, Map<String, String> headersParams, Class<T> resultType) throws IOException {
        // 发送请求获取响应字符串
        String resultStr = get(url, params, headersParams);
        // 字符串转化为对象
        return JsonUtil.toBean(resultStr, resultType);
    }

    /**
     * @param url
     * @param headersParams
     * @param resultType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T postForObject(String url, FormBody formBody, Map<String, String> headersParams, Class<T> resultType) throws IOException {
        // 发送请求获取响应字符串
        String resultStr = postWithForm(url, formBody, headersParams);
        // 字符串转化为对象
        return JsonUtil.toBean(resultStr, resultType);
    }

    public static <T> T postForObject(String url, FormBody formBody, Map<String, String> headersParams, TypeReference<T> resultType) throws IOException {
        // 发送请求获取响应字符串
        String resultStr = postWithForm(url, formBody, headersParams);
        // 字符串转化为对象
        return JsonUtil.toBean(resultStr, resultType);
    }

    /**
     * 发送get请求，并返回指定数据类型的对象
     *
     * @param url           请求url
     * @param params        json数据体
     * @param headersParams 请求头
     * @param resultType    返回数据类型
     * @param <T>           返回数据类型
     * @return 请求响应
     * @throws IOException
     */
    public static <T> T getForObject(String url, Map<String, String> params, Map<String, String> headersParams, TypeReference<T> resultType) throws IOException {
        // 发送请求获取响应字符串
        String resultStr = get(url, params, headersParams);
        return JsonUtil.toBean(resultStr, resultType);
    }

    /**
     * 携带json数据体，发送put请求，并返回指定数据类型的对象
     *
     * @param url           请求url
     * @param jsonParams    json数据体
     * @param headersParams 请求头
     * @param resultType    返回数据类型
     * @param <T>           返回数据类型
     * @return 请求响应
     * @throws IOException
     */
    public static <T> T putForObjectWithJson(String url, Map<String, Object> jsonParams, Map<String, String> headersParams, Class<T> resultType) throws IOException {
        // 发送请求获取响应字符串
        String resultStr = putWithJson(url, jsonParams, headersParams);
        // 字符串转化为对象
        return JsonUtil.toBean(resultStr, resultType);
    }

    public static String putForObjectWithJson(String url, Map<String, Object> jsonParams, Map<String, String> headersParams) throws IOException {
        // 发送请求获取响应字符串
        return putWithJson(url, jsonParams, headersParams);
    }

    private static Headers setHeaders(Map<String, String> headersParams) {
        Headers headers;
        okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
        if (headersParams != null) {
            for (Map.Entry<String, String> entry : headersParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                headersBuilder.add(key, value);
            }
        }
        headers = headersBuilder.build();
        return headers;
    }

    /**
     * 通过参数返回FormBody对象
     *
     * @param params 用于构建FormBody的参数
     * @return FormBody
     */
    private static FormBody getFormBody(Map<String, String> params) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder(Charset.forName(ENCODING));
        // 封装请求参数
        if (MapUtil.isNotEmpty(params)) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        return formBodyBuilder.build();
    }

    /**
     * Set URI
     *
     * @param url    接口对应url
     * @param params 接口url需要的参数
     */
    private static HttpUrl getHttpUrl(String url, Map<String, String> params) {
        HttpUrl.Builder newBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if (MapUtil.isNotEmpty(params)) {
            // Set params
            for (Map.Entry<String, String> param : params.entrySet()) {
                newBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }

        return newBuilder.build();
    }
}
