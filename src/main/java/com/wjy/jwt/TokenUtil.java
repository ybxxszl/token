package com.wjy.jwt;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSONObject;
import com.wjy.util.PropertiesUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;

@SuppressWarnings("deprecation")
public class TokenUtil {

	/**
	 * 存储token
	 */
	public static String getToken(String json_py) {
		String signKey = PropertiesUtil.getValue("jwt.signature.secret");// 此处就是在服务器定义的自己的密匙
		try {
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(signKey);
			Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
			JwtBuilder builder = Jwts.builder().setPayload(json_py.toString()).signWith(signatureAlgorithm, signingKey);
			return builder.compact();
		} catch (Exception e) {
			return "error";
		}
	}

	/**
	 * 判断是否token值看是否登录成功
	 */
	public static String isLogin(String jwt) {
		String signingKey = PropertiesUtil.getValue("jwt.signature.secret");// 此处就是在服务器定义的自己的密匙
		String params = "";
		if (jwt.split("\\.").length == 3) {
			String header = jwt.split("\\.")[0];
			String payload = jwt.split("\\.")[1];
			System.out.println(Base64Codec.BASE64URL.decodeToString(header));
			System.out.println(Base64Codec.BASE64URL.decodeToString(payload));
			String sign = jwt.split("\\.")[2];// 带过来的签名
			String headerNew = getToken(Base64Codec.BASE64URL.decodeToString(payload)).split("\\.")[0];
			String signNew = getToken(Base64Codec.BASE64URL.decodeToString(payload)).split("\\.")[2];
			System.out.println("新的token：" + getToken(Base64Codec.BASE64URL.decodeToString(payload)));
			System.out.println(signNew);
			if (header.equals(headerNew) && sign.equals(signNew)) {// 进行安全校验（头部和签名）
				Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(signingKey))
						.parseClaimsJws(jwt).getBody();
				if (claims != null) {
					long expdate = (Long) claims.get("expDate");
					long nowMillis = System.currentTimeMillis();
					if (expdate > nowMillis) {// 判断token有效性
						String username = (String) claims.get("username");
						String password = (String) claims.get("password");
						String name = "admin1";// 从本地读取用户名
						String pword = "123456";// 从本地读取密码
						if (name.equals(username) && pword.equals(password)) {
							params = "success";// 校验成功，有此用户
						} else {
							params = "用户名或密码错误---faile";
						}
					} else {
						params = "timeout";
					}
				}
			} else {
				params = "修改数据--faile";
			}
		}
		return params;
	}

	public static void main(String[] args) {
		JSONObject json_py = new JSONObject();
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + Integer.parseInt("6000");// 一分钟过期
		json_py.put("username", "admin1");
		json_py.put("password", "123456");
		json_py.put("expDate", expMillis);
		System.out.println("expDate:" + expMillis);
		String token = TokenUtil.getToken(json_py.toString());
		System.out.println(json_py.toString());
		System.out.println("token的值：" + token);
		String params = TokenUtil.isLogin(token);
		System.out.println("返回参数params：" + params);
	}

}
