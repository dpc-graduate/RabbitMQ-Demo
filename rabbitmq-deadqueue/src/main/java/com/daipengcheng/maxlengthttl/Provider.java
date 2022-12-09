package com.daipengcheng.maxlengthttl;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.RoutingKeyConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Provider {
    public static final int total = 11;

    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            //声明正常交换机
            channel.exchangeDeclare(ExchangeConstants.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
            for (int i = 0; i < total; i++) {
                String message = "INFO" + i;
                channel.basicPublish(ExchangeConstants.NORMAL_EXCHANGE, RoutingKeyConstants.NORMAL_KEY, properties, message.getBytes(StandardCharsets.UTF_8));
                log.info("生产了一条消息:{}",message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
