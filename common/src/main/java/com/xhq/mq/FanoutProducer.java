package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

public class FanoutProducer {

  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner sc=new Scanner(System.in);
        while(sc.hasNext()){
            String message = sc.nextLine();
            //发布消息到交换机，
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            //打印发送的信息
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
  }
}