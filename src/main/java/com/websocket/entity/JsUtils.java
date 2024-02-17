package com.websocket.entity;

import com.alibaba.fastjson.JSONObject;
import kong.unirest.HttpResponse;

import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/17 15:15
 */
public class JsUtils {


    private final static String[] sendEmojiList = {"#E-s05", "#E-s07", "#E-s09", "#E-s11", "#E-s15", "#E-s18", "#E-s20", "#E-s22", "#E-s23", "#E-s28", "#E-s30", "#E-s31", "#E-s35", "#E-s38", "#E-s48", "#E-s52", "#E-s54", "#E-s57", "#E-s36", "#E-s33", "#E-s39", "#E-s01", "#E-s02", "#E-s03", "#E-s04", "#E-s08", "#E-s10", "#E-s12", "#E-s13", "#E-s14", "#E-s16", "#E-s17", "#E-s24", "#E-s25", "#E-s26", "#E-s27", "#E-s29", "#E-s32", "#E-s34", "#E-s37", "#E-s40", "#E-s41", "#E-s42", "#E-s43", "#E-s46", "#E-s47", "#E-s49", "#E-s50", "#E-s51", "#E-s53", "#E-s55", "#E-s59", "#E-s61", "#E-s64", "#E-s65", "#E-s69", "#E-s72", "#E-s06", "#E-s19", "#E-s21", "#E-s66", "#E-s56", "#E-s58", "#E-s60", "#E-s62", "#E-s63", "#E-s44", "#E-s67", "#E-s68", "#E-s70", "#E-s71", "#E-s45"};


    /*
     *   */

    /**
     * 获取时间
     *
     * @returns {*} 2023-11-17 14:52:16
     */
    /*
    function getDate(fmt) {
        return new Date().Format(fmt);
    }*/
    public static String getDate(Long time) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(time);
    }


    /*
     *   */

    /**
     * 获取时间戳
     *
     * @returns {number}
     */
    /*
    function getTimestamp() {
        return new Date().getTime();
    }
    */
    public  static Long getTime() {
        return System.currentTimeMillis();
    }

    /*
        getCustomEmoji() {
            return sendEmojiList[Math.floor((Math.random() * sendEmojiList.length))];
        }
    */
    public  static String getCustomEmoji() {
        return sendEmojiList[(int) (Math.random() * sendEmojiList.length)];
    }


