package com.oil.activity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.Urls;

public class gps_service extends Service {
	/*
	 * private LocationClient mLocationClient; private BDLocationListener
	 * myListener;
	 */
	private StringBuffer sb;
	private String str;
	private int date = 1000;
	private Timer mTimer;
	private int starttime;
	private int endtime;
	private int span;

	private LocationClient mLocationClient;// 定位服务
	public MyLocationListener mMyLocationListener;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private LocationClientOption option;

	private SharedPreferences sf;

	/**
	 * 更新储存的时间信息
	 */
	private Handler setHand = new Handler() {
		public void handleMessage(Message msg) {
			try {
				JSONObject json = new JSONObject(msg.obj.toString());
				JSONObject times = json.getJSONObject("data").getJSONObject(
						"time");
				int time = times.getInt("time");
				String start_time = times.getString("start_time");
				String end_time = times.getString("end_time");
				Editor editor = sf.edit();
				if (span != time) {// 若与服务器上的时间不相同则更改间隔时间
					editor.putInt("updateScan", time);
					span = time;
					option.setScanSpan(span * 60000);
					mLocationClient.setLocOption(option);
				}
				if (getMinutes(start_time) != starttime) {
					starttime = getMinutes(start_time);
					editor.putInt("start_time", starttime);
				}
				if (getMinutes(end_time) != endtime) {
					endtime = getMinutes(end_time);
					editor.putInt("end_time", endtime);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		sf = getSharedPreferences("workTime", Activity.MODE_PRIVATE);
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();

		InitLocation();
		mLocationClient.registerLocationListener(mMyLocationListener);// 为地理信息服务设置监听事件
		mLocationClient.start();// 启动地理信息服务

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY_COMPATIBILITY;
	}

	@Override
	public void onDestroy() {
		mLocationClient.stop();// 停止地理信息服务
		super.onDestroy();
		// mLocationClient.unRegisterLocationListener(myListener);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			Calendar cal = Calendar.getInstance();// 当前日期
			int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
			int minute = cal.get(Calendar.MINUTE);// 获取分钟
			int nowtime = (hour * 60) + minute;

			System.out.println("=====================================");
			System.out.println("starttime==" + starttime);
			System.out.println("endtime==" + endtime);
			System.out.println("nowtime==" + nowtime);
			System.out.println("=====================================");
			if (nowtime >= starttime && nowtime <= endtime) {//判断是否为工作时间
				final StringBuffer params = new StringBuffer();
				params.append("longitude=" + location.getLongitude());
				params.append("&latitude=" + location.getLatitude());
				params.append("&describe=" + location.getAddrStr());
				System.out.println("GPS开始上传");
				System.out.println(params.toString());
				try {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String req = HttpRequestUtil.PostHttp(
									Urls.LOCATION, params.toString(),
									(OilApplication) getApplication());
							System.out.println("Gps上传返回========" + req);
							Message msg = setHand.obtainMessage();
							msg.obj = req;
							setHand.sendMessage(msg);
						}
					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Log.i("BaiduLocationApiDem", sb.toString());
				// System.out.println("GPS定位信息" + sb.toString());
			}
		}
	}

	private void InitLocation() {
		option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		starttime = sf.getInt("start_time", 480); // 开始时间
		endtime = sf.getInt("end_time", 1080);// 结束时间
		span = sf.getInt("updateScan", 60);
		option.setScanSpan(span * 60000);// 设置发起定位请求的间隔时间,默认为1小时
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	private String sendToServer(BDLocation location) {
		StringBuffer params = new StringBuffer();
		params.append("longitude=" + location.getLongitude());
		params.append("&latitude=" + location.getLatitude());
		params.append("&describe=" + location.getAddrStr());

		return params.toString();
	}

	public int getMinutes(String time) {
		String[] times = time.split(":");
		int hour = Integer.valueOf(times[0]);
		int minute = Integer.valueOf(times[1]);
		return (hour * 60) + minute;
	}
}
