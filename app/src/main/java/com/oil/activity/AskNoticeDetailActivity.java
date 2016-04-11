package com.oil.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class AskNoticeDetailActivity extends FinalActivity {

	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;
	@ViewInject(id = R.id.txt_title)
	TextView txt_title;
	@ViewInject(id = R.id.txt_date)
	TextView txt_date;
	@ViewInject(id = R.id.txt_author)
	TextView txt_author;
	@ViewInject(id = R.id.txt_asktype)
	TextView txt_asktype;
	@ViewInject(id = R.id.txt_verify_desc)
	TextView txt_verify_desc;
	@ViewInject(id = R.id.web_show) TextView web_show;
	@ViewInject(id = R.id.lin_file)
	RelativeLayout lin_file;
	@ViewInject(id = R.id.txt_filename)
	TextView txt_filename;
	@ViewInject(id = R.id.bt_down, click = "downFile")
	Button bt_down;
	@ViewInject(id=R.id.dialog_pro) RelativeLayout dialog_pro;
	@ViewInject(id=R.id.lin_verify) LinearLayout lin_verify;

	private String serverUrl;
	private String id;
	private Thread getThread;
	private Handler getHand;
	private String result;
	private String filePath;
	private ScrollView sv;
	// private boolean fileDown;

	public static AskNoticeDetailActivity and;

	/*
	 * private Handler handler = new Handler(){ public void
	 * handleMessage(android.os.Message msg) { if(fileDown){
	 * Toast.makeText(getApplicationContext
	 * (),"下载成功！",Toast.LENGTH_SHORT).show(); }else{
	 * Toast.makeText(getApplicationContext
	 * (),"下载失败，文件已存在！",Toast.LENGTH_SHORT).show(); } // Looper.loop(); } };
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ask_notice_detail);
		sv = (ScrollView) findViewById(R.id.ask_notice_scroll);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		and = this;

		serverUrl = Urls.URL;
		/* 获取Bundle */
		Bundle bundle = this.getIntent().getExtras();

		/* 通过key值从bundle中取值 */
		id = bundle.getString("id");

		getThread = new Thread(new Runnable() {

			@Override
			public void run() {
				result = HttpRequestUtil.sendGet(serverUrl
						+ "services/ask_notice_detail", "id=" + id,
						(OilApplication) getApplication());
				try {
					JSONObject json = new JSONObject(result);
					if (json.getInt("status") == 200) {
						getHand.sendMessage(getHand.obtainMessage());

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		getThread.start();

		getHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					JSONObject json = new JSONObject(result);
					Log.e("asdsadasdsad", json + "");
					JSONObject notice = json.getJSONObject("data");
					web_show.setTextSize((float) 18.5);
					web_show.setText(notice.getString("content"));
					txt_title.setText(notice.getString("title"));
					txt_author.setText(notice.getString("author"));
					txt_asktype.setText(notice.getString("ask_type"));
					txt_date.setText(DateFormaterUtil.getDateToString(notice
							.getLong("create_time")));
					filePath = notice.getString("path");
					if (StringUtil.isBlank(filePath) || filePath.equals("null")) {
						lin_file.setVisibility(View.GONE);
					} else {
						lin_file.setVisibility(View.VISIBLE);
						txt_filename.setText(filePath);
					}
					if(!notice.getString("verify_desc").equals("null")) {
						txt_verify_desc.setText(notice.getString("verify_desc")
							.equals("null") ? "" : notice
							.getString("verify_desc"));
					} else {
						lin_verify.setVisibility(View.GONE);
					}
					sv.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog_pro.setVisibility(View.GONE);
			}
		};

	}

	public void backClick(View v) {
		finish();
	}

	public void downFile(View v) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Urls.SAVEFILE + filePath));
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onDestroy() {
		ThreadKILL.killthread(getThread);
		super.onDestroy();
	}
	// 返回按钮监听 两秒内双加返回两次则退出程序
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				Intent intent=new Intent(AskNoticeDetailActivity.this,MainMenu.class);
				startActivity(intent);
				finish();
				return true;
			}
			// else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// super.openOptionsMenu();
			// return true;
			// }
			return false;
		}

		
				
			
		
}
