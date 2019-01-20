package com.wjy.util;

import java.util.UUID;

/**
 * @author ybxxszl
 * @date 2018年10月9日
 * @description UUID工具类
 */
public class UUIDUtil {

    /**
     * @return String 随机的32位字符串
     * @date 2018年10月9日
     * @author ybxxszl
     * @description 获取UUID
     */
    public static String getUUID() {

        return UUID.randomUUID().toString();

    }

}
