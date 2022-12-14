package com.daipengcheng.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class AppStartUpTest {
    @Resource
    private RabbitAdmin rabbitAdmin;

    @Test
    public void declareExchange() {
        rabbitAdmin.declareExchange(new DirectExchange("adminFanoutEx", false, false, null));
        rabbitAdmin.declareExchange(new DirectExchange("adminDirectEx", false, false, null));
        rabbitAdmin.declareExchange(new DirectExchange("adminTopicEx", false, false, null));
    }

    @Test
    public void declareQueue() {
        rabbitAdmin.declareQueue(new Queue("adminFanoutQ", false, false, false));
        rabbitAdmin.declareQueue(new Queue("adminDirectQ", false, false, false));
        rabbitAdmin.declareQueue(new Queue("adminTopicQ", false, false, false));
    }

    @Test
    public void declareBind() {
        rabbitAdmin.declareBinding(new Binding("adminFanoutQ", Binding.DestinationType.QUEUE,
                "adminFanoutEx", "", null));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("adminDirectQ", false, false, false)).
                to(new DirectExchange("adminDirectEx", false, false, null)).with("adminDirectK"));
        rabbitAdmin.declareBinding(new Binding("adminTopicQ", Binding.DestinationType.QUEUE,
                "adminTopicEx", "adminDirectK.*", null));
    }
}
