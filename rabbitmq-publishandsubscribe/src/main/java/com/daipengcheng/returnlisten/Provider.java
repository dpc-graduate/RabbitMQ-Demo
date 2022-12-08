package com.daipengcheng.returnlisten;

import com.daipengcheng.constants.ExchangeConstants;
import com.daipengcheng.util.RabbitMQConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*生产者代码*/
public class Provider {

    private static final String NOKEY = "Nokey";           //普通用户


    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQConnectUtil.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ExchangeConstants.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        /*添加消息ReturnListener*/
        /*参数详解：
         *   replyCode:  路由是否成功的响应码
         *   replyText:  文本说明
         *   exchange：   具体路由的交换机
         *   routingKey： 路由Key
         *   properties： 消息配置，可指定消息具体参数（SpringBoot章节详细讲解）
         *
         * */
        channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
            System.out.println("ReturnListener获到不可路由消息!");
            System.out.println("replyCode: " + replyCode);
            System.out.println("replyText: " + replyText);
            System.out.println("exchange: " + exchange);
            System.out.println("routingKey: " + routingKey);
            System.out.println("properties: " + properties);
            System.out.println("body: " + new String(body));
        });

        /*要指定 mandatory 为 true，默认的 false为 RabbitMQ自动删除不可路由消息 */
        channel.basicPublish(ExchangeConstants.DIRECT_EXCHANGE, NOKEY, true, null, "这是一条测试消息".getBytes(StandardCharsets.UTF_8));
    }

}
class consumer{
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQConnectUtil.newConnection();
        Channel channel = connection.createChannel();
        String queue = channel.queueDeclare().getQueue();
        //绑定了监听就不会生效了
        channel.queueBind(queue,ExchangeConstants.DIRECT_EXCHANGE,"Nokey");
    }
}