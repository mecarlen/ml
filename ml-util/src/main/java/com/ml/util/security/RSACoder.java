package com.ml.util.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 非对称加密算法 - RSA
 * 
 * </pre>
 * 
 * @author mecarlen.wang 2018年4月8日 下午7:45:20
 */
public class RSACoder {
	/** AES算法 */
	public static final String ALGORITHM = "RSA";
	/** 公钥 */
	public static final String PUBLIC_KEY = "RSA_Public_Key";
	/** 私钥 */
	public static final String PRIVATE_KEY = "RSA_Private_Key";
	// logger
	private static Logger RSACODER_LOGGER = LoggerFactory.getLogger(RSACoder.class);

	/**
	 * <pre>
	 * 初始化密钥
	 * 
	 * </pre>
	 * 
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(1024);// 512-65536 & 64的倍数
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * <pre>
	 * 获取公钥
	 * 
	 * </pre>
	 * 
	 * @return RSAPublicKey
	 */
	public static RSAPublicKey getPublicKey(Map<String, Object> keyMap) {
		return (RSAPublicKey) keyMap.get(PUBLIC_KEY);
	}

	/**
	 * <pre>
	 * 获取私钥
	 * 
	 * </pre>
	 * 
	 * @return RSAPrivateKey
	 */
	public static RSAPrivateKey getPrivateKey(Map<String, Object> keyMap) {
		return (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
	}

	/**
	 * <pre>
	 * 使用公钥对数据进行加密
	 * 
	 * </pre>
	 * 
	 * @param data
	 *            byte[]
	 * @param publicKey
	 *            RSAPublicKey
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			RSACODER_LOGGER.error("encrypt byte[] exception", ex);
			return null;
		}
	}

	public static String encrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return BASE64Coder.encodeBase64URLSafeString(cipher.doFinal(data.getBytes()));
		} catch (Exception ex) {
			RSACODER_LOGGER.error("encrypt String exception", ex);
			return null;
		}
	}

	/**
	 * <pre>
	 * 使用私钥解密
	 * 
	 * </pre>
	 * 
	 * @param data
	 *            byte[]
	 * @param privateKey
	 *            RSAPrivateKey
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			RSACODER_LOGGER.error("decrypt byte[] exception", ex);
			return null;
		}
	}

	public static String decrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(BASE64Coder.decodeBase(data)));
		} catch (Exception ex) {
			RSACODER_LOGGER.error("decrypt byte[] exception", ex);
			return null;
		}
	}
}
