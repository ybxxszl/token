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

import com.wjy.util.PropertiesUtil;
import com.wjy.util.URLUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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

	@SuppressWarnings("deprecation")
	public static String createJWT(Map<String, String> map, long expMills) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		byte[] key = DatatypeConverter.parseBase64Binary(secret);
		Key k = new SecretKeySpec(key, signatureAlgorithm.getJcaName());

		JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam(typ, alg);

		Set<Entry<String, String>> set = map.entrySet();

		Iterator<Entry<String, String>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, String> entry = iterator.next();

			jwtBuilder.claim(entry.getKey(), entry.getValue());

		}

		jwtBuilder.signWith(signatureAlgorithm, k);

		long nowMills = System.currentTimeMillis();

		Date now = new Date(nowMills);

		Date exp = new Date(expMills + nowMills);

		jwtBuilder.setIssuer(iss).setExpiration(exp).setNotBefore(now);

		String jwt = jwtBuilder.compact();

		System.out.println("token:" + jwt);

		return jwt;

	}

	public static Map<String, String> parseJWT(String jwt) {

		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jwt)
				.getBody();

		Map<String, String> map = new HashMap<String, String>();

		Set<Entry<String, Object>> set = claims.entrySet();

		Iterator<Entry<String, Object>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, Object> entry = iterator.next();

			String key = entry.getKey();
			String value = entry.getValue().toString();

			map.put(key, value);

			System.out.println(key + ": " + value);

		}

		return map;

	}

	public static void main(String[] args) throws Exception {

		Map<String, String> map = URLUtil.getParams(
				"https://nzsq.vilsale.com/mvapi/miniprogram/produceEvidenceList?produceId=0f3d7760-c11f-4ed2-bee1-7a1dc01f21ef&corpId=b66b0fb1-6b6a-4293-beb3-365bf5e13267&beginSize=0&endSize=10");

		parseJWT(createJWT(map, 10000));

	}

}
