package com.daipengcheng;

import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DeadConsumer {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQConnectUtil.newConnection();
        Channel channel = connection.createChannel();
        DeliverCallback successBack = (consumerTag, message) -> {
            log.info("DeadConsumer用户接收到的信息为:{}", new String(message.getBody()));
        };
        CancelCallback cancelCallback = a -> {
            log.info("DeadConsumer用户进行取消消费操作!");
        };
        channel.basicConsume(QueueConstants.DEAD_QUEUE, true, successBack, cancelCallback);
    }
}
