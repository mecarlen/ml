package com.ml.mnc;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <pre>
 * 服务层基础Junit
 * 
 * </pre>
 * 
 * @author mecarlen 2018年11月5日 下午2:51:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = { "classpath:applicationContext-service-test.xml" })
public class ServiceTest {
	
}
