package com.oil.domain;

public class CathodeMonth {
	private int _id;
	private String pl;
	private String section;
	private String spec;
	private String jinzhan;
	private String rMonth;
	private String comment;
	private String createdBy;
	private String auditor;
	private String estimated;
	private String actual;
	private String wdtd;
	private String ljtd;
	private String jztd;
	private String fljctd;
	private String jcgxtd;
	private String qttd;
	private String oMaxA;
	private String oMinA;
	private String oAvgA;
	private String oMaxV;
	private String oMinV;
	private String oAvgV;
	private String ttdVMax;
	private String ttdVMin;
	private String sdl;
	private String bhl;
	private String sbwhl;
	public String getTtdVMax() {
		return ttdVMax;
	}
	public void setTtdVMax(String ttdVMax) {
		this.ttdVMax = ttdVMax;
	}
	public String getJcgxtd() {
		return jcgxtd;
	}
	public void setJcgxtd(String jcgxtd) {
		this.jcgxtd = jcgxtd;
	}
	public String getJztd() {
		return jztd;
	}
	public void setJztd(String jztd) {
		this.jztd = jztd;
	}
	public String getJinzhan() {
		return jinzhan;
	}
	public void setJinzhan(String jinzhan) {
		this.jinzhan = jinzhan;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
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
	public String getrMonth() {
		return rMonth;
	}
	public void setrMonth(String rMonth) {
		this.rMonth = rMonth;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getEstimated() {
		return estimated;
	}
	public void setEstimated(String estimated) {
		this.estimated = estimated;
	}
	public String getActual() {
		return actual;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	public String getWdtd() {
		return wdtd;
	}
	public void setWdtd(String wdtd) {
		this.wdtd = wdtd;
	}
	public String getLjtd() {
		return ljtd;
	}
	public void setLjtd(String ljtd) {
		this.ljtd = ljtd;
	}
	public String getFljctd() {
		return fljctd;
	}
	public void setFljctd(String fljctd) {
		this.fljctd = fljctd;
	}
	public String getQttd() {
		return qttd;
	}
	public void setQttd(String qttd) {
		this.qttd = qttd;
	}
	public String getoMaxA() {
		return oMaxA;
	}
	public void setoMaxA(String oMaxA) {
		this.oMaxA = oMaxA;
	}
	public String getoMinA() {
		return oMinA;
	}
	public void setoMinA(String oMinA) {
		this.oMinA = oMinA;
	}
	public String getoAvgA() {
		return oAvgA;
	}
	public void setoAvgA(String oAvgA) {
		this.oAvgA = oAvgA;
	}
	public String getoMaxV() {
		return oMaxV;
	}
	public void setoMaxV(String oMaxV) {
		this.oMaxV = oMaxV;
	}
	public String getoMinV() {
		return oMinV;
	}
	public void setoMinV(String oMinV) {
		this.oMinV = oMinV;
	}
	public String getoAvgV() {
		return oAvgV;
	}
	public void setoAvgV(String oAvgV) {
		this.oAvgV = oAvgV;
	}
	public String getTtdVMin() {
		return ttdVMin;
	}
	public void setTtdVMin(String ttdVMin) {
		this.ttdVMin = ttdVMin;
	}
	public String getSdl() {
		return sdl;
	}
	public void setSdl(String sdl) {
		this.sdl = sdl;
	}
	public String getBhl() {
		return bhl;
	}
	public void setBhl(String bhl) {
		this.bhl = bhl;
	}
	public String getSbwhl() {
		return sbwhl;
	}
	public void setSbwhl(String sbwhl) {
		this.sbwhl = sbwhl;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("pl="+pl);
		sb.append("&section="+section);
		sb.append("&spec="+spec);
		sb.append("&jinzhan="+jinzhan);
		sb.append("&r_month="+rMonth);
		sb.append("&comment="+comment);
		sb.append("&created_by="+createdBy);
		sb.append("&auditor="+auditor);
		sb.append("&estimated="+estimated);
		sb.append("&actual="+actual);
		sb.append("&wdtd="+wdtd);
		sb.append("&ljtd="+ljtd);
		sb.append("&jztd="+jztd);
		sb.append("&fljctd="+fljctd);
		sb.append("&jcgxtd="+jcgxtd);
		sb.append("&qttd="+qttd);
		sb.append("&o_max_a="+oMaxA);
		sb.append("&o_min_a="+oMinA);
		sb.append("&o_avg_a="+oAvgA);
		sb.append("&o_max_v="+oMaxV);
		sb.append("&o_min_v="+oMinV);
		sb.append("&o_avg_v="+oAvgV);
		sb.append("&tdd_v_max="+ttdVMax);
		sb.append("&tdd_v_min="+ttdVMin);
		sb.append("&sdl="+sdl);
		sb.append("&bhl="+bhl);
		sb.append("&sbwhl="+sbwhl);
		return sb.toString();
	}
	
}
