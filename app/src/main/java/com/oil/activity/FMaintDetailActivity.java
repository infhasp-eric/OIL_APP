package com.oil.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.Urls;

public class FMaintDetailActivity extends FinalActivity {
	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;
	@ViewInject(id = R.id.bt_add, click = "addClick")
	Button bt_add;
	@ViewInject(id = R.id.bt_save, click = "saveClick")
	Button bt_save;
	@ViewInject(id = R.id.et_no)
	EditText et_no;
	@ViewInject(id = R.id.et_lcz_no)
	EditText et_lcz_no;
	@ViewInject(id = R.id.et_address)
	EditText et_address;
	@ViewInject(id = R.id.et_longitude)
	EditText et_longitude;
	@ViewInject(id = R.id.et_latitude)
	EditText et_latitude;
	@ViewInject(id = R.id.et_jgxs)
	EditText et_jgxs;
	@ViewInject(id = R.id.et_ssxz_desc)
	EditText et_ssxz_desc;
	@ViewInject(id = R.id.et_m_time)
	EditText et_m_time;
	@ViewInject(id = R.id.et_description)
	EditText et_description;
	@ViewInject(id = R.id.et_recorder)
	EditText et_recorder;
	@ViewInject(id = R.id.sp_categoryT)
	Spinner sp_categoryT;
	@ViewInject(id = R.id.sp_category)
	Spinner sp_category;

	ArrayAdapter<String> type1Ada;
	ArrayAdapter<String> type2Ada;
	private String categoryT, category;
	private Integer ctIndex;
	private Integer clickNum = 0;
	private ProgressDialog dialog = null;
	private List<String> noList = new ArrayList<String>();
	private List<String> lcz_noList = new ArrayList<String>();
	private List<String> addressList = new ArrayList<String>();
	private List<String> longitudeList = new ArrayList<String>();
	private List<String> latitudeList = new ArrayList<String>();
	private List<String> jgxsList = new ArrayList<String>();
	private List<String> ssxz_descList = new ArrayList<String>();
	private List<String> m_timeList = new ArrayList<String>();
	private List<String> descriptionList = new ArrayList<String>();
	private List<String> recorderList = new ArrayList<String>();
	private List<String> categoryTList = new ArrayList<String>();
	private List<String> categoryList = new ArrayList<String>();

	String[] categoryTs = { "明管", "跨越", "公路穿越", "河流穿越", "护坡堡坎", "里程桩", "测试桩",
			"牺牲阳极", "警告牌", "呼吸管" };
	String[][] categorys = { { "一字型" }, { "八字型", "一字型" }, { "直埋", "涵洞", "套管" },
			{ "大开挖", "定向钻", "顶管", "隧道" }, { "条石", "卵石" },
			{ "水泥方桩", "塑料三角桩", "水泥三角桩" }, { "电缆", "钢芯" }, { "电缆" },
			{ "水泥双脚", "塑钢单脚", "钢板双脚" }, { "钢板", "水泥沉降井" } };

