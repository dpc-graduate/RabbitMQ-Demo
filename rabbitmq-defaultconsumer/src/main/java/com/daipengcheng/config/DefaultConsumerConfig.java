package com.daipengcheng.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DefaultConsumerConfig extends DefaultConsumer {
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public DefaultConsumerConfig(Channel channel) {
        super(channel);
    }

    @Override
    public void handleCancel(String consumerTag) {
        log.info("消费者取消消费");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        log.info("消费者对消息进行消费!");
        log.info("消费者接收到的consumerTag为:{}" + consumerTag);
        log.info("消费者接收到的envelope为:{}" + envelope);
        log.info("消费者接收到的信息配置为:{}" + properties);
        log.info("消费者接收到的信息为:{}" + new String(body));
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }
}
