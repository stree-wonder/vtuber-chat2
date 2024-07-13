package com.xhq.messageservice.mq;

import com.rabbitmq.client.Channel;
import com.xhq.common.Constant.Constant;
import com.xhq.messageservice.domain.UserMessage;
import com.xhq.messageservice.service.UserMessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyMessageConsumer {


    @Autowired
    private UserMessageService userMessageService;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {Constant.MQ_QUENE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String questionId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", questionId);
        if(questionId==null||questionId==""){
            //id为空，删除sse连接，消息队列取消确认
            channel.basicNack(deliveryTag,false,false);
        }
        UserMessage question=userMessageService.getMessageByMessageId(questionId);
        if(question==null){
            //数据库没有问题，删除连接，取消确认
            channel.basicNack(deliveryTag,false,false);
        }
        if(question.getContent()==null||question.getContent()==""){

        }

        //修改问题状态为 running
        question.setStatus("running");
        userMessageService.updateById(question);

        //调用通易千问api
        userMessageService.generateAnswer(question.getContent(),question.getSubjectId());
        channel.basicAck(deliveryTag, false);
    }

}
