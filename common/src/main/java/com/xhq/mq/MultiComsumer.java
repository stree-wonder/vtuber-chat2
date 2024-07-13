package com.xhq.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MultiComsumer {
  //定义队列名称
  private static final String TASK_QUEUE_NAME = "task_queue";

  public static void main(String[] argv) throws Exception {
      //创建一个连接工厂
    ConnectionFactory factory = new ConnectionFactory();
      //设置rabbitmq主机的名称
    factory.setHost("localhost");
      //创建一个新的连接
    final Connection connection = factory.newConnection();

      for (int i = 0; i < 2; i++) {
          //创建一个新频道
          final Channel channel = connection.createChannel();

          //声明队列参数，包括队列名称，持久化，非排他，非自动删除，其他参数；如果队列不存在，就创建他
          channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
          System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

          //。每个消费者同时可处理的最多消息数
          channel.basicQos(1);

          int finalI=i;
          DeliverCallback deliverCallback = (consumerTag, delivery) -> {
              String message = new String(delivery.getBody(), "UTF-8");

              try {
                  //接收信息，模拟处理任务
                  System.out.println(" [x] Received '编号："+finalI+":" + message + "'");
                  Thread.sleep(5000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              } finally {
                  //打印完成信息处理
                  System.out.println(" [x] doce '" + message + "'");
                  //手动发送应答，告诉mq消息已经被接受处理
                  channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
              }

          };
          //开始消费数据，设置队列名，是否自动确认，投递回调，消费者取消回调。
          channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
      }


  }

}