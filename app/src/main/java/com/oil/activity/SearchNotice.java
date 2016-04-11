package com.oil.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.oil.dialog.CustomerDatePickerDialog;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchNotice extends Activity {
	private ArrayAdapter sAdapter, authAdapter;
	private Spinner statusSp, sp_uid;
	private String[] status = {"全部","正常","关闭"};
	private String[] verify_status = {"全部","未审核","通过","不通过"};
	private String[] authors = {"全部"};
	private int[] uids = {0};
	private Button searchBtn,resetBtn;
	private TextView start_date, end_date;
	private EditText searchKey;
	private DatePickerDialog mDialog;
	private int searchType;
	private int year, month,day;
	private int close;
	private int uid;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private Handler hand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			authAdapter = new ArrayAdapter<String>(SearchNotice.this,
					R.layout.spinner_style,authors);
			authAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
			sp_uid.setAdapter(authAdapter);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_notice);
		searchBtn = (Button) findViewById(R.id.search_btn);
		resetBtn = (Button) findViewById(R.id.reset_btn);
		searchKey = (EditText) findViewById(R.id.search_key);
		start_date = (TextView) findViewById(R.id.start_date);
		end_date = (TextView) findViewById(R.id.end_date);
		statusSp = (Spinner) findViewById(R.id.sp_status);
		sp_uid = (Spinner) findViewById(R.id.sp_uid);

		searchType = getIntent().getIntExtra("actionSearchKey", 1);
		
		if(searchType == 1) {
			sAdapter = new ArrayAdapter<String>(SearchNotice.this,
				R.layout.spinner_style,status);
		} else {
			sAdapter = new ArrayAdapter<String>(SearchNotice.this,
					R.layout.spinner_style,verify_status);
		}
		authAdapter = new ArrayAdapter<String>(SearchNotice.this,
				R.layout.spinner_style,authors);
		sAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
		authAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
		statusSp.setAdapter(sAdapter);
		sp_uid.setAdapter(authAdapter);

		statusSp.setSelection(0);
		
		System.out.println("searchType===========" + searchType);

		new GetAuthors().start();
		findViewById(R.id.bt_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
		
		start_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog = new CustomerDatePickerDialog(SearchNotice.this,new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						start_date.setText(formatDate(3,arg1,arg2,arg3));
					}
				}, year, month, day);
				mDialog.show();
			}
		});
		
		end_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog = new CustomerDatePickerDialog(SearchNotice.this,new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						end_date.setText(formatDate(3,arg1,arg2,arg3));
					}
				}, year, month, day);
				mDialog.show();
			}
		});
		
		statusSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(searchType == 1) {
					if("全部".equals(arg0.getItemAtPosition(arg2).toString())){
						close = -1;
					}else if("正常".equals(arg0.getItemAtPosition(arg2).toString())){
						close = 1;
					}else if("关闭".equals(arg0.getItemAtPosition(arg2).toString())){
						close = 0;
					}
				} else {
					if("未审核".equals(arg0.getItemAtPosition(arg2).toString())){
						close = 0;
					}else if("通过".equals(arg0.getItemAtPosition(arg2).toString())){
						close = 1;
					}else if("不通过".equals(arg0.getItemAtPosition(arg2).toString())){
						close = -1;
					} else {
						close = -2;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sp_uid.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				System.out.println("==========================" +arg2);
				uid = uids[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String f_time = start_date.getText().toString();
				String t_time = end_date.getText().toString();
				StringBuffer searchData = new StringBuffer();

				if(!StringUtil.isBlank(searchKey.getText().toString())){
					searchData.append("key_w=" + searchKey.getText().toString());				
				}
				try {
					if(!StringUtil.isBlank(f_time) && StringUtil.isBlank(t_time)) {
						Toast.makeText(getApplicationContext(), "请选择结束时间", Toast.LENGTH_SHORT).show();
						return;
					}
					if(StringUtil.isBlank(f_time) && !StringUtil.isBlank(t_time)) {
						Toast.makeText(getApplicationContext(), "请选择开始时间", Toast.LENGTH_SHORT).show();
						return;
					}
					if(sdf.parse(f_time).getTime() > sdf.parse(t_time).getTime()) {
						Toast.makeText(getApplicationContext(), "请选择正确的开始时间与结束时间", Toast.LENGTH_SHORT).show();
						return;
					} else {
						if(searchData.length() > 0) {
							searchData.append("&");
						}
						searchData.append("f_time=" + f_time + "&t_time=" + t_time);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(searchType == 1) {
					if(close >= 0){
						searchData.append("&close=" + close);
					}
				} else {
					if(close >= -1) {
						searchData.append("&verify_status=" + close);
					}
				}
				if(uid > 0) {
					searchData.append("&uid=" + uid);
				}

				Intent intent = new Intent();
				intent.setAction("actionSearchKey");
				intent.putExtra("searchKey",searchType);
				intent.putExtra("isSearch",true);
				System.out.println(searchData.toString());
				intent.putExtra("searchData",searchData.toString());
				sendBroadcast(intent);
				finish();
			}
		});
		
		resetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				searchKey.setText("");
				start_date.setText("");
				end_date.setText("");
			}
		});
	}
	
	private String formatDate(int dateType2,int years,int months,int days) {
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(years+"");
		 if(dateType2 == 2||dateType2 == 3){
			if (months < 9) {
				sbDate.append("-0"+(months + 1));
			} else {
				sbDate.append("-" + (months + 1));
			}
		 }
		if(dateType2 == 3){
			if (days < 10) {
				sbDate.append("-0" + days);
			}else {
				sbDate.append("-" + days);
			}
		}
		return sbDate.toString();
	}
	
	public class GetAuthors extends Thread {
		@Override
		public void run() {
			try{
				int type = 2;
				if(searchType == 1) {
					type = 2;
				} else {
					type = 3;
				}
				System.out.println(Urls.GETAUTH + "?type=" + type);
				System.out.println("==============================");
				String res = HttpRequestUtil.getDataFromServer(Urls.GETAUTH + "?type=" + type);
				JSONObject json = new JSONObject(res);
				JSONArray authorArr = json.getJSONArray("data");
				authors = new String[authorArr.length()+1];
				uids = new int[authorArr.length()+1];
				uids[0] = 0;
				authors[0] = "全部";
				for(int i = 1; i <= authorArr.length(); i++) {
					authors[i] = authorArr.getJSONObject(i-1).getString("name");
					uids[i] = authorArr.getJSONObject(i-1).getInt("id");
				}
				hand.sendMessage(hand.obtainMessage());
				System.out.println("发布人" + res);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
