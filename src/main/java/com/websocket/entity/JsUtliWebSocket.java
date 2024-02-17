package com.websocket.entity;

import com.alibaba.fastjson.JSONObject;
import com.websocket.entity.to.Body;
import com.websocket.entity.to.From;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/17 16:24
 */
public class JsUtliWebSocket extends WebSocketClient {

    private Logger logger = LoggerFactory.getLogger(JsUtliWebSocket.class);


    private String info;


    /**
     * CopyOnWriteArrayList 线程安全的  防止删除多个
     */
    private static List<JsUtliWebSocket> customerList = new CopyOnWriteArrayList();

    public JsUtliWebSocket(URI serverUri) {
        super(serverUri);
    }


    public JsUtliWebSocket(URI serverUri, String info) {
        super(serverUri);
        this.info = info;
    }

    public static List<JsUtliWebSocket> getCustomerList() {
        return customerList;
    }

    public void setInfos(String info) {
        this.info = info;
    }

    public String getInfos() {
        return this.info;
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        JsUtliWebSocket customerWss = getCustomerWss(this.getInfos());
        JSONObject jsonObject = JSONObject.parseObject(this.getInfos());
        if (customerWss != null) {
            customerWss.send("{\n" +
                    "            \"from\": {\"pin\": " + jsonObject.getString("pin") + ", \"app\": \"im.customer\", \"clientType\": \"m\"},\n" +
                    "            \"datetime\": " + JsUtils.getDate(System.currentTimeMillis()) + ",\n" +
                    "            \"ver\": \"4.2\",\n" +
                    "            \"type\": \"auth\",\n" +
                    "            \"to\": {\"app\": \"im.customer\", \"pin\": \"@im.jd.com\"},\n" +
                    "            \"timestamp\": " + System.currentTimeMillis() + ",\n" +
                    "            \"aid\": " + jsonObject.getString("aid") + ",\n" +
                    "            \"id\": " + JsUtils.uuid2() + ",\n" +
                    "            \"body\": {\n" +
                    "                \"clientType\": \"comet\",\n" +
                    "                \"clientVersion\": \"1.0.0\",\n" +
                    "                \"ext\": {\"sourceClientType\": \"\"},\n" +
                    "                \"clientKind\": \"customer\",\n" +
                    "                \"presence\": \"chat\",\n" +
                    "                \"dvc\": \"HN7DQL6XFPANLU6CUBXNS4532WBMYV23FWD6LAZFXT5BBPPOYM2DS4MTQWOIJCMLQ6KUHPERJUROPGCQIAWIMDQN4Q\"\n" +
                    "            }\n" +
                    "        }");
            customerWss.onping();
            logger.info("帐号wss打开,找到帐号:{}", jsonObject.getString("pin"));
        } else {
            logger.info("帐号wss打开,未找到到帐号:{}", jsonObject.getString("pin"));
        }
    }

    @Override
    public void onMessage(String s) {
        JsUtliWebSocket customerWss = getCustomerWss(this.info);
        if (customerWss != null) {
            JSONObject jsonObject = JSONObject.parseObject(this.info);
            JSONObject message = JSONObject.parseObject(s);
            logger.info("帐号wss接收消息,找到帐号：{}", jsonObject.getString("pin"));
            switch (message.getString("type")) {
                case "ack":
                    logger.info("ack 账号:{}", jsonObject.getString("pin"));
                    break;
                case "chat_message":
             /*   const pin = message.from.pin;
                const content = message.body.content;
                   if (pin !== null && content !== null && pin !== "@im.jd.com" && message.body.msg !== "留言成功") {
                    const venderId = message.body.chatinfo.venderId;
                       customerChatEvaluate(customerWss, venderId, pin)//给个评价
                   }*/
                    String pin = message.getObject("from", From.class).getPin();
                    String content = message.getObject("body", Body.class).getType();
                    String msg = message.getObject("body", Body.class).getMsg();
                    if (pin != null && content != null && !"@im.jd.com".equals(pin) && !"留言成功".equals(msg)) {
                        String venderId = message.getObject("body", Body.class).getChatinfo().getVenderId();

                        customerChatEvaluate(customerWss, venderId, pin);//给个评价

                        logger.info("给个评价完成 账号:{}", pin);
                    }
                    break;
                case "chat_evaluate":
                    //console.log(message.body.msg)
                    String msg1 = message.getObject("body", Body.class).getMsg();
                    logger.info(msg1);
                    break;
                default:
                    break;
            }
        } else {
            logger.info("帐号wss接收消息,未找到帐号:{}", JSONObject.parseObject(this.info).getString("pin"));
        }
    }

    /**
     * function customerWssClose(e) {
     * const customerWss = getCustomerWss(e.target.info);
     * if (customerWss !== null) {
     * console.log("帐号wss关闭,找到帐号", customerWss.info.pin)
     * clearInterval(customerWss.onping)
     * removeCustomer(customerWss.info)
     * } else {
     * console.log("帐号wss关闭,未找到帐号", e.target.info.pin)
     * }
     * }
     *
     * @param i
     * @param s
     * @param b
     */
    @Override
    public void onClose(int i, String s, boolean b) {
        String customerWss = this.getInfos();
        if (customerWss != null) {
            logger.info("帐号wss关闭,找到帐号:{}", JSONObject.parseObject(customerWss).getString("pin"));
            //关闭定时器   心跳自动任务停止
            onpingClose(customerWss);
            //删除保存的账号
            removeCustomer(customerWss);
        }
    }


