package com.oil.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.adapter.ReplyAdapter;
import com.oil.domain.NotiStu;
import com.oil.layout.MyListView;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONParse;
import com.oil.utils.PreviewUtil;
import com.oil.utils.ReplyType;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

public class NotiStuContentActivity extends Activity {

	Intent intent;
	private TextView title, time, path;
	private ImageButton down;
	private ImageView iv;
	private TextView content;
	private RelativeLayout dialog_pro;
	private MyListView lVReply;
	private ReplyAdapter adapter;
	private NotiStu stu = null;
	private ScrollView scroll;
	private RelativeLayout rl, layout;
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private Date date;
	public static NotiStuContentActivity instan;
	// private ProgressDialog dialog = null;
	private boolean result;
	private boolean isSecond = false;
	private String id;
	private Bitmap bitmap;
	
	private Handler imageHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(bitmap != null){
				iv.setImageBitmap(bitmap);
			}else{
//				Toast.makeText(getApplicationContext(),"获取图片失败",Toast.LENGTH_SHORT).show();
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			stu = new NotiStu();
			stu = JSONParse.parseNotiStuDetail(msg.obj.toString());
			if (stu != null) {
				date = new Date(Long.parseLong(stu.getTime()));
				time.setText(format.format(date));
				title.setText(stu.getTitle());
				if (!StringUtils.isEmpty(stu.getPath())
						&& !stu.getPath().equals("null")) {
					layout.setVisibility(View.VISIBLE);
					System.out.println(stu.getPath());
					path.setText(StringUtils.PathtoDate2(stu.getPath()));
					if(stu.getPath().endsWith("jpg")||stu.getPath().endsWith("png") || stu.getPath().endsWith("jpeg")){
						new Thread(new Runnable() {
							@Override
							public void run() {
								bitmap =PreviewUtil.getNetBitmap(stu.getPath());
								Message msg = imageHandler.obtainMessage();
								imageHandler.sendMessage(msg);
							}
						}).start();
					}
				}
				
				/*content.getSettings().setSupportZoom(true);
				content.setBackgroundColor(Color.WHITE);
			    //v.loadUrl("about:blank");

				content.clearCache(true);
				content.getSettings().setDefaultTextEncodingName("utf-8");
			    //v.loadData(new String(Temp).replaceAll("#", "%23").replaceAll("%",
			    //        "%25").replaceAll("\'", "%27"), "text/html", "utf-8");
			    //v.loadData(Temp, "text/html", "utf-8");
				content.loadDataWithBaseURL(null,"<p style=\"word-break:break-all;\">" + stu.getContent() + "</p>", "text/html", "utf-8",null);
				content.setHorizontalScrollBarEnabled(false);
				content.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				content.setScrollContainer(false);*/
				content.setText(Html.fromHtml(stu.getContent()));
				if (stu.getComment().size() != 0) {
					adapter = new ReplyAdapter(stu.getComment());
					lVReply.setAdapter(adapter);
				} else {
					lVReply.setVisibility(View.INVISIBLE);
				}
				scroll.postDelayed(new Runnable() {
					@Override
					public void run() {
						scroll.scrollTo(0, 0);
						scroll.setVisibility(View.VISIBLE);
					}
				}, 0);
			} else {
				Toast.makeText(getApplicationContext(), "请检查您的网络连接！",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			dialog_pro.setVisibility(View.GONE);
			// dialog.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.noti_stu_content);
		instan = this;

		initWidget();
		intent = getIntent();
		id = intent.getStringExtra("id");
		startSubThread(id);
		findViewById(R.id.back_noti_stu_content).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});

		findViewById(R.id.noti_stu_next).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent1 = new Intent(
								NotiStuContentActivity.this, NotiStuReply.class);
						intent1.putExtra("id", intent.getStringExtra("id"));
						intent1.putExtra("ReplyType", ReplyType.NOTICE);
						startActivity(intent1);
						isSecond = true;
					}
				});
		down.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.SAVEFILE + stu.getPath()));
				startActivity(intent);
				
			}
		});

	}

	private void initWidget() {
		title = (TextView) findViewById(R.id.content_title);
		path = (TextView) findViewById(R.id.stu_filedown_path);
		iv = (ImageView) findViewById(R.id.extra_picture);
		content = (TextView) findViewById(R.id.content_content);
		time = (TextView) findViewById(R.id.content_time);
		lVReply = (MyListView) findViewById(R.id.noti_stu_feeback_list);
		scroll = (ScrollView) findViewById(R.id.content_scroll);
		rl = (RelativeLayout) findViewById(R.id.dialog_rl);
		down = (ImageButton) findViewById(R.id.stu_filedown);
		layout = (RelativeLayout) findViewById(R.id.stu_filedown_layout);
		dialog_pro = (RelativeLayout) findViewById(R.id.dialog_pro);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isSecond) {
			refresh();
		}
	}

	private void refresh() {

		isSecond = false;
		finish();
		Intent intent = new Intent(NotiStuContentActivity.this,
				NotiStuContentActivity.class);
		intent.putExtra("id", id);
		// bundle.put
		startActivity(intent);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (rl.isShown()) {
				rl.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	private void startSubThread(final String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = HttpRequestUtil.getDataFromServer(Urls.NSD + id,
						(OilApplication) getApplication());
				Log.v("+++++++", "" + msg.obj);
				handler.sendMessage(msg);
			}
		}).start();
	}
}
