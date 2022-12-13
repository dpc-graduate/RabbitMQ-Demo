package com.daipengcheng;

import com.daipengcheng.constants.QueueConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Consumer {

    //队列名称

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQConnectUtil.newConnection();
        Channel channel = connection.createChannel();
        /*消费者成功消费回调逻辑*/
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            log.info("接收到的信息为:{}", new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);     //手动消息应答
        };
        /*消费者取消消费回调逻辑*/
        CancelCallback cancelCallback = a -> log.info("进行取消消费操作!");
        channel.basicConsume(QueueConstants.NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }
}
