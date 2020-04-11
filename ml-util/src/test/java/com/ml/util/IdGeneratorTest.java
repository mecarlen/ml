package com.ml.util;

import org.junit.Test;

/**
 * <pre>
 * <b>ID生成器</b>
 * 
 * </pre>
 * 
 * @author mecarlen.Wang 2020年4月10日 下午11:02:12
 */
public class IdGeneratorTest {
	@Test
	public void uuid() {
		UUID.id32();
	}
	
	@Test
	public void snowflakeId() {
		SnowflakeID idWorker = new SnowflakeID(31, 31);
		for (int i = 0; i < 1000000; i++) {
			long id = idWorker.nextId();
			System.out.println(Long.toBinaryString(id));
			System.out.println(id);
		}
	}
}
