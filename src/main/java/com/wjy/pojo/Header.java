package com.wjy.pojo;

public class Header {

	// 类型
	private String typ;

	// 算法
	private String alg;

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getAlg() {
		return alg;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public Header() {
		super();
	}

	public Header(String typ, String alg) {
		super();
		this.typ = typ;
		this.alg = alg;
	}

	@Override
	public String toString() {
		return "Header [typ=" + typ + ", alg=" + alg + "]";
	}

}
