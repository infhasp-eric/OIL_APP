package com.oil.utils;



public class ValidateUtil {
	/**
	 * 校验首尾是否含有空格
	 * @param str
	 * @return
	 */
	public static Boolean hasSpaceHeadAndTail(String str){
		String HAS_SPACE = "(^\\S*)|(\\S*$)";
		return str.matches(HAS_SPACE);
	}
	//校验发货时间格式是否正确
	public static Boolean isSendTime(String str) {
		String IS_SEND_TIME = "(^(\\d+\\.\\d{2}\\,\\d+\\.\\d{2}\\;?)+$)";
		return str.matches(IS_SEND_TIME);
	}
	/**
	 * 是否是正整形数字
	 * @param str
	 * @return
	 */
	public static Boolean isIntNumber(String str) {
		String IS_INT_NUMBER = "(^[0-9]*$)";
		return str.matches(IS_INT_NUMBER);
	}
	
	/**
	 * 电话号码校验	
	 * @param str
	 * @return
	 */
	public static Boolean isPhone(String str) {
		String isa = "[0-9-]+";
		return str.matches(isa);
	}
	
	/**
	 * 仅能中文，英文，数字及下划线
	 * @param str
	 * @return
	 */
	public static Boolean isEnable(String str) {
		String isa = "[a-zA-Z0-9_\u4e00-\u9fa5]+";
		return str.matches(isa);
	}
	
	
	public static Boolean isEnableCh(String str) {
		String isa = "[a-zA-Z0-9\u4e00-\u9fa5\\s]+";
		return str.matches(isa);
	}
	
	/**
	 * 仅能英文，数字及下划线
	 * @param str
	 * @return
	 */
	public static Boolean isEnableUC(String str) {
		String isa = "[a-zA-Z0-9_]+";
		return str.matches(isa);
	}
	
	/**
	 * 仅能英文，数字及下划线
	 * @param str
	 * @return
	 */
	public static Boolean isEnableEN(String str) {
		String isa = "[a-zA-Z0-9]+";
		return str.matches(isa);
	}
	
	public static void main(String [] args) {
		/*String send = "0:00-11:00;15:00-18:00;20:00-22:00";
		String sendTimes = send.replaceAll("：", ".")
				.replaceAll("-", ",").replaceAll("；", ";")
				.replaceAll(" ", "").replace(":", ".");
		//去掉时间中的制表符
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(sendTimes);
        sendTimes = m.replaceAll("");*/
		String path = "吉面馆.jpg";
	}

}
