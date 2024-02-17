package com.websocket.entity.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能描述：
 *
 * @Description
 * @Author: zf
 * @Date: 2023/12/18 16:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Body {

    String type;
    String msg;

    Chatinfo chatinfo;

}
