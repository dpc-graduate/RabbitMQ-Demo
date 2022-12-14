package com.daipengcheng;

import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {
    public static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        useMessage("liu");
//        useMessage("zhu");
    }

    static void useMessage(String queue) {
        Connection connection = RabbitMQConnectUtil.newConnection();
        try {
            Channel channel = connection.createChannel();
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                log.info("消费者:{}消费消息:{}", queue,new String(message.getBody()));
            };
            CancelCallback cancelCallback = consumerTag -> {
                log.info("消费者取消消费消息回调:{}", consumerTag);
            };
            /**
             * 消费消息
             * args1 队列
             * args2 是否自动ack
             * args3  消费回调
             * args4  取消消费回调
             */
            channel.basicConsume(queue, true, deliverCallback, cancelCallback);
            log.info("消费者消费完毕");
        } catch (Exception e) {
            log.error("error happens:{}", e.getMessage());
        }
    }
}
