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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ml.base.controller.PageInterceptor;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
@EnableSwagger2
@ImportResource(locations = { "classpath:applicationContext.xml" })
public class ApplicationInitializer extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApplicationInitializer.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApplicationInitializer.class, args);
	}

	/**
	 * <pre>
	 * REST API doc
	 * 
	 * 描述
	 * API DOC支持的环境，dev-开发，test-测试,pre-预发
	 * 
	 * </pre>
	 */
	@Configuration
	@EnableSwagger2
	static class RestAPIDoc {
		@Value("${swagger.enable}")
		private boolean swaggerEnable;

		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2).enable(swaggerEnable).apiInfo(apiInfo()).select()
					.apis(RequestHandlerSelectors.basePackage("com.ml.mnc"))
					.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
					.build();

		}

		private ApiInfo apiInfo() {
			return new ApiInfoBuilder().title("MNC-admin").description("MNC管理端").version("1.0")
					.termsOfServiceUrl("http://mnc-dev.ml.com").license("LICENSE")
					.contact(new Contact("wangfenghua", "http://mnc-test.ml.com", "wangfenghua@gmail.com"))
					.build();
		}
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
		HttpMessageConverter<?> converter = fastConverter;
		// 5.返回HttpMessageConverters对象
		return new HttpMessageConverters(converter);
	}

	@Configuration
	public static class SpringMvcConfiguration implements WebMvcConfigurer {

		/**
		 * <pre>
		 * 添加拦截器
		 *
		 * </pre>
		 */
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// 分页
			registry.addInterceptor(new PageInterceptor()).addPathPatterns("/**/page/**");
		}

	}

}
