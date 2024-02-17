package com.websocket.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.websocket.entity.CustomizedWebSocketClient;
import com.websocket.entity.utills;
import com.websocket.req.CustomizedWebSocketClientTest2Bean;
import com.websocket.services.CustomizedWebSocketClientServices;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/15 14:12
 */
@Service
public class CustomizedWebSocketClientServicesImpl implements CustomizedWebSocketClientServices {


    @Override
    public void test() throws Exception {
        /**
         * wss://imio.jd.com/websocket/?appId=im.customer&clientType=comet&_wid_=84dd7bf8-dc17-10c7-d6b1-65feb0b19d01&aid=yPVqsZ1K&pin=%E6%B8%B8%E5%AE%A2-1700386701415
         */
//游客叮咚
        String customAid = utills.getCustomAid();
//  {"code":"0","timeStamp":1702538756601,"blocked":0,"pin":"游客-1702538756591","time":"2023-12-14 15:25:56","aid":"hDkRKaJM","msg":"请求成功","subCode":"0"}
        JSONObject js = JSONObject.parseObject(customAid);
        if (js == null || "".equals(js)) {
            return;
        }
        String pin = js.getString("pin");
        String aid = js.getString("aid");
        String uuid = UUID.randomUUID().toString();
        //
        //wss://imio.jd.com/websocket/?appId=im.customer&clientType=comet&_wid_=84dd7bf8-dc17-10c7-d6b1-65feb0b19d01&aid=yPVqsZ1K&pin=%E6%B8%B8%E5%AE%A2-1700386701415
        String dduri = "wss://imio.jd.com/websocket/?appId=im.customer&clientType=comet" + "&_wid_=" + uuid + "&aid=" + aid + "&pin=" + URLEncoder.encode(pin);
        //当前时间锉
        long l = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(l);
        //心跳
        //{"from":{"app":"im.customer","pin":"游客-1690801316137","clientType":"comet"},"datetime":"2023-07-31 19:01:56","ver":"4.2","aid":"LHHhoWlc",
        // "type":"client_heartbeat","to":{"app":"im.customer"},"timestamp":1690801316407}
        //String 心跳="{\"from\":{\"app\":\"im.customer\",\"pin\":\""+pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"aid\":\""+aid+"\",\"type\":\"client_heartbeat\",\"to\":{\"app\":\"im.customer\"},\"timestamp\":"+l+"}";
        CustomizedWebSocketClient customizedWebSocketClient = new CustomizedWebSocketClient(new URI(dduri), js);
        customizedWebSocketClient.connect();
    }


