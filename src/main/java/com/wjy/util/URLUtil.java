package com.wjy.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class URLUtil {

	public static Map<String, Object> getPathAndParams(String url) throws Exception {

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

	public static String getPath(String url) throws Exception {

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

	public static Map<String, Object> getParams(String url) throws Exception {

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

	public static void main(String[] args) throws Exception {

		Map<String, Object> map = getPathAndParams(null);

		Set<Entry<String, Object>> set = map.entrySet();

		Iterator<Entry<String, Object>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, Object> entry = iterator.next();

			System.out.println(entry.getKey() + ": " + entry.getValue());

		}

	}

}
