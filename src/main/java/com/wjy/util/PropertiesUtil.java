package com.wjy.util;

import java.util.ResourceBundle;

/**
 * @author ybxxszl
 * @date 2018年9月9日
 * @description Properties工具类
 */
public class PropertiesUtil {

    private final static ResourceBundle resource = ResourceBundle.getBundle("resources");

    /**
     * @param key 键
     * @return String 值
     * @date 2018年9月9日
     * @author ybxxszl
     * @description 根据键获取值
     */
    public static String getValue(String key) {

        return resource.getString(key);

    }

    /**
     * @param key          键
     * @param defaultValue 默认值
     * @return String 值
     * @date 2018年9月9日
     * @author ybxxszl
     * @description 根据键获取值，如果没有相应键，则返回默认值
     */
    public static String getValueOrDefault(String key, String defaultValue) {

        if (resource.containsKey(key)) {
            return resource.getString(key);
        } else {
            return defaultValue;
        }

    }

}
