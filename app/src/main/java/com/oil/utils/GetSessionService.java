package com.oil.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.oil.activity.LoginActivity;
import com.oil.activity.OilApplication;
import com.oil.activity.gps_service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class GetSessionService extends Service {
	private Timer timer = new Timer(true);

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//任务
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				((OilApplication) getApplication()).setJSESSIONID(null);
				HttpRequestUtil.getSession((OilApplication) getApplication());
				Log.e("Tag","StartService**************************************");
			}
		};
		//启动定时器
		timer.schedule(task, 0, 10*60*1000);
		return Service.START_STICKY_COMPATIBILITY;
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
