package com.oil.utils;

/**
 * 这里是一大堆地址
 * 
 * @author East
 * 
 */
public class Urls {

	// 服务器地址
	public static final String URL = "http://121.40.16.166/oil/";
	public static final String WEBSHOW = "http://121.40.16.166/oil";
	//正式服务器地址
//	public static final String URL = "http://121.40.16.166/oil/";
//	public static final String WEBSHOW = "http://121.40.16.166/oil";
//	public static String URL = "http://10.0.0.10:8080/oil/";
//	public static final String WEBSHOW = "http://10.0.0.10:8080/oil";
	 //传送userId和channelId
	 public static final String IDSAVE = URL + "services/base/push/save";
	 
	// GPS信息采集
	public static final String GPShttp = URL + "services/save_location";
	// 登录
	public static final String LOGIN = URL + "services/login";
	public static final String CHANGEPASSWD = URL + "services/change_pwd";

	// 管道保护电位测量记录列表
	public static final String PPPMR = URL + "services/base/pl_measure/list";
	public static final String PPPMRS = URL + "services/base/pl_measure/save";
	public static final String PPPMRD = URL + "admin/base/pl_measure/show?id=";
	// 阴极保护电位月综合记录
	public static final String CPSRMR = URL + "services/base/rc_comp/list";
	public static final String CPSRMRS = URL + "services/base/rc_comp/save";
	// 绝缘接头（法兰）性能测试记录
	public static final String IJPMR = URL + "services/base/p_measure/list";
	public static final String IJPMRS = URL + "services/base/p_measure/save";
	// 获取管线
	public static final String PL = URL
			+ "services/base/pipeline/get?by_user=1";
	// 获取起止段落
	public static final String SECTION = URL
			+ "services/base/section/get?by_user=1&pl_id=";
	// 获取管线规格
	public static final String SPEC = URL
			+ "services/base/spec/get?by_user=1&pl_section_id=";
	// 管线巡检工作记录
	public static final String PPWR = URL + "services/base/pl_patrol/list";
	public static final String PPWRS = URL + "services/base/pl_patrol/save";
	// 高后果区动态资料
	public static final String HCTR = URL + "services/base/d_sequel/list";
	// 静态高后果区资料
	public static final String HCSR = URL + "services/base/h_sequel/list";
	public static final String HCSRS = URL + "services/base/h_sequel/save";
	// 通知与学习
	public static final String NS = URL + "services/notice_list";
	public static final String NSD = URL + "services/notice_detail?id=";
	// 回复公共通知
	public static final String NSDR = URL + "services/notice_reply";
	// 回复临时性通知
	public static final String TMPR = URL + "services/tmp_notice_reply";
	// 回复一事一案
	public static final String EVER = URL + "services/event/reply";
	// 上传文件接口链接
	public static final String UPLOAD = URL + "services/uploadFile?fileName=";

	public static final String SAVEFILE = URL + "file/";
	
	public static final String LOCATION = URL + "services/save_location";
	
	public static final String GETAUTH = URL + "services/getAuthors";
	

	//public static final String WEBSHOW = "http://10.0.0.2:8080/oil";
	
}
