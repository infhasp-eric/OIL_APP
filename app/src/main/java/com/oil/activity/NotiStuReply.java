package com.oil.activity;

import java.io.File;

import net.tsz.afinal.FinalActivity;

import org.json.JSONObject;

import com.oil.domain.NotiStu;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ReplyType;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NotiStuReply extends FinalActivity {

	private Intent intent;
	private EditText etContent;
	private TextView tvPath, noti_stu_title;
	private String path = new String();
	private String result = new String();
	public static final int FILE_SELECT_CODE = 1;
	private Integer replyType;
	private String replyUrl;
	private Thread replyThrad = null;
	private ProgressDialog dialog = null;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(getApplicationContext(), "回复成功", 1000).show();
			dialog.dismiss();
			finish();
			//NotiStuContentActivity.instan.finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.noti_stu_reply);
		initWidget();
		intent = getIntent();
		replyType = intent.getIntExtra("ReplyType", 0);
		String id = intent.getStringExtra("id");
		
		if(replyType == ReplyType.NOTICE) {
			noti_stu_title.setText("学习和通知");
		} else if(replyType == ReplyType.TMP_NOTICE) {
			noti_stu_title.setText("闭环及临时性工作请示");
		}
		
		findViewById(R.id.back_noti_stu_reply).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		findViewById(R.id.upload_file).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setType("*/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						try {
							startActivityForResult(Intent.createChooser(intent,
									"请选择一个文件上传"),
									FILE_SELECT_CODE);
						} catch (android.content.ActivityNotFoundException ex) {
							Toast.makeText(getApplicationContext(),
									"请安装一个文件管理器！",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		
		dialog = new ProgressDialog(NotiStuReply.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存，请稍后...");
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					if(replyThrad != null) {
						replyThrad.interrupt();
					}
					dialog.dismiss();
				}
				return false;
			}
		});
		
		findViewById(R.id.new_reply_btn).setOnClickListener(
				
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (!"".equals(etContent.getText().toString())) {
							final StringBuffer sb = new StringBuffer();
							sb.append("id=" + intent.getStringExtra("id"));
							sb.append("&msg_content=" + etContent.getText().toString());
							dialog.show();
							if (!StringUtil.isBlank(path)) {
								replyThrad = new Thread(new Runnable() {
									@Override
									public void run() {
										File file = new File(path);
										try {
											result = HttpRequestUtil.uploadFile(file);
											JSONObject json = new JSONObject(result);
											if (json.getInt("status") == 200) {
												String str = json.getJSONArray("data").getString(0);
												//Toast.makeText(getApplicationContext(), "回复成功", 1500).show();
												if("null".equals(str)){
													Looper.prepare();
													Message msg = handler.obtainMessage();
													msg.what =2;
													handler.sendMessage(msg);
												}else{
													sb.append("&fileName=" + str);
													startSubThread(sb);
												}
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
								replyThrad.start();
							}else{
								sb.append("&fileName=");
								startSubThread(sb);
							}
						} else {
							Toast.makeText(getApplicationContext(), "请填写回复内容", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				path = FilterUtil.getPath(this, uri);
				if (path != null) {
					tvPath.setText(path.substring(path.lastIndexOf("/")+1,path.length()));
					tvPath.setVisibility(View.VISIBLE);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startSubThread(final StringBuffer sb) {
		if(replyType == ReplyType.NOTICE) {
			replyUrl = Urls.NSDR;
		} else if(replyType == ReplyType.TMP_NOTICE) {
			replyUrl = Urls.TMPR;
		} else if(replyType == ReplyType.EVENT) {
			replyUrl = Urls.EVER;
		}
		//System.out.println(replyUrl);
		//System.out.println(sb.toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				Message msg = handler.obtainMessage();
				msg.obj = HttpRequestUtil.PostHttp(replyUrl, sb.toString(),(OilApplication) getApplication());
				msg.what =1;
				handler.handleMessage(msg);
			}
		}).start();
	}

	private void initWidget() {
		etContent = (EditText) findViewById(R.id.new_reply_content);
		tvPath = (TextView) findViewById(R.id.show_path);
		noti_stu_title = (TextView) findViewById(R.id.noti_stu_title);
		etContent.setHorizontallyScrolling(false);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		if(replyThrad != null) {
			replyThrad.interrupt();
		}
		super.onDestroy();
	}
	
}
