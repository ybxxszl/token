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

/**
 * @date 2018年10月18日
 * @author ybxxszl
 * @description JWT
 */
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

	public static String createJWT(Map<String, Object> map, long mills) {

		JSONObject header = new JSONObject();

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

	public static void verifyJWT(String jwt) throws Exception {

		String verifyTYP = null;
		String verifyALG = null;

		String verifyISS = null;
		long verifyNBF = 0;
		long verifyEXP = 0;

		Map<String, Object> verifyHeader = new HashMap<String, Object>();

		Map<String, Object> verifyClaims = new HashMap<String, Object>();

		String[] jwts = jwt.split("[.]");

		String header = jwts[0];
		String payload = jwts[1];
		String signature = jwts[2];

		JSONObject headerJSON = JSONObject.parseObject(Base64Codec.BASE64URL.decodeToString(header));

		verifyTYP = headerJSON.getString("typ");
		verifyALG = headerJSON.getString("alg");

		Set<Entry<String, Object>> set1 = headerJSON.entrySet();
		Iterator<Entry<String, Object>> iterator1 = set1.iterator();

		while (iterator1.hasNext()) {

			Entry<String, Object> entry1 = iterator1.next();

			verifyHeader.put(entry1.getKey(), entry1.getValue());

		}

		JSONObject payloadJSON = JSONObject.parseObject(Base64Codec.BASE64URL.decodeToString(payload));

		verifyISS = payloadJSON.remove("iss").toString();
		verifyNBF = Long.parseLong(payloadJSON.remove("nbf").toString());
		verifyEXP = Long.parseLong(payloadJSON.remove("exp").toString());

		Set<Entry<String, Object>> set2 = payloadJSON.entrySet();
		Iterator<Entry<String, Object>> iterator2 = set2.iterator();

		while (iterator2.hasNext()) {

			Entry<String, Object> entry2 = iterator2.next();

			verifyClaims.put(entry2.getKey(), entry2.getValue());

		}

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		byte[] keys = DatatypeConverter.parseBase64Binary(secret);

		Key key = new SecretKeySpec(keys, signatureAlgorithm.getJcaName());

		JwtBuilder jwtBuilder = Jwts.builder();

		jwtBuilder.setHeader(verifyHeader);
		jwtBuilder.setIssuer(verifyISS).setNotBefore(new Date(verifyNBF)).setExpiration(new Date(verifyEXP));

		jwtBuilder.addClaims(verifyClaims); // 自定义值

		jwtBuilder.signWith(key);

		System.out.println(jwtBuilder.compact());

	}

	public static void main(String[] args) throws Exception {

		Map<String, Object> map = URLUtil.getParamsByParam("id=123456");

		verifyJWT(createJWT(map, 1000000));

	}

}
