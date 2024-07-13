package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.xhq.common.Constant.Constant;
import com.xhq.common.domain.UserMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MyMessageConsumer {


    @Autowired


    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {Constant.MQ_QUENE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String questionId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", questionId);

        channel.basicAck(deliveryTag, false);
    }

}
