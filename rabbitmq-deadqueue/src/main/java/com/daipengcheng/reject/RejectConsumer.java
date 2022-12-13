package com.daipengcheng.reject;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.constants.RoutingKeyConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RejectConsumer {
    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            //声明死信交换机
            channel.exchangeDeclare(ExchangeConstants.DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", ExchangeConstants.DEAD_EXCHANGE);
            arguments.put("x-dead-letter-routing-key", RoutingKeyConstants.DEAD_KEY);
            //创建正常队列
            channel.queueDeclare(QueueConstants.NORMAL_QUEUE,false,false,false,arguments);
            //创建死信队列
            channel.queueDeclare(QueueConstants.DEAD_QUEUE,false,false,false,null);
            //正常交换机绑定正常队列
            channel.queueBind(QueueConstants.NORMAL_QUEUE,ExchangeConstants.NORMAL_EXCHANGE,RoutingKeyConstants.NORMAL_KEY);
            //死信交换机绑定死信队列
            channel.queueBind(QueueConstants.DEAD_QUEUE,ExchangeConstants.DEAD_EXCHANGE,RoutingKeyConstants.DEAD_KEY);
            DeliverCallback deliverCallback=((consumerTag, message) -> {
                String info=new String(message.getBody(), StandardCharsets.UTF_8);
                if (info.equals("INFO:5")) {
                    log.info("正常消费者拒绝消息,消息:{}",info);
                    channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
                }else {
                    log.info("正常消费者消费消息:{}",info);
                    channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                }
            });
            channel.basicConsume(QueueConstants.NORMAL_QUEUE,false,deliverCallback, (CancelCallback) null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
