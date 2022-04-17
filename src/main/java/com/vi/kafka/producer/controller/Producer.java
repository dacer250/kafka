package com.vi.kafka.producer.controller;

import com.vi.kafka.common.Book;
import com.vi.kafka.common.ResBean;
import com.vi.kafka.producer.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/sendMsg")
    public ResBean sendMsg(@RequestBody Book book) {
        return sendMessageService.sendMsg(book);
    }

    @PostMapping("/sendMsgLambda")
    public ResBean sendMsgLambda(@RequestBody Book book) {
        return sendMessageService.sendMsgLambda(book);
    }

    @PostMapping("/sendMsgAsync")
    public ResBean sendMsgAsync(@RequestBody Book book) {
        return sendMessageService.sendMsgAsync(book);
    }

    @PostMapping("/sendMsgTx1")
    public ResBean sendMsgTx1(@RequestBody Book book) {
        return sendMessageService.sendMsgTx1(book);
    }

    @PostMapping("/sendMsgTx2")
    public ResBean sendMsgTx2(@RequestBody Book book) {
        return sendMessageService.sendMsgTx2(book);
    }
}
