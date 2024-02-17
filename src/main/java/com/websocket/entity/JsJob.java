package com.websocket.entity;

import com.alibaba.fastjson.JSONObject;
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
 * @Date: 2023/12/17 18:27
 */
public class JsJob extends QuartzJobBean {

    private JsUtliWebSocket jsUtliWebSocket;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
         JobDataMap jdMap = context.getJobDetail().getJobDataMap();  //获得传递过来的参数
         jsUtliWebSocket = (JsUtliWebSocket) jdMap.get("jsUtliWebSocket");
         String infos = jsUtliWebSocket.getInfos();
        //当前系统时间
        long ls = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //当前时间格式化
        String format = simpleDateFormat.format(ls);
        String pin= JSONObject.parseObject(infos).getString("pin");
        String aid= JSONObject.parseObject(infos).getString("aid");
        String 心跳="{\"from\":{\"app\":\"im.customer\",\"pin\":\""+pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"type\":\"client_heartbeat\",\"to\":{\"app\":\"im.customer\"},\"timestamp\":"+ls+",\"aid\":\""+aid+"\"}";
        jsUtliWebSocket.send(心跳);
    }






}
