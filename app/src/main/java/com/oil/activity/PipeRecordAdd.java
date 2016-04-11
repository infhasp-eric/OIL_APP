package com.oil.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.domain.PipeMeasure;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

public class PipeRecordAdd extends Activity {

	private EditText no, unit, deadline, joint, cathA, cathV, potential,
			examiner, remark;
	private TextView date;
	private String[] trans = new String[5];
	private List<String> list = new ArrayList<String>();
	private PipeMeasure measure;
	private int year, month, day;
	private ProgressDialog dialog = null;
	private ImageView imageView;
	int i = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			dialog.dismiss();
			try {
				if("200".equals(new JSONObject(msg.obj.toString()).getString("status"))){
					Toast.makeText(
							getApplicationContext(),
							new JSONObject(msg.obj.toString()).getString("message"),
							Toast.LENGTH_SHORT).show();
					finish();
					RecordCreate.instance.finish();
				}
			} catch (JSONException e) {
				if(!StringUtil.isBlank(msg.obj.toString())) {
				Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),"发生未知错误",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			Log.e("Tag", msg.obj.toString());
		}
	};

	private Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				findViewById(R.id.pipe_table_a).setVisibility(View.GONE);
				findViewById(R.id.pipe_table_v).setVisibility(View.GONE);
				
				imageView.setImageResource(R.drawable.oo);
			}
			else if (msg.what == 1) {
				findViewById(R.id.pipe_table_a).setVisibility(View.VISIBLE);
				findViewById(R.id.pipe_table_v).setVisibility(View.VISIBLE);
				imageView.setImageResource(R.drawable.oodown);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pipe_record_add);
		imageView = (ImageView) findViewById(R.id.img);
		imageView.setImageResource(R.drawable.oo);
		Intent intent = getIntent();
		trans = intent.getBundleExtra("bd").getStringArray("trans");

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		initTextView();

		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天

		date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpd = new DatePickerDialog(PipeRecordAdd.this,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								year = arg1;
								month = arg2;
								day = arg3;
								if(month < 9&&day < 10){
									date.setText(year+ "-0"+(month + 1) + "-0" + day);
						        }else if(month < 9){
						        	date.setText(year+ "-0"+(month + 1) + "-" + day);
						        }else if(day < 10){
						        	date.setText(year+ "-"+(month + 1) + "-0" + day);
						        }else{
						        	date.setText(year+ "-"+(month + 1) + "-" + day);
						        }
							}
						}, year, month, day);
				dpd.show();
			}
		});
		findViewById(R.id.pipe_table).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Thread thread = new Thread() {
					@Override
					public void run() {
						i++;
						if(i>1){
							i=0;
						}
						Message message = new Message();
						message.what = i;
						hand.sendMessage(message);
						super.run();
					}
				};
				thread.start();
			}
		});
		findViewById(R.id.pipe_add_back_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});

		findViewById(R.id.pipe_add_continue_add).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (!StringUtils.isEmpty(no.getText().toString())
								&& !StringUtils.isEmpty(unit.getText()
										.toString())
								&& !StringUtils.isEmpty(deadline.getText()
										.toString())) {
							list.add(no.getText().toString());
							list.add(date.getText().toString());
							list.add(joint.getText().toString());
							list.add(cathA.getText().toString());
							list.add(cathV.getText().toString());
							list.add(potential.getText().toString());
							list.add(examiner.getText().toString());
							list.add(remark.getText().toString());
							joint.setText("");
							no.setText("");
							date.setText("");
							joint.setText("");
							cathA.setText("");
							cathV.setText("");
							potential.setText("");
							examiner.setText("");
							remark.setText("");
							findViewById(R.id.pipe_table_a).setVisibility(
									View.GONE);
							findViewById(R.id.pipe_table_v).setVisibility(
									View.GONE);
							imageView.setImageResource(R.drawable.oo);

						} else {
							Toast.makeText(getApplicationContext(),
									"测试桩号，保存期限，保存单位为必填项", 1).show();
						}
					}
				});

		findViewById(R.id.pipe_add_save).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (!StringUtils.isEmpty(no.getText().toString())
								&& !StringUtils.isEmpty(unit.getText()
										.toString())
								&& !StringUtils.isEmpty(deadline.getText()
										.toString())) {
							dialog = new ProgressDialog(PipeRecordAdd.this);
							dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							dialog.setMessage("数据上传中，请稍后...");
							dialog.show();
							dialog.setCancelable(false);
							measure = new PipeMeasure();
							list.add(no.getText().toString());
							list.add(date.getText().toString());
							list.add(joint.getText().toString());
							list.add(cathA.getText().toString());
							list.add(cathV.getText().toString());
							list.add(potential.getText().toString());
							list.add(examiner.getText().toString());
							list.add(remark.getText().toString());
							measure.setPl(trans[0]);
							measure.setSection(trans[1]);
							measure.setSpec(trans[2]);
							measure.setJinzhan(trans[3]);
							measure.setcMonth(trans[4]);
							measure.setUnit(unit.getText().toString());
							measure.setSaveDate(deadline.getText().toString());
							String[] noStr = new String[list.size() / 8];
							String[] dateStr = new String[list.size() / 8];
							String[] jointStr = new String[list.size() / 8];
							String[] cathAStr = new String[list.size() / 8];
							String[] cathVStr = new String[list.size() / 8];
							String[] potStr = new String[list.size() / 8];
							String[] exaStr = new String[list.size() / 8];
							String[] remStr = new String[list.size() / 8];
							for (int i = 0, j = 0; i < list.size(); i += 8, j++) {
								noStr[j] = list.get(i);
								dateStr[j] = list.get(i + 1);
								jointStr[j] = list.get(i + 2);
								cathAStr[j] = list.get(i + 3);
								cathVStr[j] = list.get(i + 4);
								potStr[j] = list.get(i + 5);
								exaStr[j] = list.get(i + 6);
								remStr[j] = list.get(i + 7);
							}
							measure.setNo(noStr);
							measure.setmDate(dateStr);
							measure.setPotential(jointStr);
							measure.setA(cathAStr);
							measure.setV(cathVStr);
							measure.setTongdian(potStr);
							measure.setMeasurer(exaStr);
							measure.setRemark(remStr);
							startSubThread(measure);
						} else {
							Toast.makeText(getApplicationContext(),
									"请填写测试桩号，保存期限，保存单位", 1).show();
						}
					}

				});
	}

	protected void startSubThread(final PipeMeasure me) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = HttpRequestUtil.PostHttp(Urls.PPPMRS, me.toString(),
						(OilApplication) getApplication());
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void initTextView() {
		no = (EditText) findViewById(R.id.test_number);
		date = (TextView) findViewById(R.id.test_date);
		unit = (EditText) findViewById(R.id.save_unit);
		deadline = (EditText) findViewById(R.id.save_deadline);
		joint = (EditText) findViewById(R.id.potential_v);
		cathA = (EditText) findViewById(R.id.potential_protection_a);
		cathV = (EditText) findViewById(R.id.potential_protection_v);
		potential = (EditText) findViewById(R.id.power_on_dot);
		examiner = (EditText) findViewById(R.id.survey_crew);
		remark = (EditText) findViewById(R.id.remark);
	}
}
