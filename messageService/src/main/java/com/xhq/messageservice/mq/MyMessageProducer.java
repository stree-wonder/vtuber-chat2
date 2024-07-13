package com.xhq.messageservice.mq;

import com.xhq.common.Constant.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String questionId) {
        rabbitTemplate.convertAndSend(Constant.MQ_EXCHANGE, Constant.MQ_ROUTING_KEY, questionId);
    }

}
