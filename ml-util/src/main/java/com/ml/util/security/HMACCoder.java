/**
 * @Project: SEED V1.0
 * @Title: HMAC.java
 * @Package com.seed.common.security
 */
package com.ml.util.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>
 * HMAC加密，单项加密产生唯一加密串，与SHA、MD5区别在于可指定KEY作为加密参数参与到加密过程中，<br>
 * 通过引入自定义KEY，增强安全性。
 * </p>
 * 
 * @version V1.0
 * @author metanoia.lang 2012-6-1 code源于网络
 * 
 */
public class HMACCoder {
	private final static String	KEY_MAC	= "HmacSHA1";

	/**
	 * 
	 * <p>
	 * 获得HMAC KEY值
	 * </p>
	 * 
	 * @return String 每次获得的KEY不同
	 * @throws NoSuchAlgorithmException
	 */
	public static String initMacKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
		SecretKey secretKey = keyGenerator.generateKey();
		return BASE64Coder.encodeBase64URLSafeString(secretKey.getEncoded());
	}

	/**
	 * <p>
	 * HMAC加密
	 * </p>
	 * 
	 * @param data
	 *            byte[]
	 * @param key
	 *            String MAC KEY
	 * @return byte[]
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] encryptHMAC(byte[] data, String key)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKey secretKey = new SecretKeySpec(key.getBytes(), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);

	}

	/**
	 * 
	 * <p>
	 * HMAC加密后的字节数组再交给BASE64加密得到最终的加密字符串
	 * </p>
	 * 
	 * @param data
	 *            byte[] data 待加密数据
	 * @param key
	 *            String MAC KEY
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptHMACString(byte[] data, String key)
			throws InvalidKeyException, NoSuchAlgorithmException {
		return BASE64Coder.encodeBase64URLSafeString(HMACCoder.encryptHMAC(data, key));
	}

	private HMACCoder() {
	}
}
