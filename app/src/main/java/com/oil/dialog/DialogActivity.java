package com.oil.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.oil.activity.Change_Passwd;
import com.oil.activity.LoginActivity;
import com.oil.activity.MainMenu;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.gps_service;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.Urls;

/**
 * @author yangyu 功能描述：弹出Activity界面
 */
public class DialogActivity extends Activity implements OnClickListener {
	private LinearLayout layout01, layout02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);

		initView();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 得到布局组件对象并设置监听事件
		layout01 = (LinearLayout) findViewById(R.id.llayout01);
		layout02 = (LinearLayout) findViewById(R.id.llayout02);

		layout01.setOnClickListener(this);
		layout02.setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llayout01:
			Intent intent1 = new Intent(DialogActivity.this,
					Change_Passwd.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.llayout02:
			((OilApplication) getApplication()).setJSESSIONID(null);
			HttpRequestUtil.getDataFromServer(Urls.URL + "services/login_out");
			Intent gpsintent = new Intent(DialogActivity.this, gps_service.class);
			stopService(gpsintent);
			Intent intent = new Intent(DialogActivity.this, LoginActivity.class);
			intent.putExtra("cancel", "exit");
			startActivity(intent);
			finish();
			break;

		}

	}
}
