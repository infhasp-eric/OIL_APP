package com.oil.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.oil.adapter.PcListAdapter;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

public class PlCurveFragment extends Fragment implements IXListViewListener{

	private XListView list;
	private LinearLayout lin_pro;
	private Thread getThread;
	private Handler listHand, aHand;
	private Boolean isEnd = false,isSearch = false;
	private Boolean isRefer = false, isNoRe = false;
	private PcListAdapter pcListAdapter;
	private List<Map<String, Object>> listItems;
	private String lastRes;//最后一次所请求到的参数
	private String searchData = new String();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View oneLayout = LayoutInflater.from(BaseDataFragment.ctx).inflate(
				R.layout.fragment_pl, null);
		isSearch = getArguments().getBoolean("isSearch");
		searchData = getArguments().getString("searchData");
		init(oneLayout);
		return oneLayout;
	}
	
	private void init(View v) {
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		list = (XListView) v.findViewById(R.id.list_view);
		list.setPullLoadEnable(true);
		list.setXListViewListener(this);
		
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				updateList();
			}
		});
		getThread.start(); // 启动线程

		listHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// super.handleMessage(msg);
				lin_pro.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				pcListAdapter = new PcListAdapter(BaseDataFragment.ctx,
						listItems); // 创建适配器
				list.setAdapter(pcListAdapter);
				// 更新
				list.invalidate();
				//dialog.dismiss();
				if(isEnd) {
					list.setIsEnd(isEnd);
				}
				onLoad();
			}
		};
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(BaseDataFragment.ctx,
						WebViewActivity.class);
				intent.putExtra("url", Urls.URL
						+ "admin/base/pl_curve/show?id=" + pcListAdapter.listItems.get(arg2 - 1).get("id"));
				startActivity(intent);
			}

		});
	}
	
	@Override
	public void onRefresh() {
		isRefer = true;
		isEnd = false;
		
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				isNoRe = updateList();//标记是否有新信息
			}
		});
		getThread.start(); // 启动线程

		listHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// super.handleMessage(msg);
				if(isNoRe) {//没有新数据则打印，并且不更新列表
					Toast.makeText(BaseDataFragment.ctx.getApplicationContext(), "没有新信息", Toast.LENGTH_SHORT).show();
				} else {
					pcListAdapter = new PcListAdapter(BaseDataFragment.ctx,
							listItems); // 创建适配器
					list.setAdapter(pcListAdapter);
					//dialog.dismiss();
					// 更新
					list.invalidate();
					list.setIsEnd(isEnd);
				}
				isRefer = false;
				onLoad();
			}
		};
	}

	@Override
	public void onLoadMore() {
		if ((getThread == null || !getThread.isAlive()) && !isRefer) {

			try {
				getThread = new Thread(new Runnable() {

					@Override
					public void run() {
						updateListByUp();
					}
				});

				getThread.start();

				aHand = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						JSONArray jarrat = (JSONArray) msg.obj;
						// 判断数组书否为空
						if (jarrat != null) {
							System.out.println(jarrat.length());
							for (int i = 0; i < jarrat.length(); i++) {
								try {
									JSONObject json1 = jarrat.getJSONObject(i);
									pcListAdapter.addItem(getItem(json1));
									// fmListAdapter.listItems.add(getItem(json1));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							pcListAdapter.notifyDataSetChanged();// 提醒adapter更新
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
	
	public boolean updateList() {
		String res = new String();
		if(isSearch){
			res = HttpRequestUtil.sendGet(Urls.URL
					+ "services/base/pl_curve/list", searchData,
					(OilApplication) BaseDataFragment.ctx.getApplication());
		}else{
			res = HttpRequestUtil.sendGet(Urls.URL
					+ "services/base/pl_curve/list", "",
					(OilApplication) BaseDataFragment.ctx.getApplication());
		}
		try {
			JSONObject json = new JSONObject(res);
			int totalRecord = json.getInt("totalRecord");
			if (json.getInt("status") == 200) {
				if (totalRecord > 0) {
					listItems = getListItems(json.getJSONArray("data"));
					if (totalRecord == listItems.size()) {
						isEnd = true;
					}
				} else {
					listItems = new ArrayList<Map<String, Object>>();
					isEnd = true;
				}
			}
		} catch (JSONException e) {
			listItems = new ArrayList<Map<String, Object>>();
			isEnd = true;
			e.printStackTrace();
		}
		listHand.sendMessage(listHand.obtainMessage());
		if(!StringUtil.isBlank(lastRes) && lastRes.equals(res)) {
			//两次相同时则返回true
			return true;
		} else {
			lastRes = res;
		}
		//两次不同则返回false
		return false;
	}
	
	public void updateListByUp() {
		if (pcListAdapter!= null&&pcListAdapter.getCount() > 0) {
			String res = new String();
			try {
				if(isSearch){
					res = HttpRequestUtil.sendGet(Urls.URL
							+ "services/base/pl_curve/list", "pageOffset="
									+ pcListAdapter.listItems.size() + searchData,
									(OilApplication) BaseDataFragment.ctx.getApplication());
				}else{
					res = HttpRequestUtil.sendGet(Urls.URL
							+ "services/base/pl_curve/list", "pageOffset="
							+ pcListAdapter.listItems.size(),
							(OilApplication) BaseDataFragment.ctx.getApplication());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JSONArray jarrat = null;
			try {
				JSONObject json = new JSONObject(res);
				jarrat = json.getJSONArray("data");
				int totalRecord = json.getInt("totalRecord");
				if ((pcListAdapter.getCount() + jarrat.length()) == totalRecord) {
					isEnd = true;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			Message msg = aHand.obtainMessage();
			msg.obj = jarrat;
			aHand.sendMessage(msg);
		}
	}
	
	//序列化返回的参数
	private List<Map<String, Object>> getListItems(JSONArray array) {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject json = array.getJSONObject(i);
				Map<String, Object> map = getItem(json);
				listItems.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listItems;
	}
	
	public Map<String, Object> getItem(JSONObject json) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", json.getInt("id"));
			map.put("pl_name", json.getString("pl_name"));
			map.put("pl_section_name", json.getString("pl_section_name"));
			map.put("pl_spec_name", json.getString("pl_spec_name"));
			map.put("unit", json.getString("unit")); // 阀室名称
			map.put("c_month", json.getString("c_month").substring(0, 4) + "-"
					+ json.getString("c_month").substring(4));
			map.put("create_time", DateFormaterUtil.getDateToString(json
					.getLong("create_time")));
			map.put("verify",
					ParamsUtil.getVerifyByStatus(json.getInt("status")));
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime(DateFormaterUtil.getCurrentDate("MM-dd HH:mm:ss"));
	}
	
	protected void ond() {
		this.onDestroy();
	}

}
