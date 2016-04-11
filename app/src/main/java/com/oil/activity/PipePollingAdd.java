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
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.dialog.LodingActivtyDialog;
import com.oil.domain.PipePolling;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

public class PipePollingAdd extends Activity {

	private String picPath = new String();
	private EditText worPos, worCont, quesDeal, voiRec, worker, verifier;
	private TextView yearMonDay;
	private ImageView iv;
	private String[] trans = new String[6];
	private List<String> dateList = new ArrayList<String>();
	private List<String> workPlaceList = new ArrayList<String>();
	private List<String> contentList = new ArrayList<String>();
	private List<String> voiceRecordList = new ArrayList<String>();
	private List<String> questionList = new ArrayList<String>();
	private List<String> workerList = new ArrayList<String>();
	private List<String> auditorList = new ArrayList<String>();
	private List<String> fileNameList = new ArrayList<String>();
	private List<String> fileList = new ArrayList<String>();
	private PipePolling polling = new PipePolling();
	private String result;
	private int year, month, day;
	private ProgressDialog Dialog;
	private int count;
	int desiredWidth, desiredHeight;
	private boolean saveClicked = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Dialog.dismiss();
			try {
				if ("200".equals(new JSONObject(msg.obj.toString())
						.getString("status"))) {
					Toast.makeText(
							getApplicationContext(),
							new JSONObject(msg.obj.toString())
									.getString("message"), Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getApplicationContext(), "保存失败",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "保存失败",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			finish();
			RecordCreate.instance.finish();
		}
	};

	private Handler tostHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(Dialog != null && Dialog.isShowing()) {
				Dialog.dismiss();
			}
			String message = msg.obj.toString();
			Toast.makeText(getApplicationContext(), message, 1).show();
		}
	};

	private Handler uphandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				if (!StringUtil.isBlank(result)) {
					// System.out.println("===============================");
					iv.setImageBitmap(null);
					iv.setVisibility(View.GONE);
					picPath = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	private Thread uploadThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pipe_polling_add);
		initWidget();
		Intent intent = getIntent();
		trans = intent.getBundleExtra("bd").getStringArray("trans");

		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);

		desiredWidth = (int) (wm.getDefaultDisplay().getWidth() * 0.8);
		desiredHeight = (int) (wm.getDefaultDisplay().getHeight() * 0.3);

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

		yearMonDay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickerDialog dpd = new DatePickerDialog(
						PipePollingAdd.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								year = arg1;
								month = arg2;
								day = arg3;
								if (month < 9 && day < 10) {
									yearMonDay.setText(year + "-0"
											+ (month + 1) + "-0" + day);
								} else if (month < 9) {
									yearMonDay.setText(year + "-0"
											+ (month + 1) + "-" + day);
								} else if (day < 10) {
									yearMonDay.setText(year + "-" + (month + 1)
											+ "-0" + day);
								} else {
									yearMonDay.setText(year + "-" + (month + 1)
											+ "-" + day);
								}
							}
						}, year, month, day);
				dpd.show();
			}
		});

		findViewById(R.id.pipe_polling_back_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});

		findViewById(R.id.pipe_polling_choose_pic).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(intent, 1);
					}
				});

		findViewById(R.id.pipe_polling_continue_add).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (judgeNull()) {
							fileList.add(picPath);
							// if(!StringUtil.isBlank(picPath)) {
							// File file = new File(picPath);
							iv.setVisibility(View.GONE);
							picPath = new String();
							// fileList.add(file);
							// } else {
							// fileList.add(null);
							// }
							dateList.add(yearMonDay.getText().toString());
							workPlaceList.add(worPos.getText().toString());
							contentList.add(worCont.getText().toString());
							questionList.add(quesDeal.getText().toString());
							voiceRecordList.add(voiRec.getText().toString());
							workerList.add(worker.getText().toString());
							auditorList.add(verifier.getText().toString());
							yearMonDay.setText("");
							worPos.setText("");
							worCont.setText("");
							quesDeal.setText("");
							voiRec.setText("");
							worker.setText("");
							verifier.setText("");
						}
					}
				});

		findViewById(R.id.pipe_polling_save).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (judgeNull()) {
							Dialog = new ProgressDialog(
									PipePollingAdd.this);
							Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							Dialog.setMessage("正在保存，请稍后...");
							Dialog.setCancelable(false);
							Dialog.show();
							if (!saveClicked) {
								dateList.add(yearMonDay.getText().toString());
								workPlaceList.add(worPos.getText().toString());
								contentList.add(worCont.getText().toString());
								questionList.add(quesDeal.getText().toString());
								voiceRecordList
										.add(voiRec.getText().toString());
								workerList.add(worker.getText().toString());
								auditorList.add(verifier.getText().toString());
								fileList.add(picPath);
								saveClicked = true;
							}
							polling.setPl(trans[0]);
							polling.setSection(trans[1]);
							polling.setSpec(trans[2]);
							polling.setJinzhan(trans[3]);
							polling.setpMonth(trans[4]);
							polling.setpDate(dateList);
							polling.setWorkPlace(workPlaceList);
							polling.setContent(contentList);
							polling.setQuestion(questionList);
							polling.setVoiceRecord(voiceRecordList);
							polling.setWorker(workerList);
							polling.setAuditor(auditorList);
							polling.setSaveJinzhan(trans[5]);

							final List<File> files = new ArrayList<File>();
							for (String fpath : fileList) {
								if (!StringUtil.isBlank(fpath)) {
									final File file = new File(fpath);
									files.add(file);
								} else {
									files.add(null);
								}
							}

							uploadThread = new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										result = HttpRequestUtil.uploadImages(files);

										JSONObject json = new JSONObject(result);
										if (json.getInt("status") == 200) {
											JSONArray fileName = json.getJSONArray("data");
											for (int i = 0; i < fileName.length(); i++) {
												if (!fileName.getString(i).equals("null")) {
													fileNameList.add(fileName.getString(i));
												} else {
													fileNameList.add("");
												}
											}
											polling.setFileName(fileNameList);
											startSubThread();
										} else {
											Message msg = tostHand
													.obtainMessage();
											msg.obj = "图片上传失败，请重试";
											tostHand.sendMessage(msg);
											Dialog.dismiss();
										}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										Message msg = tostHand.obtainMessage();
										msg.obj = "图片上传失败，请重试";
										tostHand.sendMessage(msg);
										e.printStackTrace();
									} catch (Exception e) {
										// TODO: handle exception
										Message msg = tostHand.obtainMessage();
										msg.obj = "发生未知错误";
										tostHand.sendMessage(msg);
										e.printStackTrace();
									}
								}
							});
							uploadThread.start();

						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径
			 */
			Uri uri = data.getData();
			// Log.e(TAG, "uri = "+ uri);
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					/***
					 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
					 * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
					 */
					if (path.endsWith("jpg") || path.endsWith("png")
							|| path.endsWith("jpeg")) {
						picPath = path;
						Bitmap destBitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						Bitmap bitmap;
						if (destBitmap.getWidth() > desiredWidth
								|| destBitmap.getHeight() > desiredHeight) {
							bitmap = Bitmap.createScaledBitmap(destBitmap,
									desiredWidth, desiredHeight, true);
							destBitmap.recycle();
						} else {
							bitmap = destBitmap;
						}
						iv.setImageBitmap(bitmap);
						iv.setVisibility(View.VISIBLE);
					} else {
						alert();
					}
				} else {
					alert();
				}

			} catch (Exception e) {
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 弹出提示框
	 */
	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

	protected void startSubThread() {
		Message msg = handler.obtainMessage();
		Log.e("Tag", polling.toString());
		msg.obj = HttpRequestUtil.PostHttp(Urls.PPWRS, polling.toString(),
				(OilApplication) getApplication());
		handler.sendMessage(msg);
	}

	public boolean judgeNull() {
		if (StringUtil.isBlank(worPos.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请填写工作地点",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (StringUtil.isBlank(yearMonDay.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请选择日期", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	private void initWidget() {
		yearMonDay = (TextView) findViewById(R.id.pipe_polling_year_month_day);
		worPos = (EditText) findViewById(R.id.pipe_polling_work_position);
		worCont = (EditText) findViewById(R.id.pipe_polling_work_content);
		quesDeal = (EditText) findViewById(R.id.pipe_polling_question_dealing);
		voiRec = (EditText) findViewById(R.id.pipe_polling_voice_record);
		worker = (EditText) findViewById(R.id.pipe_polling_worker);
		verifier = (EditText) findViewById(R.id.pipe_polling_verify);
		iv = (ImageView) findViewById(R.id.pipe_polling_pic_view);
	}

}
