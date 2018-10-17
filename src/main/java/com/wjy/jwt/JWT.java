package com.wjy.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.wjy.util.PropertiesUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWT {

	private static String typ = null;
	private static String alg = null;

	private static String iss = null;
	private static String exp = null;

	private static String secret = null;

	private static JSONObject header = null;
	private static JSONObject payload = null;

	static {

		typ = PropertiesUtil.getValue("jwt.header.typ");
		alg = PropertiesUtil.getValue("jwt.header.alg");

		iss = PropertiesUtil.getValue("jwt.payload.iss");
		exp = PropertiesUtil.getValue("jwt.payload.exp");

		secret = PropertiesUtil.getValue("jwt.signature.secret");

		header.put("typ", typ);
		header.put("alg", alg);

		payload.put("iss", iss);
		payload.put("exp", exp);

	}

	private final static String id = "c5337055-7ae7-4227-b4f7-d65e94b45575";

	public static String createJWT(String username, String roles, String privileges) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// 生成签名密钥
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// 添加构成JWT的参数
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").claim("user_name", username)
				.claim("user_role", roles).claim("user_privilege", privileges).signWith(signatureAlgorithm, signingKey);
		// 添加Token过期时间
		if (expiresSecond >= 0) {
			long expMillis = nowMillis + expiresSecond;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp).setNotBefore(now);
		}

		// 生成JWT
		return builder.compact();
	}

	public static Claims parseJWT(String jsonWebToken) {
		try {
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
					.parseClaimsJws(jsonWebToken).getBody();
			return claims;
		} catch (Exception ex) {
			return null;
		}
	}

}
