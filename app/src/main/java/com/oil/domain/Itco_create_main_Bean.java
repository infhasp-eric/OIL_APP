package com.oil.domain;

public class Itco_create_main_Bean {
	private Integer id;
	private Integer pl;
	private Integer section;
	private Integer spec;

	private String auditor;

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

	public String toString() {
		StringBuffer params = new StringBuffer();
		if (id != null && id.intValue() > 0) {
			params.append("id=" + id);
			params.append("&pl=" + pl);
		} else {
			params.append("pl=" + pl);
		}
		params.append("&section=" + section);
		params.append("&spec=" + spec);
		return params.toString();
	}
}
