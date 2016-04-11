package com.oil.utils;

/**
 * 判断字符串是否为null或者""
 * 
 * @author East
 * 
 */

public class StringUtils {
	/**
	 * 
	 * @param orinal
	 * @return if string is empty(including null), return true, else false
	 */
	public static boolean isEmpty(String orinal) {
		if (orinal == null) {
			return true;
		} else {
			orinal = orinal.trim();
			if (orinal == null) {
				return true;

			} else {
				if (orinal.equals("")) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	/**
	 * 服务器返回的路径（时间戳）转换成有格式的时间
	 * 
	 * @param path
	 * @return
	 */
	public static String PathtoDate(String path) {

		String str = path;
//		int start = path.lastIndexOf("/");
//		if (start < 0) {
//			start = fileUrl.lastIndexOf("\\");
//		}
		String filename = path.substring(path.lastIndexOf("/") + 1,path.lastIndexOf("/") + 14
				);
		String date = DateFormaterUtil.getDateToString(
				Long.parseLong(filename), "yyyy年MM月dd日" + "上传的附件");
		return date;
	}
	
	public static String getFileName(String path) {
		int index = 0;
		index = path.lastIndexOf("\\") + 1;
		String path1 = path.substring(index);
		index = path1.lastIndexOf("/") + 1;
		System.out.println(path1);
		return path1.substring(index);
	}
	
	public static String PathtoDate2(String path) {

		String str = path;
		int start = path.lastIndexOf("/") + 1;
		if (start < 0) {
			start = str.lastIndexOf("\\") + 1;
		}
		String date;
		try {
		String filename = path.substring(start,start + 13
				);
		date = DateFormaterUtil.getDateToString(
				Long.parseLong(filename), "yyyy年MM月dd日" + "上传的附件");
		} catch (Exception e) {
			date = str;
		}
		return date;
	}
	
}
