package com.pci.hithot.entity;

import com.ebay.xcelite.annotations.Column;

/**
 * Record every click record
 * 
 * @author jianliu
 *
 */
public class ClickRecord {
	@Column
	private String youxiang;
	@Column
	private String bianhao;
	@Column
	private String wenzhangming;

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

	public String getWenzhangming() {
		return wenzhangming;
	}

	public void setWenzhangming(String wenzhangming) {
		this.wenzhangming = wenzhangming;
	}
}
