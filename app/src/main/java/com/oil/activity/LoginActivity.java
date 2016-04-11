package com.oil.activity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
import com.oil.dialog.LodingActivtyDialog;
import com.oil.utils.GetSessionService;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

/***
 * 登录
 * 
 * @author east
 * 
 */
public class LoginActivity extends BaseActivity {
	private EditText username, password;
	private Button loginButton;
	private String resultCode = "chushi", errorMessage = "chushi";
	private LodingActivtyDialog Dialog;
	public static Intent getSessionIntent;
	public static Intent gpsIntent;
	private String usernameText;
	private String passwordText;
	private static OilApplication app;
	private static boolean judgeFirst = true;
	private CheckBox isRemenber;
	public static SharedPreferences pre;
	public static SharedPreferences.Editor editor;
	private String value = null;
	private String errorMsg;
	
	private Handler tostHand = new Handler(){
		public void handleMessage(Message msg) {
			if(Dialog != null && Dialog.isShowing()) {
				Dialog.cancel();
			}
			Toast.makeText(getApplicationContext(), msg.obj + "", 1).show();
		};
	};;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activaty);
		// gps信息采集服务开启
		// Intent intent1 = new Intent(LoginActivity.this, gps_service.class);
		// intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startService(intent1);

		gpsIntent = new Intent(LoginActivity.this, gps_service.class);
		getSessionIntent = new Intent(LoginActivity.this,GetSessionService.class);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		Intent intent = getIntent();
		Dialog = new LodingActivtyDialog(LoginActivity.this);
		value = intent.getStringExtra("cancel");
		pre = getSharedPreferences("longinvalue", MODE_WORLD_WRITEABLE);
		editor = pre.edit();
		app = (OilApplication) getApplication();

		// Log.e("Tag", "inloginactivity");

		initViewId();
		// isRemenber.setChecked(true);

		initLoginButton();
		if (pre.getBoolean("isChecked", false)) {
			isRemenber.setChecked(true);
			username.setText(pre.getString("name", ""));
			password.setText(pre.getString("pass", ""));

			usernameText = pre.getString("name", "");
			passwordText = pre.getString("pass", "");
		} else {
			isRemenber.setChecked(false);
			username.setText(pre.getString("name", ""));
			password.setText("");

			usernameText = pre.getString("name", "");
			passwordText = "";
		}
		try {
			if (value.equals("exit")) {
				Dialog.cancel();
				MainMenu.MM.finish();
				password.setText("");
				passwordText = pre.getString("pass", "");
				judgeFirst = true;
				// initLoginButton();
			}
		} catch (Exception e) {
			// initLoginButton();
			if (!StringUtils.isEmpty(usernameText)
					&& !StringUtils.isEmpty(passwordText)) {
				Dialog.show();
				Dialog.setCanceledOnTouchOutside(false);
				app.setUsername(usernameText);
				app.setPassword(passwordText);
				LoginHttpConnectTask task2 = new LoginHttpConnectTask();
				Dialog.setTask(task2);
				task2.execute();
			}
		}

	}

	/**
	 * 初始化控件
	 */
	private void initViewId() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login);
		isRemenber = (CheckBox) findViewById(R.id.RE_passwd);
	}

	/**
	 * 登录监听
	 */
	private void initLoginButton() {
		if (loginButton == null) {
			return;
		}
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				usernameText = username.getText().toString();
				passwordText = password.getText().toString();
				if (StringUtils.isEmpty(usernameText)
						|| StringUtils.isEmpty(passwordText)) {
					Toast.makeText(LoginActivity.this, "请输入账号密码",
							Toast.LENGTH_SHORT).show();
				} else {
					if (isRemenber.isChecked()) {
						editor.putString("name", username.getText().toString());
						editor.putString("pass", password.getText().toString());
						editor.putBoolean("isChecked", true);
						editor.commit();
					} else {
						editor.putString("name", username.getText().toString());
						editor.putString("pass", password.getText().toString());
						editor.putBoolean("isChecked", false);
						editor.commit();
					}

					LoginHttpConnectTask task2 = new LoginHttpConnectTask();
					Dialog.show();
					Dialog.setCanceledOnTouchOutside(false);
//					Dialog.setTask(task2);
					app.setUsername(usernameText);
					app.setPassword(passwordText);
					task2.execute();
				}

			}

		});

		isRemenber.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isRemenber.isChecked() == false) {

				}
			}
		});

	}

	/**
	 * 
	 * @author east 登录请求异步任务
	 * 
	 */

	public class LoginHttpConnectTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			usernameText = username.getText().toString();
			passwordText = password.getText().toString();
			NameValuePair username = new NameValuePair() {

				@Override
				public String getValue() {
					return null;
				}

				@Override
				public String getName() {
					return null;
				}
			};
			StringBuffer buffer = new StringBuffer();
			buffer.append("username=");
			buffer.append(usernameText);
			buffer.append("&password=");
			buffer.append(passwordText);
			String params = buffer.toString();
			Log.v("params", "" + params);
			String json = null;
			try {
				System.out.println("==============开始登陆=============");
				json = HttpRequestUtil.postLogin(usernameText, passwordText,
						(OilApplication) getApplication());
			} catch (SocketTimeoutException ioe) {
				// TODO Auto-generated catch block
				ioe.printStackTrace();
				errorMsg = "网络连接超时，请重试";
				return errorMsg;
				
			} catch (IOException e) {
				e.printStackTrace();
				errorMsg = "网络连接错误，请重试";
				return errorMsg;
			}
			//System.out.println(json);
			try {
				JSONObject object = new JSONObject(json);
				resultCode = object.getString("status");

				errorMessage = object.getString("message");
			} catch (Exception e) {
				errorMessage = json.toString();
			}
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("---------运行到这里---------------");
			if (resultCode.equals(200 + "")) {
				jumpToActivity(LoginActivity.this, MainMenu.class);
				startService(getSessionIntent);
				startService(gpsIntent);
				Log.v("Tag","start");
				finish();
				Dialog.cancel();
			} else if (result.equals("") || result.equals(null)) {
				Dialog.cancel();
				Toast.makeText(LoginActivity.this, "网络连接失败，请检查网络后再试...", 100).show();
			} else if (resultCode.equals(202 + "")) {
				Dialog.cancel();
				Toast.makeText(LoginActivity.this, errorMessage,100).show();
			} else if(!StringUtils.isEmpty(errorMsg)) {
				Dialog.cancel();
				Toast.makeText(LoginActivity.this, errorMsg, 100).show();
			} else {
				Dialog.cancel();
				Toast.makeText(LoginActivity.this, "发生未知错误", 100).show();
			}
			super.onPostExecute(result);
		}

	}

	// 返回按钮监听 两秒内双加返回两次则退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			exitBy2Click();
			return true;
		}

		return false;
	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);

		} else {
			((OilApplication) getApplication()).setJSESSIONID(null);
			finish();
			System.exit(0);

		}
	}

	public static void sendIdToServer(final String uid, final String cid) {
		if (judgeFirst) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = null;
					try {
						result = HttpRequestUtil.PostHttp(Urls.IDSAVE, "UserId=" + uid + "&ChannelId=" + cid, app);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.e("Tag", "cid+uid result = " + result);
				}
			}).start();
			judgeFirst = false;
		}
	}
}
