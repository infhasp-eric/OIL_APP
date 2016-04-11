package com.oil.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.ProperUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

public class VPatrolDetailActivity extends FinalActivity {

	@ViewInject(id = R.id.sp_type1)
	Spinner sp_type1;
	@ViewInject(id = R.id.sp_type2)
	Spinner sp_type2;
	@ViewInject(id = R.id.sp_type2_layout)
	LinearLayout sp_type2_layout;
	@ViewInject(id = R.id.ly_group1)
	LinearLayout ly_group1;
	@ViewInject(id = R.id.ly_group2)
	LinearLayout ly_group2;
	@ViewInject(id = R.id.ly_group3)
	LinearLayout ly_group3;
	@ViewInject(id = R.id.ly_group4)
	LinearLayout ly_group4;
	@ViewInject(id = R.id.cb_normal1)
	CheckBox cb_normal1;
	@ViewInject(id = R.id.cb_normal2)
	CheckBox cb_normal2;
	@ViewInject(id = R.id.cb_normal3)
	CheckBox cb_normal3;
	@ViewInject(id = R.id.cb_normal4)
	CheckBox cb_normal4;
	@ViewInject(id = R.id.cb_normal5)
	CheckBox cb_normal5;
	@ViewInject(id = R.id.cb_normal6)
	CheckBox cb_normal6;
	@ViewInject(id = R.id.cb_normal7)
	CheckBox cb_normal7;
	@ViewInject(id = R.id.cb_normal8)
	CheckBox cb_normal8;
	@ViewInject(id = R.id.cb_normal9)
	CheckBox cb_normal9;
	@ViewInject(id = R.id.cb_normal10)
	CheckBox cb_normal10;
	@ViewInject(id = R.id.cb_normal11)
	CheckBox cb_normal11;
	@ViewInject(id = R.id.cb_normal12)
	CheckBox cb_normal12;
	@ViewInject(id = R.id.cb_normal13)
	CheckBox cb_normal13;
	@ViewInject(id = R.id.et_handle1)
	EditText et_handle1;
	@ViewInject(id = R.id.et_handle2)
	EditText et_handle2;
	@ViewInject(id = R.id.et_handle3)
	EditText et_handle3;
	@ViewInject(id = R.id.et_handle4)
	EditText et_handle4;
	@ViewInject(id = R.id.et_handle5)
	EditText et_handle5;
	@ViewInject(id = R.id.et_handle6)
	EditText et_handle6;
	@ViewInject(id = R.id.et_handle7)
	EditText et_handle7;
	@ViewInject(id = R.id.et_handle8)
	EditText et_handle8;
	@ViewInject(id = R.id.et_handle9)
	EditText et_handle9;
	@ViewInject(id = R.id.et_handle10)
	EditText et_handle10;
	@ViewInject(id = R.id.et_handle11)
	EditText et_handle11;
	@ViewInject(id = R.id.et_handle12)
	EditText et_handle12;
	@ViewInject(id = R.id.et_handle13)
	EditText et_handle13;
	@ViewInject(id = R.id.et_remark1)
	EditText et_remark1;
	@ViewInject(id = R.id.et_remark2)
	EditText et_remark2;
	@ViewInject(id = R.id.et_remark3)
	EditText et_remark3;
	@ViewInject(id = R.id.et_remark4)
	EditText et_remark4;
	@ViewInject(id = R.id.et_remark5)
	EditText et_remark5;
	@ViewInject(id = R.id.et_remark6)
	EditText et_remark6;
	@ViewInject(id = R.id.et_remark7)
	EditText et_remark7;
	@ViewInject(id = R.id.et_remark8)
	EditText et_remark8;
	@ViewInject(id = R.id.et_remark9)
	EditText et_remark9;
	@ViewInject(id = R.id.et_remark10)
	EditText et_remark10;
	@ViewInject(id = R.id.et_remark11)
	EditText et_remark11;
	@ViewInject(id = R.id.et_remark12)
	EditText et_remark12;
	@ViewInject(id = R.id.et_remark13)
	EditText et_remark13;
	@ViewInject(id = R.id.bt_save, click = "saveToServer")
	Button bt_save;
	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;

	List<CheckBox> cbList;
	List<String> normalList = new ArrayList<String>();
	List<EditText> handleList = new ArrayList<EditText>();
	List<EditText> remarkList = new ArrayList<EditText>();

	ArrayAdapter<String> at_type1;
	ArrayAdapter<String> at_type2;

	String param;
	String serverUrl;

	private String[] type1 = { "阀室", "放空区", "阀井" };
	private String[] type2 = { "设备检查", "基础设施检查" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vpatrol_detail);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		/* 获取Bundle */
		Bundle bundle = this.getIntent().getExtras();

		/* 通过key值从bundle中取值 */
		param = bundle.getString("params");
		serverUrl = Urls.URL;

