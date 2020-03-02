package com.ml.mnc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

/**
 * <pre>
 * 应用启动类
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年9月2日 下午2:17:40
 */
@SpringBootApplication
@EnableEurekaClient
@ImportResource(locations = { "classpath:applicationContext.xml" })
public class ApplicationInitializer extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApplicationInitializer.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApplicationInitializer.class, args);
	}


	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		// 1.定义一个converters转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		// 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty);
		// 3.在converter中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		// 4.将converter赋值给HttpMessageConverter
		// 5.返回HttpMessageConverters对象
		return new HttpMessageConverters(fastConverter);
	}

	@Configuration
	static class UscConfig {
		@Value("${usc.admin.addresses}")
		private String adminAddresses;

		@Value("${usc.executor.appname}")
		private String appName;

		@Value("${usc.executor.ip}")
		private String ip;

		@Value("${usc.executor.port}")
		private int port;

		@Value("${usc.accessToken}")
		private String accessToken;

		@Value("${usc.executor.logpath}")
		private String logPath;

		@Value("${usc.executor.logretentiondays}")
		private int logRetentionDays;

		@Bean(initMethod = "start", destroyMethod = "destroy")
		public XxlJobSpringExecutor executor() {
			XxlJobSpringExecutor executor = new XxlJobSpringExecutor();

			executor.setAdminAddresses(adminAddresses);
			executor.setAppName(appName);
			executor.setIp(ip);
			executor.setPort(port);
			executor.setAccessToken(accessToken);
			executor.setLogPath(logPath);
			executor.setLogRetentionDays(logRetentionDays);

			return executor;
		}
	}
}
