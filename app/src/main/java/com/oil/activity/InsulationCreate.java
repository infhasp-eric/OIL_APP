package com.oil.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class InsulationCreate extends FinalActivity {
	@ViewInject(id = R.id.insulation_line)
	Spinner lineSp;
	@ViewInject(id = R.id.insulation_pra)
	Spinner praSp;
	@ViewInject(id = R.id.insulation_standard)
	Spinner standardSp;
	@ViewInject(id = R.id.insulation_well)
	EditText wellEt;
	@ViewInject(id = R.id.insulation_year, click = "choseDate")
	TextView yearEt;
	@ViewInject(id = R.id.insulation_edit)
	EditText editEt;
	@ViewInject(id = R.id.insulation_examine)
	EditText examineEt;
	@ViewInject(id = R.id.next_btn)
	Button nextBtn;
	@ViewInject(id = R.id.insulation_btn)
	Button backBtn;
	private List<GetIdName> list = new ArrayList<GetIdName>();
	private List<GetIdName> listS = new ArrayList<GetIdName>();
	private List<GetIdName> listSp = new ArrayList<GetIdName>();
	private String[] lineName, sectionName, specName;
	private ArrayAdapter<String> adapter;
	private int positionId, sectionId, specId;
	private String[] trans = new String[7];
	public static InsulationCreate ins;
	private ProgressDialog dialog = null;
	private DatePickerDialog mDialog;
	private String yearAndmonth = "";
	private Thread spinnerThread;
	private boolean checkSp;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				try {
					list = JSONParse.parseIdName(msg.obj.toString());
					lineName = new String[list.size()];
					lineName = FilterUtil.filterName(list);
					adapter = new ArrayAdapter<String>(InsulationCreate.this,
							R.layout.spinner_style, lineName);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					lineSp.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (msg.what == 2) {
				try {
					listS = JSONParse.parseIdName(msg.obj.toString());
					sectionName = new String[listS.size()];
					sectionName = FilterUtil.filterName(listS);
					adapter = new ArrayAdapter<String>(InsulationCreate.this,
							R.layout.spinner_style, sectionName);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					praSp.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (msg.what == 3) {
				try {
					listSp = JSONParse.parseIdName(msg.obj.toString());
					specName = new String[listSp.size()];
					specName = FilterUtil.filterName(listSp);
					adapter = new ArrayAdapter<String>(InsulationCreate.this,
							R.layout.spinner_style, specName);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					standardSp.setAdapter(adapter);
					dialog.dismiss();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.insulation_create);
		ins = this;
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.show();
		dialog.setCancelable(false);
		startSubThread(Urls.PL, 1);
		AdapterUtil.updateAdapter(lineSp, adapter, ins);
		AdapterUtil.updateAdapter(praSp, adapter, ins);
		AdapterUtil.updateAdapter(standardSp, adapter, ins);
		
		Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
        Date mydate=new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
        
		yearEt.setText(mycalendar.get(Calendar.YEAR)+"");
		yearAndmonth = mycalendar.get(Calendar.YEAR)+"";
		
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					InsulationCreate.this.finish();
				}
				return false;
			}
		});
		
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		lineSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(praSp, adapter, ins);
				AdapterUtil.updateAdapter(standardSp, adapter, ins);
				try {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i)
								.getName()
								.equals(parent.getItemAtPosition(position)
										.toString())) {
							positionId = list.get(i).get_id();
							trans[0] = positionId + "";
							startSubThread(Urls.SECTION + positionId, 2);
							break;
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					finish();
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		praSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(standardSp, adapter, ins);
				try {
					for (int i = 0; i < listS.size(); i++) {
						if (listS
								.get(i)
								.getName()
								.equals(parent.getItemAtPosition(position)
										.toString())) {
							sectionId = listS.get(i).get_id();
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
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		standardSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					for (int i = 0; i < listSp.size(); i++) {
						if (listSp
								.get(i)
								.getName()
								.equals(parent.getItemAtPosition(position)
										.toString())) {
							specId = listSp.get(i).get_id();
							trans[2] = specId + "";
							checkSp = true;
						}else{
							checkSp = false;
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		lineSp.setVisibility(View.VISIBLE);
		praSp.setVisibility(View.VISIBLE);
		standardSp.setVisibility(View.VISIBLE);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(spinnerThread != null && spinnerThread.isAlive()) {
					Toast.makeText(getApplicationContext(), "正在加载，请稍等...", 200).show();
					return;
				}
				if(!checkSp){
					Toast.makeText(getApplicationContext(),"请检查管线名称，起止段落，规格是否有空",Toast.LENGTH_SHORT).show();
					return;
				}
				
				trans[3] = wellEt.getText().toString();//井站
				if(trans[3].length() > 50 || trans[3].length() == 0) {
					Toast.makeText(getApplicationContext(),"井站长度不得为空且不能大于50",Toast.LENGTH_SHORT).show();
					return;
				}
				trans[4] = yearAndmonth;
				trans[5] = editEt.getText().toString();//填报人
				if(trans[5].length() > 10 || trans[5].length() == 0) {
					Toast.makeText(getApplicationContext(),"请填写填报人，且长度不能大于10",Toast.LENGTH_SHORT).show();
					return;
				}
				trans[6] = examineEt.getText().toString();//审核人
				if(trans[6].length() > 10 || trans[6].length() == 0) {
					Toast.makeText(getApplicationContext(),"请填写审核人，且长度不能大于10",Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringUtils.isEmpty(trans[3])
						|| StringUtils.isEmpty(trans[4])
						|| StringUtils.isEmpty(trans[5])
						|| StringUtils.isEmpty(trans[6])) {
					Toast.makeText(getApplicationContext(), "请填写完所有资料再进行下一步", 1).show();
				}else {
							
					/*Intent intent = new Intent(InsulationCreate.this,
							InsulationAdd.class);*/
					Intent intent = new Intent(InsulationCreate.this,
							InsulationElecActivity.class);
					Bundle bundle = new Bundle();
					bundle.putStringArray("proper", trans);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}
		});
	}

	private void startSubThread(final String str, final int i) {
		spinnerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = HttpGetDataByGet.getDataFromServer(str,
						((OilApplication) getApplication()).getJSESSIONID());
				msg.what = i;
				handler.sendMessage(msg);
			}
		});
		spinnerThread.start();
	}

	public void choseDate(View v) {
		final Calendar cal = Calendar.getInstance();
		mDialog = new CustomerDatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				int year = arg1;
				yearEt.setText(year + "");
				yearAndmonth = year + "";
			}
		}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
				.get(Calendar.DAY_OF_MONTH));
		mDialog.show();

		DatePicker dp = CustomerDatePickerDialog
				.findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());
		if (dp != null) {
			((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
					.getChildAt(2).setVisibility(View.GONE);
			((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
					.getChildAt(1).setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ThreadKILL.killthread(spinnerThread);
	}
}
