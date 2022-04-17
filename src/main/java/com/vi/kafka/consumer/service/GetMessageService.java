package com.vi.kafka.consumer.service;

import com.alibaba.fastjson.JSON;
import com.vi.kafka.common.Book;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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
    public static final String TOPIC_TEST = "book_topic";
    //消费者组mygroup
    public static final String TOPIC_GROUP = "mygroup";

    @KafkaListener(topics = TOPIC_TEST, groupId = TOPIC_GROUP)
    public void topicTest(ConsumerRecord<?, ?> record, Acknowledgment ack,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<Object> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            Book book = JSON.parseObject(msg.toString(), Book.class);
            System.out.println(book);
            log.info("topic_test 消费了： Topic:" + topic + ",Message:" + msg);
            ack.acknowledge();
        }
    }
}