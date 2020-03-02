package com.ml.util;

/**
 * <p>
 * 生成全球唯一标示 UUID
 * 
 * </p>
 * 
 * @author Metanoia.Lang
 * @author mecarlen 2015年7月30日 上午9:26:33
 */
public class UUID {
	/**
	 * <p>
	 * 获得一个去掉"-"的UUID 32bits 例: "863a74e5c9a54395862179721918d7a7"
	 * </p>
	 * 
	 * @return String UUID
	 */
	public static String id32() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * <p>
	 * 获得指定数目的UUID<br>
	 * 例:{"e7d83456191d4f8fa37c722df3af4017","196a0e99817f46679fb82d52d4ec39a3"}
	 * </p>
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] id32(int number) {
		if (number < 1) {
			return null;
		}
		String[] uuidArray = new String[number];
		for (int i = 0; i < number; i++) {
			uuidArray[i] = id32();
		}
		return uuidArray;
	}

	private UUID() {
	}
}
