package com.oil.utils;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.oil.activity.R;

public class ParamsUtil {
	/**
	 * 将list序列成发送给服务器需要的格式
	 * @param list 数组
	 * @param name 数组对应的名称
	 * @return
	 */
	public static String listToParams(List<String> list, String name) {
		StringBuffer params = new StringBuffer();
		int i = 0;
		System.out.println(name + "-------------------" + list.size());
		for(String param : list) {
			params.append("&" + name + "=" + URLEncoder.encode(param));
			i++;
		}
		return params.toString();
	}
	
	/**
	 * 将多个list序列成发送给服务器需要的格式
	 * @param lists 所有list
	 * @param names 每个list对应的name列表
	 * @return
	 */
	public static String listToParams(List<List<String>> lists, List<String> names) {
		StringBuffer params = new StringBuffer();
		int i = 0;
		for(List<String> list : lists) {
			System.out.println(i+"==============================");
			params.append(listToParams(list, names.get(i)));
			i++;
		}
		return params.toString();
	}
	
	public static String mapToParams(Map<String, String> map) {
		StringBuffer params = new StringBuffer();
        Set<String> key = map.keySet();
        for (Iterator it = key.iterator(); it.hasNext();) {
            String s = (String) it.next();
            params.append("&" + s +"=" + map.get(s));
        }
        return params.toString();
    }
	
	public static String getVerifyByStatus(int status) {
		String verify = "";
		switch (status) {
		case 0:
			verify = "未审核";
			break;

		case 1:
			verify = "通过";
			break;
			
		default:
			verify = "不通过"; 
			break;
		}
		return verify;
	}
	
	public static Drawable getBackground(Context context, String status) {
		Resources resources = context.getResources();
		int id;
		if(status.equals("未审核")) {
			id = R.drawable.bg_pend;
		} else if(status.equals("通过")) {
			id = R.drawable.bg_pass;
		} else {
			id = R.drawable.bg_unpass;
		}
		Drawable dra = resources.getDrawable(id); 
		return dra;
	}
	
	public static Drawable getBackground(Context context,int status) {
		Resources resources = context.getResources();
		int id;
		if(status == 0) {
			id = R.drawable.bg_pend;
		} else if(status == 1) {
			id = R.drawable.bg_pass;
		} else {
			id = R.drawable.bg_unpass;
		}
		Drawable dra = resources.getDrawable(id); 
		return dra;
	}
	
	public static Drawable getBackgroundAsk(Context context, String status) {
		Resources resources = context.getResources();
		int id;
		if(status.equals("正常")) {
			id = R.drawable.bg_pass;
		} else {
			id = R.drawable.bg_unpass;
		}
		Drawable dra = resources.getDrawable(id); 
		return dra;
	}
}
