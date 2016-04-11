package com.oil.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.WebViewActivity;
import com.oil.adapter.High_DCA_Information_List_Adapter;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.Urls;

public class HighDynamicFragment extends Fragment implements IXListViewListener {

	private XListView list;
	private LinearLayout lin_pro;
	private List<Map<String, Object>> listdatas;
	private High_DCA_Information_List_Adapter HIGH_DCA_ListAdapter;

	private Thread getThread;
	private Handler holder;
	private Handler newHand;
	private Thread upThread;
	boolean isrung = false;
	private Boolean isEnd = false;
	private boolean isRefer = false;
	private boolean isSearch = false,judge = true,isRefresh = false;
	private String lastStr;
	private String searchData = new String();
	private Handler tostHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(getActivity().getApplicationContext(),
					msg.obj.toString(), Toast.LENGTH_SHORT).show();
			isRefer = false;

			lin_pro.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
			onLoad();
		}
	};
	
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			judge = intent.getBooleanExtra("which",false);
			if(judge){
				isSearch = intent.getBooleanExtra("isSearch",false);
				Log.e("Tag","receiver = "+intent.getStringExtra("searchData"));
				searchData = intent.getStringExtra("searchData");
				lin_pro.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
				getThread = new Thread(new Runnable() {

					@Override
					public void run() {
						updateList();
					}
				});
				getThread.start();
			}
		}
	};
	
	private BroadcastReceiver createReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			judge = arg1.getBooleanExtra("which",false);
			isRefresh = arg1.getBooleanExtra("isRefresh",false);
			if(judge&&isRefresh){
				getThread = new Thread(new Runnable() {
					@Override
					public void run() {
						// 这里写入子线程需要做的工作
						isSearch = false;
						searchData = new String();
						searchData = "";
						updateList();
					}
				});
				getThread.start(); // 启动线程
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.high_dca_information_list, null);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("actionSearch");
		getActivity().registerReceiver(myReceiver,filter);
		
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("actionCreate");
		getActivity().registerReceiver(createReceiver,filter1);

		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		list = (XListView) v
				.findViewById(R.id.high_dca_information_list_listview);

		// 将事件与listView进行绑定
		list.setPullLoadEnable(true);
		list.setXListViewListener(this);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				intent.putExtra(
						"url",
						Urls.URL
								+ "admin/base/d_sequel/show?id="
								+ HIGH_DCA_ListAdapter.listdatas.get(
										position - 1).get("id"));
				startActivity(intent);

			}

		});
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				updateList();
			}
		});
		getThread.start(); // 启动线程

		holder = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				HIGH_DCA_ListAdapter = new High_DCA_Information_List_Adapter(
						getActivity(), listdatas); // 创建适配器
				lin_pro.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				list.setAdapter(HIGH_DCA_ListAdapter);
				list.setIsEnd(isEnd);
				// 更新
				list.invalidate();
			}
		};
		/**
		 * 查询按钮事件
		 */
		return v;
	}


	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime(DateFormaterUtil.getCurrentDate("MM-dd hh:mm:ss"));
	}

	@Override
	public void onRefresh() {
		isRefer = true;
		isEnd = false;
		getThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				updateList();
			}
		});
		getThread.start(); // 启动线程

		holder = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				HIGH_DCA_ListAdapter = new High_DCA_Information_List_Adapter(
						getActivity(), listdatas); // 创建适配器
				// Log.e("items", listdatas.size() + "======================");
				list.setAdapter(HIGH_DCA_ListAdapter);
				// 更新
				list.invalidate();
				list.setIsEnd(isEnd);
				isRefer = false;
				onLoad();
			}
		};
		//onLoad();
	}

	@Override
	public void onLoadMore() {
		if ((upThread == null || !upThread.isAlive()) && !isRefer) {
			try {
				upThread = new Thread(new Runnable() {
					@Override
					public void run() {
						updateListByUp();
					}
				});

				upThread.start();

				newHand = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						JSONArray jarrat = (JSONArray) msg.obj;
						// 判断数组书否为空
						if (jarrat != null) {
							for (int i = 0; i < jarrat.length(); i++) {
								try {
									JSONObject json1 = jarrat.getJSONObject(i);
									listdatas.add(getItem(json1));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							HIGH_DCA_ListAdapter.notifyDataSetChanged();// 提醒adapter更新
							// 对列表设置是否加载完成
							if (isEnd) {
								list.setIsEnd(isEnd);
							}
							onLoad();
						}
					}
				};
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateList() {
		String data = new String();
		if(isSearch){
			data = searchData;
		}else{
			data = "";
		}
		String res = HttpRequestUtil.sendGet(Urls.URL
				+ "services/base/d_sequel/list", data,
				(OilApplication) getActivity().getApplication());
		if(!res.equals(lastStr)) {
			lastStr = res;
		} else {
			Message msg = tostHand.obtainMessage();
			msg.obj = "没有新信息";
			tostHand.sendMessage(msg);
			return;
		}
		try {
			JSONObject json = new JSONObject(res);
			if (json.getInt("status") == 200) {
				listdatas = getListItems(json.getJSONArray("data"));
				int totalRecord = json.getInt("totalRecord");
				System.out.println("totalRecord=========" + totalRecord);
				if (totalRecord > 0) {
					listdatas = getListItems(json.getJSONArray("data"));
					System.out.println("size========" + listdatas.size());
					if (totalRecord == listdatas.size() || listdatas.size() < 50) {
						isEnd = true;
					}
				} else {
					listdatas = new ArrayList<Map<String, Object>>();
					isEnd = true;
				}
			}
		} catch (JSONException e) {
			listdatas = new ArrayList<Map<String, Object>>();
			isEnd = true;
			e.printStackTrace();
		}
		holder.sendMessage(holder.obtainMessage());
	}

	private List<Map<String, Object>> getListItems(JSONArray array) {
		List<Map<String, Object>> listdatas = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject json = array.getJSONObject(i);
				listdatas.add(getItem(json));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listdatas;
	}

	public void updateListByUp() {
		if (HIGH_DCA_ListAdapter != null && HIGH_DCA_ListAdapter.getCount() > 0) {
			String data = new String();
			if(isSearch){
				data = searchData;
			}else{
				data = "";
			}
			String res = HttpRequestUtil.sendGet(Urls.URL
					+ "services/base/d_sequel/list", "pageOffset="
					+ HIGH_DCA_ListAdapter.listdatas.size() + "&" + data,
					(OilApplication) getActivity().getApplication());
			JSONArray jarrat = null;
			try {
				JSONObject json = new JSONObject(res);
				jarrat = json.getJSONArray("data");

				int totalRecord = json.getInt("totalRecord");
				if ((HIGH_DCA_ListAdapter.getCount() + jarrat.length()) == totalRecord) {
					isEnd = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Message msg = newHand.obtainMessage();
			msg.obj = jarrat;
			newHand.sendMessage(msg);
		}
	}

	public Map<String, Object> getItem(JSONObject json) {
		try {
			Map<String, Object> itco_map = new HashMap<String, Object>();
			itco_map.put("id", json.getInt("id"));
			try {
				itco_map.put("status", json.getString("status"));// 审核状态
				itco_map.put("pl_name", json.getString("pl_name"));// 管线名称
				itco_map.put("pl_section_name",
						json.getString("pl_section_name"));// 起止段落
				itco_map.put("pl_spec_name", json.getString("pl_spec_name"));// 管线规格
				itco_map.put("create_time", DateFormaterUtil.getDateToString(
						json.getLong("create_time"), "yyyy-MM-dd HH:mm:ss"));// 创建时间
			} catch (Exception e) {
				itco_map.put("status", "");
				itco_map.put("pl_name", "");
				itco_map.put("pl_section_name", "");
				itco_map.put("pl_spec_name", "");
				itco_map.put("create_time", "");

			}// 时间
				// listdatas.add(itco_map);
			return itco_map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(myReceiver);
		getActivity().unregisterReceiver(createReceiver);
	}
}
