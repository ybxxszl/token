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
	public static String createJWT(Map<String, Object> map, long mills) {

		// 构建header
		Map<String, Object> header = new HashMap<String, Object>();

		header.put("typ", typ); // 类型
		header.put("alg", alg); // 算法

		long nbfMills = System.currentTimeMillis(); // 获取当前时间

		Date nbf = new Date(nbfMills); // 生效时间
		Date exp = new Date(nbfMills + mills); // 过期时间

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		byte[] keys = DatatypeConverter.parseBase64Binary(secret);

		Key key = new SecretKeySpec(keys, signatureAlgorithm.getJcaName());

		JwtBuilder jwtBuilder = Jwts.builder();

		jwtBuilder.setHeader(header);
		jwtBuilder.setIssuer(iss).setNotBefore(nbf).setExpiration(exp);

		jwtBuilder.addClaims(map); // 自定义值

		jwtBuilder.signWith(key);

		return jwtBuilder.compact();

	}

	/*
	 * 解析JWT，获取自定义值
	 */
	public static Map<String, Object> parseJWT(String jwt) {

		byte[] keys = DatatypeConverter.parseBase64Binary(secret);

		Claims claims = Jwts.parser().setSigningKey(keys).parseClaimsJws(jwt).getBody();

		Map<String, Object> kv = new HashMap<String, Object>();

		Set<Entry<String, Object>> set = claims.entrySet();
		Iterator<Entry<String, Object>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, Object> entry = iterator.next();

			kv.put(entry.getKey(), entry.getValue()); // 自定义值

		}

		kv.remove("iss");
		kv.remove("nbf");
		kv.remove("exp");

		return kv;

	}

	public static void verifyJWT(String jwt) {

		String[] jwts = jwt.split("[.]");

		String header = jwts[0];
		String payload = jwts[1];
		String signature = jwts[2];

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		byte[] keys = DatatypeConverter.parseBase64Binary(secret);

		Key key = new SecretKeySpec(keys, signatureAlgorithm.getJcaName());

		JwtBuilder jwtBuilder = Jwts.builder().signWith(key);

	}

	public static void main(String[] args) throws Exception {

		Map<String, Object> map = URLUtil.getParams("/getToken?id=123456");

		String jwt = createJWT(map, 1000000);

		System.out.println("JWT: " + jwt);

		Map<String, Object> kv = parseJWT(jwt);

		Set<Entry<String, Object>> set = kv.entrySet();
		Iterator<Entry<String, Object>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, Object> entry = iterator.next();

			// System.out.println(entry.getKey() + ": " + entry.getValue());

		}

		verifyJWT(jwt);

	}

}
