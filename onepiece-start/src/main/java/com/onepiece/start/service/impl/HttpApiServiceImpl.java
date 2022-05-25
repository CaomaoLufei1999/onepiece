package com.onepiece.start.service.impl;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @描述 用于发送Http请求的接口实现类
 * @作者 天天发呆的程序员
 * @创建时间 2022-05-25
 */
@Service
public class HttpApiServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(HttpApiServiceImpl.class);

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig requestConfig;

    // 编码格式: 发送编码格式统一用UTF-8
    private static final String ENCODING = "UTF-8";

    public String doGet(String url, Map<String, String> headers, Map<String, String> params) {
        // 初始化Http客户端,创建访问的地址
        HttpGet httpGet = getUrlParam(url, params);
        httpGet.setConfig(requestConfig);
        packageHeader(headers, httpGet);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            logger.info("响应状态为: {}", response.getStatusLine());
            if (responseEntity != null) {
                logger.info("响应内容长度为: {}", responseEntity.getContentLength());
                logger.info("响应内容为: {}", EntityUtils.toString(responseEntity));
            }
            assert responseEntity != null;
            return EntityUtils.toString(responseEntity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            // 回收response到连接池
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String doGet(String url, Map<String, String> params) {
        return doGet(url, null, params);
    }

    public String doGet(String url) {
        return doGet(url, null, null);
    }

    public String doPost(String url, Map<String, String> headers, Map<String, String> params, Map<String, String> UrlParams) {
        // 初始化Http客户端，创建访问的地址
        HttpPost httpPost = postUrlParam(url, UrlParams);
        httpPost.setConfig(requestConfig);

        packageHeader(headers, httpPost);
        packageParam(headers, httpPost);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            // 回收response到连接池
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String doPost(String url, Map<String, String> headers, Map<String, String> urlParams) {
        return doPost(url, headers, null, urlParams);
    }

    public String doPost(String url, Map<String, String> urlParams) {
        return doPost(url, null, null, urlParams);
    }

    public String doPost(String url) {
        return doPost(url, new HashMap<>());
    }

    public String doPost(String url, Map<String, String> headers, Map<String, String> params, String json) {
        // 初始化Http客户端，创建访问的地址
        HttpPost httpPost = postUrlParam(url, params);
        httpPost.setConfig(requestConfig);
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/json;charset=utf8");
        packageHeader(headers, httpPost);
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        try {
            httpPost.setEntity(new StringEntity(json));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 回收response到连接池
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String doPost(String url, String json) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=utf8");
        return doPost(url, headers, null, json);
    }

    /**
     * 获取get请求url参数
     *
     * @param url    请求链接
     * @param params 参数集合
     * @return
     */
    private HttpGet getUrlParam(String url, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            // 创建Get请求
            return new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException("url语法错误！");
        }
    }

    /**
     * 获取post请求url参数
     *
     * @param url    请求链接
     * @param params 参数集合
     * @return
     */
    private HttpPost postUrlParam(String url, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            // 创建Get请求
            return new HttpPost(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException("url语法错误！");
        }
    }

    /**
     * Description: 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    private void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    private void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置到请求的http对象中
            try {
                httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("不支持的地址格式！");
            }
        }
    }
}