/*    function getShopAid(shopCookie) {
        return new Promise((resolve, reject) => {
            axios.get("https://dongdong.jd.com/workbench/checkin.json?version=2.6.3&client=openweb", {
                    headers: {
                "Cookie": shopCookie,
            }
        }).then(res => {
                    resolve(res.data.data)
            }).catch(error => {
                    reject(error)
            })
        })
    }*/

    public  static String getShopAid(String shopCookie) {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("Cookie", shopCookie);
        HttpResponse<String> stringHttpResponse = HttpUnirest.doGet("https://dongdong.jd.com/workbench/checkin.json?version=2.6.3&client=openweb", null, objectObjectHashMap);
        String body = stringHttpResponse.getBody();
        return body;
    }

    /*
     */

    /**
     * 创建游客帐号
     *
     * @returns {Promise<unknown>}
     */
    /*

    function getCustomerAid() {
        return new Promise((resolve, reject) => {
            axios({
                    url: "http://api.m.jd.com/client.action",
                    data: "functionId=getAidInfo&body=%7B%22aidClientType%22%3A%22comet%22%2C%22aidClientVersion%22%3A%22comet+-v1.0.0%22%2C%22appId%22%3A%22im.customer%22%2C%22os%22%3A%22comet%22%2C%22entry%22%3A%22jd_web_jdsyqyfw%22%2C%22reqSrc%22%3A%22s_comet%22%7D&client=wh5&clientVersion=1.0.0&loginType=3",
                    method: "POST",
                    headers: {
                "Referer": "http://jdcs.m.jd.com/chat/index.action?&sku=",
                        "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                        "User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36"
            }
        }).then(res => {
                    resolve(res.data)
            }).catch(error => {
                    reject(error)
            })
        })
    }
*/
    public   static String getCustomerAid() {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        HashMap<String, Object> param = new HashMap<>();
        param.put("functionId", "getAidInfo");
        param.put("body", "%7B%22aidClientType%22%3A%22comet%22%2C%22aidClientVersion%22%3A%22comet+-v1.0.0%22%2C%22appId%22%3A%22im.customer%22%2C%22os%22%3A%22comet%22%2C%22entry%22%3A%22jd_web_jdsyqyfw%22%2C%22reqSrc%22%3A%22s_comet%22%7D");
        param.put("client", "wh5");
        param.put("clientVersion", "1.0.0");
        param.put("loginType", "3");
        objectObjectHashMap.put("Referer", "http://jdcs.m.jd.com/chat/index.action?&sku=");
        objectObjectHashMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        objectObjectHashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
        HttpResponse<String> stringHttpResponse = HttpUnirest.doPost("http://api.m.jd.com/client.action", param, objectObjectHashMap);
        return stringHttpResponse.getBody();
    }

 /*
  async function createCustomerWebSocket() {
    const customerInfo = await getCustomerAid();
        if (customerInfo.pin !== "" && customerInfo.pin !== null) {
            //wss://imio.jd.com/websocket/?appId=im.customer&clientType=m&_wid_=84dd7bf8-dc17-10c7-d6b1-65feb0b19d01&aid=yPVqsZ1K&pin=%E6%B8%B8%E5%AE%A2-1700386701415
        const customerWss = new ws("wss://imio.jd.com/websocket/?pin=" + customerInfo.pin + "&appId=im.customer&aid=" + customerInfo.aid + "&clientType=comet&_wid_=" + uuid2());
            // const customerWss = new ws("wss://imio.jd.com/websocket/?appId=im.customer&clientType=m&_wid_=" + uuid2() + "&aid=" + customerInfo.aid + "&pin=" + customerInfo.pin, options);
            customerWss.info = customerInfo;
            customerList.push(customerWss);
            customerWss.onclose = customerWssClose;
            customerWss.onopen = customerWssOpen;
            customerWss.onerror = customerWssError;
            customerWss.onmessage = customerWssMessage;
            customerWss.onping = setInterval(customerWssPing, 15000, customerInfo)//添加定时心跳;
            return {"code": 200, "msg": "创建成功", "data": {"pin": customerInfo.pin}};
        }
        return {"code": 500, "msg": "创建失败"};
    }*/

  public static  String   createCustomerWebSocket(){
      String customerAid = getCustomerAid();
      try {
          Thread.sleep(500);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
      if (customerAid==null||"".equals(customerAid.trim())){
          return "创建出错";
      }
      JSONObject jsonObject = JSONObject.parseObject(customerAid);
      if (jsonObject.get("pin")!=null&& !"".equals(jsonObject.get("pin").toString().trim())){
          try {
              URI uri = new URI("wss://imio.jd.com/websocket/?pin=" + URLEncoder.encode(jsonObject.getString("pin")) + "&appId=im.customer&aid=" + jsonObject.get("aid") + "&clientType=comet&_wid_=" + uuid2());
              //连接
              JsUtliWebSocket webSocket = new JsUtliWebSocket(uri,customerAid);
              webSocket.connect();
              //获取到所有保存的连接
              List<JsUtliWebSocket> customerList = JsUtliWebSocket.getCustomerList();
               //连接成功添加进去
              customerList.add(webSocket);
            //  webSocket.onping();
              return "   {\"code\": 200, \"msg\": \"创建成功\", \"data\": {\"pin\": "+ JSONObject.parseObject(customerAid).getString("pin")+"}};";
          } catch (Exception e) {
              //"{"code": 500, "msg": "创建失败"}"
              throw new RuntimeException(e);
          }
      }
      return  "{\"code\": 500, \"msg\": \"创建失败\"}" ;
  }

/*
    async function addCustomerWebSocket(customerInfo) {
        console.log("添加游客", customerInfo)
    const customerWss = new ws("wss://imio.jd.com/websocket/?appId=im.customer&clientType=comet&_wid_=" + uuid2() + "&aid=" + customerInfo.aid + "&pin=" + customerInfo.pin);
        customerWss.info = customerInfo;
        customerList.push(customerWss);
        customerWss.onclose = customerWssClose;
        customerWss.onopen = customerWssOpen;
        customerWss.onerror = customerWssError;
        customerWss.onmessage = customerWssMessage;
        customerWss.onping = setInterval(customerWssPing, 15000, customerInfo)//添加定时心跳;
    }
    */

    public  static void addCustomerWebSocket(JsUtliWebSocket jsUtliWebSocket){
        JsUtliWebSocket.getCustomerList().add(jsUtliWebSocket);
    }

    public  static  void  客户发送信息(Long venderId,String pid){
        List<JsUtliWebSocket> customerList = JsUtliWebSocket.getCustomerList();
        for (int i = 0; i < customerList.size(); i++) {
            String infos = customerList.get(i).getInfos();
            String pin = JSONObject.parseObject(infos).getString("pin");
            String aid = JSONObject.parseObject(infos).getString("aid");
            System.out.println(pin);
            System.out.println(aid);
            long ls = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //当前时间格式化
            String format = simpleDateFormat.format(ls);
            /**
             * 12353944   venderId
             * 10070180626593   pid
             */
          //  String 原始="{\"from\":{\"app\":\"im.customer\",\"pin\":customerList[i].info.pin,\"clientType\":\"comet\"},\"datetime\":getDate(),\"ver\":\"4.2\",\"aid\":customerList[i].info.aid,\"type\":\"chat_message\",\"to\":{\"app\":\"im.waiter\"},\"timestamp\":getTimestamp(),\"id\":uuid2(),\"body\":{\"chatinfo\":{\"entry\":\"sdk_item\",\"venderId\":venderId,\"proVer\":\"pc2.0\",\"pid\":pid,\"sid\":\"\",\"verification\":\"semantic\",\"behaviour\":\"\"},\"uniformBizInfo\":{\"tenantId\":\"\",\"buId\":\"\",\"channelId\":\"\",\"ua\":\"\",\"channelTag\":\"\"},\"content\":\"dd\",\"render\":\"user\",\"type\":\"text\"}";
            String 发送="{\"from\":{\"app\":\"im.customer\",\"pin\":\""+pin+"\",\"clientType\":\"comet\"},\"datetime\":\""+format+"\",\"ver\":\"4.2\",\"aid\":\""+aid+"\",\"type\":\"chat_message\",\"to\":{\"app\":\"im.waiter\"},\"timestamp\":"+System.currentTimeMillis()+",\"id\":\""+JsUtils.uuid2()+"\",\"body\":{\"chatinfo\":{\"entry\":\"sdk_item\",\"venderId\":\""+venderId+"\",\"proVer\":\"pc2.0\",\"pid\":\""+pid+"\",\"sid\":\"\",\"verification\":\"semantic\",\"behaviour\":\"\"},\"uniformBizInfo\":{\"tenantId\":\"\",\"buId\":\"\",\"channelId\":\"\",\"ua\":\"\",\"channelTag\":\"\"},\"content\":\"dd\",\"render\":\"user\",\"type\":\"text\"}}";
            customerList.get(i).send(发送);
        }


    }





















    /**
     * 返回带-的UUID
     * @returns {string}
     */
    /*
    function uuid2() {
        function e() {
            return Math.floor(65536 * (1 + Math.random())).toString(16).substring(1)
        }

        return e() + e() + "-" + e() + "-" + e() + "-" + e() + "-" + (e() + e() + e())
    }
    */
//相当 于 uuid 带-
    public static String uuid2(){
      return UUID.randomUUID().toString();
}


/**
     * 返回不带-的UUID
     * @returns {string}
     */
/*
    function uuid() {
        function s4() {
            return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1)
        }

        return s4() + s4() + s4() + s4() + s4() + s4() + s4() + s4();
    }*/

    public static String uuid4(){
    return  UUID.randomUUID().toString().replace("-","");
}

}
