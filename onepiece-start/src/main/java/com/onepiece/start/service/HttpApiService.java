package com.onepiece.start.service;

import java.util.Map;

/**
 * @描述 用于发送Http请求的接口：封装了HttpClient组件
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-25
 */
public interface HttpApiService {

    /**
     * 发送Get请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  请求参数
     * @return
     */
    String doGet(String url, Map<String, String> headers, Map<String, String> params);

    /**
     * Get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    String doGet(String url, Map<String, String> params);

    /**
     * Get请求
     *
     * @param url 请求地址
     * @return
     */
    String doGet(String url);

    /**
     * Post请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  请求参数
     * @param params  url请求参数
     * @return
     */
    String doPost(String url, Map<String, String> headers, Map<String, String> params, Map<String, String> UrlParams);

    /**
     * Post请求
     *
     * @param url       请求地址
     * @param headers   请求头
     * @param urlParams url请求参数
     * @return
     */
    String doPost(String url, Map<String, String> headers, Map<String, String> urlParams);

    /**
     * Post请求
     *
     * @param url       请求地址
     * @param urlParams url请求参数
     * @return
     */
    String doPost(String url, Map<String, String> urlParams);

    /**
     * Post请求
     *
     * @param url 请求地址
     * @return
     */
    String doPost(String url);

    /**
     * Post请求
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  url请求参数
     * @param json    json请求体
     * @return
     */
    String doPost(String url, Map<String, String> headers, Map<String, String> params, String json);

    /**
     * 简单json请求
     *
     * @param url  请求地址
     * @param json json请求体
     * @return
     */
    String doPost(String url, String json);
}