    /**
     * function removeCustomer(customer) {
     * const index = customerList.findIndex(item => {
     * if (item.info.pin === customer.pin) {
     * return item;
     * }
     * });
     * if (index !== -1) {
     * <p>
     * customerList.splice(index, 1)
     * }
     * }
     */
    //根据 info 删除
    public static void removeCustomer(String customer) {
        //  this.customerList.removeIf(e -> e.info.contains(customer));;
        Iterator<JsUtliWebSocket> iterator = customerList.iterator();
        while (iterator.hasNext()) {
            JsUtliWebSocket is = iterator.next();
            if (is.getInfos().equals(customer)) {
                iterator.remove();
                break;
            }
            //这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
        }
    }

    @Override
    public String toString() {
        return "JsUtliWebSocket{" +
                "info='" + info + '\'' +
                '}';
    }

    @Override
    public void onError(Exception e) {
        JsUtliWebSocket customerWss = getCustomerWss(this.info);
        logger.error("onError", e);
        if (customerWss != null) {
            logger.info("帐号wss报错,找到帐号:{}", JSONObject.parseObject(this.info).getString("pin"));
        } else {
            logger.info("帐号wss报错,未到帐号:{}", JSONObject.parseObject(this.info).getString("pin"));
        }
    }

    /*
     */

    /**
     * 查找帐号wss
     *
     * @param customerInfo
     * @returns {null}
     *//*
    function getCustomerWss(customerInfo) {
        return customerList.find(item => {
        if (item.info.pin === customerInfo.pin) {
            return item;
        }
    });
    }*/
    //返回他本身
    public JsUtliWebSocket getCustomerWss(String customerInfo) {
        if (customerInfo == null) {
            return null;
        }
        for (int i = 0; i < customerList.size(); i++) {
            JsUtliWebSocket jsUtliWebSocket = customerList.get(i);
            String pin = JSONObject.parseObject(jsUtliWebSocket.getInfos()).getString("pin");
            String pin1 = JSONObject.parseObject(customerInfo).getString("pin");
            if (pin.equals(pin1)) {
                return jsUtliWebSocket;
            }
        }
        return null;
    }


    /**
     * 开启任务
     */
    public void onping() {

        JSONObject jsonObject = JSONObject.parseObject(this.info);
        JobDetail jobDetail = JobBuilder.newJob(JsJob.class)
                .withIdentity("jsjob", jsonObject.getString("pin"))
                .build();
        jobDetail.getJobDataMap().put("jsUtliWebSocket", this);

        SimpleTrigger build = TriggerBuilder.newTrigger().withIdentity("jsjob", jsonObject.getString("pin")).startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(15)
                        .repeatForever()).build();
        try {
            StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = stdSchedulerFactory.getScheduler();
            scheduler.scheduleJob(jobDetail, build);
            scheduler.start();
        } catch (Exception e) {
            // logger.error("定时任务开启报错",e);
            throw new RuntimeException(e);
        }
    }

    //关闭心跳
    public static void onpingClose(String customerWss) {
        Scheduler scheduler = null;
        try {
            JSONObject jsonObject = JSONObject.parseObject(customerWss);
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = JobKey.jobKey("jsjob", jsonObject.getString("pin"));
            //先暂停 再删除
            scheduler.pauseJob(jobKey);

            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }




    /*
     */

    /**
     * 客户给客服评价
     *
     * @param customWss
     * @param venderId
     * @param shopPin
     *//*
    function customerChatEvaluate(customWss, venderId, shopPin) {
    const customInfo = customWss.info;
    const sendMsg = {
                "from": {"app": "im.customer", "pin": customInfo.pin, "clientType": "comet"},
        "datetime": getDate(),
                "ver": "4.2",
                "aid": customInfo.aid,
                "type": "chat_evaluate",
                "to": {"app": "im.waiter"},
        "timestamp": getTimestamp(),
                "id": uuid2(),
                "body": {
            "waiter": shopPin,
                    "venderId": String(venderId),
                    "customer": customInfo.pin,
                    "star": 5,
                    "evaluate": "",
                    "pid": "",
                    "label": []
        }
    }
        customWss.send(JSON.stringify(sendMsg))
    }
    */
    public static void customerChatEvaluate(JsUtliWebSocket jsUtliWebSocket, String venderId, String shopPin) {
        JSONObject jsonObject = JSONObject.parseObject(jsUtliWebSocket.getInfos());
        String sendMsg = "{\n" +
                "                \"from\": {\"app\": \"im.customer\", \"pin\": " + jsonObject.getString("pin") + ", \"clientType\": \"comet\"},\n" +
                "        \"datetime\": " + JsUtils.getDate(System.currentTimeMillis()) + ",\n" +
                "                \"ver\": \"4.2\",\n" +
                "                \"aid\": " + jsonObject.getString("aid") + ",\n" +
                "                \"type\": \"chat_evaluate\",\n" +
                "                \"to\": {\"app\": \"im.waiter\"},\n" +
                "        \"timestamp\": " + JsUtils.getTime() + ",\n" +
                "                \"id\": " + UUID.randomUUID() + ",\n" +
                "                \"body\": {\n" +
                "            \"waiter\": " + shopPin + ",\n" +
                "                    \"venderId\": " + venderId + ",\n" +
                "                    \"customer\": " + jsonObject.getString("pin") + ",\n" +
                "                    \"star\": 5,\n" +
                "                    \"evaluate\": \"\",\n" +
                "                    \"pid\": \"\",\n" +
                "                    \"label\": []\n" +
                "        }\n" +
                "    }";
        jsUtliWebSocket.send(sendMsg);
    }


}

