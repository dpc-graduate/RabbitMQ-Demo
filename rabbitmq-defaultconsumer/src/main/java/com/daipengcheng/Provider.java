package com.daipengcheng;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class Provider {
    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QueueConstants.QUEUE_HELLO_WORLD, true, false, false, null);
            channel.exchangeDeclare(ExchangeConstants.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT, true);
            channel.queueBind(QueueConstants.QUEUE_HELLO_WORLD, ExchangeConstants.DIRECT_EXCHANGE, "hello");
            Scanner scanner = new Scanner(System.in);       //从控制台输入消息内容
            while (scanner.hasNext()) {
                String message = scanner.next();
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                        .appId("12345")
                        .contentType("application/text")
                        .build();
                channel.basicPublish(ExchangeConstants.DIRECT_EXCHANGE, "hello", properties, message.getBytes(StandardCharsets.UTF_8));      //发送消息并将消息持久化到磁盘
                log.error("消息发送完成:{}", message);
            }
        } catch (IOException e) {
            log.error("error happens:{}", e.getMessage());
        }
    }
}
