package com.xhq.mq;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;

public class SingleProducer {
    private final static String QUEUE_NAME = "SingleChannel";

    public static void main(String[] argv) throws Exception {
        //
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建队列             队列名称
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("xhq:channelProducer [*] Waiting for messages. To exit press CTRL+C");

        Scanner sc=new Scanner(System.in);

        //发送消息
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("xhq: [x] Sent '" + message + "'");

    }

}
