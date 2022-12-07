package com.daipengcheng;

import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Producer {
    public static final Logger log= LoggerFactory.getLogger(Producer.class);
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQConnectUtil.newConnection();
        log.info(String.valueOf(connection));
    }
}
