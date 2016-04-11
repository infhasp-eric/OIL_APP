package com.oil.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.domain.HighStatic;
import com.oil.domain.PictureList;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONParse;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

public class HighStaticCreateTwoAct extends Activity {

	private Intent intent;
	private List<String> list = new ArrayList<String>();
	private List<String> data = new ArrayList<String>();
	private TextView tvOne, tvTwo, tvThr, tvFou, tvFiv, tvTime;
	private HighStatic high;
	private PictureList pic = new PictureList();;
	private StringBuffer sb = new StringBuffer();
	private int year, month, day;
	private ProgressDialog dialog = null;
	private EditText rec, num, staLong, staLat, endLong, endLat, highSta,
			highEnd, highLen, highSco, plaName, feature;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.v("Tag", "msg = " + msg.obj.toString());
			try {
				Toast.makeText(getApplicationContext(),new JSONObject(msg.obj.toString()).getString("message"),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setAction("actionCreate");
				intent.putExtra("isRefresh",true);
				intent.putExtra("which",false);
				sendBroadcast(intent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dialog.dismiss();
			finish();
			HighStaticCreateOneAct.highStaticInstance.finish();
		}
	};
	
	private Handler tostHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			dialog.cancel();
			String message = msg.obj.toString();
			Toast.makeText(getApplicationContext(), message, 1).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_high_consquence_two);
		initWidget();
		intent = getIntent();
		sb.append(intent.getStringExtra("str"));

		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

		year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
		month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
		day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天

		tvTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpd = new DatePickerDialog(
						HighStaticCreateTwoAct.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								year = arg1;
								month = arg2;
								day = arg3;
								tvTime.setText(formatDate(year, month, day));
							}
						}, year, month, day);
				dpd.show();
			}
		});

		findViewById(R.id.high_create_back).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		tvOne.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(
									Intent.createChooser(intent, "请选择一个图片上传"),
									1);
						} catch (android.content.ActivityNotFoundException ex) {
							ex.printStackTrace();
						}
					}
				});
		tvTwo.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(
									Intent.createChooser(intent, "请选择一个图片上传"),
									2);
						} catch (android.content.ActivityNotFoundException ex) {
							ex.printStackTrace();
						}
					}
				});
		tvThr.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(
									Intent.createChooser(intent, "请选择一个图片上传"),
									3);
						} catch (android.content.ActivityNotFoundException ex) {
							ex.printStackTrace();
						}
					}
				});
		tvFou.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(
									Intent.createChooser(intent, "请选择一个图片上传"),
									4);
						} catch (android.content.ActivityNotFoundException ex) {
							ex.printStackTrace();
						}
					}
				});
		tvFiv.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(
									Intent.createChooser(intent, "请选择一个图片上传"),
									5);
						} catch (android.content.ActivityNotFoundException ex) {
							ex.printStackTrace();
						}
					}
				});

		findViewById(R.id.high_add).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!"".equals(num.getText().toString())) {
					getData();
					rec.setText("");
					num.setText("");
					staLong.setText("");
					staLat.setText("");
					endLong.setText("");
					endLat.setText("");
					highSta.setText("");
					highEnd.setText("");
					highLen.setText("");
					highSco.setText("");
					plaName.setText("");
					feature.setText("");
					tvTime.setText("");
					tvOne.setText("");
					tvTwo.setText("");
					tvThr.setText("");
					tvFou.setText("");
					tvFiv.setText("");
					pic = new PictureList();
				} else {
					num.setError("此项不能为空！");
				}
			}
		});

		findViewById(R.id.high_save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!"".equals(num.getText().toString())) {
					getData();
					dialog = new ProgressDialog(HighStaticCreateTwoAct.this);
					dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					dialog.setMessage("文件上传中，请稍后...");
					dialog.show();
					dialog.setCancelable(false);
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								for (int i = 0; i < list.size(); i++) {
									if (StringUtil.isBlank(list.get(i))) {
										data.add("");
									} else {
										String result = HttpRequestUtil
												.uploadImage(new File(list
														.get(i)));
										JSONArray arr = new JSONObject(result)
												.getJSONArray("data");
										data.add(arr.getString(0));
									}
								}
								sb.append(JSONParse.parseImageResult(data));
								Message msg = handler.obtainMessage();
								Log.e("Tag", sb.toString());
								msg.obj = HttpRequestUtil.PostHttp(
										Urls.HCSRS,
										sb.toString(),
										(OilApplication) HighStaticCreateTwoAct.this
												.getApplication());
								handler.sendMessage(msg);
							} catch (IOException e) {
								e.printStackTrace();
								Message msg = tostHand.obtainMessage();
								msg.obj = "图片上传失败，请稍后再试";
								tostHand.sendMessage(msg);
							} catch (Exception e) {
								Message msg = tostHand.obtainMessage();
								msg.obj = "发生未知错误";
								tostHand.sendMessage(msg);
								e.printStackTrace();
							}
						}
					}).start();
				}else {
					num.setError("此项不能为空！");
				}
			}
		});

	}

	protected void getData() {
		if ("".equals(tvOne.getText())) {
			pic.setPicOne("");
		}
		if ("".equals(tvTwo.getText())) {
			pic.setPicTwo("");
		}
		if ("".equals(tvThr.getText())) {
			pic.setPicThr("");
		}
		if ("".equals(tvFou.getText())) {
			pic.setPicFou("");
		}
		if ("".equals(tvFiv.getText())) {
			pic.setPicFiv("");
		}
		list.add(pic.getPicOne());
		list.add(pic.getPicTwo());
		list.add(pic.getPicThr());
		list.add(pic.getPicFou());
		list.add(pic.getPicFiv());
		high = new HighStatic();
		high.setEndLat(endLat.getText().toString());
		high.setEndLon(endLong.getText().toString());
		high.setFeaDes(feature.getText().toString());
		high.setHighEnd(highEnd.getText().toString());
		high.setHighSco(highSco.getText().toString());
		high.setHighSta(highSta.getText().toString());

		high.setNum(num.getText().toString());
		high.setPlaNam(plaName.getText().toString());
		high.setRecUpd(tvTime.getText().toString());
		high.setRecWor(rec.getText().toString());
		high.setStaLat(staLat.getText().toString());
		high.setStaLon(staLong.getText().toString());
		high.setHighLen(highLen.getText().toString());
		sb.append(high.toString());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				String path = new String();
				Uri uri = data.getData();
				path = FilterUtil.getPath(this, uri);
				if (path != null) {
					if(path.endsWith("jpg")||path.endsWith("png")||path.endsWith("jpeg"))
                    {
					tvOne.setText(path.substring(path.lastIndexOf("/")+1,path.length()));
					tvOne.setVisibility(View.VISIBLE);
					pic.setPicOne(path);
                    }else{
                    	Toast.makeText(getApplicationContext(),"图片格式无效，请重新选择！",Toast.LENGTH_SHORT).show();
                    }
				}
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				String path = new String();
				Uri uri = data.getData();
				path = FilterUtil.getPath(this, uri);
				if (path != null) {
					if(path.endsWith("jpg")||path.endsWith("png")||path.endsWith("jpeg"))
                    {
						tvTwo.setText(path.substring(path.lastIndexOf("/")+1,path.length()));
						tvTwo.setVisibility(View.VISIBLE);
						pic.setPicTwo(path);
                    }else{
                    	Toast.makeText(getApplicationContext(),"图片格式无效，请重新选择！",Toast.LENGTH_SHORT).show();
                    }
				}
			}
			break;
		case 3:
			if (resultCode == RESULT_OK) {
				String path = new String();
				Uri uri = data.getData();
				path = FilterUtil.getPath(this, uri);
				if (path != null) {
					if(path.endsWith("jpg")||path.endsWith("png")||path.endsWith("jpeg"))
                    {
						tvThr.setText(path.substring(path.lastIndexOf("/")+1,path.length()));
						tvThr.setVisibility(View.VISIBLE);
						pic.setPicThr(path);
                    }else{
                    	Toast.makeText(getApplicationContext(),"图片格式无效，请重新选择！",Toast.LENGTH_SHORT).show();
                    }
				}
			}
			break;
		case 4:
			if (resultCode == RESULT_OK) {
				String path = new String();
				Uri uri = data.getData();
				path = FilterUtil.getPath(this, uri);
				if (path != null) {
					if(path.endsWith("jpg")||path.endsWith("png")||path.endsWith("jpeg"))
                    {
						tvFou.setText(path.substring(path.lastIndexOf("/")+1,path.length()));
						tvFou.setVisibility(View.VISIBLE);
						pic.setPicFou(path);
                    }else{
                    	Toast.makeText(getApplicationContext(),"图片格式无效，请重新选择！",Toast.LENGTH_SHORT).show();
                    }
				}
			}
			break;
		case 5:
			if (resultCode == RESULT_OK) {
				String path = new String();
				Uri uri = data.getData();
				path = FilterUtil.getPath(this, uri);
				if (path != null) {
					if(path.endsWith("jpg")||path.endsWith("png")||path.endsWith("jpeg"))
                    {
						tvFiv.setText(path.substring(path.lastIndexOf("/")+1,path.length()));
						tvFiv.setVisibility(View.VISIBLE);
						pic.setPicFiv(path);
                    }else{
                    	Toast.makeText(getApplicationContext(),"图片格式无效，请重新选择！",Toast.LENGTH_SHORT).show();
                    }
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initWidget() {
		tvOne = (TextView) findViewById(R.id.high_picone);
		tvTwo = (TextView) findViewById(R.id.high_pictwo);
		tvThr = (TextView) findViewById(R.id.high_picthree);
		tvFou = (TextView) findViewById(R.id.high_picfour);
		tvFiv = (TextView) findViewById(R.id.high_picfive);
		tvTime = (TextView) findViewById(R.id.high_update_time);
		rec = (EditText) findViewById(R.id.high_recognition);
		num = (EditText) findViewById(R.id.high_number_rank);
		staLong = (EditText) findViewById(R.id.high_start_longitude);
		staLat = (EditText) findViewById(R.id.high_start_latitude);
		endLong = (EditText) findViewById(R.id.high_end_longitude);
		endLat = (EditText) findViewById(R.id.high_end_latitude);
		highSta = (EditText) findViewById(R.id.high_start);
		highEnd = (EditText) findViewById(R.id.high_end);
		highLen = (EditText) findViewById(R.id.high_length);
		highSco = (EditText) findViewById(R.id.high_score);
		plaName = (EditText) findViewById(R.id.high_palce_name);
		feature = (EditText) findViewById(R.id.high_feature_discription);
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
