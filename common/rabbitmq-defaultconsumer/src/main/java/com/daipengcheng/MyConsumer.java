package com.daipengcheng;


import com.daipengcheng.config.DefaultConsumerConfig;
import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class MyConsumer {


    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQConnectUtil.newConnection();
        Channel channel = connection.createChannel();
        channel.basicConsume(QueueConstants.QUEUE_HELLO_WORLD, new DefaultConsumerConfig(channel));
    }
}
