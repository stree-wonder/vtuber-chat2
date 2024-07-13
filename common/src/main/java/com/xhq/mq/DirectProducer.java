package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class DirectProducer {

  private static final String EXCHANGE_NAME = "direct-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
      try (Connection connection = factory.newConnection();
           Channel channel = connection.createChannel()) {
          channel.exchangeDeclare(EXCHANGE_NAME, "direct");

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
              channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
              //打印发送的信息
              System.out.println(" [x] Sent '" + message + "'");
          }
      }
  }
  //..
}