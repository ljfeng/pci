package com.pci.hithot.entity;

import com.ebay.xcelite.annotations.Column;

public class TopicProposal implements Comparable<TopicProposal>{
	@Column
	private String youxiang;
	@Column
	private String wenzhangming;
	@Column
	private String wenzhangId;
	@Column
	private int score;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getWenzhangId() {
		return wenzhangId;
	}

	public void setWenzhangId(String wenzhangId) {
		this.wenzhangId = wenzhangId;
	}

	public String getYouxiang() {
		return youxiang;
	}

	public void setYouxiang(String youxiang) {
		this.youxiang = youxiang;
	}

	public String getWenzhangming() {
		return wenzhangming;
	}

	public void setWenzhangming(String wenzhangming) {
		this.wenzhangming = wenzhangming;
	}

	public int compareTo(TopicProposal o) {
		return o.score - this.score;
	}

}
