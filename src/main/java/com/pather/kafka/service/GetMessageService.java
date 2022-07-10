package com.pather.kafka.service;

import com.alibaba.fastjson.JSON;
import com.pather.kafka.common.ClickEvent;
import com.pather.kafka.common.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Eric Tseng
 * @description SendMessageService
 * @since 2022/4/16 17:32
 */
@Service
@Slf4j
public class GetMessageService {
    //自定义topic
    public static final String TOPIC_USER_EVENT = "TOPIC_CLICK_EVENT";
    //消费者组mygroup
    public static final String CONSUMER_GROUP = "mygroup-1";

    @KafkaListener(topics = TOPIC_USER_EVENT, groupId = CONSUMER_GROUP)
    public void consume(ConsumerRecord<?, ?> record, Acknowledgment ack,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<Object> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            Event<ClickEvent>event = JSON.parseObject(msg.toString(), Event.class);
            System.out.println(event);
            log.info("TOPIC_CLICK_EVENT 消费了： Topic:" + topic + ",Message:" + msg);
            ack.acknowledge();
        }
    }
}