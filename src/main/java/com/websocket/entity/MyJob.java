package com.websocket.entity;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/14 17:46
 */
public class MyJob extends QuartzJobBean {


    private CustomizedWebSocketClient customizedWebSocketClient;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jdMap = context.getJobDetail().getJobDataMap();  //获得传递过来的参数
        String aid= jdMap.get("aid").toString();
        String pin= jdMap.get("pin").toString();
        customizedWebSocketClient = (CustomizedWebSocketClient) jdMap.get("customizedWebSocketClient");

        //当前系统时间
        long l = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //当前时间格式化
        String format = simpleDateFormat.format(l);

       //{
        //                "from": {"app": "im.customer", "pin": customerInfo.pin, "clientType": "comet"},
        //                "datetime": getDate(),
        //                "ver": "4.2",
        //                "aid": customerInfo.aid,
        //                "type": "chat_message",
        //                "to": {"app": "im.waiter"},
        //                "timestamp": getTimestamp(),
        //                "id": uuid2(),
        //                "body": {
        //                    "chatinfo": {
        //                        "entry": "jd_search",
        //                        "venderId": "12353944",
        //                        "proVer": "pc2.0",
        //                        "pid": "10070180626593"),
        //                        "sid": "",
        //                        "verification": "semantic",
        //                        "behaviour": "",
        //                        "distinguishPersonJimi": 2
        //                    },
        //                    "uniformBizInfo": {"tenantId": "", "buId": "", "channelId": "", "ua": "", "channelTag": ""},
        //                    "content": "发送测试",
        //                    "render": "user",
        //                    "type": "text"
        //                }
        //            }
   //     String 心跳2="{\"from\":{\"app\":\"im.customer\",\"pin\":\"" +pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"aid\":\""+aid+"\",\"type\":\"chat_message\",\"to\":{\"app\":\"im.waiter\"},\"timestamp\":"+l+",\"id\":\""+ UUID.randomUUID() +"\",\"body\":{\"chatinfo\":{\"entry\":\"jd_search\",\"venderId\":\"12353944\",\"proVer\":\"pc2.0\",\"pid\":\"10070180626593\",\"sid\":\"\",\"verification\":\"semantic\",\"behaviour\":\"142745318359734812\",\"distinguishPersonJimi\":2},\"uniformBizInfo\":{\"tenantId\":\"\",\"buId\":\"\",\"channelId\":\"\",\"ua\":\"\",\"channelTag\":\"\"},\"content\":\"https://item.jd.com/10052854283794.html\",\"render\":\"user\",\"type\":\"text\"}}";
        String 心跳="{\"from\":{\"app\":\"im.customer\",\"pin\":\""+pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"aid\":\""+aid+"\",\"type\":\"client_heartbeat\",\"to\":{\"app\":\"im.customer\"},\"timestamp\":"+l+"}";
        customizedWebSocketClient.send(心跳);

    }




}
