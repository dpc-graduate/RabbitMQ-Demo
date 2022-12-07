package com.dmbjz.one;

import com.dmbjz.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

/*Topic模式 生产者案例*/
public class Provider {

    private static final String EXCHANGE_NAME = "TopicExchange";
    private static final String ORANGE_KEY= "*super.orange.cake";
    private static final String RABBIT_KEY = "baidu.ad.rabbit";
    private static final String LAZY_KEY = "lazy.user.admin";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        /*声明交换机
         * 参数一：交换机名称
         * 参数二：交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        /*模拟不同会员等级接收到的不同消息内容*/
        for (int i = 0; i < 9; i++) {
            String message = "当前是待发送的消息，序号:"+i;
            if(i%3==0){
                channel.basicPublish(EXCHANGE_NAME,ORANGE_KEY,null,message.getBytes(StandardCharsets.UTF_8));  //发送ORANGE消息
            }
            if(i%3==1){
                channel.basicPublish(EXCHANGE_NAME,RABBIT_KEY,null,message.getBytes(StandardCharsets.UTF_8));  //发送RABBIT消息
            }
            if(i%3==2){
                channel.basicPublish(EXCHANGE_NAME,LAZY_KEY,null,message.getBytes(StandardCharsets.UTF_8));   //发送LAZY消息
            }
            System.out.println("消息发送: " + message);
        }

    }


}
