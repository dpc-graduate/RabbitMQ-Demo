package com.daipengcheng;

import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Producer {
    public static final Logger log = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            makeMessage(QueueConstants.QUEUE_HELLO_WORLD, "hello world" + i);
        }
    }

    static void makeMessage(String queue, String message) {
        Connection connection = RabbitMQConnectUtil.newConnection();
        try {
            Channel channel = connection.createChannel();
            channel.confirmSelect();
            /**
             * 创建队列
             * args1 队列名称
             * args2 是否持久化
             * args3 设置是否只能由一个消费者使用
             * args4 是否消费完成后删除队列
             * args5 其他参数
             */
            channel.queueDeclare(queue, true, false, false, null);
            /**
             * 发布消息
             * args1 交换机
             * args2 队列
             * args3 消息其他设置
             * args4 消息
             */
            channel.basicPublish("", queue, null, message.getBytes(StandardCharsets.UTF_8));
            if(channel.waitForConfirms()){
                log.info("消息成功发送到队列,队列:{},消息:{}", queue, message);
                RabbitMQConnectUtil.closeSource(channel, connection);
            }
        } catch (IOException e) {
            log.error("exception happen", e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
