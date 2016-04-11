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
import android.view.Menu;
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
import com.oil.domain.PotentialCurve;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ProperUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

/**
 * 管道保护电位曲线图
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class PlCurveCreatActivity extends FinalActivity {

	@ViewInject(id = R.id.sp_pl)
	Spinner sp_pl;
	@ViewInject(id = R.id.sp_section)
	Spinner sp_section;
	@ViewInject(id = R.id.sp_spec)
	Spinner sp_spec;
	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;
	@ViewInject(id = R.id.et_unit)
	EditText et_unit;
	@ViewInject(id = R.id.et_year, click = "choseDate")
	TextView et_year;
	@ViewInject(id = R.id.bt_next, click = "nextClick")
	Button bt_next;
	private String serverUrl;
	private String status;
	private String[] plList;
	private int[] pl_idList;
	private String[] sectionList;
	private int[] section_idList;
	private String[] specList;
	private int[] spec_idList;
	private ArrayAdapter<String> plAdapter;
	private ArrayAdapter<String> sectionAdapter;
	private ArrayAdapter<String> specAdapter;
	private Integer pl_id;
	private Integer section_id;
	private Integer spec_id;
	private Thread newThread;
	private ProgressDialog dialog = null;
	private DatePickerDialog mDialog;
	public static PlCurveCreatActivity pcc;
	private String months = new String();
	private int year, month;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					plList = null;
					pl_idList = null;
					plList = new String[plArray.length()];
					pl_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						plList[i] = plArray.getJSONObject(i).getString("name");
						pl_idList[i] = plArray.getJSONObject(i).getInt("id");
					}
					plAdapter = new ArrayAdapter<String>(
							PlCurveCreatActivity.this, R.layout.spinner_style,
							plList);
					plAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_pl.setAdapter(plAdapter);
					sp_pl.setSelection(0, true);
					pl_id = pl_idList[0];
					sp_pl.invalidate();
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					dialog.dismiss();
				}
			} else if (msg.what == 2) {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					sectionList = null;
					section_idList = null;
					sectionList = new String[plArray.length()];
					section_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						sectionList[i] = plArray.getJSONObject(i).getString(
								"name");
						section_idList[i] = plArray.getJSONObject(i).getInt(
								"id");

						System.out.println("sectionid======"
								+ section_idList[i]);
					}
					sectionAdapter = new ArrayAdapter<String>(
							PlCurveCreatActivity.this, R.layout.spinner_style,
							sectionList);
					sectionAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_section.setAdapter(sectionAdapter);
					sp_section.setSelection(0, true);
					section_id = section_idList[0];
				} catch (Exception e) {
					e.printStackTrace();
					dialog.dismiss();
				}
			} else if (msg.what == 3) {
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
						System.out.println(specList[i]);
					}
					specAdapter = new ArrayAdapter<String>(
							PlCurveCreatActivity.this, R.layout.spinner_style,
							specList);
					specAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_spec.setAdapter(specAdapter);
					sp_spec.setSelection(0, true);
					System.out.println("清空后运行到这里！！规格！！！！");
					spec_id = spec_idList[0];
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
					dialog.dismiss();
				}
			} else {
				Toast.makeText(getApplicationContext(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				finish();
			}
		}
	};

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pl_curve_creat);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.show();
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					PlCurveCreatActivity.this.finish();
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
		// day=mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
		if ((month + 1) < 10) {
			months = "0" + (month + 1);
		}
		et_year.setText(year + "-" + months);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		pcc = this;
		// 获取配置文件
		serverUrl = Urls.URL;

		// HttpReqUtil.getJson("http://192.168.0.102:8080/oil/services/base/pipeline/get",
		// null, textView3);

		// 管线下拉列表点击事件
		sp_pl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				section_id = null;
				spec_id = null;
				sectionList = null;
				section_idList = null;
				System.out.println("已清空");
				updateAdapter(sp_section, sectionAdapter);
				updateAdapter(sp_spec, specAdapter);
				System.out.println("section_id=====" + section_id
						+ "=======spec_id----------" + spec_id);
				pl_id = pl_idList[arg2];
				ThreadKILL.killthread(newThread);
				newThread = new Thread(new Runnable() {

					@Override
					public void run() {
						plRefresh(serverUrl + "/services/base/section/get",
								"pl_id=" + pl_id, 2);
					}
				});
				newThread.start();

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
				System.out.println("Running here11111111111111");
				updateAdapter(sp_spec, specAdapter);
				spec_id = null;
				spec_idList = null;
				specList = null;
				if (section_idList != null) {
					section_id = section_idList[arg2];
					ThreadKILL.killthread(newThread);
					newThread = new Thread(new Runnable() {
						@Override
						public void run() {
							plRefresh(serverUrl + "/services/base/spec/get",
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
				plRefresh(serverUrl + "/services/base/pipeline/get", "", 1);
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

	public void nextClick(View v) {
		PotentialCurve pc = new PotentialCurve();
		if(newThread != null && newThread.isAlive()) {
			Toast.makeText(getApplicationContext(), "正在加载，请稍等...", 200).show();
			return;
		}
		// 判断三个管线id是否都有复制
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
		pc.setPl(pl_id);
		pc.setSection(section_id);
		pc.setSpec(spec_id);
		if (!StringUtil.isBlank(et_unit.getText().toString())) {
			pc.setUnit(et_unit.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "请填写保存单位",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isBlank(et_year.getText().toString())) {
			pc.setYear(et_year.getText().toString());
		} else {
			Toast.makeText(getApplicationContext(), "请选择检查日期",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(PlCurveCreatActivity.this,
				PlCurveDetailActivity.class);

		Bundle bundle = new Bundle();
		System.out.println(pc.toString());
		bundle.putString("params", pc.toString());

		intent.putExtras(bundle);

		startActivity(intent);
	}

	public void plRefresh(String url, String params, int type) {
		String repl = HttpRequestUtil.sendGet(url, params,
				(OilApplication) getApplication());
		if (repl.equals("error")) {
			Message msg = mHandler.obtainMessage();
			msg.obj = "请检查您的网络";
			mHandler.sendMessage(msg);
		} else {
			try {
				JSONObject reJson = JSONUtil.stringToJson(repl);
				status = reJson.getString("status");
				if (status.equals("200")) {
					JSONArray plArray = reJson.getJSONArray("data");
					Message msg = mHandler.obtainMessage();
					msg.what = type;
					msg.obj = plArray;
					mHandler.sendMessage(msg);
				} else {
					String message = reJson.getString("message");
					Message msg = mHandler.obtainMessage();
					msg.obj = message;
					mHandler.sendMessage(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public void choseDate(View v) {
		final Calendar cal = Calendar.getInstance();
		mDialog = new CustomerDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				int year = arg1;
				int month = arg2;
				months = new String();
				if ((month + 1) < 10) {
					months = "0" + (month + 1);
				} else {
					months = month + "";
				}
				et_year.setText(year + "-" + months);
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

	protected void onDestroy() {
		if (newThread != null) {
			newThread.interrupt();
		}
		super.onDestroy();
	}

	// 初始化adapter
	private void updateAdapter(Spinner sp, ArrayAdapter adapter) {
		String[] sList = new String[] { "" };
		adapter = null;
		adapter = new ArrayAdapter<String>(PlCurveCreatActivity.this,
				R.layout.spinner_style, sList);
		adapter.setDropDownViewResource(R.layout.spinner_drop_down);
		sp.setAdapter(adapter);
		sp.invalidate();
	}
}
