package com.oil.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.dialog.CustomerDatePickerDialog;
import com.oil.domain.RunRecord;
import com.oil.utils.AdapterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ProperUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

/**
 * 阴极保护站运行记录
 * 
 * @author Eric
 * 
 */
@SuppressLint("NewApi")
public class MainActivity extends FinalActivity {

	@ViewInject(id = R.id.bt_next, click = "btNextClick")
	Button bt_next;
	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;
	@ViewInject(id = R.id.et_jingzhan)
	EditText et_jingzhan;
	@ViewInject(id = R.id.et_year, click = "choseDate")
	TextView et_year;
	@ViewInject(id = R.id.sp_pl)
	Spinner sp_pl;
	@ViewInject(id = R.id.sp_section)
	Spinner sp_section;
	@ViewInject(id = R.id.sp_spec)
	Spinner sp_spec;
	@ViewInject(id = R.id.et_auditor)
	EditText et_auditor;
	private DatePickerDialog mDialog;
	String status;
	String[] plList;
	int[] pl_idList;
	String[] sectionList;
	int[] section_idList;
	String[] specList;
	int[] spec_idList;
	ArrayAdapter<String> plAdapter;
	ArrayAdapter<String> sectionAdapter;
	ArrayAdapter<String> specAdapter;
	Integer pl_id;
	Integer section_id;
	Integer spec_id;
	String url;
	private int year, month;
	public static MainActivity ma = null;
	private String yearAndmonth = new String();
	private ProgressDialog dialog = null;

