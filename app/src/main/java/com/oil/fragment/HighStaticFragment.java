package com.oil.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.WebViewActivity;
import com.oil.adapter.HighConsquenceTrendsAdapter;
import com.oil.domain.PipeRecord;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.JSONParse;
import com.oil.utils.Urls;

public class HighStaticFragment extends Fragment implements IXListViewListener {

	private LinearLayout lin_pro;
	private XListView lvStatic;
	private boolean isFirst = true;
	private HighConsquenceTrendsAdapter adapter;
	private Thread getThread;
	private Boolean isEnd = false;
	private String judgeStr = new String();
	private String searchData = new String();
	private boolean judge = false, isSearch = false, isRefresh = false;
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			judge = intent.getBooleanExtra("which", false);
			if (!judge) {
				isSearch = intent.getBooleanExtra("isSearch", false);
				searchData = intent.getStringExtra("searchData");
				lin_pro.setVisibility(View.VISIBLE);
				lvStatic.setVisibility(View.GONE);
				startSubThread();
			}
		}
	};

	private BroadcastReceiver createReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			judge = arg1.getBooleanExtra("which", false);
			isRefresh = arg1.getBooleanExtra("isRefresh", false);
			if (!judge && isRefresh) {
				isSearch = false;
				searchData = new String();
				searchData = "";
				startSubThread();
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				try {
					adapter = new HighConsquenceTrendsAdapter(
							JSONParse.parseHighStaticRecord(msg.obj.toString()));
				} catch (Exception e) {
					adapter = new HighConsquenceTrendsAdapter(new ArrayList<PipeRecord>());
					e.printStackTrace();
				}
				lin_pro.setVisibility(View.GONE);
				lvStatic.setVisibility(View.VISIBLE);
				lvStatic.setAdapter(adapter);
				if (isFirst) {
					isFirst = false;
				}
				isEnd = false;
				if (adapter.getCount() < 50) {
					isEnd = true;
				}
				lvStatic.setIsEnd(isEnd);
				lvStatic.invalidate();
				onLoad();
			} else if (msg.what == 2) {
				List<PipeRecord> list = null;
				try {
					list = JSONParse.parseHighStaticRecord(msg.obj.toString());
				} catch (Exception e) {
					list = new ArrayList<PipeRecord>();
					e.printStackTrace();
				}
				if (list.size() == 0) {
					isEnd = true;
					lvStatic.setIsEnd(isEnd);
				} else {
					for (PipeRecord pp : list) {
						adapter.list.add(pp);
					}
					adapter.notifyDataSetChanged();
					lvStatic.invalidate();
				}

				onLoad();
			}
		}
	};

	private Handler toastHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(getActivity().getApplicationContext(),
					msg.obj.toString(), 1).show();
			lin_pro.setVisibility(View.GONE);
			lvStatic.setVisibility(View.VISIBLE);
			onLoad();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.high_dca_information_list, null);

		IntentFilter filter = new IntentFilter();
		filter.addAction("actionSearch");
		getActivity().registerReceiver(myReceiver, filter);

		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("actionCreate");
		getActivity().registerReceiver(createReceiver, filter1);

		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		lvStatic = (XListView) v.findViewById(R.id.high_dca_information_list_listview);
		lvStatic.setPullLoadEnable(true);
		lvStatic.setXListViewListener(this);
		startSubThread();

		lvStatic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				intent.putExtra("url",
						Urls.URL + "admin/base/h_sequel/show?id="
								+ adapter.list.get(arg2 - 1).get_id());
				startActivity(intent);
			}
		});

		return v;
	}

	// 需要加上下拉无更多信息的提示
	private void startSubThread() {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String res = new String();
				if (isSearch) {
					res = HttpGetDataByGet.getDataFromServer(Urls.HCSR + "?"
							+ searchData, ((OilApplication) getActivity()
							.getApplication()).getJSESSIONID());
				} else {
					res = HttpGetDataByGet.getDataFromServer(Urls.HCSR,
							((OilApplication) getActivity().getApplication())
									.getJSESSIONID());
				}
				if (judgeStr.equals(res)) {
					Message msg = toastHand.obtainMessage();
					msg.obj = "没有新信息";
					toastHand.sendMessage(msg);
				} else {
					judgeStr = res;
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = res;
					handler.sendMessage(msg);
				}

			}
		});
		getThread.start();
	}

	private void onLoad() {
		lvStatic.stopRefresh();
		lvStatic.stopLoadMore();
		lvStatic.setRefreshTime(DateFormaterUtil
				.getCurrentDate("MM-dd hh:mm:ss"));
	}

	@Override
	public void onRefresh() {
		startSubThread();
	}

	@Override
	public void onLoadMore() {
		if(getThread==null || !getThread.isAlive()) {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = 2;
				String res = new String();
				if (isSearch) {
					res = Urls.HCSR + "?pageOffset=" + adapter.list.size()
							+ "&" + searchData;
				} else {
					res = Urls.HCSR + "?pageOffset=" + adapter.list.size();
				}
				msg.obj = HttpGetDataByGet.getDataFromServer(res,
						((OilApplication) getActivity().getApplication())
								.getJSESSIONID());
				handler.sendMessage(msg);
			}
		});
		getThread.start();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(myReceiver);
		getActivity().unregisterReceiver(createReceiver);
	}
}
