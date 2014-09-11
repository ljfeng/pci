package com.pci.hithot.entity;

import com.ebay.xcelite.annotations.Column;

/**
 * youxiang + bianhao + count
 * 
 * @author jianliu
 *
 */
public class UserPreference implements Comparable<UserPreference>{
	@Column
	private String youxiang;
	@Column
	private String bianhao;
	@Column
	private int count = 0;
	@Column
	private int day = 0;
	public String getYouxiang() {
		return youxiang;
	}
	public void setYouxiang(String youxiang) {
		this.youxiang = youxiang;
	}
	public String getBianhao() {
		return bianhao;
	}
	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int compareTo(UserPreference o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
