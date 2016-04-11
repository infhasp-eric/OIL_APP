package com.oil.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.WebViewActivity;
import com.oil.adapter.RcListAdapter;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.Urls;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CathodeRuntimeFragment extends Fragment implements IXListViewListener{
	
	private XListView crLv;
	private LinearLayout lin_pro;
	private Thread getThread;
	private RcListAdapter rcListAdapter;
	private Boolean isEnd = false,isSearch = false;
	private String searchData = new String();
	private String judgeStr = new String();
	private List<Map<String, Object>> listItems;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			lin_pro.setVisibility(View.GONE);
			crLv.setVisibility(View.VISIBLE);
			try {
				JSONObject json = new JSONObject(msg.obj.toString());
				if (json.getInt("status") == 200) {
					listItems = getListItems(json.getJSONArray("data"));
					if (listItems.size() < 50) {
						isEnd = true;
					}
				} else {
					listItems = new ArrayList<Map<String, Object>>();
					isEnd = true;
				}
			} catch (JSONException e) {
				listItems = new ArrayList<Map<String, Object>>();
				isEnd = true;
				e.printStackTrace();
			}
			rcListAdapter = new RcListAdapter(BaseDataFragment.ctx,
					listItems); // 创建适配器
			crLv.setAdapter(rcListAdapter);
			// 更新
			crLv.invalidate();
			if(isEnd) {
				crLv.setIsEnd(isEnd);
			}
			onLoad();
		}
	};
	
	private Handler aHand = new Handler(){
		public void handleMessage(android.os.Message msg) {
			JSONArray jarrat = null;
			try {
				JSONObject json = new JSONObject(msg.obj.toString());
				jarrat = json.getJSONArray("data");
				if (jarrat.length() < 50) {
					isEnd = true;
				}
				if (jarrat != null) {
					for (int i = 0; i < jarrat.length(); i++) {
						try {
							JSONObject json1 = jarrat.getJSONObject(i);
							rcListAdapter.addItem(getItem(json1));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					rcListAdapter.notifyDataSetChanged();// 提醒adapter更新
					// 对列表设置是否加载完成
					if (isEnd) {
						crLv.setIsEnd(isEnd);
					}
					onLoad();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
	
	private Handler toastHand = new Handler(){
		public void handleMessage(Message msg) {
			Toast.makeText(BaseDataFragment.ctx,msg.obj.toString(),Toast.LENGTH_SHORT).show();
			onLoad();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(BaseDataFragment.ctx).inflate(R.layout.fragment_pl,null);
		isSearch = getArguments().getBoolean("isSearch");
		searchData = getArguments().getString("searchData");
		init(v);
		return v;
	}

	private void init(View v) {
		crLv = (XListView) v.findViewById(R.id.list_view);
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		crLv.setPullLoadEnable(true);
		crLv.setXListViewListener(this);
		
		startSubThread();
		
		crLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BaseDataFragment.ctx,
						WebViewActivity.class);
				intent.putExtra("url", Urls.URL + "admin/base/rc/show?id="
						+ rcListAdapter.listItems.get(position - 1).get("id"));
				startActivity(intent);

			}
		});
	}
	
	
	
	private void startSubThread() {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				String res = new String();
				if(isSearch){
					res = HttpRequestUtil.sendGet(Urls.URL
							+ "services/base/rc/list", searchData,
							(OilApplication) BaseDataFragment.ctx.getApplication());
				}else{
					res = HttpRequestUtil.sendGet(Urls.URL
							+ "services/base/rc/list", "",
							(OilApplication) BaseDataFragment.ctx.getApplication());
				}
				if(judgeStr.equals(res)){
					Message msg = toastHand.obtainMessage();
					msg.obj = "没有新信息";
					toastHand.sendMessage(msg);
				}else{
					judgeStr = res;
					Message msg = handler.obtainMessage();
					msg.obj = res;
					handler.sendMessage(msg);
				}
			}
		});
		getThread.start();
	}

	private void onLoad() {
		crLv.stopRefresh();
		crLv.stopLoadMore();
		crLv.setRefreshTime(DateFormaterUtil
				.getCurrentDate("MM-dd HH:mm:ss"));
	}
	
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
			map.put("jinzhan", json.getString("jinzhan"));
			map.put("r_month", json.getString("r_month").substring(0, 4) + "-"
					+ json.getString("r_month").substring(4));
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

	@Override
	public void onRefresh() {
		startSubThread();
	}

	@Override
	public void onLoadMore() {
		if(getThread == null || !getThread.isAlive()) {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = aHand.obtainMessage();
				try {
					if(isSearch){
						msg.obj = HttpRequestUtil.sendGet(Urls.URL
								+ "services/base/rc/list", "pageOffset="
										+ rcListAdapter.getCount() + "&" + searchData,
										(OilApplication) BaseDataFragment.ctx.getApplication());
					}else{
						msg.obj = HttpRequestUtil.sendGet(Urls.URL
								+ "services/base/rc/list", "pageOffset="
										+ rcListAdapter.getCount(),
										(OilApplication) BaseDataFragment.ctx.getApplication());
					}
				} catch (Exception e) {
					msg.obj = "";
					e.printStackTrace();
				}
				aHand.sendMessage(msg);
			}
		});
		getThread.start();
		}
	}
}
