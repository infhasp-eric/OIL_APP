package com.oil.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

public class Change_Passwd extends FinalActivity {
	@ViewInject(id = R.id.change_passwd_back_bt, click = "backlst")
	Button back;
	@ViewInject(id = R.id.change_passwd_ok_bt, click = "ok")
	Button ok;
	@ViewInject(id = R.id.change_passwd_oldpasswd) EditText oldpasswd_et;
	@ViewInject(id = R.id.change_passwd_newpasswd)
	EditText newpasswd_et;
	@ViewInject(id = R.id.change_passwd_oknewpasswd)
	EditText oknewpasswd_et;
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.change_passwd);

	}

	public void backlst(View V) {
		finish();
	}

	public void ok(View V) {
		String oldpasswd = oldpasswd_et.getText().toString();
		String newpasswd = newpasswd_et.getText().toString();
		String oknewpasswd = oknewpasswd_et.getText().toString();
		if(StringUtils.isEmpty(oldpasswd)) {
			Toast.makeText(getApplicationContext(), "请输入原始密码", 1).show();
		} else if(StringUtils.isEmpty(newpasswd)) {
			Toast.makeText(getApplicationContext(), "请输入新密码", 1).show();
		} else if(StringUtils.isEmpty(oknewpasswd)) {
			Toast.makeText(getApplicationContext(), "请确认新密码", 1).show();
		} else {
			if (!newpasswd.equals(oknewpasswd)) {
				Toast.makeText(getApplicationContext(), "两次输入的密码不一致，请重新输入", 1).show();
				newpasswd_et.setText("");
				oknewpasswd_et.setText("");
			} else {
				// 此处联网更改密码
				dialog = new ProgressDialog(this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setMessage("数据加载中，请稍后...");
				dialog.show();
				dialog.setCancelable(false);
				ChangePasswdTask changePasswdTask = new ChangePasswdTask();
				changePasswdTask.execute();
			}

		}

	}

	public class ChangePasswdTask extends AsyncTask<String, Void, String> {
		Integer status;
		String message;

		@Override
		protected String doInBackground(String... params) {
			String oldpasswd = oldpasswd_et.getText().toString();
			String newpasswd = newpasswd_et.getText().toString();
			StringBuffer buffer = new StringBuffer();
			buffer.append("old_password=");
			buffer.append(oldpasswd);
			buffer.append("&password=");
			buffer.append(newpasswd);

			String json = HttpRequestUtil.sendGet(Urls.CHANGEPASSWD,
					buffer.toString(), (OilApplication) getApplication());
			System.out.println(json + "=============================");
			try {
				JSONObject jsonObject = new JSONObject(json);
				status = jsonObject.getInt("status");
				message = jsonObject.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = 404;
			}
			Log.e("更改密码++++++++++++++", json);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			if (status == 200) {
				dialog.cancel();
				Toast.makeText(getApplicationContext(), "密码修改成功", 1).show();
				Intent intent = new Intent(Change_Passwd.this,
						LoginActivity.class);
				// String json = HttpRequestUtil.sendGet(
				// "http://115.29.244.142/oil/services/login_out", "",
				// (OilApplication) getApplication());
				intent.putExtra("cancel", "exit");
				startActivity(intent);
				finish();
				MainMenu.MM.finish();

			} else if (status == 201) {
				dialog.cancel();
				Toast.makeText(getApplicationContext(), message, 1).show();
			} else if (status == null) {
				dialog.cancel();
				Toast.makeText(getApplicationContext(), "请检查网络", 1).show();
			} else {
				dialog.cancel();
				Toast.makeText(getApplicationContext(), "发生未知错误", 1).show();
			}

			super.onPostExecute(result);
		}
	}
}
