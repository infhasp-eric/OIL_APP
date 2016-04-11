package com.oil.utils;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class ProperUtil {
	private static Properties urlProps;

	//获取配置文件权限
	public static Properties getProperties(Context c) {
		Properties props = new Properties();
		try {
			// 读取配置文件
			InputStream in = c.getAssets().open("appConfig.properties");
			props.load(in);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		urlProps = props;

		//System.out.println(urlProps.getProperty("serverUrl"));
		return urlProps;
	}

}
