package com.daipengcheng.maxlength;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.constants.RoutingKeyConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class MaxlengthProvider {
    public static final int total = 11;

    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            //声明正常交换机
            channel.exchangeDeclare(ExchangeConstants.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            //声明死信交换机
            channel.exchangeDeclare(ExchangeConstants.DEAD_EXCHANGE, BuiltinExchangeType.DIRECT,true);
            //死信队列
            channel.queueDeclare(QueueConstants.DEAD_QUEUE, false, false, false, null);
            //死信交换机绑定到死信队列
            channel.queueBind(QueueConstants.DEAD_QUEUE, ExchangeConstants.DEAD_EXCHANGE, RoutingKeyConstants.DEAD_KEY);
            for (int i = 0; i < total; i++) {
                String message = "INFO" + i;
                channel.basicPublish(ExchangeConstants.NORMAL_EXCHANGE, RoutingKeyConstants.NORMAL_KEY, null, message.getBytes(StandardCharsets.UTF_8));
                log.info("生产了一条消息:{}",message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