	private String serverUrl, param;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fmaint_detail);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		/* 获取Bundle */
		Bundle bundle = this.getIntent().getExtras();
		/* 通过key值从bundle中取值 */
		param = bundle.getString("params");
		/* 通过key值从bundle中取值 */
		serverUrl = Urls.URL;

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存，请稍后...");
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					FMaintDetailActivity.this.finish();
				}
				return false;
			}
		});

		sp_categoryT.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type2Ada = new ArrayAdapter<String>(FMaintDetailActivity.this,
						R.layout.spinner_style, categorys[arg2]);
				type2Ada.setDropDownViewResource(R.layout.spinner_drop_down);
				sp_category.setAdapter(type2Ada);
				// sp_category.setSelection(0, true);
				categoryT = categoryTs[arg2];
				ctIndex = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		sp_category.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				category = categorys[ctIndex][arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		type1Ada = new ArrayAdapter<String>(FMaintDetailActivity.this,
				R.layout.spinner_style, categoryTs);
		type1Ada.setDropDownViewResource(R.layout.spinner_drop_down);
		sp_categoryT.setAdapter(type1Ada);
		// sp_categoryT.setSelection(0, true);
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

	public void addClick(View v) {
		if ("".equals(et_no.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请填写编号", 1000).show();
			return;
		}
		if("".equals(et_lcz_no.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请填写头里程桩号", 1000).show();
			return;
		}
		getValue();
		et_no.setText("");
		et_lcz_no.setText("");
		et_address.setText("");
		et_longitude.setText("");
		et_latitude.setText("");
		et_jgxs.setText("");
		et_ssxz_desc.setText("");
		et_m_time.setText("");
		et_description.setText("");
		et_recorder.setText("");
	}

	public void saveClick(View v) {

		if ("".equals(et_no.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请填写编号", 1000).show();
			return;
		}
		if ("".equals(et_lcz_no.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请填写头里程桩号", 1000).show();
			return;
		}

		if (clickNum == 0) {
			getValue();
		}
		clickNum++;
		List<List<String>> paramList = new ArrayList<List<String>>();
		List<String> nameList = new ArrayList<String>();
		paramList.add(noList);
		nameList.add("no");
		paramList.add(lcz_noList);
		nameList.add("lcz_no");
		paramList.add(addressList);
		nameList.add("address");
		paramList.add(longitudeList);
		nameList.add("longitude");
		paramList.add(latitudeList);
		nameList.add("latitude");
		paramList.add(jgxsList);
		nameList.add("jgxs");
		paramList.add(ssxz_descList);
		nameList.add("ssxz_desc");
		paramList.add(m_timeList);
		nameList.add("m_time");
		paramList.add(descriptionList);
		nameList.add("description");
		paramList.add(recorderList);
		nameList.add("recorder");
		paramList.add(categoryTList);
		nameList.add("categoryT");
		paramList.add(categoryList);
		nameList.add("category");

		dialog.show();

		param += ParamsUtil.listToParams(paramList, nameList);
		SavaTask savaTask = new SavaTask();
		savaTask.execute();
	}

	public void getValue() {
		String no = et_no.getText().toString();
		noList.add(no);
		String lcz_no = et_lcz_no.getText().toString();
		lcz_noList.add(lcz_no);
		String address = et_address.getText().toString();
		addressList.add(address);
		String longitude = et_longitude.getText().toString();
		longitudeList.add(longitude);
		String latitude = et_latitude.getText().toString();
		latitudeList.add(latitude);
		String jgxs = et_jgxs.getText().toString();
		jgxsList.add(jgxs);
		String ssxz_desc = et_ssxz_desc.getText().toString();
		ssxz_descList.add(ssxz_desc);
		String m_time = et_m_time.getText().toString();
		m_timeList.add(m_time);
		String description = et_description.getText().toString();
		descriptionList.add(description);
		String recorder = et_recorder.getText().toString();
		recorderList.add(recorder);
		categoryList.add(category);
		categoryTList.add(categoryT);
	}

	public class SavaTask extends AsyncTask<String, Void, String> {
		JSONObject rejs;
		String req;

		@Override
		protected String doInBackground(String... params) {
			req = HttpRequestUtil.PostHttp(serverUrl
					+ "/services/base/f_maint/save", param,
					(OilApplication) getApplication());

			return req;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				rejs = JSONUtil.stringToJson(req);
				if (rejs.getString("status").equals("200")) {
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "保存成功", 1000)
							.show();
					// Intent intent = new Intent();
					FMaintCreateActivity.fmc.finish();
//					intent.setClass(FMaintDetailActivity.this,
//							FMaintListActivity.class);
//					startActivity(intent);
					finish();
				} else {
					dialog.dismiss();
					Toast.makeText(getApplicationContext(),
							rejs.getString("message"), 1000).show();
				}
			} catch (JSONException e) {
				dialog.dismiss();
				e.printStackTrace();

				Toast.makeText(getApplicationContext(), "发生未知错误", 1000).show();
			} catch (Exception e) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), req, 1000).show();
			}
			super.onPostExecute(result);
		}

	}
}
