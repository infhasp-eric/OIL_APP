package com.oil.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

/**
 * 父界面，提供全屏快捷跳转等方法
 * 
 * @author East
 * 
 */
public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

	}

	/**
	 * 界面跳转
	 * 
	 * @param from
	 *            本界面
	 * @param to
	 *            下一个界面
	 */
	public void jumpToActivity(Context from, Class<?> to) {
		Intent intent = new Intent();
		intent.setClass(from, to);
		startActivity(intent);
	}
	/**
	 * 得到输入框中的值
	 * @param et
	 * @return
	 */
	public String getStringFrom(EditText et) {
		return et.getText().toString();
	}
}
