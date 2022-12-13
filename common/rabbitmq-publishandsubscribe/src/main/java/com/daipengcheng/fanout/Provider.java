package com.daipengcheng.fanout;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Provider {
    static Logger log = LoggerFactory.getLogger(Provider.class);

    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(ExchangeConstants.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                /**
                 * args1 交换机名称
                 * args2 路由key
                 * args3 消息持久化
                 * args4 消息
                 */
                channel.basicPublish(ExchangeConstants.FANOUT_EXCHANGE, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                log.info("消息发生完成:{}",message);
            }
        } catch (Exception e) {
            log.error("error happens:{}", e.getMessage());
        }
    }
}
