package com.oil.domain;

public class PotentialCurve {
	private Integer id;
	private Integer pl;
	private Integer section;
	private Integer spec;
	private String unit;
	private String year;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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
		params.append("&unit=" + unit);
		params.append("&c_month=" + year);
		return params.toString();
	}

}
