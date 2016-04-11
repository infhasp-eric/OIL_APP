package com.oil.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.oil.dialog.CustomerDatePickerDialog;
import com.oil.utils.StringUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchKeyAct extends Activity {
	private Button searchBtn,resetBtn;
	private TextView start_date, end_date;
	private EditText searchKey;
	private DatePickerDialog mDialog;
	private int searchType;
	private int year, month,day;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_key_act);
		searchBtn = (Button) findViewById(R.id.search_btn);
		resetBtn = (Button) findViewById(R.id.reset_btn);
		searchKey = (EditText) findViewById(R.id.search_key);
		start_date = (TextView) findViewById(R.id.start_date);
		end_date = (TextView) findViewById(R.id.end_date);
		
		searchType = getIntent().getIntExtra("actionSearchKey", 1);
		
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
				mDialog = new CustomerDatePickerDialog(SearchKeyAct.this,new OnDateSetListener() {
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
				mDialog = new CustomerDatePickerDialog(SearchKeyAct.this,new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						end_date.setText(formatDate(3,arg1,arg2,arg3));
					}
				}, year, month, day);
				mDialog.show();
			}
		});

		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String f_time = start_date.getText().toString();
				String t_time = end_date.getText().toString();
				StringBuffer searchData = new StringBuffer();

				if(!StringUtil.isBlank(searchKey.getText().toString())){
					if(searchType == 3){
						searchData.append("s_title=" + searchKey.getText().toString());
					}
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

				Intent intent = new Intent();
				intent.setAction("actionSearchKey");
				intent.putExtra("searchKey",searchType);
				intent.putExtra("isSearch",true);
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
}