	private Thread newThread;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			if (msg.what == 1) {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					plList = new String[plArray.length()];
					pl_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						plList[i] = plArray.getJSONObject(i).getString("name");
						pl_idList[i] = plArray.getJSONObject(i).getInt("id");
					}
					plAdapter = new ArrayAdapter<String>(MainActivity.this,
							R.layout.spinner_style, plList);
					plAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_pl.setAdapter(plAdapter);
					sp_pl.setSelection(0, true);
					pl_id = pl_idList[0];
					sp_pl.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (msg.what == 2) {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					sectionList = new String[plArray.length()];
					section_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						sectionList[i] = plArray.getJSONObject(i).getString(
								"name");
						section_idList[i] = plArray.getJSONObject(i).getInt(
								"id");
					}
					sectionAdapter = new ArrayAdapter<String>(
							MainActivity.this, R.layout.spinner_style,
							sectionList);
					sectionAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_section.setAdapter(sectionAdapter);
					sp_section.setSelection(0, true);
					section_id = section_idList[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					specList = null;
					spec_idList = null;
					specList = new String[plArray.length()];
					spec_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						specList[i] = plArray.getJSONObject(i)
								.getString("name");
						spec_idList[i] = plArray.getJSONObject(i).getInt("id");
					}
					specAdapter = new ArrayAdapter<String>(MainActivity.this,
							R.layout.spinner_style, specList);
					specAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_spec.setAdapter(specAdapter);
					sp_spec.setSelection(0, true);
					spec_id = spec_idList[0];
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	private Handler tostHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(getApplicationContext(), msg.obj.toString(), 100).show();
		}
	};

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cpsr_r);

		ma = this;
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		dialog = new ProgressDialog(MainActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.setCancelable(false);
		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					MainActivity.this.finish();
					return true;
				}
				return false;
			}
		});

		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		if (month < 9) {
			et_year.setText(year + "-0" + (month + 1));
		} else {
			et_year.setText(year + "-" + (month + 1));
		}
		// HttpReqUtil.getJson("http://192.168.0.102:8080/oil/services/base/pipeline/get",
		// null, textView3);

		// 初始化管线下拉框，以免刚进入activity时网速过慢导致此下拉框不显示
		plList = new String[] { "全部管线" };
		plAdapter = new ArrayAdapter<String>(MainActivity.this,
				R.layout.spinner_style, plList);
		plAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
		sp_pl.setAdapter(plAdapter);

		// 管线下拉列表点击事件
		sp_pl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				section_id = null;
				spec_id = null;
				AdapterUtil.updateSearchAdapter(sp_section, 2, sectionAdapter,
						MainActivity.this);
				AdapterUtil.updateSearchAdapter(sp_spec, 3, specAdapter,
						MainActivity.this);
				sectionList = null;
				section_idList = null;
				if (pl_idList != null) {
				pl_id = pl_idList[arg2];
				newThread = new Thread(new Runnable() {

					@Override
					public void run() {
						plRefresh(Urls.URL + "/services/base/section/get",
								"pl_id=" + pl_id, 2);
					}
				});
				newThread.start();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// seltion_idList = listener.getIdList();

		// 管段下拉列表点击时间
		sp_section.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				AdapterUtil.updateSearchAdapter(sp_spec, 3, specAdapter,
						MainActivity.this);
				spec_id = null;
				specList = null;
				spec_idList = null;
				if (section_idList != null) {
					section_id = section_idList[arg2];
					newThread = new Thread(new Runnable() {

						@Override
						public void run() {
							plRefresh(Urls.URL + "/services/base/spec/get",
									"pl_section_id=" + section_id, 3);
						}
					});
					newThread.start();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// sp_pl.setOnClickListener();

		sp_spec.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (spec_idList != null) {
					spec_id = spec_idList[arg2];
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// 加载管线列表内容
		newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				plRefresh(Urls.URL + "services/base/pipeline/get", "", 1);
			}
		});
		newThread.start(); // 启动线程

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void backClick(View v) {
		finish();
	}

	public void btNextClick(View v) {
		RunRecord rc = new RunRecord();
		// textView.setText("text set form button");
		// 判断三个管线id是否都有复制
		if(newThread != null && newThread.isAlive()) {
			Toast.makeText(getApplicationContext(), "正在加载，请稍等...", 200).show();
			return;
		}
		if (pl_id == null || pl_id.intValue() <= 0) {
			Toast.makeText(getApplicationContext(), "请选择管线", 200).show();
			return;
		} else if (section_id == null || section_id.intValue() <= 0) {
			Toast.makeText(getApplicationContext(), "请选择管段", 200).show();
			return;
		} else if (spec_id == null || spec_id.intValue() <= 0) {
			Toast.makeText(getApplicationContext(), "请选择管线规格", 200).show();
			return;
		}
		rc.setPl_id(pl_id);
		rc.setPl_section_id(section_id);
		rc.setPl_spec_id(spec_id);

		// rc.setPl_id(pl_idList.get(sp_pl.get));
		if (!StringUtil.isBlank(et_jingzhan.getText().toString())) {
			rc.setJinzhan(et_jingzhan.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "请输入井站", 1000).show();
			return;
		}
		if (!StringUtil.isBlank(et_auditor.getText().toString())) {
			rc.setAuditor(et_auditor.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "请填写填报人", 1000).show();
			return;
		}
		if (!StringUtil.isBlank(et_year.getText().toString())) {
			rc.setR_month(et_year.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "请选择日期", 1000).show();
			return;
		}

		Intent intent = new Intent(MainActivity.this, RcCreatActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("params", rc.toString());
		intent.putExtras(bundle);

		startActivity(intent);

	}

	public void choseDate(View v) {
		final Calendar cal = Calendar.getInstance();
		mDialog = new CustomerDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				int year = arg1;
				int month = arg2;
				String months = null;
				if ((month + 1) < 10) {
					months = "0" + (month + 1);
				} else {
					months = (month + 1) + "";
				}
				et_year.setText(year + "-" + months);
				yearAndmonth = year + "-" + months;
			}
		}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
				.get(Calendar.DAY_OF_MONTH));
		mDialog.show();

		DatePicker dp = CustomerDatePickerDialog
				.findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());
		if (dp != null) {
			((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
					.getChildAt(2).setVisibility(View.GONE);
		}
	}

	public void plRefresh(String url, String params, int type) {
		String repl = HttpRequestUtil.sendGet(url, params,
				(OilApplication) getApplication());
		if (repl.equals("error")) {
			Message msg = tostHand.obtainMessage();
			msg.obj = "请检查您的网络";
			dialog.dismiss();
			finish();
			tostHand.sendMessage(msg);
		} else {
			try {
				JSONObject reJson = JSONUtil.stringToJson(repl);
				status = reJson.getString("status");
				if (status.equals("200")) {
					JSONArray plArray = reJson.getJSONArray("data");
					// Toast.makeText(getApplicationContext(),
					// plList.toString(), 1000).show();
					Message msg = mHandler.obtainMessage();
					msg.what = type;
					msg.obj = plArray;
					mHandler.sendMessage(msg);
					// et_auditor.setText(pl_idList[0] + "");
				} else {
					String message = reJson.getString("message");

					// Toast.makeText(getApplicationContext(), message, 1000)
					// .show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	protected void onDestroy() {
		ThreadKILL.killthread(newThread);
		super.onDestroy();
	}
}
