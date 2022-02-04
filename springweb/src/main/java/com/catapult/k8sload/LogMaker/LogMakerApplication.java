package com.catapult.k8sload.LogMaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.catapult.k8sload.LogMaker.GenerateLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootApplication
@RestController
@EnableAsync
public class LogMakerApplication {
    private final static Logger logger = LoggerFactory.getLogger(LogMakerApplication.class);
    @Autowired
    private GenerateLogService generateLogService;
    public static void main(String[] args) {
        SpringApplication.run(LogMakerApplication.class, args);
    }

    @GetMapping("/load")
    public String load(@RequestParam(value = "size", defaultValue = "small") String size,
                        @RequestParam(value = "delay", defaultValue = "10000") String delay) throws InterruptedException{
        generateLogService.generateLogs(size, delay);
        return "Started";
    }

    @GetMapping("/stopload")
    public String stopload() {
        return generateLogService.stoplog();
    }


    @GetMapping("/health")
    public String health() throws UnknownHostException {
        String msg = String.format("I am healthy ! - from host %s", InetAddress.getLocalHost().getHostName());
        logger.info(msg);
        return msg;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "Test") String name) {
        logger.info(String.format("Hello %s!", name));
        return String.format("Hello %s!", name);
    }

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }

}
