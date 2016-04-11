package com.oil.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oil.activity.MainMenu;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.adapter.TipsAdapter;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;
/**
 * 提醒列表
 * @author Administrator
 *
 */
public class NotiFragment extends Fragment implements IXListViewListener {

	private XListView listview;
	private LinearLayout lin_noinfo, lin_pro;
	private TipsAdapter tipsAdapter;
	private List<Map<String, Object>> listItems;
	private Boolean isEnd = false;
	private String lastStr;
	private boolean isRefer = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 更新
			if (!StringUtil.isBlank(lastStr)
					&& lastStr.equals(msg.obj.toString())) {
				Toast.makeText(getActivity().getApplicationContext(), "没有新信息",
						100).show();
				
			} else {
				isEnd = false;
				listview.setIsEnd(isEnd);
				lastStr = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(msg.obj.toString());

					Log.e("Tag", "res" + json);

					if (json.getInt("status") == 200) {
						listItems = getListItems(json.getJSONArray("data"));
						if (listItems.size() <= 4) {
							isEnd = true;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tipsAdapter = new TipsAdapter(MainMenu.MM, listItems); // 创建适配器
				if (tipsAdapter.getCount() == 0) {
					// view.setText("当前没有任何通知");
					Toast.makeText(MainMenu.MM, "当前没有任何通知！",
							Toast.LENGTH_SHORT).show();
				} else {
					listview.setAdapter(tipsAdapter);
				}
				if (tipsAdapter.getCount() < 50) {
					isEnd = true;
				}
			}
			isRefer = false;
			if (isEnd) {
				listview.setIsEnd(isEnd);
			}
			onLoad();
		}
	};

	private Handler newhand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 更新
			lastStr = msg.obj.toString();
			try {
				JSONObject json = new JSONObject(msg.obj.toString());

				Log.e("Tag", "res" + json);

				if (json.getInt("status") == 200) {
					listItems = getListItems(json.getJSONArray("data"));
					if (listItems.size() <= 49) {
						isEnd = true;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				listItems = new ArrayList<Map<String, Object>>();
				isEnd = true;
				e.printStackTrace();
			}
			tipsAdapter = new TipsAdapter(getActivity(), listItems); // 创建适配器
			
			if (tipsAdapter != null && tipsAdapter.getCount() == 0) {
				// view.setText("当前没有任何通知");
				Toast.makeText(getActivity(), "当前没有任何通知！", Toast.LENGTH_SHORT)
						.show();
				isEnd = true;
			}
			listview.setAdapter(tipsAdapter);
			
			if (tipsAdapter != null && tipsAdapter.getCount() < 50) {
				isEnd = true;
			}
			isRefer = false;
			if (isEnd) {
				listview.setIsEnd(isEnd);

			}
			lin_pro.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			onLoad();
		}
	};

	private Handler ahand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 更新
			JSONArray array = (JSONArray) msg.obj;
			List<Map<String, Object>> up = getListItems(array);
			for (Map<String, Object> map : up) {
				tipsAdapter.listItems.add(map);
			}
			if(listItems.size()<50){
				isEnd=true;
			}
			if (isEnd) {
				listview.setIsEnd(isEnd);
			}
			listview.invalidate();
			isRefer = false;
			onLoad();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.notification_study, null);
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		listview = (XListView) v.findViewById(R.id.notification_study_list);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				newList();
			}
		}).start();
		
		return v;
	}

	public void updateList() {
		String res = HttpRequestUtil.sendGet(Urls.URL + "services/welcome",
				null, (OilApplication) getActivity().getApplication());

		Message msg = handler.obtainMessage();
		msg.obj = res;
		handler.sendMessage(msg);

	}

	public void newList() {
		String res = HttpRequestUtil.sendGet(Urls.URL + "services/welcome",
				null, (OilApplication) getActivity().getApplication());

		Message msg = newhand.obtainMessage();
		msg.obj = res;
		newhand.sendMessage(msg);

	}

	public void updateListByUp(String param) {
		String res = HttpRequestUtil.sendGet(Urls.URL + "services/welcome",
				param, (OilApplication) getActivity().getApplication());
		try {
			JSONObject json = new JSONObject(res);
			Log.e("Tag", "res" + res);

			if (json.getInt("status") == 200) {
				JSONArray array = json.getJSONArray("data");
				if (array.length() <= 4) {
					isEnd = true;
				}
				Message msg = ahand.obtainMessage();
				msg.obj = array;
				ahand.sendMessage(msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public List<Map<String, Object>> getListItems(JSONArray array) {
		JSONObject json;
		List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length(); i++) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				json = array.getJSONObject(i);
				String date = DateFormaterUtil.getDateToString(json
						.getLong("create_time"));
				String content = json.getString("content");
				Log.i("Tag", content);
				map.put("date", date);
				map.put("content", content);
				map.put("type", getType(json.getString("url")));
				if(getType(json.getString("url")) != 0) {
					map.put("id", getId(json.getString("url")) + "&tips_id=" + json.getInt("id"));
				} else {
					map.put("id", json.getString("url") + "&tips_id=" + json.getInt("id"));
				}
				itemList.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return itemList;
	}

	public void onLoad() {
		listview.setRefreshTime(DateFormaterUtil
				.getCurrentDate("MM-dd hh:mm:ss"));
		listview.stopRefresh();
		listview.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isRefer) {
			isRefer = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					updateList();
				}
			}).start();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!isRefer) {
			isRefer = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					updateListByUp("pageOffset=" + tipsAdapter.getCount());
				}
			}).start();
		}
	}
	
	public Integer getType(String url) {
		String[] strs = url.split("/");
		String keyword = strs[2];
		Integer type;
		if(keyword.contains("event")) {
			type = 1;
		} else if (keyword.contains("tmp_notice_create")) {
			type = 3;
		} else if (keyword.contains("notice_create")) {
			type = 2;
		} else {
			type = 0;
		}
		return type;
	}
	
	public Integer getId(String url) {
		int index = url.lastIndexOf("id=") + 3;
		Integer id = Integer.valueOf(url.substring(index));
		return id;
	}
}
