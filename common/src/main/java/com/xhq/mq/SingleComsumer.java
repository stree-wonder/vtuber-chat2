package com.xhq.mq;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class SingleComsumer {
    private final static String QUEUE_NAME = "SingleChannel";

    public static void main(String[] argv) throws Exception {
        //
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建队列             队列名称
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("xhq:Comsumerchannel [*] Waiting for messages. To exit press CTRL+C");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("xhq: [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

    }

}
