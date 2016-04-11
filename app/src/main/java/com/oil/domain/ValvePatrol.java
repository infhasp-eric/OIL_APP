package com.oil.domain;

public class ValvePatrol {
	private Integer id;
	private Integer pl;
	private Integer section;
	private Integer spec;
	private String valve_name;
	private String day;
	private String checker;

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

	public String getValve_name() {
		return valve_name;
	}

	public void setValve_name(String valve_name) {
		this.valve_name = valve_name;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String toString() {
		StringBuffer params = new StringBuffer();
		if(id != null && id.intValue() > 0) {
			params.append("id=" + id);
			params.append("&pl="+pl);
		} else {
			params.append("pl="+pl);
		}
		params.append("&section="+section);
		params.append("&spec="+spec);
		params.append("&valve_name="+valve_name + "");
		params.append("&check_date=" + day);
		params.append("&checker=" + checker + "");
		return params.toString();
	}
}
