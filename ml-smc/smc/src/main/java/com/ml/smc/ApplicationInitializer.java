package com.ml.smc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ImportResource;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * <pre>
 * 应用初始化加载
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2019年7月16日 下午4:00:25
 */
@SpringBootApplication
@EnableEurekaClient
@EnableAdminServer
@ImportResource(locations = {"classpath:applicationContext.xml"})
public class ApplicationInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
    /**
    @Configuration
    public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        private final String adminContextPath;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");
            successHandler.setDefaultTargetUrl(adminContextPath + "/");

            http.authorizeRequests()
                    //授予对所有静态资产和登录页面的公共访问权限。
                    .antMatchers(adminContextPath + "/assets/**").permitAll()
                    .antMatchers(adminContextPath + "/login").permitAll()
                    .antMatchers(adminContextPath + "/logout").permitAll()
                    //必须对每个其他请求进行身份验证
                    .anyRequest().authenticated()
                    .and()
                    //配置登录和注销
                    .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                    .logout().logoutUrl(adminContextPath + "/logout").and()
                    //启用HTTP-Basic支持。这是Spring Boot Admin Client注册所必需的
                    .httpBasic().and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  
                    .ignoringAntMatchers(
                        adminContextPath + "/instances",   
                        adminContextPath +"/actuator/**",
                        adminContextPath+"/hystrix/**"
                    );
            // @formatter:on
        }
    }*/
}
