package com.xsg.controller;

import com.xsg.sender.ASender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @ClassName: MsgController
 * @Description: TODO
 * @Author: 渣渣辉
 * @Date: 2019/7/10  17:04
 * @version : V1.0
 */
@RestController
public class MsgController {
    @Autowired
    private ASender producer;

    @RequestMapping("/msg")
    public void setMsg(String msg){
        producer.sendMsg(msg);
    }


}
