package com.daipengcheng;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Provider {
    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(ExchangeConstants.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-max-priority", 10);
            channel.queueDeclare(QueueConstants.NORMAL_QUEUE, false, false, false, arguments);
            channel.queueBind(QueueConstants.NORMAL_QUEUE, ExchangeConstants.FANOUT_EXCHANGE, "", null);
            for (int i = 0; i < 10; i++) {
                String message = "第" + i + "条消息";
                if (i % 5 == 0) {
                    AMQP.BasicProperties properties = new AMQP.BasicProperties()
                            .builder().priority(5).build();         //设置消息优先级为5
                    channel.basicPublish(ExchangeConstants.FANOUT_EXCHANGE, "", properties, message.getBytes(StandardCharsets.UTF_8));

                } else {
                    channel.basicPublish(ExchangeConstants.FANOUT_EXCHANGE, "", null, message.getBytes(StandardCharsets.UTF_8));
                }

            }
            log.info("消息全部发送");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
