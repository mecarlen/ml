/**
 * @Project: SEED V1.0
 * @Title: PBECode.java
 * @Package com.seed.common.security
 */
package com.ml.util.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * <p>
 * 对称加密-解密算法<br>
 * 
 * PBE-Password based encryption(基于密码加密)<br>
 * 口令由用户自己保管，采用随机数(通常叫盐)杂凑多重加密等方法保证数据安全性，一种简便加密方式。
 * </p>
 * 
 * @version V1.0
 * @author metanoia.lang 2012-6-11 code源于网络
 * 
 */
public class PBECoder {
	/**
	 * 支持以下任意一种算法
	 * 
	 * <p>
	 * PBEWithMD5AndDES;PBEWithMD5AndTripleDES;PBEWithSHA1AndDESede;
	 * PBEWithSHA1AndRC2_40
	 * </p>
	 */
	public static final String	ALGORITHM	= "PBEWithSHA1AndDESede";

	/**
	 * 
	 * <p>
	 * 初始化盐
	 * </p>
	 * 
	 * @return byte[]
	 */
	public static byte[] initSalt() {
		byte[] salt = new byte[8];
		Random random = new Random();
		random.nextBytes(salt);
		return salt;
	}

	/**
	 * 
	 * <p>
	 * 转换密钥
	 * </p>
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static Key toKey(String password) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		return secretKey;
	}

	/**
	 * 
	 * <p>
	 * 加密
	 * </p>
	 * 
	 * @param data
	 *            byte[] 被加密数据
	 * @param password
	 *            String 密码
	 * @param salt
	 *            byte[] 盐
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String password, byte[] salt)
			throws Exception {
		Key key = toKey(password);

		PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		return cipher.doFinal(data);
	}

	/**
	 * 
	 * <p>
	 * 加密
	 * </p>
	 * 
	 * @param data
	 *            byte[] 被加密数据
	 * @param password
	 *            String 密码
	 * @param salt
	 *            byte[] 盐
	 * @return String 密文经BASE64加密
	 * @throws Exception
	 */
	public static String encryptString(byte[] data, String password, byte[] salt)
			throws Exception {
		return BASE64Coder.encodeBase64URLSafeString(encrypt(data, password, salt));
	}

	/**
	 * 
	 * <p>
	 * 解密
	 * </p>
	 * 
	 * @param data
	 *            byte[] 密文
	 * @param password
	 *            String 密码
	 * @param salt
	 *            byte[] 盐
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String password, byte[] salt)
			throws Exception {
		Key key = toKey(password);

		PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		return cipher.doFinal(data);
	}

	/**
	 * 
	 * <p>
	 * 解密
	 * </p>
	 * 
	 * @param data
	 *            String 密文(经BASE64加密)
	 * @param password
	 *            String 密码
	 * @param salt
	 *            byte[] 盐
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decryptString(String dataStr, String password,
			byte[] salt) throws Exception {
		byte[] data = BASE64Coder.decodeBase(dataStr);
		return decrypt(data, password, salt);
	}
}
