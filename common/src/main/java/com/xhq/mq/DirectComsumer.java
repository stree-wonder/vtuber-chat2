package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class DirectComsumer {
  private static final String EXCHANGE_NAME = "direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    //建立连接
    Connection connection = factory.newConnection();
    //创建通道
    Channel channel = connection.createChannel();

    //声明交换机
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    //创建队列1
    String queueName1 = "direct_quene1";
    channel.queueDeclare(queueName1, true, false, false, null);
    channel.queueBind(queueName1, EXCHANGE_NAME, "q1");
    //创建队列2
    String queueName2 = "direct_quene2";
    channel.queueDeclare(queueName2, true, false, false, null);
    channel.queueBind(queueName2, EXCHANGE_NAME, "q2");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    //创建交付回调函数1
    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [quene1] Received '" + message + "'");
    };
    //创建交付回调函数1
    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [quene2] Received '" + message + "'");
    };
    //开始消费消息队列1
    channel.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
    //开始消费消息队列1
    channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
  }
}