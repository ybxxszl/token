package com.wjy.jwt;

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
import io.jsonwebtoken.impl.Base64Codec;

@SuppressWarnings("deprecation")
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
	public static String createJWT(Map<String, String> map, long mills) {
		
		// 构建header
		Map<String, Object> header = new HashMap<String, Object>();
		
		header.put("typ", typ);
		header.put("alg", alg);
		
		// 构建payload
		JSONObject payload = new JSONObject();
		
		payload.put("iss", iss);
		
		long nbfMills = System.currentTimeMillis(); // 获取当前时间

		Date nbf = new Date(nbfMills); // 生效时间
		Date exp = new Date(nbfMills + mills); // 过期时间
		
		payload.put("nbf", nbf);
		payload.put("exp", exp);
		
		Set<Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, String> entry = iterator.next();
			
			payload.put(entry.getKey(), entry.getValue()); // 自定义

		}

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		byte [] bs = DatatypeConverter.parseBase64Binary(secret);

		Key key = new SecretKeySpec(bs, signatureAlgorithm.getJcaName());
		
		JwtBuilder jwtBuilder = Jwts.builder().setHeader(header).setPayload(payload.toString()).signWith(key);

		String jwt = jwtBuilder.compact();
		
		System.out.println("JWT:" + jwt);

		return jwt;

	}

	/*
	 * 解析JWT
	 */
	public static Map<String, Object> parseJWT(String jwt) {
		
		byte [] bs = DatatypeConverter.parseBase64Binary(secret);

		Claims claims = Jwts.parser().setSigningKey(bs).parseClaimsJws(jwt).getBody();

		Map<String, Object> map = new HashMap<String, Object>();
		
		Set<Entry<String, Object>> set = claims.entrySet();
		Iterator<Entry<String, Object>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, Object> entry = iterator.next();

			String key = entry.getKey();
			Object value = entry.getValue();

			map.put(key, value); // 自定义

		}

		map.remove("iss");
		map.remove("nbf");
		map.remove("exp");

		return map;

	}
	
	public static void verifyJWT(String jwt) {

		String [] jwts = jwt.split("[.]");
		
		String header = jwts[0];
		String payload = jwts[1];
		String signature = jwts[2];
		
		System.out.println("header: " + header);
		System.out.println("payload: " + payload);
		System.out.println("signature: " + signature);
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder().setPayload(Base64Codec.BASE64URL.decodeToString(payload).toString()).signWith(signatureAlgorithm, signingKey);
		System.out.println(builder.compact());

	}

	public static void main(String[] args) throws Exception {

		Map<String, String> map = URLUtil.getParams("/getToken?id=123456");

		parseJWT(createJWT(map, 10000));
		verifyJWT(createJWT(map, 10000));

	}

}
