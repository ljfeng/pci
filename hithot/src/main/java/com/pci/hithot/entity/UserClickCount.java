package com.pci.hithot.entity;

import com.ebay.xcelite.annotations.Column;

public class UserClickCount {
	@Column
	private String youxiang;
	@Column
	private int count = 0;
	public String getYouxiang() {
		return youxiang;
	}
	public void setYouxiang(String youxiang) {
		this.youxiang = youxiang;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
