package example.day17_;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean(name = "queueExecutor")
    public ThreadPoolTaskExecutor queueExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 동시에 오직 1개만 실행되도록 설정
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        // 대기열 큐 용량 설정
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("QueueWorker-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}