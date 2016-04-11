package com.oil.fragment;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.WebViewActivity;
import com.oil.adapter.InsulationAdapter;
import com.oil.domain.InsulationRecord;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.JSONUtil;
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

public class InsulationFunctionFragemnt extends Fragment implements
		IXListViewListener {

	private XListView insLv;
	private LinearLayout lin_pro;
	private Thread getThread;
	private Boolean isEnd = false, isSearch = false;
	private String searchData = new String();
	private InsulationAdapter adapter;
	private String judgeStr = new String();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				lin_pro.setVisibility(View.GONE);
				insLv.setVisibility(View.VISIBLE);
				JSONObject json = new JSONObject(msg.obj.toString());
				int totalRecord = json.getInt("totalRecord");
				adapter = new InsulationAdapter(
						JSONUtil.parseInsulation(msg.obj.toString()));
				if (adapter.getCount() == totalRecord) {
					isEnd = true;
				}
			} catch (JSONException e) {
				adapter = new InsulationAdapter(new ArrayList<InsulationRecord>());
				e.printStackTrace();
			}
			insLv.setAdapter(adapter);
			if (isEnd) {
				insLv.setIsEnd(isEnd);
			}
			onLoad();
		}
	};
	
	private Handler aHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				lin_pro.setVisibility(View.GONE);
				insLv.setVisibility(View.VISIBLE);
				List<InsulationRecord> list = JSONUtil.parseInsulation(msg.obj.toString());
				if (list.size() == 0) {
					isEnd = true;
				} else {
					for (InsulationRecord il : list) {
						adapter.list.add(il);
					}
					if (list.size() <= 4) {
						isEnd = true;
					}
				}
				adapter.notifyDataSetChanged();// 提醒adapter更新
				if (isEnd) {
					insLv.setIsEnd(isEnd);
				}
				onLoad();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private Handler toastHand = new Handler(){
		public void handleMessage(Message msg) {
			Toast.makeText(BaseDataFragment.ctx,msg.obj.toString(),Toast.LENGTH_SHORT).show();
			lin_pro.setVisibility(View.GONE);
			insLv.setVisibility(View.VISIBLE);
			onLoad();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(BaseDataFragment.ctx).inflate(
				R.layout.activity_pipe_measure, null);
		isSearch = getArguments().getBoolean("isSearch");
		searchData = getArguments().getString("searchData");
		init(v);
		return v;
	}

	protected void onLoad() {
		insLv.stopRefresh();
		insLv.stopLoadMore();
		insLv.setRefreshTime(DateFormaterUtil.getCurrentDate("MM-dd HH:mm:ss"));
	}

	private void init(View v) {
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		insLv = (XListView) v.findViewById(R.id.pipe_measure_list);
		// 将事件与listView进行绑定
		insLv.setPullLoadEnable(true);
		insLv.setXListViewListener(this);
		
		startSubThread();
		
		insLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(BaseDataFragment.ctx,WebViewActivity.class);
				intent.putExtra("url",
						Urls.URL + "admin/base/p_measure/show?id="
								+ adapter.list.get(arg2 - 1).getId());
				startActivity(intent);
			}

		});
	}

	private void startSubThread() {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("=======================1");
				String res = new String();
				if(isSearch){
					res = HttpGetDataByGet.getDataFromServer(Urls.IJPMR+"?"+searchData,
							((OilApplication) BaseDataFragment.ctx.getApplication()).getJSESSIONID());
				}else{
					res = HttpGetDataByGet.getDataFromServer(Urls.IJPMR,
							((OilApplication) BaseDataFragment.ctx.getApplication()).getJSESSIONID());
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
				String res = new String();
				try {
					if(isSearch){
						res = Urls.IJPMR + "?pageOffset=" + adapter.list.size()+ "&" + searchData;
					}else{
						res = Urls.IJPMR + "?pageOffset=" + adapter.list.size();
					}
				} catch (Exception e) {
					res = "";
					e.printStackTrace();
				}
				msg.obj = HttpGetDataByGet.getDataFromServer(res,((OilApplication) BaseDataFragment.ctx.getApplication()).getJSESSIONID());
				aHand.sendMessage(msg);
			}
		});
		getThread.start();
		}
	}
}
