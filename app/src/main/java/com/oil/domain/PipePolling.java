package com.oil.domain;

import java.util.List;

public class PipePolling {
	private String pl;
	private String section;
	private String spec;
	private String jinzhan;
	private String pMonth;
	private String saveJinzhan;
	private List<String> pDate;
	private List<String> workPlace;
	private List<String> content;
	private List<String> question;
	private List<String> voiceRecord;
	private List<String> worker;
	private List<String> auditor;
	private List<String> fileName;
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
	public String getpMonth() {
		return pMonth;
	}
	public void setpMonth(String pMonth) {
		this.pMonth = pMonth;
	}
	public List<String> getpDate() {
		return pDate;
	}
	public void setpDate(List<String> pDate) {
		this.pDate = pDate;
	}
	public List<String> getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(List<String> workPlace) {
		this.workPlace = workPlace;
	}
	public List<String> getContent() {
		return content;
	}
	public void setContent(List<String> content) {
		this.content = content;
	}
	public List<String> getQuestion() {
		return question;
	}
	public void setQuestion(List<String> question) {
		this.question = question;
	}
	public List<String> getVoiceRecord() {
		return voiceRecord;
	}
	public void setVoiceRecord(List<String> voiceRecord) {
		this.voiceRecord = voiceRecord;
	}
	public List<String> getWorker() {
		return worker;
	}
	public void setWorker(List<String> worker) {
		this.worker = worker;
	}
	public List<String> getAuditor() {
		return auditor;
	}
	public void setAuditor(List<String> auditor) {
		this.auditor = auditor;
	}
	
	public String getSaveJinzhan() {
		return saveJinzhan;
	}
	public void setSaveJinzhan(String saveJinzhan) {
		this.saveJinzhan = saveJinzhan;
	}
	public List<String> getFileName() {
		return fileName;
	}
	public void setFileName(List<String> fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("pl=" + pl);
		sb.append("&section=" + section);
		sb.append("&spec=" + spec);
		sb.append("&jinzhan=" + jinzhan);
		sb.append("&p_month=" + pMonth);
		sb.append("&save_jinzhan=" + saveJinzhan);
		for(int i = 0; i < pDate.size(); i++){
			sb.append("&p_date=" + pDate.get(i) + "");
		}
		for(int i = 0; i < workPlace.size(); i++){
			sb.append("&work_place=" + workPlace.get(i) + "");
		}
		for(int i = 0; i < content.size(); i++){
			sb.append("&content=" + content.get(i) + "");
		}
		for(int i = 0; i < question.size(); i++){
			sb.append("&question=" + question.get(i) + "");
		}
		for(int i = 0; i < voiceRecord.size(); i++){
			sb.append("&voice_record=" + voiceRecord.get(i) + "");
		}
		for(int i = 0; i < worker.size(); i++){
			sb.append("&worker=" + worker.get(i) + "");
		}
		for(int i = 0; i < auditor.size(); i++){
			sb.append("&auditor=" + auditor.get(i) + "");
		}
		try {
			for(String s : fileName) {
				sb.append("&fileName=" + s + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
