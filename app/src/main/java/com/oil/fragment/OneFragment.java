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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oil.ResideMenu.ResideMenu;
import com.oil.ResideMenu.ResideMenuItem;
import com.oil.activity.Itco_create_main;
import com.oil.activity.Itco_manage_View;
import com.oil.activity.MainMenu;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.SearchAct;
import com.oil.adapter.Itco_mana_list_Adapter;
import com.oil.dialog.CustiomViewPager;
import com.oil.layout.XListView;
import com.oil.layout.XListView.IXListViewListener;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

public class OneFragment extends Fragment implements IXListViewListener {

	private XListView list;
	private Button create;
	private LinearLayout lin_pro;
	private List<Map<String, Object>> listdatas;
	private Itco_mana_list_Adapter ITCO_ListAdapter;

	private Thread getThread;
	private Handler newHand;
	private String lastRes;// 保存最后一次刷新的数据
	boolean isNoRe = false;// 标记是否有更新
	boolean isrung = false;
	private Boolean isEnd = false;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    private boolean isSearch = false,judge = true,isRefresh = false;
    private String searchData = new String();
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			judge = intent.getBooleanExtra("which", false);
			if (!judge) {
				isSearch = intent.getBooleanExtra("isSearch", false);
				searchData = intent.getStringExtra("searchData");
				updateList();
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
				updateList();
			}
		}
	};
	// private ProgressDialog dialog = null;
    private Handler holder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(listdatas != null && listdatas.size() < 50) {
				isEnd = true;
			}
			if (isNoRe) {// 没有新数据则打印，并且不更新列表
				Toast.makeText(getActivity(), "没有新信息", 1000).show();
			} else {
				ITCO_ListAdapter = new Itco_mana_list_Adapter(
						getActivity(), listdatas); // 创建适配器
				Log.e("items", listdatas.size() + "======================");
				list.setAdapter(ITCO_ListAdapter);
				// 更新
				list.invalidate();
				if (ITCO_ListAdapter.getCount() < 50) {
					isEnd = true;
				}
			}
			if (isEnd) {
				list.setIsEnd(isEnd);
			}
			lin_pro.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
			onLoad();
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View oneLayout = LayoutInflater.from(getActivity()).inflate(
				R.layout.itco_mana_list, null);
		IntentFilter filter = new IntentFilter();
		filter.addAction("actionSearch");
		getActivity().registerReceiver(myReceiver,filter);
		
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("actionCreate");
		getActivity().registerReceiver(createReceiver,filter1);
		init(oneLayout);
		setUpMenu();
		return oneLayout;
	}

	private void init(View v) {
		lin_pro = (LinearLayout) v.findViewById(R.id.lin_pro);
		list = (XListView) v.findViewById(R.id.itco_list);
		create = (Button) v.findViewById(R.id.itco_create);
		list.setPullLoadEnable(true);
		list.setXListViewListener(this);
		// dialog = new ProgressDialog(getActivity());
		// dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// dialog.setMessage("数据加载中，请稍后...");
		// dialog.show();
		// dialog.setCancelable(false);
		// 将事件与listView进行绑定
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

		holder = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				ITCO_ListAdapter = new Itco_mana_list_Adapter(getActivity(),
						listdatas); // 创建适配器
				list.setAdapter(ITCO_ListAdapter);
				// 更新
				list.invalidate();
				if (ITCO_ListAdapter.getCount() < 50) {
					isEnd = true;
				}
				if (isEnd) {
					list.setIsEnd(isEnd);
				}
				lin_pro.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				onLoad();
			}
		};
		// listview控件的监听
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(),
						Itco_manage_View.class);
				String listid = ITCO_ListAdapter.listdatas.get(
						position - 1).get("id").toString();
				Bundle bundle = new Bundle(); // 创建Bundle对象
				bundle.putString("listid", listid); // 装入数据
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// 创建按钮监听
		create.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			}
		});
	}
	
	//展开侧边菜单
			private void setUpMenu() {
		        itemCalendar = new ResideMenuItem(getActivity(), R.drawable.bt_create, "新建");
		        itemSettings = new ResideMenuItem(getActivity(), R.drawable.bt_query, "查询");

		        itemSettings.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(),SearchAct.class);
						intent.putExtra("dateType",3);
						intent.putExtra("type",6);
						startActivityForResult(intent,300);
						MainMenu.closeMenu();
					}
				});
		        
		        itemCalendar.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(),Itco_create_main.class);
						startActivity(intent);
						MainMenu.closeMenu();
					}
				});
		        //itemSettings.setOnClickListener(getActivity());
		        final List<ResideMenuItem> resideList = new ArrayList<ResideMenuItem>();
		        resideList.add(itemCalendar);
		        resideList.add(itemSettings);
		        // You can disable a direction by setting ->

		        create.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	//Toast.makeText(getActivity().getApplicationContext(), "点击了弹出", 100).show();
		                //System.out.println("=================打开了高后果区");
		            	MainMenu.setResideMenuItem(resideList);
		            	MainMenu.openMenu();
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

	// 返回按钮监听
	// public void back_listener(View V) {
	// finish();
	// }

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	private boolean updateList() {
		isEnd = false;
		list.setIsEnd(isEnd);
		String res = new String();
		if(isSearch){
			res = HttpRequestUtil.sendGet(Urls.URL + "services/event/list",
					searchData, (OilApplication) getActivity().getApplication());
		}else{
			res = HttpRequestUtil.sendGet(Urls.URL + "services/event/list",
					null, (OilApplication) getActivity().getApplication());
		}
		try {
			JSONObject json = new JSONObject(res);
			if (json.getInt("status") == 200) {
				listdatas = getListItems(json.getJSONArray("data"));
			}
			int totalRecord = json.getInt("totalRecord");
			if (listdatas.size() == totalRecord) {
				isEnd = true;
			}

		} catch (JSONException e) {
			isEnd = true;
			e.printStackTrace();
		}
		holder.sendMessage(holder.obtainMessage());
		if (!StringUtil.isBlank(lastRes) && lastRes.equals(res)) {
			// 两次相同时则返回true
			return true;
		} else {
			lastRes = res;
		}
		// 两次不同则返回false
		return false;
	}

	/**
	 * 解析数据
	 * 
	 */
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

	/**
	 * 更新更多数据
	 */
	public void updateListByUp() {

		try {
			String res = new String();
			if(isSearch){
				res = HttpRequestUtil.sendGet(Urls.URL
						+ "services/event/list", "pageOffset="
						+ ITCO_ListAdapter.listdatas.size() + "&" + searchData,
						(OilApplication) getActivity().getApplication());
			}else{
				res = HttpRequestUtil.sendGet(Urls.URL
						+ "services/event/list", "pageOffset="
						+ ITCO_ListAdapter.listdatas.size(),
						(OilApplication) getActivity().getApplication());
			}
			JSONObject json = new JSONObject(res);
			JSONArray jarrat = json.getJSONArray("data");
			int totalRecord = json.getInt("totalRecord");
			if (jarrat.length() == 0
					|| ITCO_ListAdapter.getCount() + jarrat.length() == totalRecord) {
				isEnd = true;
			}
			Message msg = newHand.obtainMessage();
			msg.obj = jarrat;
			newHand.sendMessage(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
	}

	/**
	 * 把数据装进集合
	 * 
	 */
	public Map<String, Object> getItem(JSONObject json) {
		try {
			Map<String, Object> itco_map = new HashMap<String, Object>();
			itco_map.put("id", json.getInt("id"));
			itco_map.put("status", json.getString("status"));
			itco_map.put("created_by", json.getString("reporter"));// 创建人
			itco_map.put("position_start", json.getString("position_start"));// 地段
			itco_map.put("pl_name", json.getString("pl_name"));// 管线名称
			itco_map.put("pl_section_name", json.getString("pl_section_name"));// 起止段落
			itco_map.put("pl_spec_name", json.getString("pl_spec_name"));// 管线规格
			try {

				itco_map.put("discover_date", DateFormaterUtil.getDateToString(
						json.getLong("discover_date"), "yyyy-MM-dd HH:mm:ss"));// 时间
			} catch (Exception e) {
				itco_map.put("discover_date", "");
			}// 时间
				// listdatas.add(itco_map);
			return itco_map;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				isNoRe = updateList();
			}
		});
		getThread.start(); // 启动线程

		isEnd = false;
		onLoad();
	}

	/**
	 * 添加更多数据进来
	 */
	@Override
	public void onLoadMore() {
		if(getThread == null || !getThread.isAlive()) {
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				updateListByUp();
			}
		});

		getThread.start();
		
		newHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				JSONArray jarrat = (JSONArray) msg.obj;
				for (int i = 0; i < jarrat.length(); i++) {
					try {
						JSONObject json1 = jarrat.getJSONObject(i);
						ITCO_ListAdapter.listdatas.add(getItem(json1));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				ITCO_ListAdapter.notifyDataSetChanged();// 提醒adapter更新
				if(ITCO_ListAdapter.getCount()<50){
					isEnd=true;
				}
				if (isEnd) {
					list.setIsEnd(isEnd);
				}
				onLoad();
			}
		};
		}
	}

	// 将显示初始化
	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime(DateFormaterUtil
				.getCurrentDate("MM-dd HH:mm:ss"));
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 20){
			isSearch = true;
			searchData = data.getStringExtra("searchData");
			Log.e("Tag",data.getStringExtra("searchData"));
			getThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// 这里写入子线程需要做的工作
					isNoRe = updateList();
				}
			});
			getThread.start(); // 启动线程
			isEnd = false;
			onLoad();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(createReceiver);
		getActivity().unregisterReceiver(myReceiver);
	}

}
