package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

import java.util.Scanner;

public class MultiProducer {

  //定义队列名称
  private static final String TASK_QUEUE_NAME = "task_queue";

  public static void main(String[] argv) throws Exception {
    //创建一个连接工厂
    ConnectionFactory factory = new ConnectionFactory();
    //设置rabbitmq主机的名称
    factory.setHost("localhost");
    //创建一个新的连接
    try (Connection connection = factory.newConnection();
         //创建一个新频道
         Channel channel = connection.createChannel()) {
        //声明队列参数，包括队列名称，是否持久化，
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        Scanner sc=new Scanner(System.in);
        while(sc.hasNext()){
            String message = sc.nextLine();
            //发布消息到队列，设置信息持久化
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }

    }
  }

}