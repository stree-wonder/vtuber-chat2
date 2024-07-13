package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class FanoutComsumer {
  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    //建立连接
    Connection connection = factory.newConnection();
    //创建两个通道
    Channel channel1 = connection.createChannel();
    Channel channel2 = connection.createChannel();

    //声明交换机
    channel1.exchangeDeclare(EXCHANGE_NAME, "fanout");
    //创建队列1
    String queueName1 = "fanout_quene1";
    channel1.queueDeclare(queueName1, true, false, false, null);
    channel1.queueBind(queueName1, EXCHANGE_NAME, "");
    //创建队列2
    String queueName2 = "fanout_quene2";
    channel2.queueDeclare(queueName2, true, false, false, null);
    channel2.queueBind(queueName2, EXCHANGE_NAME, "");
    channel2.exchangeDeclare(EXCHANGE_NAME, "fanout");



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
    channel1.basicConsume(queueName1, true, deliverCallback1, consumerTag -> { });
    //开始消费消息队列1
    channel2.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
  }
}