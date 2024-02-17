package com.websocket.entity;


import kong.unirest.HttpResponse;

import java.util.HashMap;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/14 15:18
 */
public class utills {

    /**
     * 获取游客咚咚aid
     *
     * @return
     */
    public static String getCustomAid() {
        String url = "https://api.m.jd.com/client.action";
        HashMap<String, Object> param = new HashMap<>();
        param.put("functionId", "getAidInfo");
        param.put("body", "{\"aidClientType\":\"comet\",\"aidClientVersion\":\"comet -v1.0.0\",\"appId\":\"im.customer\",\"os\":\"comet\",\"entry\":\"jd_web_jdsyqyfw\",\"reqSrc\":\"s_comet\"}");
        param.put("client", "wh5");
        param.put("clientVersion", "1.0.0");
        param.put("loginType", "3");
        //functionId=getAidInfo&body={%22aidClientType%22:%22comet%22,%22aidClientVersion%22:%22comet%20-v1.0.0%22,%22appId%22:%22im.customer%22,%22os%22:%22comet%22,%22entry%22:%22jd_web_jdsyqyfw%22,%22reqSrc%22:%22s_comet%22}&client=wh5&clientVersion=1.0.0&loginType=3
        HashMap<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Referer", "https://jdcs.jd.com/jdchat/custom.action?entry=jd_web_jdsyqyfw&venderId=1");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42");
        HttpResponse<String> stringHttpResponse = HttpUnirest.doGet(url, param, header);
        return stringHttpResponse.getBody();
    }




    /**
     * 获取京东咚咚aid
     *
     * @return
     */
    public static String getJdUserInfoAid(String cookie) {
        String url = "https://api.m.jd.com/client.action";
        HashMap<String, Object> param = new HashMap<>();
        param.put("functionId", "getAidInfo");
//        param.put("body", "{\"aidClientType\":\"comet\",\"aidClientVersion\":\"comet -v1.0.0\",\"appId\":\"im.customer\",\"os\":\"comet\",\"entry\":\"jd_web_wydpylxkf\",\"reqSrc\":\"s_comet\"}");//PC端
        param.put("body", "{\"userPin\":\"\",\"shopId\":\"\",\"venderId\":\"\",\"pid\":\"\",\"token\":\"\",\"aidClientType\":\"m\",\"aidClientVersion\":\"m -v1.0.0\",\"appId\":\"im.customer\",\"os\":\"m\",\"entry\":\"m_shop\",\"reqSrc\":\"s_h5\"}");//PC端
        param.put("client", "wh5");
        param.put("clientVersion", "1.0.0");
//        param.put("loginType", "3");
        //functionId=getAidInfo&body={%22aidClientType%22:%22comet%22,%22aidClientVersion%22:%22comet%20-v1.0.0%22,%22appId%22:%22im.customer%22,%22os%22:%22comet%22,%22entry%22:%22jd_web_jdsyqyfw%22,%22reqSrc%22:%22s_comet%22}&client=wh5&clientVersion=1.0.0&loginType=3
        HashMap<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Referer", "https://jdcs.m.jd.com/chat/index.action?venderId=12800579&entry=m_shop&sceneval=2");
        header.put("Cookie", cookie);
        header.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36");
//        String res = HttpUtility.doPost(url, param, header);
        String res = HttpUnirest.doPost(url, param, header).getBody();
        return res;
    }









}
