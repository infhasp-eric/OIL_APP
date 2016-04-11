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
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.domain.ValveMaint;
import com.oil.utils.AdapterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ProperUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

@SuppressLint("NewApi")
public class VMaintCreateActivity extends FinalActivity {

	@ViewInject(id = R.id.sp_pl)
	Spinner sp_pl;
	@ViewInject(id = R.id.sp_section)
	Spinner sp_section;
	@ViewInject(id = R.id.sp_spec)
	Spinner sp_spec;
	@ViewInject(id = R.id.et_valve_name)
	EditText et_valve_name;
	@ViewInject(id = R.id.tv_date)
	TextView et_day;
	@ViewInject(id = R.id.bt_next, click = "nextClick")
	Button bt_next;
	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;
	String serverUrl;
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
	private int year, month, day;
	private ProgressDialog dialog = null;
	public static VMaintCreateActivity vmc;
	private Thread newThread, plthread, spthread;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			JSONArray plArray = (JSONArray) msg.obj;
			if (msg.what == 1) {
				try {
					plList = null;
					pl_idList = null;
					plList = new String[plArray.length()];
					pl_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						plList[i] = plArray.getJSONObject(i).getString("name");
						pl_idList[i] = plArray.getJSONObject(i).getInt("id");
					}
					plAdapter = new ArrayAdapter<String>(
							VMaintCreateActivity.this, R.layout.spinner_style,
							plList);
					plAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_pl.setAdapter(plAdapter);
					sp_pl.setSelection(0, true);
					pl_id = pl_idList[0];
					sp_pl.invalidate();
					dialog.cancel();
				} catch (Exception e) {
					dialog.cancel();
				}
			} else if (msg.what == 2) {
				try {
					sectionList = null;
					section_idList = null;
					sectionList = new String[plArray.length()];
					section_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						sectionList[i] = plArray.getJSONObject(i).getString(
								"name");
						section_idList[i] = plArray.getJSONObject(i).getInt(
								"id");
					}
					sectionAdapter = new ArrayAdapter<String>(
							VMaintCreateActivity.this, R.layout.spinner_style,
							sectionList);
					sectionAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_section.setAdapter(sectionAdapter);
					sp_section.setSelection(0, true);
					section_id = section_idList[0];
					dialog.cancel();
				} catch (Exception e) {
					dialog.cancel();
				}
			} else {
				try {
					specList = null;
					spec_idList = null;
					specList = new String[plArray.length()];
					spec_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						specList[i] = plArray.getJSONObject(i)
								.getString("name");
						spec_idList[i] = plArray.getJSONObject(i).getInt("id");
					}
					// et_auditor.setText(seltion_idList[0] + "");
					specAdapter = new ArrayAdapter<String>(
							VMaintCreateActivity.this, R.layout.spinner_style,
							specList);
					specAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_spec.setAdapter(specAdapter);
					sp_spec.setSelection(0, true);
					spec_id = spec_idList[0];
					dialog.cancel();
				} catch (Exception e) {
					dialog.cancel();
				}
			}
		}
	};

	private Handler toastHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(getApplicationContext(), msg.obj.toString(), 1)
					.show();
		}
	};

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vmaint_create);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍候...");
		dialog.show();
		dialog.setCancelable(false);

		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					VMaintCreateActivity.this.finish();
				}
				return false;
			}
		});
		// et_month.setText("test");
		vmc = this;
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// 获取配置文件
		serverUrl = Urls.URL;

		// HttpReqUtil.getJson("http://192.168.0.102:8080/oil/services/base/pipeline/get",
		// null, textView3);

		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
		et_day.setText(formatDate(year, month, day));
		et_day.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpd = new DatePickerDialog(
						VMaintCreateActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								year = arg1;
								month = arg2;
								day = arg3;
								et_day.setText(formatDate(year, month, day));
							}
						}, year, month, day);
				dpd.show();
			}
		});

		// 管线下拉列表点击事件
		sp_pl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				AdapterUtil.updateAdapter(sp_section, sectionAdapter,
						VMaintCreateActivity.this);
				AdapterUtil.updateAdapter(sp_spec, specAdapter,
						VMaintCreateActivity.this);
				sectionList = null;
				section_idList = null;
				if (pl_idList != null) {
					pl_id = pl_idList[arg2];
					section_id = null;
					plthread = new Thread(new Runnable() {

						@Override
						public void run() {
							plRefresh(serverUrl + "/services/base/section/get",
									"pl_id=" + pl_id, 2);
						}
					});
					plthread.start();
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
				AdapterUtil.updateAdapter(sp_spec, specAdapter,
						VMaintCreateActivity.this);
				specList = null;
				spec_idList = null;
				if (section_idList != null) {
					section_id = section_idList[arg2];
					spec_id = null;
					spthread = new Thread(new Runnable() {

						@Override
						public void run() {
							plRefresh(serverUrl + "/services/base/spec/get",
									"pl_section_id=" + section_id, 3);
						}
					});
					spthread.start();
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
	protected void onDestroy() {
		ThreadKILL.killthread(newThread);
		ThreadKILL.killthread(plthread);
		ThreadKILL.killthread(spthread);

		super.onDestroy();
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

	public void nextClick(View v) {
		if ((newThread != null && newThread.isAlive())
				|| (plthread != null && plthread.isAlive())
				|| (spthread != null && spthread.isAlive())) {
			Toast.makeText(getApplicationContext(), "正在加载，请稍等...", 200).show();
			return;
		}
		ValveMaint vm = new ValveMaint();
		if (pl_id == null || pl_id.intValue() <= 0) {
			Toast.makeText(getApplicationContext(), "请选择管线", 1000).show();
			return;
		} else if (section_id == null || section_id.intValue() <= 0) {
			Toast.makeText(getApplicationContext(), "请选择管段", 1000).show();
			return;
		} else if (spec_id == null || spec_id.intValue() <= 0) {
			Toast.makeText(getApplicationContext(), "请选择管线规格", 1000).show();
			return;
		}
		vm.setPl(pl_id);
		vm.setSection(section_id);
		vm.setSpec(spec_id);
		if (StringUtil.isBlank(et_valve_name.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请填写阀室名称", 1000).show();
			return;
		}
		if (StringUtil.isBlank(et_day.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请填写检查日期", 1000).show();
			return;
		}
		vm.setValve_name(et_valve_name.getText().toString());
		vm.setDay(et_day.getText().toString());
		Intent intent = new Intent(VMaintCreateActivity.this,
				VMaintDetailActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("params", vm.toString());
		System.out.println(vm.toString());
		// bundle.put
		intent.putExtras(bundle);

		startActivity(intent);
	}

	public void plRefresh(String url, String params, int type) {
		String repl = HttpRequestUtil.sendGet(url, params,
				(OilApplication) getApplication());
		if (repl.equals("error")) {
			Message msg = toastHand.obtainMessage();
			msg.obj = "请检查您的网络";
			toastHand.sendMessage(msg);
			dialog.dismiss();
			finish();
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
					Message msg = toastHand.obtainMessage();
					msg.obj = message;
					toastHand.sendMessage(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public void backClick(View v) {
		finish();
	}

	private String formatDate(int years, int months, int days) {
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(years + "");
		if (months < 9) {
			sbDate.append("-0" + (months + 1));
		} else {
			sbDate.append("-" + (months + 1));
		}
		if (days < 10) {
			sbDate.append("-0" + days);
		} else {
			sbDate.append("-" + days);
		}
		return sbDate.toString();
	}
}
