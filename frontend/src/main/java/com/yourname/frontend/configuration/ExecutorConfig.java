package com.yourname.frontend.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description spring 线程池配置
 * 如果在@Aysnc中没有指定线程池，会默认使用spring提供的默认线程池SimpleAsyncTaskExecutor。
 * 缺点：线程池为每个任务都单独创建一个线程，不会重用线程。这样，就跟最原始实现异步的方式有
 * 了相同的缺点：异步处理业务太多时，同时运行的线程太多，可能导致服务器崩溃。因此，我们一定
 * 要创建自己的线程池，并给异步任务指定到哪个线程池去执行。
 *
 * @Author dell
 * @Date 12/3/2022 8:49 AM
 */
@Configuration
@Slf4j
public class ExecutorConfig {

    @Bean
    public Executor asyncServiceExecutor() {
        log.info("开始线程池：asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(50);
        //配置最大线程数
        executor.setMaxPoolSize(100);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-task-");
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;

    }
}
