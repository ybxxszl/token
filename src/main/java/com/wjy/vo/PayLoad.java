package com.wjy.vo;

public class PayLoad {

	// 编号
	private String jti;

	// 主题
	private String sub;

	// 签发者
	private String iss;

	// 受众
	private String aud;

	// 签发时间
	private String iat;

	// 生效时间
	private String nbf;

	// 过期时间
	private String exp;

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}

	public String getAud() {
		return aud;
	}

	public void setAud(String aud) {
		this.aud = aud;
	}

	public String getIat() {
		return iat;
	}

	public void setIat(String iat) {
		this.iat = iat;
	}

	public String getNbf() {
		return nbf;
	}

	public void setNbf(String nbf) {
		this.nbf = nbf;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public PayLoad() {
		super();
	}

	public PayLoad(String jti, String sub, String iss, String aud, String iat, String nbf, String exp) {
		super();
		this.jti = jti;
		this.sub = sub;
		this.iss = iss;
		this.aud = aud;
		this.iat = iat;
		this.nbf = nbf;
		this.exp = exp;
	}

	@Override
	public String toString() {
		return "PayLoad [jti=" + jti + ", sub=" + sub + ", iss=" + iss + ", aud=" + aud + ", iat=" + iat + ", nbf="
				+ nbf + ", exp=" + exp + "]";
	}

}
