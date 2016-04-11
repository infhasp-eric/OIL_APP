package com.oil.domain;

public class InsulationRecord {
	private Integer id;
	private String line_name;
	 private String start_end;
	 private String line_standard;
	 private String well;
	 private String year;
	 private String createTime;
	 private String createBy;
	 private String examine;
	 private String state;
	 
	 public void setId(Integer id) {
		 this.id = id;
	 }
	 public Integer getId() {
		 return id;
	 }
	 public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getExamine() {
		return examine;
	}
	public void setExamine(String examine) {
		this.examine = examine;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLine_name() {
		return line_name;
	}
	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}
	public String getStart_end() {
		return start_end;
	}
	public void setStart_end(String start_end) {
		this.start_end = start_end;
	}
	public String getLine_standard() {
		return line_standard;
	}
	public void setLine_standard(String line_standard) {
		this.line_standard = line_standard;
	}
	public String getWell() {
		return well;
	}
	public void setWell(String well) {
		this.well = well;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
