package com.oil.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oil.dialog.LodingActivtyDialog;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.ProperUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

@SuppressLint("NewApi")
public class VMaintDetailActivity extends FinalActivity {

	@ViewInject(id = R.id.et_particips)
	EditText et_particips;
	@ViewInject(id = R.id.et_content)
	EditText et_content;
	@ViewInject(id = R.id.et_question)
	EditText et_question;
	@ViewInject(id = R.id.bt_save, click = "saveToServer")
	Button bt_save;
	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;
	String param;
	String serverUrl;
	private ProgressDialog Dialog;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vmaint_detail);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		/* 获取Bundle */
		Bundle bundle = this.getIntent().getExtras();

		/* 通过key值从bundle中取值 */
		param = bundle.getString("params");
		serverUrl = Urls.URL;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void saveToServer(View v) {
		Dialog = new ProgressDialog(
				VMaintDetailActivity.this);
		Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		Dialog.setMessage("正在保存，请稍后...");
		Dialog.setCancelable(false);
		Dialog.show();
		Dialog.setCanceledOnTouchOutside(false);
		SaveTask saveTask = new SaveTask();
		saveTask.execute();
	}

	public void backClick(View v) {
		finish();
	}

	public class SaveTask extends AsyncTask<String, Void, String> {
		JSONObject rejs;
		String req;

		@Override
		protected String doInBackground(String... params) {
			Map<String, String> saveMap = new HashMap<String, String>();
			String particips = et_particips.getText().toString();
			saveMap.put("particips", particips);
			String content = et_content.getText().toString();
			saveMap.put("content", content);
			String question = et_question.getText().toString();
			saveMap.put("question", question);
			param += ParamsUtil.mapToParams(saveMap);

			// et_question.setText(params);

			req = HttpRequestUtil.PostHttp(serverUrl
					+ "/services/base/v_maint/save", param,
					(OilApplication) getApplication());

			rejs = JSONUtil.stringToJson(req + "");

			return req;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				if (rejs.getInt("status") == 200) {
					Dialog.cancel();
					Toast.makeText(getApplicationContext(), "保存成功", 1000)
							.show();
					VMaintCreateActivity.vmc.finish();
					//VMaintListActivity.vml.finish();
					/*Intent intent = new Intent(VMaintDetailActivity.this,
							VMaintListActivity.class);
					startActivity(intent);*/
					finish();
				} else if (rejs.getString("status") != null) {
					Dialog.dismiss();
					Toast.makeText(getApplicationContext(),

					rejs.getString("message"), 1).show();
				} else {
					Dialog.dismiss();
					Toast.makeText(getApplicationContext(), "请检查网络后再试", 1000)
							.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Dialog.dismiss();
				if(!StringUtil.isBlank(req)) {
					Toast.makeText(getApplicationContext(), req, 1000).show();
				} else {
					Toast.makeText(getApplicationContext(), "发生未知错误", 1000).show();
				}
			}
			super.onPostExecute(result);
		}

	}
}
