package com.vi.kafka.producer.service;

import com.alibaba.fastjson.JSON;
import com.vi.kafka.common.Book;
import com.vi.kafka.common.ResBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

/**
 * @author Eric Tseng
 * @description SendMessageService
 * @since 2022/4/16 17:33
 */
@Service
@Slf4j
public class SendMessageService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    //自定义topic
    public static final String TOPIC_TEST = "book_topic";

    /**
     * 异步发送常规写法
     *
     * @param book
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResBean sendMsg(Book book) {
        ResBean resBean = new ResBean();
        String obj2String = JSON.toJSONString(book);
        log.info("准备发送消息为：{}", obj2String);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(TOPIC_TEST, obj2String);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.info(TOPIC_TEST + " - 生产者 发送消息失败：" + throwable.getMessage());
            }
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                log.info(TOPIC_TEST + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });
        return resBean;
    }

    /**
     * 异步发送lambda写法
     *
     * @param book
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResBean sendMsgLambda(Book book) {
        ResBean resBean = new ResBean();
        String obj2String = JSON.toJSONString(book);
        log.info("准备发送消息为：{}", obj2String);
        //发送消息
        kafkaTemplate.send(TOPIC_TEST, obj2String).addCallback(success -> {
            // 消息发送到的topic
            assert success != null;
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
        }, failure -> {
            System.out.println("失败");
        });
        return resBean;
    }

    /**
     * 同步发送
     * 默认情况下 kafkaTemplate 是采取异步方式发送的，如果希望同步发送消息只需要在 send
     * 方法后面调用 get() 方法即可，get() 方法返回的即为结果（如果发送失败则抛出异常）。
     * get 方法还有一个重载方法 get(long timeout, TimeUnit unit)，当 send 方法耗时大于 get 方法所设定的参数时会抛出一个超时异常。
     * 虽然超时了，但仅仅是抛出异常，消息还是会发送成功的。
     *
     * @param book
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResBean sendMsgAsync(Book book) {
        String obj2String = JSON.toJSONString(book);
        log.info("准备发送消息为：{}", obj2String);
        try {
            SendResult<String, Object> sendResult = kafkaTemplate.send(TOPIC_TEST, obj2String).get();
            assert sendResult != null;
            String topic = sendResult.getRecordMetadata().topic();
            int partition = sendResult.getRecordMetadata().partition();
            long offset = sendResult.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("发送消息失败:" + e.getMessage());
            return ResBean.error("发送失败");
        }
        return ResBean.success();
    }

    /**
     * 开启事务发送，使用注解方式
     * kafka 事务属性是指一系列的生产者生产消息和消费者提交偏移量的操作在一个事务，
     * 或者说逻辑上要属于一个原子操作，同时成功或同时失败。
     * 比如在一个方法里同时发送多条消息，在发生异常的时候可以进行回滚，确保消息监听器
     * 不会接受到一些错误的或者不需要的消息。
     *
     * @param book
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public ResBean sendMsgTx1(Book book) {
        String obj2String = JSON.toJSONString(book);
        log.info("准备发送消息为：{}", obj2String);
        kafkaTemplate.send(TOPIC_TEST, obj2String);
        if ("eric".equals(book.getAuthor())) {
            throw new RuntimeException("fail");
        }
        return ResBean.success();
    }

    /**
     * 开启事务发送，声明式事务方式
     *
     * @param book
     * @return
     */
    public ResBean sendMsgTx2(Book book) {
        String obj2String = JSON.toJSONString(book);
        log.info("准备发送消息为：{}", obj2String);
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            kafkaTemplate.send(TOPIC_TEST, obj2String);
            if ("eric".equals(book.getAuthor())) {
                throw new RuntimeException("fail");
            }
            return true;
        });
        return ResBean.success();
    }
}
