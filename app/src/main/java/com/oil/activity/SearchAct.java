package com.oil.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.oil.dialog.CustomerDatePickerDialog;
import com.oil.domain.GetIdName;
import com.oil.utils.AdapterUtil;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.JSONParse;
import com.oil.utils.Urls;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class SearchAct extends Activity {
	
	private Spinner plSp,secSp,speSp,statusSp;
	private ArrayAdapter<String> sAdapter;
	private List<GetIdName> listPl;
	private List<GetIdName> listSec;
	private List<GetIdName> listSpec;
	private String[] namePl, nameSec, nameSpec;
	private int positionId, sectionId, specId,statusId;
	private Thread getThread;
	private TextView tvDate;
	private Button searchBtn,resetBtn;
	private int year,month,day,dateType;
	private DatePickerDialog mDialog;
	private int type;
	private StringBuffer searchData = new StringBuffer();
	private String[] status = {"全部状态","通过","未审核","不通过"};
	
	private Handler sHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					listPl.addAll(JSONParse.parseIdName(msg.obj.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				namePl = new String[listPl.size()];
				namePl = FilterUtil.filterName(listPl);
				sAdapter = new ArrayAdapter<String>(SearchAct.this,
						R.layout.spinner_style, namePl);
				sAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
				plSp.setAdapter(sAdapter);
			} else if (msg.what == 2) {
				try {
					listSec.addAll(JSONParse.parseIdName(msg.obj.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				nameSec = new String[listSec.size()];
				nameSec = FilterUtil.filterName(listSec);
				sAdapter = new ArrayAdapter<String>(SearchAct.this,
						R.layout.spinner_style, nameSec);
				sAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
				secSp.setAdapter(sAdapter);
			} else if (msg.what == 3) {
				try {
					listSpec.addAll(JSONParse.parseIdName(msg.obj.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				nameSpec = new String[listSpec.size()];
				nameSpec = FilterUtil.filterName(listSpec);
				sAdapter = new ArrayAdapter<String>(SearchAct.this,
						R.layout.spinner_style, nameSpec);
				sAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
				speSp.setAdapter(sAdapter);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		plSp = (Spinner) findViewById(R.id.sp_pl);
		secSp = (Spinner) findViewById(R.id.sp_section);
		speSp = (Spinner) findViewById(R.id.sp_spec);
		statusSp = (Spinner) findViewById(R.id.sp_status);
		tvDate = (TextView) findViewById(R.id.tv_date);
		searchBtn = (Button) findViewById(R.id.search_btn);
		resetBtn = (Button) findViewById(R.id.reset_btn);
		
		sAdapter = new ArrayAdapter<String>(SearchAct.this,
				R.layout.spinner_style,status);
		sAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
		statusSp.setAdapter(sAdapter);
		
		findViewById(R.id.bt_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if(positionId != 0){
					searchData.append("pl="+positionId);
					if(sectionId != 0){
						searchData.append("&section="+sectionId);
						if(specId != 0){
							searchData.append("&spec="+specId);
						}
					}
				}
				
				if(!"".equals(tvDate.getText().toString())){
					if(positionId != 0){
						searchData.append("&");
					}
					if(dateType == 1){
						searchData.append("m_year="+tvDate.getText().toString());
					}else if(dateType == 2){
						if(type == 1||type == 2){
							searchData.append("c_month="+tvDate.getText().toString());
						}else if(type == 7){
							searchData.append("p_month="+tvDate.getText().toString());
						}else{
							searchData.append("r_month="+tvDate.getText().toString());
						}
					}else{
						if(type == 6){
							searchData.append("create_date="+tvDate.getText().toString());
						}else if(type == 10){
							searchData.append("create_time="+tvDate.getText().toString());
						}else{
							searchData.append("check_date="+tvDate.getText().toString());
						}
					}
				}
				if(statusId!=200){
					if(!"".equals(tvDate.getText().toString())||positionId != 0){
						searchData.append("&");
					}
					searchData.append("status="+statusId);
				}
				intent.putExtra("searchData",searchData.toString());
				Log.v("Tag","searchdata="+searchData.toString());
				setResult(20,intent);
				finish();
			}
		});
		
		resetBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				plSp.setSelection(0);
				secSp.setSelection(0);
				speSp.setSelection(0);
				statusSp.setSelection(0);
				tvDate.setText("");
			}
		});
		
		startSpinnerThread(Urls.URL + "services/base/pipeline/get", 1);

		plSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				for (int i = 0; i < listPl.size(); i++) {
					if (listPl.get(i).getName()
							.equals(arg0.getItemAtPosition(arg2).toString())) {
						positionId = listPl.get(i).get_id();
						break;
					}
				}
				startSpinnerThread(Urls.URL
						+ "services/base/section/get?pl_id=" + positionId, 2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		secSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				for (int i = 0; i < listSec.size(); i++) {
					if (listSec.get(i).getName()
							.equals(arg0.getItemAtPosition(arg2).toString())) {
						sectionId = listSec.get(i).get_id();
						break;
					}
				}
				startSpinnerThread(Urls.URL
						+ "services/base/spec/get?pl_section_id=" + sectionId,
						3);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		speSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				for (int i = 0; i < listSpec.size(); i++) {
					if (listSpec.get(i).getName()
							.equals(arg0.getItemAtPosition(arg2).toString())) {
						specId = listSec.get(i).get_id();
						break;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		statusSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if("通过".equals(arg0.getItemAtPosition(arg2).toString())){
					statusId = 1;
				}else if("未审核".equals(arg0.getItemAtPosition(arg2).toString())){
					statusId = 0;
				}else if("不通过".equals(arg0.getItemAtPosition(arg2).toString())){
					statusId = -1;
				}else{
					statusId = 200;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
		
		dateType = getIntent().getIntExtra("dateType",1);
		type = getIntent().getIntExtra("type",1);
		
		tvDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog = new CustomerDatePickerDialog(SearchAct.this,new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						tvDate.setText(formatDate(dateType,arg1,arg2,arg3));
					}
				}, year, month, day);
				mDialog.show();
				
				if(dateType!=3){
					DatePicker dp = CustomerDatePickerDialog
							.findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());
					if (dp != null) {
						if(dateType == 2){
							((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
							.getChildAt(2).setVisibility(View.GONE);
						}else{
							((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
								.getChildAt(2).setVisibility(View.GONE);
							((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
								.getChildAt(1).setVisibility(View.GONE);
						}
					}
				}
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

	protected void startSpinnerThread(final String url, final int msgWhat) {
		if(msgWhat == 1){
			listPl = new ArrayList<GetIdName>();
			GetIdName namePL = new GetIdName();
			namePL.set_id(0);
			namePL.setName("全部管线");
			listPl.add(namePL);
			AdapterUtil.updateSearchAdapter(plSp,1,sAdapter,SearchAct.this);
		}else if(msgWhat == 2){
			listSec = new ArrayList<GetIdName>();
			GetIdName nameSE = new GetIdName();
			nameSE.set_id(0);
			nameSE.setName("全部管段");
			listSec.add(nameSE);
			AdapterUtil.updateSearchAdapter(secSp,2,sAdapter,SearchAct.this);
		}else if(msgWhat == 3){
			listSpec = new ArrayList<GetIdName>();
			GetIdName nameSP = new GetIdName();
			nameSP.set_id(0);
			nameSP.setName("全部规格");
			listSpec.add(nameSP);
			AdapterUtil.updateSearchAdapter(speSp,3,sAdapter,SearchAct.this);
		}
		getThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = sHandler.obtainMessage();
				msg.obj = HttpGetDataByGet.getDataFromServer(url);
				msg.what = msgWhat;
				sHandler.sendMessage(msg);
			}
		});
		getThread.start();
	}
	
}