		cbList = new ArrayList<CheckBox>();
		cbList.add(cb_normal1);
		cbList.add(cb_normal2);
		cbList.add(cb_normal3);
		cbList.add(cb_normal4);
		cbList.add(cb_normal5);
		cbList.add(cb_normal6);
		cbList.add(cb_normal7);
		cbList.add(cb_normal8);
		cbList.add(cb_normal9);
		cbList.add(cb_normal10);
		cbList.add(cb_normal11);
		cbList.add(cb_normal12);
		cbList.add(cb_normal13);
		handleList.add(et_handle1);
		handleList.add(et_handle2);
		handleList.add(et_handle3);
		handleList.add(et_handle4);
		handleList.add(et_handle5);
		handleList.add(et_handle6);
		handleList.add(et_handle7);
		handleList.add(et_handle8);
		handleList.add(et_handle9);
		handleList.add(et_handle10);
		handleList.add(et_handle11);
		handleList.add(et_handle12);
		handleList.add(et_handle13);
		remarkList.add(et_remark1);
		remarkList.add(et_remark2);
		remarkList.add(et_remark3);
		remarkList.add(et_remark4);
		remarkList.add(et_remark5);
		remarkList.add(et_remark6);
		remarkList.add(et_remark7);
		remarkList.add(et_remark8);
		remarkList.add(et_remark9);
		remarkList.add(et_remark10);
		remarkList.add(et_remark11);
		remarkList.add(et_remark12);
		remarkList.add(et_remark13);

		sp_type1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					sp_type2_layout.setVisibility(View.VISIBLE);
					ly_group1.setVisibility(View.GONE);
					ly_group2.setVisibility(View.GONE);
					ly_group3.setVisibility(View.GONE);
					ly_group4.setVisibility(View.GONE);
					at_type2 = new ArrayAdapter<String>(
							VPatrolDetailActivity.this, R.layout.spinner_style,
							type2);
					at_type2.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_type2.setAdapter(at_type2);
					sp_type2.setSelection(0, true);
					break;
				case 1:
					sp_type2_layout.setVisibility(View.GONE);
					ly_group1.setVisibility(View.GONE);
					ly_group2.setVisibility(View.GONE);
					ly_group3.setVisibility(View.VISIBLE);
					ly_group4.setVisibility(View.GONE);
					break;

				default:
					sp_type2_layout.setVisibility(View.GONE);
					ly_group1.setVisibility(View.GONE);
					ly_group2.setVisibility(View.GONE);
					ly_group3.setVisibility(View.GONE);
					ly_group4.setVisibility(View.VISIBLE);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		sp_type2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					ly_group1.setVisibility(View.VISIBLE);
					ly_group2.setVisibility(View.GONE);
					ly_group3.setVisibility(View.GONE);
					ly_group4.setVisibility(View.GONE);
					break;

				default:
					ly_group1.setVisibility(View.GONE);
					ly_group2.setVisibility(View.VISIBLE);
					ly_group3.setVisibility(View.GONE);
					ly_group4.setVisibility(View.GONE);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		at_type1 = new ArrayAdapter<String>(VPatrolDetailActivity.this,
				R.layout.spinner_style, type1);
		at_type1.setDropDownViewResource(R.layout.spinner_drop_down);
		sp_type1.setAdapter(at_type1);
		sp_type1.setSelection(0, true);

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

	public void backClick(View v) {
		finish();
	}

	public void saveToServer(View v) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		for (CheckBox cb : cbList) {
			if (cb.isChecked()) {
				paramsMap.put("normal" + (cbList.indexOf(cb) + 1), "1");
			}
		}
		for (EditText et : handleList) {
			String handle = et.getText().toString();
			if (!StringUtil.isBlank(handle)) {
				paramsMap.put("handle" + (handleList.lastIndexOf(et) + 1),
						handle);
			}
		}
		for (EditText et : remarkList) {
			String remark = et.getText().toString();
			if (!StringUtil.isBlank(remark)) {
				paramsMap.put("remark" + (remarkList.indexOf(et) + 1), remark);
			}
		}

		param += ParamsUtil.mapToParams(paramsMap);
		SaveTask saveTask = new SaveTask();
		saveTask.execute();
	}

	private class SaveTask extends AsyncTask<String, Void, String> {
		String req;
		JSONObject rejs;

		@Override
		protected String doInBackground(String... params) {
			req = HttpRequestUtil.PostHttp(serverUrl
					+ "services/base/v_patrol/save", param,
					(OilApplication) getApplication());

			return req;
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("运行到这里！！！！！！！！");
			System.out.println(result);
			try {
				System.out.println(req);
				rejs = JSONUtil.stringToJson(req);
				if (rejs.getInt("status") == 200) {
					Toast.makeText(getApplicationContext(), "保存成功",
							Toast.LENGTH_SHORT).show();
					VPatrolCreateActivity.vpa.finish();
					//VpListActivity.vpl.finish();
					/*Intent intent = new Intent(VPatrolDetailActivity.this,
							VpListActivity.class);*/
					//startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							rejs.getString("message"), Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "发生未知错误",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "请检查您的网络",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}
}
