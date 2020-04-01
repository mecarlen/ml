package com.ml.mnc;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <pre>
 * Junit基础类
 * 
 * </pre>
 * 
 * @author mecarlen 2018年8月6日 下午4:14:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = { "classpath:applicationContext-repository-test.xml" })
public class RepositoryTest {
	
}
