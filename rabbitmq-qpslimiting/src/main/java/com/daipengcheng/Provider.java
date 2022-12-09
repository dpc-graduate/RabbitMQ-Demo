package com.daipengcheng;

import com.daipengcheng.config.RabbitMQThreadPool;
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
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class Provider {
    public static final int total = 2000;

    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QueueConstants.SLOW_QUEUE, true, false, false, null);
            channel.exchangeDeclare(ExchangeConstants.FAST_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueBind(QueueConstants.SLOW_QUEUE, ExchangeConstants.FAST_EXCHANGE, RoutingKeyConstants.VIP_KEY);
            ThreadPoolExecutor threadPool = RabbitMQThreadPool.createThreadPool();
            for (int i = 0; i < total; i++) {
                final int temp=i;
                threadPool.submit(() -> {
                    String message = Thread.currentThread().getName() + ":信息标识标识 " + temp;
                    try {
                        channel.basicPublish(ExchangeConstants.FAST_EXCHANGE, RoutingKeyConstants.VIP_KEY, null, message.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        log.info("error happens{}", e.getMessage());
                    }
                });
            }
            threadPool.shutdown();
            while (!threadPool.isTerminated()) {

            }
            log.info("所有消息已经发送完成");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
