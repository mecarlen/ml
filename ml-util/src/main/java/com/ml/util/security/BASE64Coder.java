/**
 * @Project: SEED V1.0
 * @Title: Base64.java
 * @Package com.seed.common.security
 */
package com.ml.util.security;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * 按照RFC2045的定义，Base64被定义为：Base64内容传送编码被设计用来把任意序列的8位字<br>
 * 节描述为 一种不易被人直接识别的形式。（The Base64 Content-Transfer-Encoding is <br>
 * designed to represent arbitrary sequences of octets in a form that need not<br>
 * be humanly readable.）<br>
 * BASE64 常用于邮件、HTTP加密
 * </p>
 * 
 * <p>
 * NOTE: 利用apache commons-codec-1.6包实现
 * </p>
 * 
 * @version V1.0
 * @author metanoia.lang 2012-5-31 code源于网络
 * 
 */
public class BASE64Coder {

	/**
	 * <p>
	 * Encodes binary data using the base64 algorithm but does not chunk the
	 * output.
	 * </p>
	 * 
	 * @param binaryData
	 *            byte[] binary data to encode
	 * @return byte[] containing Base64 characters in their UTF-8 representation
	 */
	public static byte[] encodeBase64(byte[] binaryData) {
		return Base64.encodeBase64(binaryData);
	}

	/**
	 * 
	 * <p>
	 * Encodes binary data using a URL-safe variation of the base64 algorithm
	 * but does not chunk the output. The url-safe variation emits - and _
	 * instead of + and / characters.
	 * </p>
	 * 
	 * @param binaryData
	 *            byte[] binary data to encode
	 * @return byte[] containing Base64 characters in their UTF-8
	 *         representation.
	 */
	public static byte[] encodeBase64URLSafe(byte[] binaryData) {
		return Base64.encodeBase64URLSafe(binaryData);
	}

	/**
	 * 
	 * <p>
	 * Encodes binary data using the base64 algorithm but does not chunk the
	 * output.
	 * </p>
	 * 
	 * @param binaryData
	 *            byte[] binary data to encode
	 * @return String containing Base64 characters.
	 */
	public static String encodeBase64String(byte[] binaryData) {
		return Base64.encodeBase64String(binaryData);
	}

	/**
	 * 
	 * <p>
	 * Encodes binary data using a URL-safe variation of the base64 algorithm
	 * but does not chunk the output. The url-safe variation emits - and _
	 * instead of + and / characters.
	 * </p>
	 * 
	 * @param binaryData
	 *            byte[] binary data to encode
	 * @return String containing Base64 characters
	 */
	public static String encodeBase64URLSafeString(byte[] binaryData) {
		return Base64.encodeBase64URLSafeString(binaryData);
	}

	/**
	 * 
	 * <p>
	 * Decodes Base64 data into octets
	 * </p>
	 * 
	 * @param base64Data
	 *            byte[] Byte array containing Base64 data
	 * @return byte[] Array containing decoded data.
	 */
	public static byte[] decodeBase(byte[] base64Data) {
		return Base64.decodeBase64(base64Data);
	}

	/**
	 * 
	 * <p>
	 * Decodes a Base64 String into octets
	 * </p>
	 * 
	 * @param base64String
	 *            String containing Base64 data
	 * @return Array containing decoded data.
	 */
	public static byte[] decodeBase(String base64String) {
		return Base64.decodeBase64(base64String);
	}

	/**
	 * 
	 * <p>
	 * Tests a given byte array to see if it contains only valid characters
	 * within the Base64 alphabet. Currently the method treats whitespace as
	 * valid.
	 * </p>
	 * 
	 * @param arrayOctet
	 *            byte array to test
	 * @return true if all bytes are valid characters in the Base64 alphabet or
	 *         if the byte array is empty; false, otherwise
	 */
	public static boolean isBase64(byte[] arrayOctet) {
		return Base64.isBase64(arrayOctet);
	}

	/**
	 * 
	 * <p>
	 * Tests a given String to see if it contains only valid characters within
	 * the Base64 alphabet. Currently the method treats whitespace as valid.
	 * </p>
	 * 
	 * @param base64
	 *            String to test
	 * @return true if all characters in the String are valid characters in the
	 *         Base64 alphabet or if the String is empty; false, otherwise
	 */
	public static boolean isBase64(String base64) {
		return Base64.isBase64(base64);
	}

	private BASE64Coder() {
	}
}
