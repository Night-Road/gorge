package com.yourname.frontend.controller;

import com.yourname.frontend.mq.MessageBody;
import com.yourname.frontend.mq.QueueTopic;
import com.yourname.frontend.service.RabbitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/12/2022 7:41 PM
 */
@Controller
@RequestMapping("mq")
public class MqTestController {

    @Resource
    RabbitService rabbitService;

    @RequestMapping("send.json")
    @ResponseBody
    public String send(@RequestParam("message")String message){
        MessageBody messageBody = new MessageBody();
        messageBody.setTopic(QueueTopic.TEST);
        messageBody.setDetail(message);
        rabbitService.send(messageBody);

        //发送延迟message
        rabbitService.sendDelay(messageBody,10*1000);

        return "success";
    }
}
