package com.daipengcheng.config;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RabbitMQThreadPool {
    private RabbitMQThreadPool(){}
    public static ThreadPoolExecutor createThreadPool() {
        return new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    }
}
