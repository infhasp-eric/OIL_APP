package com.oil.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.oil.utils.FilterUtil;

public class JointProper {
	private String pl;
	private String section;
	private String spec;
	private String wells;
	private String m_year;
	private String createBy;
	private String auditor;
	private String month_1;
	private String month_2;
	private String month_3;
	private String month_4;
	private String month_5;
	private String month_6;
	private String month_7;
	private String month_8;
	private String month_9;
	private String month_10;
	private String month_11;
	private String month_12;

	public String getPl() {
		return pl;
	}

	public void setPl(String pl) {
		this.pl = pl;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getWells() {
		return wells;
	}

	public void setWells(String wells) {
		this.wells = wells;
	}

	public String getM_year() {
		return m_year;
	}

	public void setM_year(String m_year) {
		this.m_year = m_year;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getMonth_1() {
		return month_1;
	}

	public void setMonth_1(String month_1) {
		this.month_1 = month_1;
	}

	public String getMonth_2() {
		return month_2;
	}

	public void setMonth_2(String month_2) {
		this.month_2 = month_2;
	}

	public String getMonth_3() {
		return month_3;
	}

	public void setMonth_3(String month_3) {
		this.month_3 = month_3;
	}

	public String getMonth_4() {
		return month_4;
	}

	public void setMonth_4(String month_4) {
		this.month_4 = month_4;
	}

	public String getMonth_5() {
		return month_5;
	}

	public void setMonth_5(String month_5) {
		this.month_5 = month_5;
	}

	public String getMonth_6() {
		return month_6;
	}

	public void setMonth_6(String month_6) {
		this.month_6 = month_6;
	}

	public String getMonth_7() {
		return month_7;
	}

	public void setMonth_7(String month_7) {
		this.month_7 = month_7;
	}

	public String getMonth_8() {
		return month_8;
	}

	public void setMonth_8(String month_8) {
		this.month_8 = month_8;
	}

	public String getMonth_9() {
		return month_9;
	}

	public void setMonth_9(String month_9) {
		this.month_9 = month_9;
	}

	public String getMonth_10() {
		return month_10;
	}

	public void setMonth_10(String month_10) {
		this.month_10 = month_10;
	}

	public String getMonth_11() {
		return month_11;
	}

	public void setMonth_11(String month_11) {
		this.month_11 = month_11;
	}

	public String getMonth_12() {
		return month_12;
	}

	public void setMonth_12(String month_12) {
		this.month_12 = month_12;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("pl=" + pl);
		sb.append("&section=" + section +"");
		sb.append("&spec=" + spec +"");
		sb.append("&jinzhan=" + wells +"");
		sb.append("&created_by=" + createBy +"");
		sb.append("&auditor=" + auditor +"");
		sb.append("&m_year=" + m_year +"");
		sb.append("&month_1=" + month_1 + "");
		sb.append("&month_2=" + month_2 + "");
		sb.append("&month_3=" + month_3 + "");
		sb.append("&month_4=" + month_4 + "");
		sb.append("&month_5=" + month_5 + "");
		sb.append("&month_6=" + month_6 + "");
		sb.append("&month_7=" + month_7 + "");
		sb.append("&month_8=" + month_8 + "");
		sb.append("&month_9=" + month_9 + "");
		sb.append("&month_10=" + month_10 + "");
		sb.append("&month_11=" + month_11 + "");
		sb.append("&month_12=" + month_12 + "");
		return sb.toString();
	}
}
