package com.ml.src;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

/**
 * <pre>
 * 应用初始化加载
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年7月29日 下午1:16:20
 */
@SpringBootApplication
@EnableEurekaServer
public class ApplicationInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
    
    /**
     * <pre>
     * 修改SBA-Actuator展示最大并发请求数1000
     * </pre>
     * */
    @Bean
    public InMemoryHttpTraceRepository traceRepository() {
        InMemoryHttpTraceRepository traceRepository = new InMemoryHttpTraceRepository();
        traceRepository.setCapacity(1000);
        return traceRepository;
    } 
}
