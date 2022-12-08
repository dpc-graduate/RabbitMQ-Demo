package com.daipengcheng.fanout;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {
    static Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue, ExchangeConstants.FANOUT_EXCHANGE, "");
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                log.info("消费者开始消费消息:{}", new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            };
            CancelCallback cancelCallback = consumerTag -> {
                log.info("消费者取消消费");
            };
            channel.basicConsume(queue, false, deliverCallback, cancelCallback);
        } catch (Exception e) {
            log.error("error happens:{}", e.getMessage());
        }
    }
}
