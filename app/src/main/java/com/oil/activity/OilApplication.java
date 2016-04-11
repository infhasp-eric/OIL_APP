package com.oil.activity;

import com.baidu.frontia.FrontiaApplication;

public class OilApplication extends FrontiaApplication{
	private String JSESSIONID;
	
	private String WEBJSSIONID;
	private String username;
	private String password;
    private String DOWNPATH;
	
	@Override
    public void onCreate() {
            super.onCreate();
            setJSESSIONID(NULL); //初始化全局变量
            setWEBJSSIONID(NULL);
            setUsername(NULL);
            setPassword(NULL);
            setDOWNPATH(NULL);
    }
    public String getJSESSIONID() {
            return JSESSIONID;
    }
    public void setJSESSIONID(String JSESSIONID) {
            this.JSESSIONID = JSESSIONID;
    }
    
    public String getWEBJSSIONID() {
		return WEBJSSIONID;
	}
	public void setWEBJSSIONID(String wEBJSSIONID) {
		WEBJSSIONID = wEBJSSIONID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

    public String getDOWNPATH() {
		return DOWNPATH;
	}
	public void setDOWNPATH(String dOWNPATH) {
		DOWNPATH = dOWNPATH;
	}
    private static final String NULL = null;
}
