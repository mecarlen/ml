package com.ml.util.security;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * AES 对称加密算法实现
 * 
 * </pre>
 * 
 * @author mecarlen.wang 2018年4月4日 上午9:58:47
 */
public class AESCoder {

	/**  AES算法 */
	public static final String ALGORITHM = "AES";

	// logger
	private static Logger AESCODER_LOGGER = LoggerFactory.getLogger(AESCoder.class);

	/**
	 * <pre>
	 * 转换密钥<br>
	 * 
	 * </pre>
	 * @param key byte[]
	 * @return Key
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		SecureRandom random = new SecureRandom();
		random.setSeed(key);
		keyGenerator.init(random);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}

	/**
	 * <pre>
	 * 加密
	 * 
	 * </pre>
	 * 
	 * @param data
	 *            byte[]
	 * @param key
	 *            String
	 * @return byte[]
	 */
	public static byte[] encrypt(byte[] data, String key) {
		if (null == data)
			return null;
		try {
			Key k = toKey(BASE64Coder.decodeBase(key));
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, k);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			AESCODER_LOGGER.error("encrypt byte[] exception", ex);
			return null;
		}
	}

	public static String encrypt(String data, String key) {
		if (data == null)
			return null;
		try {
			byte[] inputsData = data.getBytes();
			String ekey = AESCoder.initKey(key);
			inputsData = AESCoder.encrypt(inputsData, ekey);
			return BASE64Coder.encodeBase64URLSafeString(inputsData);
		} catch (Exception ex) {
			AESCODER_LOGGER.error("encrypt String exception", ex);
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] decrypt(byte[] data, String key) {
		if (null == data)
			return null;
		try {
			Key k = toKey(BASE64Coder.decodeBase(key));
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, k);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			AESCODER_LOGGER.error("decrypt byte[] exception", ex);
			return null;
		}
	}

	public static String decrypt(String data, String key) {
		if (data == null)
			return null;
		try {
			byte[] inputData = BASE64Coder.decodeBase(data);
			String ekey = AESCoder.initKey(key);
			inputData = AESCoder.decrypt(inputData, ekey);
			return new String(inputData);
		} catch (Exception ex) {
			AESCODER_LOGGER.error("decrypt String exception", ex);
			return null;
		}
	}

	/**
	 * <pre>
	 * 生成密钥
	 * 
	 * </pre>
	 * 
	 * @return String
	 * @throws Exception
	 */
	public static String initKey() throws Exception {
		return initKey(null);
	}

	/**
	 * 生成密钥
	 * 
	 * @param seed
	 *            String
	 * @return
	 * @throws Exception
	 */
	public static String initKey(String seed) throws Exception {
		SecureRandom secureRandom = null;

		if (seed != null) {
			secureRandom = new SecureRandom(BASE64Coder.decodeBase(seed));
		} else {
			secureRandom = new SecureRandom();
		}

		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);

		SecretKey secretKey = kg.generateKey();

		return BASE64Coder.encodeBase64URLSafeString(secretKey.getEncoded());
	}

}
