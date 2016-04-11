package com.oil.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.dialog.CustomerDatePickerDialog;
import com.oil.domain.GetIdName;
import com.oil.utils.AdapterUtil;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.JSONParse;
import com.oil.utils.StringUtils;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class RecordCreate extends Activity {

	private Spinner plSpinner, secSpinner, specSpinner;
	private EditText station,protectStation;
	TextView recordTime;
	private TextView title;
	private TableRow protectRow;
	private Thread spinnerThread;
	private List<GetIdName> listPl = new ArrayList<GetIdName>();
	private List<GetIdName> listSec = new ArrayList<GetIdName>();
	private List<GetIdName> listSpec = new ArrayList<GetIdName>();
	private String[] namePl, nameSec, nameSpec;
	private ArrayAdapter<String> adapter;
	private int positionId, sectionId, specId;
	private String[] trans = new String[6];
	private int judge;
	private ProgressDialog dialog = null;
	private int year, month, day;
	private String yearAndmonth = "";
	private DatePickerDialog mDialog;
	private boolean checkSP;

	public static RecordCreate instance = null;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					listPl = JSONParse.parseIdName(msg.obj.toString());
					namePl = new String[listPl.size()];
					namePl = FilterUtil.filterName(listPl);
					adapter = new ArrayAdapter<String>(RecordCreate.this,
							R.layout.spinner_style, namePl);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					plSpinner.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (msg.what == 2) {
				try {
					listSec = JSONParse.parseIdName(msg.obj.toString());
					nameSec = new String[listSec.size()];
					nameSec = FilterUtil.filterName(listSec);
					adapter = new ArrayAdapter<String>(RecordCreate.this,
							R.layout.spinner_style, nameSec);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					secSpinner.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (msg.what == 3) {
				try {
					listSpec = JSONParse.parseIdName(msg.obj.toString());
					nameSpec = new String[listSpec.size()];
					nameSpec = FilterUtil.filterName(listSpec);
					adapter = new ArrayAdapter<String>(RecordCreate.this,
							R.layout.spinner_style, nameSpec);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					specSpinner.setAdapter(adapter);
					dialog.dismiss();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					finish();
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pipe_record_create);
		instance = this;
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.setCancelable(false);
		dialog.show();
		title = (TextView) findViewById(R.id.name_title);
		protectRow = (TableRow) findViewById(R.id.protect_row);
		protectStation = (EditText) findViewById(R.id.pipe_polling_protect_well);

		Intent intent = getIntent();
		judge = intent.getIntExtra("judge", 3);
		if (judge == 1) {
			title.setText("管道保护电位测量记录");
			protectRow.setVisibility(View.GONE);
		} else if (judge == 2) {
			title.setText("阴极保护站运行月综合记录");
			protectRow.setVisibility(View.GONE);
		} else {
			title.setText("管线巡检工作记录");
			protectRow.setVisibility(View.VISIBLE);
		}

		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					RecordCreate.this.finish();
				}
				return false;
			}
		});

		findViewById(R.id.back_create).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});

		station = (EditText) findViewById(R.id.well_create);
		recordTime = (TextView) findViewById(R.id.record_create);

		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		String monthsss;
		if ((month + 1) < 10) {
			monthsss = "0" + (month + 1);
		} else {
			monthsss = month + "";
		}
		recordTime.setText(year + "-" + monthsss);
		yearAndmonth = year + "-" + monthsss;

		recordTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Calendar cal = Calendar.getInstance();
				mDialog = new CustomerDatePickerDialog(RecordCreate.this,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								int year = arg1;
								int month = arg2;
								String months = null;
								if ((month + 1) < 10) {
									months = "0" + (month + 1);
								} else {
									months = month + "";
								}
								recordTime.setText(year + "-" + months);
								yearAndmonth = year + "-" + months;
							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
								.get(Calendar.DAY_OF_MONTH));
				mDialog.show();

				DatePicker dp = CustomerDatePickerDialog
						.findDatePicker((ViewGroup) mDialog.getWindow()
								.getDecorView());
				if (dp != null) {
					((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
							.getChildAt(2).setVisibility(View.GONE);
				}
			}
		});

		findViewById(R.id.next_create).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 创建数据未填写判断
						if(spinnerThread != null && spinnerThread.isAlive()) {
							Toast.makeText(getApplicationContext(), "正在加载，请稍等...", 200).show();
							return;
						}
						if(!checkSP){
							Toast.makeText(getApplicationContext(),"请检查管线名称，起止段落，规格是否有空",Toast.LENGTH_SHORT).show();
							return;
						}
						if(judge == 3){
							if (StringUtils.isEmpty(protectStation.getText().toString())){
								Toast.makeText(getApplicationContext(),"请填写保护井(站)",Toast.LENGTH_SHORT).show();
								return;
							}
						}
						if (!StringUtils.isEmpty(station.getText().toString())) {
							trans[3] = station.getText().toString();
							trans[4] = yearAndmonth;
							if(judge == 3){
								trans[5] = protectStation.getText().toString();
							}
							Intent intent1 = new Intent();
							if (judge == 1) {
								intent1.setClass(RecordCreate.this,
										PipeRecordAdd.class);
							} else if (judge == 2) {
								intent1.setClass(RecordCreate.this,
										CathodeProtectionAdd.class);
							} else {
								intent1.setClass(RecordCreate.this,
										PipePollingAdd.class);
							}
							Bundle bundle = new Bundle();
							System.out.println("============================");
							for(String s : trans) {
								System.out.print(s+"___");
							}
							System.out.println("============================");
							bundle.putStringArray("trans", trans);
							intent1.putExtra("bd", bundle);
							startActivity(intent1);
						} else {
							Toast.makeText(getApplicationContext(), "请填写井站",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		plSpinner = (Spinner) findViewById(R.id.pipe_create);
		secSpinner = (Spinner) findViewById(R.id.para_create);
		specSpinner = (Spinner) findViewById(R.id.spec_create);

		startSubThread(Urls.PL, 1);
		AdapterUtil.updateAdapter(plSpinner, adapter, instance);
		AdapterUtil.updateAdapter(secSpinner, adapter, instance);
		AdapterUtil.updateAdapter(specSpinner, adapter, instance);

		plSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(secSpinner, adapter, instance);
				AdapterUtil.updateAdapter(specSpinner, adapter, instance);
				try {
					for (int i = 0; i < listPl.size(); i++) {
						if (listPl.get(i).getName().equals(parent.getItemAtPosition(position)
										.toString())) {
							positionId = listPl.get(i).get_id();
							trans[0] = positionId + "";
							startSubThread(Urls.SECTION + positionId, 2);
							break;
						}else{
							
						}
					}
				} catch (Exception e) {
					dialog.dismiss();
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					finish();
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		secSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(specSpinner, adapter, instance);
				try {
					for (int i = 0; i < listSec.size(); i++) {
						if (listSec
								.get(i)
								.getName()
								.equals(parent.getItemAtPosition(position)
										.toString())) {
							sectionId = listSec.get(i).get_id();
							trans[1] = sectionId + "";
							startSubThread(Urls.SPEC + sectionId, 3);
							break;
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		specSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					for (int i = 0; i < listSpec.size(); i++) {
						if (listSpec.get(i).getName().equals(parent.getItemAtPosition(position).toString())) {
							specId = listSec.get(i).get_id();
							trans[2] = specId + "";
							checkSP =true;
						}else{
							checkSP = false;
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		plSpinner.setVisibility(View.VISIBLE);
		secSpinner.setVisibility(View.VISIBLE);
		specSpinner.setVisibility(View.VISIBLE);
	}

	private void startSubThread(final String url, final int msgWhat) {
		spinnerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = HttpGetDataByGet.getDataFromServer(url,
						((OilApplication) getApplication()).getJSESSIONID());
				msg.what = msgWhat;
				handler.sendMessage(msg);
			}
		});
		spinnerThread.start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ThreadKILL.killthread(spinnerThread);
	}
}
