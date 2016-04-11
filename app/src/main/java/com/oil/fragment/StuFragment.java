package com.oil.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.oil.activity.NotiStuContentActivity;
import com.oil.activity.R;
import com.oil.adapter.NotiStuAdapter;
import com.oil.domain.NotiStu;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONParse;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

public class StuFragment extends Fragment implements IXListViewListener {

	private XListView listview;
	private LinearLayout lin_pro;
	private List<NotiStu> list;
	private Boolean isEnd = false;
	private NotiStuAdapter adapter;
	private String lastRes;
	private Boolean isNoRe = false;
	private int type;
	private boolean isSearch = false;
    private String searchData = new String();
	final Calendar cal = Calendar.getInstance();
	private Thread getThread;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (isNoRe) {
					Toast.makeText(getActivity(), "没有新信息", 1000).show();
				} else {
					list = new ArrayList<NotiStu>();
					list = JSONParse.parseNotiStu(msg.obj.toString());
					adapter = new NotiStuAdapter(list);
					listview.setAdapter(adapter);
					listview.invalidate();
					// 需要一次判断是否加载完成
					try {
						JSONObject json = new JSONObject(msg.obj.toString());
						int totalRecord = json.getInt("totalRecord");
						if (adapter.getCount() == totalRecord) {
							isEnd = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (isEnd) {
					listview.setIsEnd(isEnd);
				}
				isNoRe = false;
				lin_pro.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				listview.stopRefresh();
				listview.stopLoadMore();
				listview.setRefreshTime(DateFormaterUtil
						.getCurrentDate("MM-dd HH:mm:ss"));
			} else if (msg.what == 2) {
				List<NotiStu> list = JSONParse.parseNotiStu(msg.obj.toString());
				System.out.println(msg.obj.toString());
				try {
					JSONObject json = new JSONObject(msg.obj.toString());
					int totalRecord = json.getInt("totalRecord");
					if (adapter.getCount() + list.size() == totalRecord) {
						isEnd = true;
					}
				} catch (Exception e) {
				}
				if (isEnd) {
					listview.setIsEnd(isEnd);
				}
				if (list.size() == 0) {
					isEnd = true;
				} else {
					for (NotiStu ns : list) {
						adapter.list.add(ns);
					}
				}
				if (isEnd) {
					listview.setIsEnd(isEnd);
				}
				adapter.notifyDataSetChanged();
				listview.invalidate();

				lin_pro.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				listview.stopRefresh();
				listview.stopLoadMore();
				listview.setRefreshTime(DateFormaterUtil.getCurrentDate("MM-dd hh:mm:ss"));
			}

		}
	};
	
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			type = intent.getIntExtra("searchKey",1);
			if (type == 3) {
				lin_pro.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				isSearch = intent.getBooleanExtra("isSearch", false);
				searchData = intent.getStringExtra("searchData");
				Log.v("Tag","myreceiver searchdata="+searchData);
				startSubThread(searchData);
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View studyLayout = LayoutInflater.from(getActivity()).inflate(
				R.layout.notification_study, null);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("actionSearchKey");
		getActivity().registerReceiver(myReceiver, filter);
		
		init(studyLayout);
		startSubThread("");

		return studyLayout;
	}

	private void init(final View v) {
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		listview = (XListView) v.findViewById(R.id.notification_study_list);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						NotiStuContentActivity.class);
				intent.putExtra("id", list.get(arg2 - 1).getId());
				startActivity(intent);
			}
		});
	}

	protected void updateListByUp(String data) {
		String res = null;
		res = HttpRequestUtil.PostHttp(Urls.NS,"pageOffset="
				+ adapter.getCount() + "&" + data);
		
		Message msg = handler.obtainMessage();
		msg.what = 2;
		msg.obj = res;
		handler.sendMessage(msg);
	}

	private void startSubThread(final String data) {
		isEnd = false;
		listview.setIsEnd(isEnd);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				Message msg = handler.obtainMessage();
				String res = new String();
				if(isSearch){
					res = Urls.NS;
					searchData = data;
					//Log.e("Tag","stugetdatafromserver=" + Urls.NS + "?"+ data+"======="+URLEncoder.encode(data,"utf-8"));
				}else{
					res = Urls.NS;
					searchData = "";
				}
				System.out.println(searchData);
				msg.obj = HttpRequestUtil.PostHttp(res, searchData);
				msg.what = 1;
				lastRes = msg.obj.toString();
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void startSubThreadByRe(final String data) {
		isEnd = false;
		listview.setIsEnd(isEnd);
		// System.out.println("开始请求！！！！！============");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				Message msg = handler.obtainMessage();
				String res = new String();
				if(isSearch){
					res = Urls.NS;
					searchData = data;
					//Log.e("Tag","stugetdatafromserver=" + Urls.NS + "?"+ data+"======="+URLEncoder.encode(data,"utf-8"));
				}else{
					res = Urls.NS;
					searchData = "";
				}
				System.out.println(searchData);
				msg.obj = HttpRequestUtil.PostHttp(res, searchData);
				msg.what = 1;
				if (!StringUtil.isBlank(lastRes)
						&& lastRes.equals(msg.obj.toString())) {
					// 两次相同时则返回true
					isNoRe = true;
				} else {
					lastRes = msg.obj.toString();
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onRefresh() {
			startSubThreadByRe(searchData);
	}

	@Override
	public void onLoadMore() {
		if(getThread == null || !getThread.isAlive()) {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				updateListByUp(searchData);
			}
		});
		getThread.start();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(myReceiver);
	}
}
