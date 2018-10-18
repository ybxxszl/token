package com.wjy.util;

import java.util.HashMap;
import java.util.Map;

public class URLUtil {

	public static Map<String, Object> getPathAndParamsByURL(String url) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		if (url != null && !"".equals(url)) {

			String[] pathAndParams = url.split("[?]");

			// 请求路径
			map.put("path", pathAndParams[0]);

			if (pathAndParams.length == 2) {

				String[] params = pathAndParams[1].split("[&]");

				for (String param : params) {

					String[] keyAndValue = param.split("[=]");

					if (keyAndValue.length == 2) {

						// 请求参数
						map.put(keyAndValue[0], keyAndValue[1]);

					}

				}

			} else {

				throw new Exception("URL中不含参数");

			}

		} else {

			throw new Exception("无URL");

		}

		return map;

	}

	public static String getPathByURL(String url) throws Exception {

		String path = null;

		if (url != null && !"".equals(url)) {

			String[] pathAndParams = url.split("[?]");

			// 请求路径
			path = pathAndParams[0];

		} else {

			throw new Exception("无URL");

		}

		return path;

	}

	public static Map<String, Object> getParamsByURL(String url) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		if (url != null && !"".equals(url)) {

			String[] pathAndParams = url.split("[?]");

			if (pathAndParams.length == 2) {

				String[] params = pathAndParams[1].split("[&]");

				for (String param : params) {

					String[] keyAndValue = param.split("[=]");

					if (keyAndValue.length == 2) {

						// 请求参数
						map.put(keyAndValue[0], keyAndValue[1]);

					}

				}

			} else {

				throw new Exception("URL中不含参数");

			}

		} else {

			throw new Exception("无URL");

		}

		return map;

	}

	public static Map<String, Object> getParamsByParam(String param) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		if (param != null && !"".equals(param)) {

			String[] params = param.split("[&]");

			for (String str : params) {

				String[] keyAndValue = str.split("[=]");

				if (keyAndValue.length == 2) {

					// 请求参数
					map.put(keyAndValue[0], keyAndValue[1]);

				}

			}

		} else {

			throw new Exception("无参数");

		}

		return map;

	}

}
