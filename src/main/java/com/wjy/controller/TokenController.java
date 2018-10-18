package com.wjy.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wjy.jwt.JWT;
import com.wjy.result.JSONResult;
import com.wjy.util.PropertiesUtil;
import com.wjy.util.URLUtil;

@RestController
public class TokenController {

	@GetMapping(value = "/getToken")
	public JSONResult getToken(HttpServletRequest request, long mills) {

		String token = null;

		String param = request.getQueryString();

		try {

			Map<String, Object> map = URLUtil.getParams(param);

			token = JWT.createJWT(map, mills);

			return JSONResult.ok(token);

		} catch (Exception e) {

			return JSONResult.errorException(e.getMessage());

		}

	}

	@GetMapping(value = "/refreshToken")
	public JSONResult refreshToken(String token, long mills) {

		try {

			Map<String, Object> map = JWT.parseJWT(token);

			map.remove("iss");
			map.remove("exp");

			token = JWT.createJWT(map, mills);

			return JSONResult.ok(token);

		} catch (Exception e) {

			return JSONResult.errorException(e.getMessage());

		}

	}

	@GetMapping(value = "/verifyToken")
	public JSONResult verifyToken(String token, boolean returnValue) {

		try {

			Map<String, Object> map = JWT.parseJWT(token);

			Object iss = map.get("iss");
			long expMillis = Long.parseLong(map.get("exp").toString());

			long nowMillis = System.currentTimeMillis();

			if (PropertiesUtil.getValue("jwt.payload.iss").equals(iss) || nowMillis <= expMillis) {

				if (returnValue) {

					map.remove("iss");
					map.remove("exp");

					return JSONResult.build(200, "yes", map);

				} else {

					return JSONResult.build(200, "yes", null);

				}

			} else {

				return JSONResult.build(200, "no", null);

			}

		} catch (Exception e) {

			return JSONResult.errorException(e.getMessage());

		}

	}

}
