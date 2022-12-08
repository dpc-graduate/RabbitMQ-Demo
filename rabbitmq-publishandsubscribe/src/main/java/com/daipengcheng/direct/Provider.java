package com.daipengcheng.direct;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.constants.RoutingKeyConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class Provider {
    public static void main(String[] args) {
        int total = 9;
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(ExchangeConstants.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
            for (int i = 0; i < total; i++) {
                String message = "当前是待发送的消息,序号:" + i;
                if (i % 3 == 0) {
                    channel.basicPublish(ExchangeConstants.DIRECT_EXCHANGE, RoutingKeyConstants.SUPER_VIP_KEY, null, message.getBytes(StandardCharsets.UTF_8));
                    log.info("超级会员消息发送成功:{}", message);
                }
                if (i % 3 == 1) {
                    channel.basicPublish(ExchangeConstants.DIRECT_EXCHANGE, RoutingKeyConstants.VIP_KEY, null, message.getBytes(StandardCharsets.UTF_8));
                    log.info("普通会员消息发送成功:{}", message);
                }
                if (i % 3 == 2) {
                    channel.basicPublish(ExchangeConstants.DIRECT_EXCHANGE, RoutingKeyConstants.NORMAL_KEY, null, message.getBytes(StandardCharsets.UTF_8));
                    log.info("一般用户消息发送成功:{}", message);
                }
            }
            RabbitMQConnectUtil.closeSource(channel, connection);
        } catch (Exception e) {
            log.error("error happens:{}", e.getMessage());
        }
    }
}
