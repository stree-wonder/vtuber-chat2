package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;

public class DlxDirectProducer {

    //定义死信交换机
  private static final String DEAD_EXCHANGE_NAME = "dead_direct-exchange";
  //定义工作交换机
  private static final String WORK_EXCHANGE_NAME = "work_direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
      try (Connection connection = factory.newConnection();
           Channel channel = connection.createChannel()) {
          //声明死信交换机
          channel.exchangeDeclare(DEAD_EXCHANGE_NAME, "direct");

          //创建老板的死信队列
          String queueName1 = "bossquene";
          channel.queueDeclare(queueName1, true, false, false, null);
          channel.queueBind(queueName1, DEAD_EXCHANGE_NAME, "boss");
          //创建外包的死信队列
          String queueName2 = "waibaoquene";
          channel.queueDeclare(queueName2, true, false, false, null);
          channel.queueBind(queueName2, DEAD_EXCHANGE_NAME, "waibao");

          //创建用于处理老板死信队列的回调函数
          DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
              String message = new String(delivery.getBody(), "UTF-8");
              //拒绝消息，不放回队列，只拒绝当前消息
              channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
              System.out.println(" [bossquene] Received '" + message + "'");
          };
          //创建用于处理外包死信队列的回调函数
          DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
              String message = new String(delivery.getBody(), "UTF-8");
              //拒绝消息，不放回队列，只拒绝当前消息
              channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
              System.out.println(" [waibaoquene] Received '" + message + "'");
          };

          //注册消费者，消费老板队列
          channel.basicConsume(queueName1, false, deliverCallback1, consumerTag -> { });
          //注册消费者，消费外包队列,自动回复false
          channel.basicConsume(queueName2, false, deliverCallback2, consumerTag -> { });

          Scanner sc=new Scanner(System.in);
          while(sc.hasNext()){
              String userinput = sc.nextLine();
              String[] strings=userinput.split(" ");
              if(strings.length<1){
                  continue;
              }
              String message=strings[0];
              String routingKey=strings[1];
              //发布消息到交换机，
              channel.basicPublish(WORK_EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
              //打印发送的信息
              System.out.println(" [x] Sent '" + message + "'");
          }
      }
  }
  //..
}