package com.websocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.websocket.entity.JsShopUtil;
import com.websocket.entity.JsUtils;
import com.websocket.entity.JsUtliWebSocket;
import com.websocket.req.CustomizedWebSocketClientTest2Bean;
import com.websocket.services.CustomizedWebSocketClientServices;
import com.websocket.services.JsShopUtilServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/12 18:26
 */
@RestController
@Slf4j
public class TestController {


    @Autowired
    public CustomizedWebSocketClientServices customizedWebSocketClientServices;

    @Autowired
    private JsShopUtilServices jsShopUtilServices;


    @GetMapping(value = "/test1")
    String contextLoads1() throws Exception {
        customizedWebSocketClientServices.test();
        return "123";
    }


    /**
     * ,produces = "application/json;charset=utf-8"
     *   有中文要设置 格式utf-8
     * @param customizedWebSocketClientTest2Bean
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/test2")
    String contextLoads2(@RequestBody CustomizedWebSocketClientTest2Bean customizedWebSocketClientTest2Bean) throws Exception {
        customizedWebSocketClientServices.test2(customizedWebSocketClientTest2Bean);
        return "test2";
    }


    /**
     *   开启 定时  帐号wss接收消息,找到帐号
     * @return
     * @throws Exception
     */
    @GetMapping("/test3")
    void contextLoads3() throws Exception {

        for (int i = 0; i <10 ; i++) {
            String customerWebSocket = JsUtils.createCustomerWebSocket();
            System.out.println(customerWebSocket);
        }
       // return customerWebSocket;
    }


    /**
     * 手动关闭定时
     * @param pin
     */
    @GetMapping("/test4")
    void test4(String pin){
        //根据pin 获取到info
        List<JsUtliWebSocket> customerList = JsUtliWebSocket.getCustomerList();
        Iterator<JsUtliWebSocket> iterator = customerList.iterator();
        // 这里用迭代 是防止 多个删除同时进行 下标出问题
        while (iterator.hasNext()) {
            JsUtliWebSocket jsUtliWebSocket = iterator.next();
            String pin1 = JSONObject.parseObject(jsUtliWebSocket.getInfos()).getString("pin");
            if (pin1.equals(pin)){
                JsUtliWebSocket.onpingClose(jsUtliWebSocket.getInfos());
                JsUtliWebSocket.removeCustomer(jsUtliWebSocket.getInfos());
                log.info("关闭定时 pin:{}",pin);
                break;
            }
        }
    }

    @GetMapping("/test5")
    void tset5(String pid)  {
        System.out.println(pid);
        new JsShopUtil().shoptcp(pid);
    }


    @GetMapping("/test6")
    void tset6()  {
        /**
         * 12353944   venderId
         * 10070180626593   pid
         */
        jsShopUtilServices.shopStart("10070180626593");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JsUtils.客户发送信息(12353944L,"10070180626593");
    }


}
