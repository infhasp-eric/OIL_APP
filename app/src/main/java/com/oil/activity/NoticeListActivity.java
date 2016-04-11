package com.oil.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oil.adapter.NoticeAdapter;
import com.oil.adapter.PcListAdapter;
import com.oil.adapter.TmpNoticeAdapter;
import com.oil.adapter.AskNoticeAdapter;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
@SuppressLint("NewApi")
public class NoticeListActivity extends FinalActivity implements IXListViewListener {

	@ViewInject(id=R.id.bt_back, click="backClick") Button bt_back; 
	@ViewInject(id=R.id.bt_creat, click="creatClick") Button bt_creat;
	@ViewInject(id=R.id.txt_title) TextView txt_title;
	@ViewInject(id=R.id.list_notice) XListView list_notice;
	
	private Integer type;
	String serverUrl;
	private List<Map<String, Object>> listItems;
	private Thread getThread;
	private Handler listHand;
	private Handler aHand;
	private List<Integer> idList = new ArrayList<Integer>();
	private Boolean isEnd = false;
	private BaseAdapter noticeAdapter;
	private Integer lastItem;
	private ProgressDialog dialog = null;
	public static NoticeListActivity nl;
	private boolean isNore;
	private String resStr;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notice_list);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.show();
		dialog.setCancelable(false);
		
		nl = this;
		serverUrl = Urls.URL;
		
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				 if (arg1 == KeyEvent.KEYCODE_BACK && arg2.getAction() == KeyEvent.ACTION_DOWN) {
		                dialog.dismiss();
		                NoticeListActivity.this.finish();
		            }
		            return false;
			}
		});

		//将事件与listView进行绑定
		list_notice.setPullLoadEnable(true);
		list_notice.setXListViewListener(this);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
        
        /*通过key值从bundle中取值*/
        type = getIntent().getIntExtra("type",1);
        System.out.println(type + "-------------------");
        
        if(type == 1) {
        	txt_title.setText(R.string.title_tmp_list);
        	bt_creat.setVisibility(View.GONE);
        } else if (type == 2) {
        	txt_title.setText(R.string.title_ask_list);
        	bt_creat.setText("");
        }
        
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
				try{
					if(type == 1) {
						noticeAdapter = new TmpNoticeAdapter(NoticeListActivity.this,listItems); // 创建适配器
					} else {
						noticeAdapter = new AskNoticeAdapter(NoticeListActivity.this,listItems);
					}
				list_notice.setAdapter((ListAdapter) noticeAdapter);
				// 更新
				list_notice.invalidate();
				dialog.dismiss();
				} catch (Exception e) {
					dialog.cancel();
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "出现错误", 1000);
				}
				
			}
		};
		
		list_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent;
				if(type == 1) {
					intent = new Intent(NoticeListActivity.this, TmpNoticeDetailActivity.class);
				} else {
					intent = new Intent(NoticeListActivity.this, AskNoticeDetailActivity.class);
				}
				Bundle bundle = new Bundle();
				System.out.println(arg2+"======================");
				bundle.putInt("id", idList.get(arg2-1));
				//bundle.put
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 返回上一界面
	 * @param v
	 */
	public void backClick(View v) {
		finish();
	}
	
	/**
	 * 新建临时性工作请示
	 * @param v
	 */
	public void creatClick(View v) {
		HttpRequestUtil.getSession((OilApplication)getApplication());
		if(type == 2) {
			Intent intent = new Intent(NoticeListActivity.this, AskNoticeCreatActivity.class);
			startActivity(intent);
		}
	}
	
	public void updateList() {
		String res = null;
		//System.out.println(type + "<<<<<<<<<<<<<<<<<<<<<");
		if(type == 1) {
			res = HttpRequestUtil.sendGet(serverUrl
				+ "/services/tmp_notice_list", null,(OilApplication)getApplication());
			System.out.println(res);
		} else {
			res = HttpRequestUtil.sendGet(serverUrl
					+ "/services/ask_notice_list", null,(OilApplication)getApplication());
			System.out.println(res);
		}
		try {
			JSONObject json = new JSONObject(res);
			if (json.getInt("status") == 200) {
				listItems = getListItems(json.getJSONArray("data"));
				int totalRecord = json.getInt("totalRecord");
				if((listItems.size() == 0) || listItems.size() == totalRecord) {
					isEnd = true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			listItems = new ArrayList<Map<String,Object>>();
		}
		if(!StringUtil.isBlank(resStr) && resStr.equals(res)) {
			isNore = true;
		} else {
			resStr = res;
		}
		listHand.sendMessage(listHand.obtainMessage());
	}
	
	private List<Map<String, Object>> getListItems(JSONArray array) {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject json = array.getJSONObject(i);
				Map<String, Object> map = null;
				if(type == 1) {
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
			//JSONObject json = array.getJSONObject(i);
			idList.add(json.getInt("id"));
			//System.out.println(json.getInt("id") + "========================");
			map.put("title", json.getString("title") + "");
			map.put("notation", json.getString("postil").equals("null")?"无":json.getString("postil"));
			map.put("creater", json.getString("author") + "");
			map.put("status", json.getInt("status")==1?"正常":"关闭"); //阀室名称
			map.put("date", DateFormaterUtil.getDateToString(json.getLong("create_time")) + "");
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, Object> getItemByType2(JSONObject json) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			//JSONObject json = array.getJSONObject(i);
			idList.add(json.getInt("id"));
			map.put("title", json.getString("title") + "");
			map.put("data", DateFormaterUtil.getDateToString(json.getLong("create_time")));
			map.put("author", json.getString("author"));
			map.put("verify", ParamsUtil.getVerifyByStatus(json.getInt("verify_status")));
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateListByUp() {
		String url;
		if(type == 1) {
			url = serverUrl + "services/tmp_notice_list";
		} else {
			url = serverUrl + "services/ask_notice_list";
		}
		String res = HttpRequestUtil.sendGet(url, "pageOffset=" + noticeAdapter.getCount(),(OilApplication)getApplication());
		try {
			JSONObject json = new JSONObject(res);
			int totalRecord = json.getInt("totalRecord");
			JSONArray jarrat = json.getJSONArray("data");
			if((jarrat.length() + noticeAdapter.getCount() == totalRecord) || jarrat.length() == 0) {
				isEnd = true;
			}
			System.out.println(jarrat);
			Message msg = listHand.obtainMessage();
			msg.obj = jarrat;
			listHand.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
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
				if(isNore) {
					Toast.makeText(getApplicationContext(), "没有新信息", 1000).show();
				} else {
					try{
						if(type == 1) {
							noticeAdapter = new TmpNoticeAdapter(NoticeListActivity.this,listItems); // 创建适配器
						} else {
							noticeAdapter = new AskNoticeAdapter(NoticeListActivity.this,listItems);
						}
					list_notice.setAdapter((ListAdapter) noticeAdapter);
					// 更新
					list_notice.invalidate();
					dialog.dismiss();
					} catch (Exception e) {
						dialog.cancel();
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "出现错误", 1000);
					}
					list_notice.setIsEnd(isEnd);
				}
				onLoad();
			}
		};
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				updateListByUp();
			}
		}).start();
		
		listHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				JSONArray jarrat = (JSONArray) msg.obj; 
				for(int i = 0; i < jarrat.length(); i++) {
					try {
						JSONObject json = jarrat.getJSONObject(i);
						System.out.println("这里+=========" + json.toString());
						Map<String, Object> map = new HashMap<String, Object>();
						if(type == 1) {
							map = getItemByType1(json);
							((TmpNoticeAdapter)noticeAdapter).addItem(map);
						} else {
							map = getItemByType2(json);
							((AskNoticeAdapter)noticeAdapter).addItem(map);
						}
						noticeAdapter.notifyDataSetChanged();//提醒adapter更新  
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
				if(isEnd) {
					list_notice.setIsEnd(isEnd);
				}
				onLoad();
			}
		};
	}
	
	private void onLoad() {
		list_notice.stopRefresh();
		list_notice.stopLoadMore();
		list_notice.setRefreshTime(DateFormaterUtil.getCurrentDate("MM-dd HH:mm:ss"));
	}

}
