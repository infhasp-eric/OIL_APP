package com.oil.activity;

import java.text.SimpleDateFormat;
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
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.domain.GetIdName;
import com.oil.domain.Itco_create_main_Bean;
import com.oil.utils.AdapterUtil;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.JSONParse;
import com.oil.utils.StringUtil;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class Itco_create_main extends FinalActivity {
	@ViewInject(id = R.id.itco_create_main_back_btn, click = "backlistener")
	Button back;
	@ViewInject(id = R.id.itco_create_main_bt_next, click = "nextlistener")
	Button next;
	@ViewInject(id = R.id.itco_create_main_sp_pl)
	Spinner sp_pl;
	@ViewInject(id = R.id.itco_create_main_sp_section)
	Spinner sp_section;
	@ViewInject(id = R.id.itco_create_main_sp_spec)
	Spinner sp_spec;
	@ViewInject(id = R.id.txt_discover_date) TextView day_et;
public static Itco_create_main icm;
	

	private List<GetIdName> listPl = new ArrayList<GetIdName>();
	private List<GetIdName> listSec = new ArrayList<GetIdName>();
	private List<GetIdName> listSpec = new ArrayList<GetIdName>();
	private String[] namePl, nameSec, nameSpec;
	private ArrayAdapter<String> adapter;
	private ProgressDialog dialog = null;
	private Integer positionId, sectionId, specId;
	private Integer pl_id;
	private Integer section_id;
	private Integer spec_id;
	private int year, month, day;
	private Thread thread;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				listPl = JSONParse.parseIdName(msg.obj.toString());
				if (listPl != null) {
					namePl = new String[listPl.size()];
					namePl = FilterUtil.filterName(listPl);
					adapter = new ArrayAdapter<String>(Itco_create_main.this,
							R.layout.spinner_style, namePl);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_pl.setAdapter(adapter);
				}
			} else if (msg.what == 2) {
				System.out.println(msg.obj.toString());
				listSec = JSONParse.parseIdName(msg.obj.toString());
				if (listSec != null) {
					nameSec = new String[listSec.size()];
					nameSec = FilterUtil.filterName(listSec);
					adapter = new ArrayAdapter<String>(Itco_create_main.this,
							R.layout.spinner_style, nameSec);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_section.setAdapter(adapter);
				}
			} else if (msg.what == 3) {
				listSpec = JSONParse.parseIdName(msg.obj.toString());
				if (listSpec != null) {
					nameSpec = new String[listSpec.size()];
					nameSpec = FilterUtil.filterName(listSpec);
					adapter = new ArrayAdapter<String>(Itco_create_main.this,
							R.layout.spinner_style, nameSpec);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_spec.setAdapter(adapter);
				}
				dialog.dismiss();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.itco_create_main);
		icm=this;
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
					Itco_create_main.this.finish();
				}
				return false;
			}
		});
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
		day_et.setText(formatDate(year, month, day));

		day_et.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpd = new DatePickerDialog(
						Itco_create_main.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								year = arg1;
								month = arg2;
								day = arg3;
								String s_month = null;
								if (month < 10) {
									s_month = "0" + (month + 1);
								} else {
									s_month = "" + (month + 1);
								}
								String s_day = null;
								if (day < 10) {
									s_day = "0" + day;
								} else {
									s_day = "" + day;
								}

								day_et.setText(year + "-" + s_month + "-"
										+ s_day);
							}
						}, year, month, day);
				dpd.show();
			}
		});

		startSubThread(Urls.PL, 1);
		sp_pl.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(sp_section, adapter,
						Itco_create_main.this);
				AdapterUtil.updateAdapter(sp_spec, adapter,
						Itco_create_main.this);

				if (listPl != null) {
					for (int i = 0; i < listPl.size(); i++) {
						if (listPl
								.get(i)
								.getName()
								.equals(parent.getItemAtPosition(position)
										.toString())) {
							positionId = listPl.get(i).get_id();
						}
					}
					pl_id = positionId;
					startSubThread(Urls.SECTION + positionId, 2);
					sectionId = null;
					specId = null;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sp_section.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(sp_spec, adapter,
						Itco_create_main.this);
				if (listSec != null) {
					for (int i = 0; i < listSec.size(); i++) {
						if (listSec
								.get(i)
								.getName()
								.equals(parent.getItemAtPosition(position)
										.toString())) {
							sectionId = listSec.get(i).get_id();
						}
					}
					section_id = sectionId;
					startSubThread(Urls.SPEC + sectionId, 3);
					specId = null;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sp_spec.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (listSpec != null) {
					for (int i = 0; i < listSpec.size(); i++) {
						if (listSpec
								.get(i)
								.getName()
								.equals(parent.getItemAtPosition(position)
										.toString())) {
							specId = listSec.get(i).get_id();
						}
					}
					spec_id = specId;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	@Override
	protected void onStart() {
		startSubThread(Urls.PL, 1);
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		ThreadKILL.killthread(thread);

		super.onDestroy();
	}

	public void backlistener(View V) {
		finish();
	}

	public void nextlistener(View V) {
		String day = day_et.getText().toString();
		if (pl_id == null) {
			Toast.makeText(getApplicationContext(), "请输入管线名称", 1).show();
		} else if (section_id == null) {
			Toast.makeText(getApplicationContext(), "请输入起止段落", 1).show();

		} else if (spec_id == null) {
			Toast.makeText(getApplicationContext(), "请输入管线规格", 1).show();
		} else if(StringUtil.isBlank(day)) {
			Toast.makeText(getApplicationContext(), "请选择检查时间", 1).show();
		} else {
			Itco_create_main_Bean bean = new Itco_create_main_Bean();
			bean.setPl(pl_id);
			bean.setSection(section_id);
			bean.setSpec(spec_id);

			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			String params = bean.toString() + "&discover_date=" + day + " " + df.format(new Date());

			Log.e("params", "+++" + params);
			Bundle bundle = new Bundle();
			bundle.putString("params", params);
			bundle.putBoolean("Ismale", true);
			Intent intent = new Intent(Itco_create_main.this,Itco_create_Next.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	private void startSubThread(final String url, final int msgWhat) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = HttpGetDataByGet.getDataFromServer(url,
						((OilApplication) getApplication()).getJSESSIONID());
				msg.what = msgWhat;
				handler.sendMessage(msg);
			}
		});
		thread.start();
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
