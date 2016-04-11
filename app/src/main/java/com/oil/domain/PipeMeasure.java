package com.oil.domain;

public class PipeMeasure {
	private int _id;
	private String pl;
	private String section;
	private String spec;
	private String jinzhan;
	private String cMonth;
	private String unit;
	private String saveDate;
	private String[] no;
	private String[] mDate;
	private String[] potential;
	private String[] a;
	private String[] v;
	private String[] tongdian;
	private String[] measurer;
	private String[] remark;
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
	public String getJinzhan() {
		return jinzhan;
	}
	public void setJinzhan(String jinzhan) {
		this.jinzhan = jinzhan;
	}
	public String getcMonth() {
		return cMonth;
	}
	public void setcMonth(String cMonth) {
		this.cMonth = cMonth;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(String saveDate) {
		this.saveDate = saveDate;
	}
	public String[] getNo() {
		return no;
	}
	public void setNo(String[] no) {
		this.no = no;
	}
	public String[] getmDate() {
		return mDate;
	}
	public void setmDate(String[] mDate) {
		this.mDate = mDate;
	}
	public String[] getPotential() {
		return potential;
	}
	public void setPotential(String[] potential) {
		this.potential = potential;
	}
	public String[] getA() {
		return a;
	}
	public void setA(String[] a) {
		this.a = a;
	}
	public String[] getV() {
		return v;
	}
	public void setV(String[] v) {
		this.v = v;
	}
	public String[] getTongdian() {
		return tongdian;
	}
	public void setTongdian(String[] tongdian) {
		this.tongdian = tongdian;
	}
	public String[] getMeasurer() {
		return measurer;
	}
	public void setMeasurer(String[] measurer) {
		this.measurer = measurer;
	}
	public String[] getRemark() {
		return remark;
	}
	public void setRemark(String[] remark) {
		this.remark = remark;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("pl="+pl);
		sb.append("&section="+section);
		sb.append("&spec="+spec);
		sb.append("&jinzhan="+jinzhan);
		sb.append("&c_month="+cMonth);
		sb.append("&unit="+unit);
		sb.append("&save_date="+saveDate);
		for(int i = 0; i < no.length;i++){
			sb.append("&no="+no[i]);
		}
		for(int i = 0; i < mDate.length;i++){
			sb.append("&m_date="+mDate[i]);
		}
		for(int i = 0; i < potential.length;i++){
			sb.append("&potential="+potential[i]);
		}
		for(int i = 0; i < a.length;i++){
			sb.append("&a="+a[i]);
		}
		for(int i = 0; i < v.length;i++){
			sb.append("&v="+v[i]);
		}
		for(int i = 0; i < tongdian.length;i++){
			sb.append("&tongdian="+tongdian[i]);
		}
		for(int i = 0; i < measurer.length;i++){
			sb.append("&measurer="+measurer[i]);
		}
		for(int i = 0; i < remark.length;i++){
			sb.append("&remark="+remark[i]);
		}
		return sb.toString();
	}
	
}
