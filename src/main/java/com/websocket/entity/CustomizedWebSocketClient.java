package com.websocket.entity;

import com.alibaba.fastjson.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;


public class CustomizedWebSocketClient extends WebSocketClient {



    /**
     * 线程安全的Boolean -是否受到消息
     */
    public AtomicBoolean hasMessage = new AtomicBoolean(false);



    /**
     * 线程安全的Boolean -是否已经连接
     */
    private AtomicBoolean hasConnection = new AtomicBoolean(false);


    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(CustomizedWebSocketClient.class);



    /**
     *  存储信息
     */
    private   static   ConcurrentHashMap<String,JSONObject> map =new ConcurrentHashMap() ;

    private    static ConcurrentHashMap<String,CustomizedWebSocketClient> CustomizedWebSocketClientmap =new ConcurrentHashMap() ;


    private    JSONObject  jsobject;


    private  String aid;
    private String  pin;


    /**
     * 构造方法
     *
     * @param serverUri
     */
    public CustomizedWebSocketClient(URI serverUri) {
        super(serverUri);
        logger.info("CustomizeWebSocketClient init:" + serverUri);
    }

    public CustomizedWebSocketClient(URI serverUri, JSONObject jsonObject) {
        super(serverUri);
        this.jsobject= jsonObject;
        logger.info("CustomizeWebSocketClient init:" + serverUri);
    }


    /**
     * 打开连接是方法
     * @param serverHandshake
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        //心跳
        //{"from":{"app":"im.customer","pin":"游客-1690801316137","clientType":"comet"},"datetime":"2023-07-31 19:01:56","ver":"4.2","aid":"LHHhoWlc",
        // "type":"client_heartbeat","to":{"app":"im.customer"},"timestamp":1690801316407}
         pin = this.jsobject.getString("pin");
         aid = this.jsobject.getString("aid");
        // sendHeartbeat(pin,aid);
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .usingJobData("aid", aid)
                .usingJobData("pin", pin)
                .withIdentity("myjob" , pin).build();
        jobDetail.getJobDataMap().put("customizedWebSocketClient",this);
        SimpleTrigger build = TriggerBuilder.newTrigger().withIdentity("myjob" , pin).startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(15)
                        .repeatForever()).build();
        try {
            StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = stdSchedulerFactory.getScheduler();
            scheduler.scheduleJob(jobDetail,build);
            scheduler.start();
            map.put(pin,jsobject);
            CustomizedWebSocketClientmap.put(pin,this);
            /**
             * scheduler.start();	            启动任务调度
             * scheduler.standby();                任务调度挂起，即暂停操作
             * scheduler.shutdown();               关闭任务调度
             *    shutdown(true)：表示等待所有正在执行的job执行完毕之后，再关闭Scheduler；
             *    shutdown(false)：表示直接关闭Scheduler
             */
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        System.out.println(this.getConcurrentHashMaps());
    }

/*

    */
/**
     *  循环发送心跳
     *//*

    private void  sendHeartbeat(String pin,String aid){
        //当前系统时间
        long l = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //当前时间格式化
        String format = simpleDateFormat.format(l);
        String 心跳="{\"from\":{\"app\":\"im.customer\",\"pin\":\""+pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"aid\":\""+aid+"\",\"type\":\"client_heartbeat\",\"to\":{\"app\":\"im.customer\"},\"timestamp\":"+l+"}";
        this.send(心跳);
    }
*/





    /**
     * 收到消息时
     *
     * @param s
     */
    @Override
    public void onMessage(String s) {
        hasMessage.set(true);
        logger.info("CustomizeWebSocketClient onMessage:" + s);
    }

    /**
     * 当连接关闭时    也关闭自启发送任务
     *
     * @param i
     * @param s
     * @param b
     */
    @Override
    public void onClose(int i, String s, boolean b) {
        this.hasConnection.set(false);
        this.hasMessage.set(false);
        /**
         *  连接关闭时候 关闭任务
         */
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = JobKey.jobKey("myjob", pin);
           //先暂停 再删除
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        map.remove(pin);
        CustomizedWebSocketClientmap.remove(pin);
        logger.info("CustomizeWebSocketClient onClose:" + s);
    }

    /**
     * 发生error时
     *
     * @param e
     */
    @Override
    public void onError(Exception e) {
        /**
         *  连接关闭时候 关闭任务
         */
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobKey jobKey = JobKey.jobKey("myjob", pin);
            //先暂停 再删除
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException s) {
            throw new RuntimeException(s);
        }finally {
            CustomizedWebSocketClientmap.remove(pin);
        }
        logger.info("CustomizeWebSocketClient onError:" + e);
    }



    @Override
    public void connect() {
        if(!this.hasConnection.get()){
            super.connect();
            hasConnection.set(true);
        }
    }



    //获取map
    public  ConcurrentHashMap<String,JSONObject> getConcurrentHashMaps(){
        return map;
    }



    public static  ConcurrentHashMap<String ,CustomizedWebSocketClient> getCustomizedWebSocketClientmap(){
        return CustomizedWebSocketClientmap;
    }



}


