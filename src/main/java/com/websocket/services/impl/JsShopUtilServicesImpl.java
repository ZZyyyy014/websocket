package com.websocket.services.impl;

import com.websocket.entity.JsShopUtil;
import com.websocket.services.JsShopUtilServices;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/19 17:57
 */
@Service
public class JsShopUtilServicesImpl implements JsShopUtilServices {




    @Override
    @Async
    public void shopStart(String pid) {
       new JsShopUtil().shoptcp(pid);
    }



}
