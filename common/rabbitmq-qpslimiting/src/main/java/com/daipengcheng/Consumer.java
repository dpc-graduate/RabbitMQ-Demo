package com.daipengcheng;

import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class Consumer {


    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQConnectUtil.newConnection();
        Channel channel = connection.createChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);  //模拟实际业务操作
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println("消费者接收到的信息为:" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        /*
         * 参数一：单条消息大小限制，一般为0（不限制）
         * 参数二：一次性消费的消息数量。会告诉 RabbitMQ 不要同时给一个消费者推送多于 N 个消息，即一旦有 N 个消息还没有 ack，则该 consumer 将 block 掉，直到积累的消息数 ack 到能接受新消息
         *     在 no_ask=false 的情况下才生效，即在自动应答的情况下这两个值是不生效的。
         *     一般设置为 1
         * 参数三：限流设置应用于 channel(true) 还是 consumer (false)
         *     通常设置为false，因为 channel 级别限流RabbitMQ当前版本尚未实现且多数情况下是共用一个 channel
         */
        //设置限流操作
        channel.basicQos(0, 10, false);
        //这里设置为 false,且消费代码没有设置应答才会生效,设置了应答就不会生效
        channel.basicConsume(QueueConstants.SLOW_QUEUE, false, deliverCallback, (CancelCallback) null);      //手动应答消费消息
    }
}
