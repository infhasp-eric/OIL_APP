package com.oil.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.dialog.LodingActivtyDialog;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

public class High_DCA_Information_Create_NEXT extends FinalActivity {
	@ViewInject(id = R.id.high_dca_information_create_next_manager)
	EditText manager_et;
	@ViewInject(id = R.id.high_dca_information_create_next_manager_phone)
	EditText manager_phone_et;
	@ViewInject(id = R.id.high_dca_information_create_next_MAMPU)
	EditText MAMPU_et;// 管理单位
	@ViewInject(id = R.id.high_dca_information_create_next_pl_name)
	EditText pl_name_et;
	@ViewInject(id = R.id.high_dca_information_create_next_pl_section)
	EditText pl_section_et;
	@ViewInject(id = R.id.high_dca_information_create_next_HC_numb)
	EditText HC_numb_et;
	@ViewInject(id = R.id.high_dca_information_create_next_HC_start)
	EditText HC_start_et;
	@ViewInject(id = R.id.high_dca_information_create_next_HC_end)
	EditText HC_end_et;
	@ViewInject(id = R.id.high_dca_information_create_next_HC_long)
	EditText HC_long_et;
	@ViewInject(id = R.id.high_dca_information_create_next_HC_address)
	EditText address_et;
	@ViewInject(id = R.id.high_dca_information_create_next_HC_signalment)
	EditText HC_signalment_et;
	@ViewInject(id = R.id.high_dca_information_create_next_remark)
	EditText remark_et;
	@ViewInject(id = R.id.tv_date)
	TextView year_et;
	@ViewInject(id = R.id.high_dca_information_create_next_back_btn, click = "backlistner")
	Button back;
	@ViewInject(id = R.id.high_dca_information_create_next_add, click = "addlistner")
	Button add;
	@ViewInject(id = R.id.high_dca_information_create_next_save, click = "savelistner")
	Button save;
	@ViewInject(id = R.id.high_dca_information_create_next_filepath, click = "choonsefilelistner")
	TextView addfile;
	private LodingActivtyDialog dialog;
	private List<String> manager_List = new ArrayList<String>();
	private List<String> manager_phone_List = new ArrayList<String>();
	private List<String> MAMPU_List = new ArrayList<String>();
	private List<String> pl_name_List = new ArrayList<String>();
	private List<String> pl_section_List = new ArrayList<String>();
	private List<String> HC_numb_List = new ArrayList<String>();
	private List<String> HC_start_List = new ArrayList<String>();
	private List<String> HC_end_List = new ArrayList<String>();
	private List<String> HC_long_List = new ArrayList<String>();
	private List<String> address_List = new ArrayList<String>();
	private List<String> HC_signalment_List = new ArrayList<String>();
	private List<String> remark_List = new ArrayList<String>();
	private List<String> date_List = new ArrayList<String>();
	private List<String> filename_List = new ArrayList<String>();
	private int flag = 0;
	private String resultstatus;
	private String resultstring;
	private String MAMPU;
	private int year, month, day;
	private String months,days;
	private String filenames;
	private String paramss;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.high_dca_information_create_next);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			Intent intent = getIntent();

			// filenames = intent.getStringExtra("filenames");
			try {

			} catch (Exception e) {
			}
			paramss = intent.getStringExtra("params");
			try {
				Log.v("East_>>>>", filenames);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		HC_signalment_et.setSingleLine(false);
		HC_signalment_et.setHorizontallyScrolling(false);

		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
		// year_et.setText(year + "-" + (month + 1) + "-" + day);

		year_et.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpd = new DatePickerDialog(
						High_DCA_Information_Create_NEXT.this,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								year = arg1;
								month = arg2;
								day = arg3;
								if ((month + 1) < 10) {
									months = "0" + (month + 1);
								} else {
									months = "" + (month + 1);
								}
								if (day < 9) {
									days = "0" + day;
								} else {
									days = "" + day;
								}

								year_et.setText(year + "-" + months + "-"
										+ days);
							}
						}, year, month, day);
				dpd.show();
			}
		});

	}

	public void backlistner(View V) {
		finish();
	}

	public void addlistner(View V) {
		MAMPU = MAMPU_et.getText().toString();
		if (StringUtils.isEmpty(MAMPU)) {

			Toast.makeText(getApplicationContext(), "添加失败,管理单位不能为空", 1000)
					.show();
		} else {
			Getcontent();
			manager_et.setText("");
			manager_phone_et.setText("");
			MAMPU_et.setText("");
			pl_name_et.setText("");
			pl_section_et.setText("");
			HC_numb_et.setText("");
			HC_start_et.setText("");
			HC_end_et.setText("");
			HC_long_et.setText("");
			address_et.setText("");
			HC_signalment_et.setText("");
			remark_et.setText("");
			year_et.setText("");
		}
	}

	public void Getcontent() {
		String manager = manager_et.getText().toString();
		manager_List.add(manager);
		String manager_phone = manager_phone_et.getText().toString();
		manager_phone_List.add(manager_phone);
		MAMPU = MAMPU_et.getText().toString();
		MAMPU_List.add(MAMPU);
		String pl_name = pl_name_et.getText().toString();
		pl_name_List.add(pl_name);
		String pl_section = pl_section_et.getText().toString();
		pl_section_List.add(pl_section);
		String HC_numb = HC_numb_et.getText().toString();
		HC_numb_List.add(HC_numb);
		String HC_start = HC_start_et.getText().toString();
		HC_start_List.add(HC_start);
		String HC_end = HC_end_et.getText().toString();
		HC_end_List.add(HC_end);
		String HC_long = HC_long_et.getText().toString();
		HC_long_List.add(HC_long);
		String address = address_et.getText().toString();
		address_List.add(address);
		String HC_signalment = HC_signalment_et.getText().toString();
		HC_signalment_List.add(HC_signalment);
		String year = year_et.getText().toString();
		date_List.add(year);
		String remark = remark_et.getText().toString();
		remark_List.add(remark);
		filenames = High_DCA_Information_Create_NEXT_FileDialog.paramssss == null ? ""
				: High_DCA_Information_Create_NEXT_FileDialog.paramssss;
		filename_List.add(filenames + "");

	}

	public void choonsefilelistner(View V) {

		Intent intent = new Intent(getApplicationContext(),
				High_DCA_Information_Create_NEXT_FileDialog.class);
		startActivity(intent);
	}

	public void savelistner(View V) {
		MAMPU = MAMPU_et.getText().toString();
		if (manager_List.size() == 0 && StringUtils.isEmpty(MAMPU)) {
			Toast.makeText(getApplicationContext(), "管理单位不能为空，不能保存", 1000)
					.show();
		} else {
			if (flag == 0) {
				Getcontent();
			}
			flag++;

			dialog = new LodingActivtyDialog(
					High_DCA_Information_Create_NEXT.this);
			dialog.show();
			dialog.setCanceledOnTouchOutside(false);
			SaveHttpTask task = new SaveHttpTask();
			task.execute();

		}
	}

	private class SaveHttpTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			List<List<String>> listall = new ArrayList<List<String>>();
			List<String> keys = new ArrayList<String>();
			Log.v("paramss", "paramss========" + paramss);
			listall.add(manager_List);
			keys.add("recogner");
			listall.add(manager_phone_List);
			keys.add("recogner_tel");
			listall.add(MAMPU_List);
			keys.add("unit_name");
			listall.add(pl_name_List);
			keys.add("pipeline_name");
			listall.add(pl_section_List);
			keys.add("section_name");
			listall.add(HC_numb_List);
			keys.add("num");
			listall.add(HC_start_List);
			keys.add("s_start");
			listall.add(HC_end_List);
			keys.add("s_end");
			listall.add(HC_long_List);
			keys.add("s_length");
			listall.add(address_List);
			keys.add("place_name");
			listall.add(HC_signalment_List);
			keys.add("description");
			listall.add(remark_List);
			keys.add("remark");
			listall.add(date_List);
			keys.add("u_date");
			listall.add(filename_List);
			keys.add("annex_file");
			Log.e("listall.size()", "llllllllllllllllll" + listall.size());
			Log.e("keys.size()", "llllllllllllllllll" + keys.size());
			// Log.v("================", filename_List.get(0));
			paramss += ParamsUtil.listToParams(listall, keys);
			Log.e("____________________________________________", paramss);
			HttpRequestUtil httpRequestUtil = new HttpRequestUtil();
			String json = httpRequestUtil.PostHttp(Urls.URL
					+ "services/base/d_sequel/save", paramss,
					(OilApplication) getApplication());
			Log.d("____________________________________________", json);
			try {
				JSONObject jsonObject = new JSONObject(json);
				resultstatus = jsonObject.getString("status");
				resultstring = jsonObject.getString("message");

			} catch (JSONException e) {
				resultstatus = "202";
				resultstring = "保存失败";
				e.printStackTrace();
			}

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			if (resultstatus.equals("200")) {
				Toast.makeText(getApplicationContext(), "保存成功", 1).show();
				dialog.cancel();
				Intent intent = new Intent();
				intent.setAction("actionCreate");
				intent.putExtra("isRefresh", true);
				intent.putExtra("which", true);
				sendBroadcast(intent);
				finish();
			} else if (resultstatus.equals("") || resultstatus.equals(null)) {
				dialog.cancel();
				Toast.makeText(getApplicationContext(), "网络出错,请检查您的网络后再保存", 1)
						.show();
			} else {
				dialog.cancel();
				Toast.makeText(getApplicationContext(), resultstring, 1).show();
			}

			super.onPostExecute(result);
		}

	}

}
