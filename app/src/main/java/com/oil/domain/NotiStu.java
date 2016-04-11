package com.oil.domain;

import java.util.List;

public class NotiStu {
	private String id;
	private String title;
	private String content;
	private String time;
	private String path;
	private List<ReplyInfo> comment;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<ReplyInfo> getComment() {
		return comment;
	}
	public void setComment(List<ReplyInfo> comment) {
		this.comment = comment;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
