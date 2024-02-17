package com.websocket;

import com.websocket.entity.JsUtils;
import com.websocket.services.CustomizedWebSocketClientServices;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebsocketApplicationTests {


    @Autowired
    CustomizedWebSocketClientServices customizedWebSocketClientServices;



    @Test
    void contextLoads()  throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler ();
        scheduler.clear ();
    }


    @Test
    void contextLoads1(){
        String customerAid = JsUtils.getCustomerAid();
        System.out.println(customerAid);
    }

}
