/**
 * @Project: SEED V1.0
 * @Title: SHA.java
 * @Package com.seed.common.security
 */
package com.ml.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * SHA加密，单向加密产生唯一加密串，虽已被破解但仍被广泛用于电子商务等信息安全领域，比MD5更安全
 * </p>
 * 
 * <p>
 * NOTE:采用 SHA-1 加密算法
 * </p>
 * 
 * @version V1.0
 * @author metanoia.lang 2012-5-31 code源于网络
 * 
 */
public class SHACoder {
	private final static String	KEY_SHA_1	= "SHA-1";

	/**
	 * SHA加密得到字节数组
	 * 
	 * @param data
	 *            byte[] 待加密数据
	 * @return byte[]
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] encryptSHA(byte[] data)
			throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA_1);
		sha.update(data);
		return sha.digest();
	}

	/**
	 * SHA加密后的字节数组再交给BASE64加密得到最终的加密字符串
	 * 
	 * @param data
	 *            byte[] 待加密数据
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptSHAString(byte[] data)
			throws NoSuchAlgorithmException {
		return BASE64Coder.encodeBase64URLSafeString(SHACoder.encryptSHA(data));
	}

	private SHACoder() {
	}
}
