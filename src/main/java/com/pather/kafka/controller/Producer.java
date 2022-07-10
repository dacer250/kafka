package com.pather.kafka.controller;

import com.pather.kafka.common.Event;
import com.pather.kafka.common.ResBean;
import com.pather.kafka.common.ClickEvent;
import com.pather.kafka.service.SendMessageService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric Tseng
 * @description Producer
 * @since 2022/4/16 15:40
 */
@RestController
@RequestMapping("/p")
public class Producer {
    @Autowired
    private SendMessageService sendMessageService;



    @GetMapping("/sendMsgAsync")
    public ResBean sendMsgAsync() {
        Event<ClickEvent> eventEvent =new Event<>();
        eventEvent.setUserId(100);
        eventEvent.setEventType("CLICK");

        eventEvent.setEventTime(new Date());
        ClickEvent ce =new ClickEvent();
        ce.setClickUrl("http://www.baidu.com");
        ce.setTargetGraphId("G100200300");
        ce.setCurrentPageUrl("http://zhidao.baidu.com");
        eventEvent.setEventInfo(ce);

        return sendMessageService.sendMsgAsync(eventEvent);
    }


}
