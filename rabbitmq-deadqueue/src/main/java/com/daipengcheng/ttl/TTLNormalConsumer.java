package com.daipengcheng.ttl;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.constants.RoutingKeyConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TTLNormalConsumer {
    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            //正常交换机
            channel.exchangeDeclare(ExchangeConstants.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.exchangeDeclare(ExchangeConstants.DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", ExchangeConstants.DEAD_EXCHANGE);
            arguments.put("x-dead-letter-routing-key", RoutingKeyConstants.DEAD_KEY);
            arguments.put("x-dead-letter-ttl", 10000);
            //正常队列
            channel.queueDeclare(QueueConstants.NORMAL_QUEUE, false, false, false, arguments);
            //死信队列
            channel.queueDeclare(QueueConstants.DEAD_QUEUE, false, false, false, null);
            //绑定交换机到队列
            channel.queueBind(QueueConstants.NORMAL_QUEUE, ExchangeConstants.NORMAL_EXCHANGE, RoutingKeyConstants.NORMAL_KEY);
            channel.queueBind(QueueConstants.DEAD_QUEUE, ExchangeConstants.DEAD_EXCHANGE, RoutingKeyConstants.DEAD_KEY);
            //消费
            DeliverCallback deliverCallback = ((consumerTag, message) -> {
                log.info("正常消费者消费,信息:{}",new String(message.getBody()));
            });
            channel.basicConsume(QueueConstants.NORMAL_QUEUE,true,deliverCallback, (CancelCallback) null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
