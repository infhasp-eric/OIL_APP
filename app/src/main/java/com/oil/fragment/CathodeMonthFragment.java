package com.oil.fragment;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.WebViewActivity;
import com.oil.adapter.CathodeProtectionAdapter;
import com.oil.domain.PipeRecord;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.JSONParse;
import com.oil.utils.Urls;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CathodeMonthFragment extends Fragment implements
		IXListViewListener {

	private Thread getThread;
	private LinearLayout lin_pro;
	private Boolean isEnd = false, isSearch = false;
	private String searchData = new String();
	private CathodeProtectionAdapter adapter;
	private XListView cathLv;
	private String judgeStr = new String();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				lin_pro.setVisibility(View.GONE);
				cathLv.setVisibility(View.VISIBLE);
				adapter = new CathodeProtectionAdapter(JSONParse.parseCathodeMonthRecord(msg.obj.toString()));
				JSONObject json = new JSONObject(msg.obj.toString());
				int totalRecord = json.getInt("totalRecord");
				if (totalRecord == adapter.getCount()) {
					isEnd = true;
				}
			} catch (Exception e) {
				adapter = new CathodeProtectionAdapter(new ArrayList<PipeRecord>());
			}
			cathLv.setAdapter(adapter);
			if (isEnd) {
				cathLv.setIsEnd(isEnd);
			}
			onLoad();
		}
	};

	private Handler aHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				List<PipeRecord> list = JSONParse
						.parseCathodeMonthRecord(msg.obj.toString());
				if (list.size() == 0) {
					isEnd = true;
				} else {
					for (PipeRecord il : list) {
						adapter.list.add(il);
					}
					if (list.size() <= 4) {
						isEnd = true;
					}
				}
				adapter.notifyDataSetChanged();// 提醒adapter更新
				if (isEnd) {
					cathLv.setIsEnd(isEnd);
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
			onLoad();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(BaseDataFragment.ctx).inflate(
				R.layout.fragment_pl, null);
		isSearch = getArguments().getBoolean("isSearch");
		searchData = getArguments().getString("searchData");
		init(v);
		return v;
	}

	private void init(View v) {
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		cathLv = (XListView) v.findViewById(R.id.list_view);
		// 将事件与listView进行绑定
		cathLv.setPullLoadEnable(true);
		cathLv.setXListViewListener(this);

		startSubThread();

		cathLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(BaseDataFragment.ctx, WebViewActivity.class);
				intent.putExtra("url", Urls.URL + "admin/base/rc_comp/show?id="
						+ adapter.list.get(arg2 - 1).get_id());
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
					res = HttpGetDataByGet.getDataFromServer(Urls.CPSRMR+"?"+searchData,
							((OilApplication) BaseDataFragment.ctx.getApplication()).getJSESSIONID());
				}else{
					res = HttpGetDataByGet.getDataFromServer(Urls.CPSRMR,
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

	protected void onLoad() {
		cathLv.stopRefresh();
		cathLv.stopLoadMore();
		cathLv.setRefreshTime(DateFormaterUtil.getCurrentDate("MM-dd HH:mm:ss"));
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
					if (isSearch) {
						res = Urls.CPSRMR + "?pageOffset="
								+ adapter.list.size() + "&" + searchData;
					} else {
						res = Urls.CPSRMR + "?pageOffset="
								+ adapter.list.size();
					}
				} catch (Exception e) {
					res = "";
					e.printStackTrace();
				}
				msg.obj = HttpGetDataByGet.getDataFromServer(res,
						((OilApplication) BaseDataFragment.ctx.getApplication())
								.getJSESSIONID());
				aHand.sendMessage(msg);
			}
		});
		getThread.start();
		}
	}
}
