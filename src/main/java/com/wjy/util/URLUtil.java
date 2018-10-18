package com.wjy.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class URLUtil {

	public static Map<String, String> getPathAndParams(String url) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

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

	public static Map<String, String> getParams(String url) throws Exception {

		Map<String, String> map = new HashMap<String, String>();

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

		Map<String, String> map = getPathAndParams(null);

		Set<Entry<String, String>> set = map.entrySet();

		Iterator<Entry<String, String>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, String> entry = iterator.next();

			System.out.println(entry.getKey() + ": " + entry.getValue());

		}

	}

}
