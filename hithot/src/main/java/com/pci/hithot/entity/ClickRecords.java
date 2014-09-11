package com.pci.hithot.entity;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

/**
 * Entity class to handle click record
 * 
 * @author jianliu
 * 
 */
@Row(colsOrder = { "qishu", "bianhao", "guanjianci", "wenzhangming",
		"jindianji", "zongdianji", "youxiang" })
public class ClickRecords {
	@Column
	private String qishu;
	public String getQishu() {
		return qishu;
	}
	public void setQishu(String qishu) {
		this.qishu = qishu;
	}
	public String getBianhao() {
		return bianhao;
	}
	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}
	public String getGuanjianci() {
		return guanjianci;
	}
	public void setGuanjianci(String guanjianci) {
		this.guanjianci = guanjianci;
	}
	public String getWenzhangming() {
		return wenzhangming;
	}
	public void setWenzhangming(String wenzhangming) {
		this.wenzhangming = wenzhangming;
	}
	public String getJindianji() {
		return jindianji;
	}
	public void setJindianji(String jindianji) {
		this.jindianji = jindianji;
	}
	public String getZongdianji() {
		return zongdianji;
	}
	public void setZongdianji(String zongdianji) {
		this.zongdianji = zongdianji;
	}
	public String getYouxiang() {
		return youxiang;
	}
	public void setYouxiang(String youxiang) {
		this.youxiang = youxiang;
	}
	@Column
	private String bianhao;
	@Column
	private String guanjianci;
	@Column
	private String wenzhangming;
	@Column
	private String jindianji;
	@Column
	private String zongdianji;
	@Column
	private String youxiang;
	
}
