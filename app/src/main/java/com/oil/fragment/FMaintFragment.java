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
import com.oil.adapter.FMListAdapter;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.Urls;

public class FMaintFragment extends Fragment implements IXListViewListener{
	
	private XListView fmLv;
	private LinearLayout lin_pro;
	private Thread getThread;
	private Boolean isEnd = false,isSearch = false;
	private String searchData = new String();
	private FMListAdapter fmListAdapter;
	private List<Map<String, Object>> listItems;
	private String judgeStr = new String();
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			lin_pro.setVisibility(View.GONE);
			fmLv.setVisibility(View.VISIBLE);
			try {
				JSONObject json = new JSONObject(msg.obj.toString());
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
					fmListAdapter = new FMListAdapter(BaseDataFragment.ctx,
							listItems); // 创建适配器
					fmLv.setAdapter(fmListAdapter);
					if (isEnd) {
						fmLv.setIsEnd(isEnd);
					}
					fmLv.invalidate();
					onLoad();
				}
			} catch (JSONException e) {
				listItems = new ArrayList<Map<String, Object>>();
				isEnd = true;
				e.printStackTrace();
			}
		}
	};
	
	private Handler aHand = new Handler(){
		public void handleMessage(Message msg) {
			JSONArray jarrat = null;
			try {
				JSONObject json = new JSONObject(msg.obj.toString());
				jarrat = json.getJSONArray("data");
				int totalRecord = json.getInt("totalRecord");
				if ((fmListAdapter.getCount() + jarrat.length()) == totalRecord) {
					isEnd = true;
				}
				if (jarrat != null) {
					for (int i = 0; i < jarrat.length(); i++) {
						JSONObject json1 = jarrat.getJSONObject(i);
						fmListAdapter.addItem(getItem(json1));
					}
					fmListAdapter.notifyDataSetChanged();// 提醒adapter更新
					// 对列表设置是否加载完成
					if (isEnd) {
						fmLv.setIsEnd(isEnd);
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
		fmLv = (XListView) v.findViewById(R.id.list_view);
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		fmLv.setPullLoadEnable(true);
		fmLv.setXListViewListener(this);
		
		startSubThread();
		
		fmLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(BaseDataFragment.ctx,
						WebViewActivity.class);
				intent.putExtra("url", Urls.URL + "admin/base/f_maint/show?id="
						+ fmListAdapter.listItems.get(position - 1).get("id"));
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
							+ "services/base/f_maint/list", searchData,
							(OilApplication) BaseDataFragment.ctx.getApplication());
				}else{
					res = HttpRequestUtil.sendGet(Urls.URL
							+ "services/base/f_maint/list", "",
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
			map.put("jinzhan", json.getString("jinzhan")); // 阀室名称
			map.put("create_date",
					DateFormaterUtil.getDateToString(
							json.getLong("create_date"), "yyyy-MM-dd"));
			map.put("create_time", DateFormaterUtil.getDateToString(json
					.getLong("create_time")));
			map.put("verifier", json.getString("verifier"));
			map.put("verify",
					ParamsUtil.getVerifyByStatus(json.getInt("status")));
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void onLoad() {
		fmLv.stopRefresh();
		fmLv.stopLoadMore();
		fmLv.setRefreshTime(DateFormaterUtil
				.getCurrentDate("MM-dd HH:mm:ss"));
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
								+ "services/base/f_maint/list", "pageOffset="
										+ fmListAdapter.getCount() + "&" + searchData,
										(OilApplication) BaseDataFragment.ctx.getApplication());
					}else{
						msg.obj = HttpRequestUtil.sendGet(Urls.URL
								+ "services/base/f_maint/list", "pageOffset="
										+ fmListAdapter.getCount(),
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
