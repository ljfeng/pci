package com.pci.hithot.entity;

import com.ebay.xcelite.annotations.Column;

public class BianhaoClickCount implements Comparable<BianhaoClickCount>{

	@Column
	private String bianhao;
	public String getBianhao() {
		return bianhao;
	}
	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}
	@Column
	private int count = 0;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int compareTo(BianhaoClickCount o) {
		return o.count - this.count;
	}

}
