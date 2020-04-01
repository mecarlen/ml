/**
 * @Project: SEED V1.0
 * @Title: MD5.java
 * @Package com.seed.common.security
 */
package com.ml.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * MD5加密，单向加密产生唯一加密串，已被破解
 * </p>
 * 
 * @version V1.0
 * @author metanoia.lang 2012-5-31 code源于网络
 * 
 */
public class MD5Coder {
	private final static String	KEY_MD5	= "MD5";

	/**
	 * 
	 * <p>
	 * MD5加密得到字节数组
	 * </p>
	 * 
	 * @param data
	 *            byte[] 待加密数据
	 * @return byte[]
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] encryptMD5(byte[] data)
			throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		return md5.digest(data);
	}

	/**
	 * 
	 * <p>
	 * MD5加密后的字节数组再交给BASE64加密得到最终的加密字符串
	 * </p>
	 * 
	 * @param data
	 *            byte[] 待加密数据
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String entryptMD5String(byte[] data)
			throws NoSuchAlgorithmException {
		return BASE64Coder.encodeBase64URLSafeString(MD5Coder.encryptMD5(data));
	}

	private MD5Coder() {
	}
}
