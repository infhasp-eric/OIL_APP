package com.oil.fragment;

import java.net.URLEncoder;
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
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.activity.AskNoticeDetailActivity;
import com.oil.activity.LoginActivity;
import com.oil.activity.MainMenu;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.TmpNoticeDetailActivity;
import com.oil.activity.R.id;
import com.oil.activity.R.layout;
import com.oil.adapter.AskNoticeAdapter;
import com.oil.adapter.TmpNoticeAdapter;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class NoticeListFragment extends Fragment implements IXListViewListener {

	private Integer type;
	private LinearLayout lin_pro;
	String serverUrl;
	private List<Map<String, Object>> listItems;
	private Thread getThread;
	private Handler listHand;
	private List<Integer> idList = new ArrayList<Integer>();
	private Boolean isEnd = false;
	private BaseAdapter noticeAdapter;
	// private ProgressDialog dialog = null;
	public static NoticeListFragment nl;
	private XListView list_notice;
	private boolean isRefer = false;
	private int i = 0;
	private String lastStr;//最后一次请求的数据
	private boolean isSearch = false;
    private String searchData = new String();

	//弹出提示框的广播
	private Handler tostHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(getActivity().getApplicationContext(),
					msg.obj.toString(), 1).show();
			onLoad();
			isRefer = false;
		}
	};
	
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			type = intent.getIntExtra("searchKey",1);
			if (type == 1||type == 2) {
				isSearch = intent.getBooleanExtra("isSearch", false);
				searchData = intent.getStringExtra("searchData");
				Log.v("Tag","myreceiver searchdata="+searchData);
				getThread = new Thread(new Runnable() {
					@Override
					public void run() {
						// 这里写入子线程需要做的工作
						updateList();
					}
				});
				getThread.start();
			}
		}
	};
	
	private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			type = intent.getIntExtra("updateType",2);
			getThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// 这里写入子线程需要做的工作
					updateList();
				}
			});
			getThread.start();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_notice_list, null);

		list_notice = (XListView) view.findViewById(R.id.list_notice);
		lin_pro = (LinearLayout) view.findViewById(R.id.lin_pro);

		nl = this;
		serverUrl = Urls.WEBSHOW;
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("actionSearchKey");
		getActivity().registerReceiver(myReceiver, filter);
		
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("actionUpdate");
		getActivity().registerReceiver(updateReceiver, filter1);

		// 将事件与listView进行绑定
		list_notice.setPullLoadEnable(true);
		list_notice.setXListViewListener(this);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}


		/* 通过key值从bundle中取值 */
		type = getArguments().getInt("type");
		isSearch = getArguments().getBoolean("isSearch");
		if(isSearch){
			searchData = getArguments().getString("searchData");
		}else{
			searchData = "";
		}

		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				updateList();
			}
		});
		if (i == 0) {
			getThread.start();
		}

		listHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				try {
					if (type == 1) {
						noticeAdapter = new TmpNoticeAdapter(MainMenu.MM,
								listItems); // 创建适配器
						i = 1;
					} else {
						noticeAdapter = new AskNoticeAdapter(MainMenu.MM,
								listItems);
						i = 1;
					}
					list_notice.setAdapter((ListAdapter) noticeAdapter);
					// 更新
					list_notice.invalidate();
					// dialog.dismiss();
				} catch (Exception e) {
					// dialog.cancel();
					e.printStackTrace();
					// Toast.makeText(getActivity(), "出现错误", 1000).show();
				}
				if (isEnd) {
					list_notice.setIsEnd(isEnd);
				}
				lin_pro.setVisibility(View.GONE);
				list_notice.setVisibility(View.VISIBLE);
				onLoad();
				isRefer = false;
			}
		};

		list_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent;
				Bundle bundle = new Bundle();
				if (type == 1) {
					intent = new Intent(getActivity(),
							TmpNoticeDetailActivity.class);
					bundle.putString(
							"id",
							(Integer) ((TmpNoticeAdapter) noticeAdapter).listItems
									.get(arg2 - 1).get("id") + "");
				} else {
					intent = new Intent(getActivity(),
							AskNoticeDetailActivity.class);
					bundle.putString(
							"id",
							(Integer) ((AskNoticeAdapter) noticeAdapter).listItems
									.get(arg2 - 1).get("id")+"");
				}
				// bundle.put
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		/**
		 * 新建临时性工作请示
		 * 
		 * @param v
		 */

		return view;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateList() {
		String res = null;
		String result = new String();
		isRefer = true;
		try {
			if (type == 1) {
				res = serverUrl + "/services/tmp_notice_list";
				
//				res = HttpRequestUtil.sendGet(serverUrl
//						+ "/services/tmp_notice_list", searchData,
//						(OilApplication) getActivity().getApplication());
			} else {
				res = serverUrl + "/services/ask_notice_list";
//				result = HttpRequestUtil.getDataFromServer(res,LoginActivity.app);
//				res = HttpRequestUtil.sendGet(serverUrl + "/services/ask_notice_list", searchData,
//						(OilApplication) getActivity().getApplication());
			}
			System.out.println("searchData================" + searchData);
			result = HttpRequestUtil.PostHttp(res, searchData, (OilApplication)getActivity().getApplication());
			System.out.println(result);
			if(!result.equals(lastStr)) {
				lastStr = result;
			} else {
				Message msg = tostHand.obtainMessage();
				msg.obj = "没有新信息";
				tostHand.sendMessage(msg);
				return;
			}
			try {
				JSONObject json = new JSONObject(result);
				if (json.getInt("status") == 200) {
					listItems = getListItems(json.getJSONArray("data"));
					int totalRecord = json.getInt("totalRecord");
					if ((listItems.size() == 0)
							|| listItems.size() == totalRecord) {
						isEnd = true;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				listItems = new ArrayList<Map<String, Object>>();
				isEnd = true;
			}
			listHand.sendMessage(listHand.obtainMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Map<String, Object>> getListItems(JSONArray array) {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject json = array.getJSONObject(i);
				Map<String, Object> map = null;
				if (type == 1) {
					map = getItemByType1(json);
				} else {
					map = getItemByType2(json);
				}
				listItems.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listItems;
	}

	public Map<String, Object> getItemByType1(JSONObject json) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// JSONObject json = array.getJSONObject(i);
			idList.add(json.getInt("id"));
			// System.out.println(json.getInt("id") +
			// "========================");
			map.put("id", json.getInt("id"));
			map.put("title", json.getString("title") + "");
			map.put("notation", json.getString("postil").equals("null") ? "无"
					: json.getString("postil"));
			map.put("creater", json.getString("author") + "");
			map.put("status", json.getInt("status") == 1 ? "正常" : "关闭"); // 阀室名称
			map.put("date",
					DateFormaterUtil.getDateToString(json
							.getLong("create_time")) + "");
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Object> getItemByType2(JSONObject json) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// JSONObject json = array.getJSONObject(i);
			idList.add(json.getInt("id"));
			map.put("id", json.getInt("id"));
			map.put("title", json.getString("title") + "");
			map.put("data", DateFormaterUtil.getDateToString(json
					.getLong("create_time")));
			map.put("author", json.getString("author"));
			map.put("verify",
					ParamsUtil.getVerifyByStatus(json.getInt("verify_status")));
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateListByUp() {
		String url;
		if (type == 1) {
			url = serverUrl + "/services/tmp_notice_list";
		} else {
			url = serverUrl + "/services/ask_notice_list";
		}
		String res = HttpRequestUtil.sendGet(url,
				"pageOffset=" + noticeAdapter.getCount() + searchData,
				(OilApplication) getActivity().getApplication());
		try {
			JSONObject json = new JSONObject(res);
			int totalRecord = json.getInt("totalRecord");
			JSONArray jarrat = json.getJSONArray("data");
			if ((jarrat.length() + noticeAdapter.getCount() == totalRecord)
					|| jarrat.length() == 0) {
				isEnd = true;
			}
			Message msg = listHand.obtainMessage();
			msg.obj = jarrat;
			listHand.sendMessage(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		isEnd = false;
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				updateList();
			}
		});
		getThread.start();

		listHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// super.handleMessage(msg);
				try {
					if (type == 1) {
						noticeAdapter = new TmpNoticeAdapter(getActivity(),
								listItems); // 创建适配器
					} else {
						noticeAdapter = new AskNoticeAdapter(getActivity(),
								listItems);
					}
					list_notice.setAdapter((ListAdapter) noticeAdapter);
					// 更新
					list_notice.invalidate();
				} catch (Exception e) {
					Toast.makeText(MainMenu.MM.getApplicationContext(), "出现错误", 1000).show();
					e.printStackTrace();
				}
				list_notice.setIsEnd(isEnd);
				onLoad();
				isRefer = false;
			}
		};
	}

	@Override
	public void onLoadMore() {
		if (getThread== null || !getThread.isAlive() || !isRefer) {
			getThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// 这里写入子线程需要做的工作
					updateListByUp();
				}
			});
			getThread.start();

			listHand = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					JSONArray jarrat = (JSONArray) msg.obj;
					for (int i = 0; i < jarrat.length(); i++) {
						try {
							JSONObject json = jarrat.getJSONObject(i);
							Map<String, Object> map = new HashMap<String, Object>();
							if (type == 1) {
								map = getItemByType1(json);
								((TmpNoticeAdapter) noticeAdapter).addItem(map);
							} else {
								map = getItemByType2(json);
								((AskNoticeAdapter) noticeAdapter).addItem(map);
							}
							noticeAdapter.notifyDataSetChanged();// 提醒adapter更新
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					if (isEnd) {
						list_notice.setIsEnd(isEnd);
					}
					onLoad();
				}
			};
		}
	}

	private void onLoad() {
		list_notice.stopRefresh();
		list_notice.stopLoadMore();
		list_notice.setRefreshTime(DateFormaterUtil.getCurrentDate("MM-dd hh:mm:ss"));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(myReceiver);
		getActivity().unregisterReceiver(updateReceiver);
	}

}
