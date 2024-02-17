package com.websocket.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/19 15:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class JsShopUtil {

    private String aid;
    private String pin;
    private Long pid;
    private Long venderId;
    private String shopName;

    private static ConcurrentHashMap<String, JsShopUtil> jsShopUtilMap = new ConcurrentHashMap<>();



 /*   case "org_new":
    socket.info.venderId = msg.body.groupid;
    socket.info.shopName = msg.body.groupname;
    console.log("商家id信息等", socket.info)
            break;
case "broadcast":
        //退出或上线
        console.log("退出或上线")
        break;
case "chat_message":
        //接收消息
        const 来源平台 = msg.from.app;
    if (来源平台 === "im.customer") {
        const 消息内容 = msg.body.content;
        const 商品Id = msg.body.chatinfo.pid;
        const 商家Id = msg.body.chatinfo.venderId;
        const 对方帐号 = msg.from.pin;
        const 当前帐号 = msg.to.pin;
        const 是否已读 = msg.readFlag;

        if(商品Id===socket.info.pid && 消息内容!==null && 消息内容!==undefined && 消息内容!=="" && 是否已读===undefined){
            socket.write(创建已读(socket.info.aid,msg.id,msg.mid,对方帐号,socket.info.pin,商家Id))
            socket.write(创建已读2(socket.info.aid,对方帐号,socket.info.pin,商家Id))
            const sendMsgPack = JSON.stringify({
                    "aid": socket.info.aid,
                    "body": {"content": 消息内容, "type": "text"},
            "from": {"app": "im.waiter", "art": "customerGroupMsg", "clientType": "pc", "pin": socket.info.pin},
            "id": uuid2().toUpperCase(),
                    "timestamp": getTimestamp(),
                    "to": {"app": "im.customer", "pin": 对方帐号},
            "type": "chat_message",
                    "ver": "4.4"
            });
            console.log(sendMsgPack)
            socket.write(sendMsgPack + "\n")
            console.log("接收消息", 当前帐号, 对方帐号, 商家Id, 商品Id, 消息内容,是否已读,data.toString())
        }
    }

    break;
case "server_msg":
        console.log("服务器消息", msg.body.msgtext)
            break;
case "failure":
        //掉线
        console.log("店铺掉线", msg.body.msg)
            break;
case "ack":
        //心跳等接收
        console.log("接收ACK", msg.body.type)
            break;
    default:
            console.log("未知类型", type, JSON.stringify(msg));
    break;*/


    public void shoptcp(String pid) {
        SSLSocket socket = null;
        BufferedReader read = null;
        PrintWriter out = null;
        try {
            SSLContext sslc = SSLContext.getInstance("TLSv1.2");
            sslc.init(null, null, null);
            /* 到这里结束======== */
/** 上面凝视包围的内容，和server端的一样。仅仅是这里变成了客户端要使用的秘钥，客户端要信任的证书 */
            SSLSocketFactory sslSocketFactory = sslc.getSocketFactory();/* 获得socketFactory */
//ap-dd3.jd.com
//ap-dd1.jd.com
//116.198.167.5:443
            socket = (SSLSocket) sslSocketFactory.createSocket("ap-dd1.jd.com", 443);/* 訪问本机的9999端口 */
//System.out.println(socket.getSession());

            socket.setKeepAlive(true);/*长连接···*/
            /*以下都是消息的处理，没什么东西了*/
            read = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            /**
             * {\"body\":{\"clientKind\":\"waiter\",\"clientType\":\"POP\",\"clientVersion\":\"10.0.8.0\",\"dvc\":\"AB559BBD-47C-44DB-B558-07DAC3985C7C\",\"eid\":\"eidWd2b4c12276s6Ayd6DfQfSgyB67F3FYV+BwMne5P1gexlmDViff1XtKeuNCesWfUbn2VBLi0OMO\\/5mLHZMYyuZFRJFiAcuJcBnjRY48aNUqZgqQty\",\"ext\":{\"safeVerify\":\"1\"},\"os\":\"Windows 10.0.18362\",\"presence\":\"chat\",\"token\":\"7A8BA338ED51044E8A7D11783B0CF7D0\",\"versionCheck\":1},\"from\":{\"app\":\"im.waiter\",\"art\":\"customerGroupMsg\",\"clientType\":\"pc\",\"pin\":\"森马66\"},\"id\":\"CF1F0140-C4DF-4C17-96EB-C4B7FBFE993C\",\"to\":{\"app\":\"im.waiter\",\"pin\":\"@im.jd.com\"},\"type\":\"auth\",\"ver\":\"4.4\"}
             */
            String 数据 = "{\"body\":{\"clientKind\":\"waiter\",\"clientType\":\"POP\",\"clientVersion\":\"10.0.8.0\",\"dvc\":\"AB559BBD-47C-44DB-B558-07DAC3985C7C\",\"eid\":\"eidWd2b4c12276s6Ayd6DfQfSgyB67F3FYV+BwMne5P1gexlmDViff1XtKeuNCesWfUbn2VBLi0OMO\\/5mLHZMYyuZFRJFiAcuJcBnjRY48aNUqZgqQty\",\"ext\":{\"safeVerify\":\"1\"},\"os\":\"Windows 10.0.18362\",\"presence\":\"chat\",\"token\":\"7A8BA338ED51044E8A7D11783B0CF7D0\",\"versionCheck\":1},\"from\":{\"app\":\"im.waiter\",\"art\":\"customerGroupMsg\",\"clientType\":\"pc\",\"pin\":\"森马66\"},\"id\":\"CF1F0140-C4DF-4C17-96EB-C4B7FBFE993C\",\"to\":{\"app\":\"im.waiter\",\"pin\":\"@im.jd.com\"},\"type\":\"auth\",\"ver\":\"4.4\"}";
            out.println(数据);
            String message;

            String aid1 = null;
            String pin1 = null;
            while (null != (message = read.readLine())) {
                JSONObject jsonObject = JSONObject.parseObject(message);
                String type = jsonObject.getString("type");
                System.out.println(message);
                switch (type) {
                    case "auth_result":
                        aid1 = jsonObject.getString("aid");
                        pin1 = jsonObject.getJSONObject("to").getString("pin");
                        this.setAid(aid1);
                        this.setPin(pin1);
                        out.println("{\"aid\":\"" + aid1 + "\",\"body\":{},\"from\":{\"app\":\"im.waiter\",\"art\":\"customerGroupMsg\",\"clientType\":\"pc\",\"pin\":\"" + pin1 + "\"},\"id\":\"" + JsUtils.uuid2().toUpperCase() + "\",\"timestamp\":" + System.currentTimeMillis() + ",\"to\":{\"app\":\"im.waiter\",\"pin\":\"@im.jd.com\"},\"type\":\"push_unread\",\"ver\":\"4.4\"}\n");
                        out.println("{\"aid\":\"" + aid1 + "\",\"body\":{},\"from\":{\"app\":\"im.waiter\",\"art\":\"customerGroupMsg\",\"clientType\":\"pc\",\"pin\":\"" + pin1 + "\"},\"id\":\"" + JsUtils.uuid2().toUpperCase() + "\",\"timestamp\":" + System.currentTimeMillis() + ",\"to\":{\"app\":\"im.waiter\",\"pin\":\"@im.jd.com\"},\"type\":\"get_waiter_info\",\"ver\":\"4.0\"}\n");
                        out.println("{\"aid\":\"" + aid1 + "\",\"body\":{},\"from\":{\"app\":\"im.waiter\",\"art\":\"customerGroupMsg\",\"clientType\":\"pc\",\"pin\":\"" + pin1 + "\"},\"id\":\"" + JsUtils.uuid2().toUpperCase() + "\",\"timestamp\":" + System.currentTimeMillis() + ",\"to\":{\"app\":\"im.waiter\",\"pin\":\"@im.jd.com\"},\"type\":\"org_new\",\"ver\":\"4.0\"}\n");
                        out.println("{\"aid\":\"" + aid1 + "\",\"body\":{},\"from\":{\"app\":\"im.waiter\",\"art\":\"customerGroupMsg\",\"clientType\":\"pc\",\"pin\":\"" + pin1 + "\"},\"id\":\"" + JsUtils.uuid2().toUpperCase() + "\",\"timestamp\":" + System.currentTimeMillis() + ",\"to\":{\"app\":\"im.waiter\",\"pin\":\"@im.jd.com\"},\"type\":\"client_heartbeat\",\"ver\":\"4.4\"}\n");
                        jsShopUtilMap.put(pin1, this);
                        break;
                    case "get_login_token":
                        break;
                    case "chat_message":
                        //接收消息
                        String 来源平台 = jsonObject.getJSONObject("from").getString("app"); //
                        if ("im.customer".equals(来源平台)) {
                            String 消息内容 = jsonObject.getJSONObject("body").getString("content");//  msg.body.content;
                            String 商品Id = jsonObject.getJSONObject("body").getString("pid");// msg.body.chatinfo.pid;
                            Long 商家Id = jsonObject.getJSONObject("body").getJSONObject("chatinfo").getLong("venderId"); // msg.body.chatinfo.venderId;
                            String 对方帐号 = jsonObject.getJSONObject("from").getString("pin"); //msg.from.pin;
                            String 当前帐号 = jsonObject.getJSONObject("to").getString("pin");  //msg.to.pin;
                            Long 是否已读 = jsonObject.getLong("readFlag"); //msg.readFlag;

                            if (pid.equals(商品Id) && 消息内容 != null && !"".equals(消息内容) && 是否已读 == null) {
                                // out.println(创建已读(socket.info.aid,msg.id,msg.mid,对方帐号,socket.info.pin,商家Id));
                                //  out.println(创建已读2(socket.info.aid,对方帐号,socket.info.pin,商家Id));
                                String sendMsgPack = "{\"aid\":\"" + this.getAid() + "\",\"body\":{\"content\":\"" + 消息内容 + "\",\"type\":\"text\"},\"from\":{\"app\":\"im.waiter\",\"art\":\"customerGroupMsg\",\"clientType\":\"pc\",\"pin\":\"" + this.getPin() + "\"},\"id\":\"" + JsUtils.uuid2().toUpperCase() + "\",\"timestamp\":" + System.currentTimeMillis() + ",\"to\":{\"app\":\"im.customer\",\"pin\":\"" + 对方帐号 + "\"},\"type\":\"chat_message\",\"ver\":\"4.4\"}";
                                log.info(sendMsgPack);
                                out.println(sendMsgPack + "\n");
                                log.info("接收消息", 当前帐号, 对方帐号, 商家Id, 商品Id, 消息内容, 是否已读, this.toString());
                            }
                        }
                        break;
                    case "get_waiter_info":
                        break;
                    case "server_msg":
                        log.info("服务器消息", jsonObject.getJSONObject("body").getString("msgtext"));
                        break;
                    case "ack":
                        log.info("接收ACK",  jsonObject.getJSONObject("body").getString("type"));
                        break;
                    case "org_new":
                        Long Long = jsonObject.getJSONObject("body").getLong("groupid");
                        String shopName = jsonObject.getJSONObject("body").getString("groupname");
                        this.setShopName(shopName);
                        this.setVenderId(Long);
                        log.info("商家id信息等 :{}", this);
                        break;
                    case "broadcast":
                        //退出或上线
                        log.info("退出或上线");
                        break;
                    case "failure":
                        log.info("店铺掉线",jsonObject.getJSONObject("body").getString("msg") );
                        //报错 即 tcp连接失败  或者有登录账号
                        if (pin1 != null) {
                            jsShopUtilMap.remove(pin1);
                        }
                        break;
                    default:
                        log.info("未知类型", type, message);
                        break;
                }
            }
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (out != null) {
                    out.close();
                }
                if (read != null) {
                    read.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
