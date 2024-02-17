package com.websocket.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/15 14:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedWebSocketClientTest2Bean {


    /**
     *  账号
     */
    public  String pin;


    /**
     *  次数
     */
    public  Integer  count;

    /**
     *  是否是全部账号发送 默认为 false
     */
    public Boolean  allPin;

    /**
     *  要发的信息
     */
    public String  message;
}