    /**
     * 从保存的 对象中取出
     *
     * @throws Exception
     */
    @Override
    public void test2(CustomizedWebSocketClientTest2Bean customizedWebSocketClientTest2Bean) throws Exception {
        /**
         *  String pin
         *  JSONObject js
         */
        if (customizedWebSocketClientTest2Bean.getPin() == null || customizedWebSocketClientTest2Bean.getPin().trim().equals("")) {
            return;
        }
        ConcurrentHashMap<String, CustomizedWebSocketClient> customizedWebSocketClientmap = CustomizedWebSocketClient.getCustomizedWebSocketClientmap();
        CustomizedWebSocketClient customizedWebSocketClient = customizedWebSocketClientmap.get(customizedWebSocketClientTest2Bean.getPin());
        //防止 有pin 但没有 jsonObjectMap
        ConcurrentHashMap<String, JSONObject> concurrentHashMap= null;
          if (customizedWebSocketClient != null) {
            concurrentHashMap = customizedWebSocketClient.getConcurrentHashMaps();
          }else {
              return;
          }
        //先判断是否所有 要发送 默认false
        if (!customizedWebSocketClientTest2Bean.getAllPin()) {
            //这是发送单个
            JSONObject js = concurrentHashMap.get(customizedWebSocketClientTest2Bean.getPin());
            //没有连接 没有json数据
            if (js == null) {
                return;
            }
            //开始连接
            String aid = js.getString("aid");
            String pin = customizedWebSocketClientTest2Bean.getPin();
            //map 中存在代表 心跳还在 不用连接
            for (int i = 0; i < customizedWebSocketClientTest2Bean.getCount(); i++) {
                //防止太快
                Thread.sleep(3000);
                //当前系统时间
                long l = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //当前时间格式化
                String format = simpleDateFormat.format(l);
            //    String 心跳 = "{\"from\":{\"app\":\"im.customer\",\"pin\":\"" + pin + "\",\"clientType\":\"comet\"},\"datetime\":\"" + format + "\",\"ver\":\"4.2\",\"aid\":\"" + aid + "\",\"type\":\"client_heartbeat\",\"to\":{\"app\":\"im.customer\"},\"timestamp\":" + l + "}";
                String 信息="{\"from\":{\"app\":\"im.customer\",\"pin\":\"" +pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"aid\":\""+aid+"\",\"type\":\"chat_message\",\"to\":{\"app\":\"im.waiter\"},\"timestamp\":"+l+",\"id\":\""+ UUID.randomUUID() +"\",\"body\":{\"chatinfo\":{\"entry\":\"jd_search\",\"venderId\":\"12353944\",\"proVer\":\"pc2.0\",\"pid\":\"10070180626593\",\"sid\":\"\",\"verification\":\"semantic\",\"behaviour\":\"142745318359734812\",\"distinguishPersonJimi\":2},\"uniformBizInfo\":{\"tenantId\":\"\",\"buId\":\"\",\"channelId\":\"\",\"ua\":\"\",\"channelTag\":\"\"},\"content\":\"https://item.jd.com/10052854283794.html\",\"render\":\"user\",\"type\":\"text\"}}";
                customizedWebSocketClient.send(信息);
                // customizedWebSocketClient.send(customizedWebSocketClientTest2Bean.getMessage());
            }
        } else {
            //发送所有
            // 使用 entrySet() 和 for-each 循环遍历 HashMap
            for (Map.Entry<String, JSONObject> entry : concurrentHashMap.entrySet()) {
                //开始连接
                JSONObject js = entry.getValue();
                String pin = js.getString("pin");
                String aid = js.getString("aid");
                //map 中存在代表 心跳还在 不用连接
                for (int i = 0; i < customizedWebSocketClientTest2Bean.getCount(); i++) {
                    //防止太快
                    Thread.sleep(3000);
                    //当前系统时间
                    long l = System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //当前时间格式化
                    String format = simpleDateFormat.format(l);
               //     String 心跳 = "{\"from\":{\"app\":\"im.customer\",\"pin\":\"" + pin + "\",\"clientType\":\"comet\"},\"datetime\":\"" + format + "\",\"ver\":\"4.2\",\"aid\":\"" + aid + "\",\"type\":\"client_heartbeat\",\"to\":{\"app\":\"im.customer\"},\"timestamp\":" + l + "}";
                    String 信息="{\"from\":{\"app\":\"im.customer\",\"pin\":\"" +pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"aid\":\""+aid+"\",\"type\":\"chat_message\",\"to\":{\"app\":\"im.waiter\"},\"timestamp\":"+l+",\"id\":\""+ UUID.randomUUID() +"\",\"body\":{\"chatinfo\":{\"entry\":\"jd_search\",\"venderId\":\"12353944\",\"proVer\":\"pc2.0\",\"pid\":\"10070180626593\",\"sid\":\"\",\"verification\":\"semantic\",\"behaviour\":\"142745318359734812\",\"distinguishPersonJimi\":2},\"uniformBizInfo\":{\"tenantId\":\"\",\"buId\":\"\",\"channelId\":\"\",\"ua\":\"\",\"channelTag\":\"\"},\"content\":\"https://item.jd.com/10052854283794.html\",\"render\":\"user\",\"type\":\"text\"}}";
                    customizedWebSocketClient.send(信息);

                }
            }
        }


    }


}


