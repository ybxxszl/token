package com.wjy.token;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSONObject;
import com.wjy.util.PropertiesUtil;
import com.wjy.util.URLUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

public class JWT {

	private static String typ = null;
	private static String alg = null;

	private static String iss = null;

	private static String secret = null;

	static {

		typ = PropertiesUtil.getValue("jwt.header.typ");
		alg = PropertiesUtil.getValue("jwt.header.alg");

		iss = PropertiesUtil.getValue("jwt.payload.iss");

		secret = PropertiesUtil.getValue("jwt.signature.secret");

	}

	/*
	 * 创建JWT
	 */
	public static String createJWT(Map<String, Object> map, long mills) throws Exception {

		String jwt = null;

		try {

			JSONObject header = new JSONObject();

			header.put("typ", typ);
			header.put("alg", alg);

			long iatMills = System.currentTimeMillis();

			Date exp = new Date(iatMills + mills);

			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

			byte[] keys = DatatypeConverter.parseBase64Binary(secret);

			Key key = new SecretKeySpec(keys, signatureAlgorithm.getJcaName());

			JwtBuilder jwtBuilder = Jwts.builder();

			jwtBuilder.setHeader(header);
			jwtBuilder.setIssuer(iss).setExpiration(exp);

			jwtBuilder.addClaims(map); // 自定义值

			jwtBuilder.signWith(key);

			jwt = jwtBuilder.compact();

		} catch (Exception e) {

			throw new Exception(e.getMessage());

		}

		return jwt;

	}

	/*
	 * 刷新JWT
	 */
	public static String refreshJWT(String jwt, long mills) throws Exception {

		try {

			String payload = jwt.split("[.]")[1];

			JSONObject object = JSONObject.parseObject(new String(Decoders.BASE64URL.decode(payload)));

			object.remove("iss");
			object.remove("exp");

			Map<String, Object> map = new HashMap<String, Object>();

			Set<Entry<String, Object>> set = object.entrySet();
			Iterator<Entry<String, Object>> iterator = set.iterator();

			while (iterator.hasNext()) {

				Entry<String, Object> entry = iterator.next();

				map.put(entry.getKey(), entry.getValue()); // 自定义值

			}

			jwt = createJWT(map, mills);

		} catch (Exception e) {

			throw new Exception(e.getMessage());

		}

		return jwt;

	}

	/*
	 * 验证JWT
	 */
	public static Map<String, Object> verifyJWT(String jwt) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		try {

			byte[] keys = DatatypeConverter.parseBase64Binary(secret);

			Claims claims = Jwts.parser().setSigningKey(keys).parseClaimsJws(jwt).getBody();

			claims.remove("iss");
			claims.remove("exp");

			Set<Entry<String, Object>> set = claims.entrySet();
			Iterator<Entry<String, Object>> iterator = set.iterator();

			while (iterator.hasNext()) {

				Entry<String, Object> entry = iterator.next();

				map.put(entry.getKey(), entry.getValue()); // 自定义值

			}

		} catch (Exception e) {

			throw new Exception(e.getMessage());

		}

		return map;

	}

	public static void main(String[] args) throws Exception {

		Map<String, Object> map = URLUtil.getParamsByParam("id=123456");

		verifyJWT(createJWT(map, 1000000));

		refreshJWT(createJWT(map, 0), 1000000);

	}

}
