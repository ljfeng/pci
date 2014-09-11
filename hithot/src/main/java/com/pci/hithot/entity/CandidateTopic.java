package com.pci.hithot.entity;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;

@Row(colsOrder = { "qishu", "bianhao", "guanjianci", "wenzhangming",
		"wenzhangId" })
public class CandidateTopic {
	@Column
	private String qishu;
	@Column
	private String bianhao;
	@Column
	private String guanjianci;
	@Column
	private String wenzhangming;
	@Column
	private String wenzhangId = "";

	public String getWenzhangId() {
		return wenzhangId;
	}

	public void setWenzhangId(String wenzhangId) {
		this.wenzhangId = wenzhangId;
	}

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
}
