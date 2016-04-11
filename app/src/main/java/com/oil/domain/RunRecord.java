package com.oil.domain;

import java.net.URLEncoder;

public class RunRecord {
	private Integer id;
	private Integer pl_id;
	private Integer pl_section_id;
	private Integer pl_spec_id;
	private String jinzhan;
	private String r_month;
	private String auditor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPl_id() {
		return pl_id;
	}

	public void setPl_id(Integer pl_id) {
		this.pl_id = pl_id;
	}

	public Integer getPl_section_id() {
		return pl_section_id;
	}

	public void setPl_section_id(Integer pl_section_id) {
		this.pl_section_id = pl_section_id;
	}

	public Integer getPl_spec_id() {
		return pl_spec_id;
	}

	public void setPl_spec_id(Integer pl_spec_id) {
		this.pl_spec_id = pl_spec_id;
	}

	public String getJinzhan() {
		return jinzhan;
	}

	public void setJinzhan(String jinzhan) {
		this.jinzhan = jinzhan;
	}

	public String getR_month() {
		return r_month;
	}

	public void setR_month(String r_month) {
		this.r_month = r_month;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	/**
	 * 将对象中的数据封装成发送给服务器需要的格式
	 */
	public String toString() {
		StringBuffer params = new StringBuffer();
		if(id != null) {
			params.append("id="+id);
			params.append("&pl=" + pl_id);
		} else {
			params.append("pl=" + pl_id);
		}
		params.append("&section=" + pl_section_id);
		params.append("&spec=" + pl_spec_id);
		params.append("&jinzhan=" + URLEncoder.encode(jinzhan));
		params.append("&auditor=" + URLEncoder.encode(auditor));
		params.append("&r_month=" + r_month);
		return params.toString();
	}
}
