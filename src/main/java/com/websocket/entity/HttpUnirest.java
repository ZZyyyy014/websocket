package com.websocket.entity;


import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Map;

public class HttpUnirest {

    /**
     * 发送POST请求
     *
     * @param url     链接
     * @param body    参数
     * @param headers 协议头
     * @return 返回内容
     */
    public static HttpResponse<String> doPost(String url, Map<String, Object> body, Map<String, String> headers) {
        HttpResponse<String> request = Unirest.post(url).headers(headers).queryString(body).asString();
        return request;
    }




    /**
     * 发送GET请求
     *
     * @param url     链接
     * @param body    参数
     * @param headers 协议头
     * @return 返回内容
     */
    public static HttpResponse<String> doGet(String url, Map<String, Object> body, Map<String, String> headers) {
        HttpResponse<String> request = Unirest.post(url).headers(headers).queryString(body).asString();
        return request;
    }


}
