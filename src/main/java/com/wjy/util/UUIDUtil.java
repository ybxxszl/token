package com.wjy.util;

import java.util.UUID;

/**
 * @date 2018年10月9日
 * @author ybxxszl
 * @description UUID工具类
 */
public class UUIDUtil {

	/**
	 * @date 2018年10月9日
	 * @author ybxxszl
	 * @description 获取UUID
	 * @return String 随机的32位字符串
	 */
	public static String getUUID() {

		return UUID.randomUUID().toString();

	}

}
