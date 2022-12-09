package com.daipengcheng.maxlengthttl;

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
public class NormalConsumer {
    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            //声明正常交换机
            channel.exchangeDeclare(ExchangeConstants.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            //声明死信交换机
            channel.exchangeDeclare(ExchangeConstants.DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            /*创建队列
             * 通过额外参数实现什么情况下转发到死信队列 ？,key都是固定的
             *   1、TTL过期时间设置(一般由生产者指定)
             *   2、死信交换机的名称
             *   3、死信交换机的RoutingKey
             * */
            Map<String, Object> arguments = new HashMap<>(8);
            //死信交换机的名称
            arguments.put("x-dead-letter-exchange", ExchangeConstants.DEAD_EXCHANGE);
            //死信交换机的RoutingKey
            arguments.put("x-dead-letter-routing-key", RoutingKeyConstants.DEAD_KEY);
            //指定队列能够积压消息的大小，超出该范围的消息将进入死信队列
            arguments.put("x-max-length", 6);
            //正常队列
            channel.queueDeclare(QueueConstants.NORMAL_QUEUE, false, false, false, arguments);
            //死信队列
            channel.queueDeclare(QueueConstants.DEAD_QUEUE, false, false, false, null);
            //正常交换机绑定到正常队列
            channel.queueBind(QueueConstants.NORMAL_QUEUE, ExchangeConstants.NORMAL_EXCHANGE, RoutingKeyConstants.NORMAL_KEY);
            //死信交换机绑定到死信队列
            channel.queueBind(QueueConstants.DEAD_QUEUE, ExchangeConstants.DEAD_EXCHANGE, RoutingKeyConstants.DEAD_KEY);
            DeliverCallback successBack = (consumerTag, message) -> {
                log.info("NormalConsumer用户接收到的信息为:{}", new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            };
            CancelCallback cancelCallback = a -> {
                log.info("NormalConsumer用户进行取消消费操作!");
            };
            //这里autoAck 为false,并且消费的时候不ack,如果超过六条就会进入死信队列
            channel.basicConsume(QueueConstants.NORMAL_QUEUE, false, successBack, cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
