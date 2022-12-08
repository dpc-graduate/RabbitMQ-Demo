package com.daipengcheng.direct;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.RoutingKeyConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer {
    static void consumer(String consumer, String exchange, String routingKey) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue, exchange, routingKey);
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                log.info("消费者:{},消费消息:{}", consumer, new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(queue, false, deliverCallback, (CancelCallback) null);
        } catch (Exception e) {
            log.error("error happens:{}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        consumer("普通用户", ExchangeConstants.DIRECT_EXCHANGE, RoutingKeyConstants.NORMAL_KEY);
        consumer("会员用户", ExchangeConstants.DIRECT_EXCHANGE, RoutingKeyConstants.VIP_KEY);
        consumer("超级用户", ExchangeConstants.DIRECT_EXCHANGE, RoutingKeyConstants.SUPER_VIP_KEY);
    }
}
