package com.oil.domain;

public class HighStatic {
	private String num;
	private String staLat;
	private String staLon;
	private String endLat;
	private String endLon;
	private String highSta;
	private String highEnd;
	private String highLen;
	private String highSco;
	private String plaNam;
	private String feaDes;
	private String recUpd;
	private String recWor;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getStaLat() {
		return staLat;
	}
	public void setStaLat(String staLat) {
		this.staLat = staLat;
	}
	public String getStaLon() {
		return staLon;
	}
	public void setStaLon(String staLon) {
		this.staLon = staLon;
	}
	public String getEndLat() {
		return endLat;
	}
	public void setEndLat(String endLat) {
		this.endLat = endLat;
	}
	public String getEndLon() {
		return endLon;
	}
	public void setEndLon(String endLon) {
		this.endLon = endLon;
	}
	public String getHighSta() {
		return highSta;
	}
	public void setHighSta(String highSta) {
		this.highSta = highSta;
	}
	public String getHighEnd() {
		return highEnd;
	}
	public void setHighEnd(String highEnd) {
		this.highEnd = highEnd;
	}
	public String getHighLen() {
		return highLen;
	}
	public void setHighLen(String highLen) {
		this.highLen = highLen;
	}
	public String getHighSco() {
		return highSco;
	}
	public void setHighSco(String highSco) {
		this.highSco = highSco;
	}
	public String getPlaNam() {
		return plaNam;
	}
	public void setPlaNam(String plaNam) {
		this.plaNam = plaNam;
	}
	public String getFeaDes() {
		return feaDes;
	}
	public void setFeaDes(String feaDes) {
		this.feaDes = feaDes;
	}
	public String getRecUpd() {
		return recUpd;
	}
	public void setRecUpd(String recUpd) {
		this.recUpd = recUpd;
	}
	public String getRecWor() {
		return recWor;
	}
	public void setRecWor(String recWor) {
		this.recWor = recWor;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("&num="+num);
		sb.append("&start_lat="+staLat);
		sb.append("&start_lon="+staLon);
		sb.append("&end_lat="+endLat);
		sb.append("&end_lon="+endLon);
		sb.append("&s_start="+highSta);
		sb.append("&s_end="+highEnd);
		sb.append("&s_length="+highLen);
		sb.append("&s_soure="+highSco);
		sb.append("&place_name="+plaNam);
		sb.append("&description="+feaDes);
		sb.append("&u_date="+recUpd);
		sb.append("&recogner="+feaDes);
		return sb.toString();
	}
}
