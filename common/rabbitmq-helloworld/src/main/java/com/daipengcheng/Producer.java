package com.daipengcheng;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer {

    public static void main(String[] args) {
        try {
            Connection connection = RabbitMQConnectUtil.newConnection();
            Channel channel = connection.createChannel();
//            channel.exchangeDeclare(ExchangeConstants.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
//            channel.queueDeclare("dai", false, false, false, null);
//            channel.queueDeclare("zhu", false, false, false, null);
//            channel.queueBind("dai",ExchangeConstants.DIRECT_EXCHANGE,"key");
//            channel.queueBind("zhu",ExchangeConstants.DIRECT_EXCHANGE,"key");
//            for (int i = 0; i < 10; i++) {
//                if(i%2==0){
//                    channel.basicPublish(ExchangeConstants.DIRECT_EXCHANGE,"key",null,"hello world".getBytes(StandardCharsets.UTF_8));
//                }else {
//                    channel.basicPublish(ExchangeConstants.DIRECT_EXCHANGE,"key1",null,"hello world".getBytes(StandardCharsets.UTF_8));
//                }
//            }
            channel.queueDeclare("liu", false, false, false, null);
           channel.queueBind("liu", ExchangeConstants.DIRECT_EXCHANGE,"key1");
        } catch (Exception e) {

        }
    }
}
