package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DlxDirectComsumer {
  //定义死信交换机
  private static final String DEAD_EXCHANGE_NAME = "dead_direct-exchange";
  //定义工作交换机
  private static final String WORK_EXCHANGE_NAME = "work_direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    //建立连接
    Connection connection = factory.newConnection();
    //创建通道
    Channel channel = connection.createChannel();

    //声明工作交换机
    channel.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");

    //创建队列小狗工作队列
    Map<String,Object> args=new HashMap<>();
    //设置队列的死信消息处理的交换机的参数
    args.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME);
    //指定死信的处理队列
    args.put("x-dead-letter-routing-key","boss");

    String queueName1 = "xiaogou_quene";
    channel.queueDeclare(queueName1, true, false, false, args);
    channel.queueBind(queueName1, WORK_EXCHANGE_NAME, "q1");

    //创建队列小猫工作队列
    Map<String,Object> args2=new HashMap<>();
    //设置队列的死信消息处理的交换机的参数
    args2.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME);
    //指定死信的处理队列
    args2.put("x-dead-letter-routing-key","waibao");

    String queueName2 = "xiaomao_quene";
    channel.queueDeclare(queueName2, true, false, false, args2);
    channel.queueBind(queueName2, WORK_EXCHANGE_NAME, "q2");


    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    //创建交付回调函数1
    DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        //拒绝消息，使其放到死信队列
      channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
        System.out.println(" [xiaogou_quene1] Received '" + message + "'");
    };
    //创建交付回调函数2
    DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      //拒绝消息，使其放到死信队列
      channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
      System.out.println(" [xiaomao_quene2] Received '" + message + "'");
    };

    //开始消费消息队列1 ,自动确认设置为否
    channel.basicConsume(queueName1, false, deliverCallback1, consumerTag -> { });
    //开始消费消息队列2,自动确认设置为否
    channel.basicConsume(queueName2, false, deliverCallback2, consumerTag -> { });

  }
}