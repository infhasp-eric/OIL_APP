package com.oil.domain;

public class FacilitiesMaintenance {
	private Integer id;
	private Integer pl;
	private Integer section;
	private Integer spec;
	private String jinzhan;
	private String day;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPl() {
		return pl;
	}

	public void setPl(Integer pl) {
		this.pl = pl;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public Integer getSpec() {
		return spec;
	}

	public void setSpec(Integer spec) {
		this.spec = spec;
	}

	public String getJinzhan() {
		return jinzhan;
	}

	public void setJinzhan(String jinzhan) {
		this.jinzhan = jinzhan;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	
	public String toString() {
		StringBuffer params = new StringBuffer();
		if(id != null && id.intValue() > 0) {
			params.append("id=" + id);
			params.append("&pl=" + pl);
		} else {
			params.append("pl=" + pl);
		}
		params.append("&section=" + section);
		params.append("&spec=" + spec);
		params.append("&jinzhan=" + jinzhan);
		params.append("&create_date=" + day);
		return params.toString();
	}

}
