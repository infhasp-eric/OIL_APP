package com.oil.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.ProperUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

/**
 * 阴极保护站运行记录
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class RcCreatActivity extends FinalActivity {
	@ViewInject(id = R.id.txt_day)
	EditText txt_day;
	@ViewInject(id = R.id.txt_time)
	EditText txt_time;
	@ViewInject(id = R.id.txt_jldy)
	EditText txt_jldy;
	@ViewInject(id = R.id.txt_zlsc_a)
	EditText txt_zlsc_a;
	@ViewInject(id = R.id.txt_zlsc_v)
	EditText txt_zlsc_v;
	@ViewInject(id = R.id.txt_tdddw)
	EditText txt_tdddw;
	@ViewInject(id = R.id.txt_recorder)
	EditText txt_recorder;
	@ViewInject(id = R.id.txt_comment)
	EditText txt_comment;
	@ViewInject(id = R.id.txt_others)
	EditText txt_others;
	@ViewInject(id = R.id.bt_add, click = "addListListen")
	Button bt_add;
	@ViewInject(id = R.id.bt_save, click = "saveToServer")
	Button bt_save;
	@ViewInject(id = R.id.bt_back, click = "backClick")
	Button bt_back;

	List<String> dayList = new ArrayList<String>();
	List<String> timeList = new ArrayList<String>();
	List<String> zlsc_aList = new ArrayList<String>();
	List<String> zlsc_vList = new ArrayList<String>();
	List<String> tdddwList = new ArrayList<String>();
	List<String> jldyList = new ArrayList<String>();
	List<String> recorderList = new ArrayList<String>();
	List<String> commentList = new ArrayList<String>();
	List<String> othersList = new ArrayList<String>();
	private SaveTask saveTask;

	String param;
	String serverUrl;
	private Integer clickNum = 0;
	private ProgressDialog dialog = null;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rc_creat);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		dialog = new ProgressDialog(RcCreatActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存，请稍后...");
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});

		/* 获取Bundle */
		Bundle bundle = this.getIntent().getExtras();

		/* 通过key值从bundle中取值 */
		param = bundle.getString("params");
		serverUrl = Urls.URL;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addListListen(View v) {
		// textView.setText("text set form button");
		// 将所有输入框中的值分别提取到对应的list中
		String recorder = getStringFrom(txt_recorder);
		if (StringUtil.isBlank(recorder)) {
			Toast.makeText(getApplicationContext(), "请填写记录人", 1000).show();
			return;
		}
		getValue(recorder);
		txt_day.setText("");
		txt_time.setText("");
		txt_jldy.setText("");
		txt_zlsc_a.setText("");
		txt_zlsc_v.setText("");
		txt_tdddw.setText("");
		txt_recorder.setText("");
		txt_comment.setText("");
		txt_others.setText("");
	}

	public void saveToServer(View v) {
		List<String> names = new ArrayList<String>();
		List<List<String>> lists = new ArrayList<List<String>>();
		if (clickNum == 0) {
			String recorder = getStringFrom(txt_recorder);
			if (StringUtil.isBlank(recorder)) {
				Toast.makeText(getApplicationContext(), "请填写记录人", 1000).show();
				return;
			}
			getValue(recorder);
		}
		if (dayList.size() == 0
				|| StringUtil.isBlank(getStringFrom(txt_recorder))) {
			Toast.makeText(getApplicationContext(), "请填写记录人", 1000).show();

		} else {
			dialog.show();
			clickNum++;
			names.add("day");
			lists.add(dayList);
			names.add("time");
			lists.add(timeList);
			names.add("jldy");
			lists.add(jldyList);
			names.add("zlsc_a");
			lists.add(zlsc_aList);
			names.add("zlsc_v");
			lists.add(zlsc_vList);
			names.add("tdddw");
			lists.add(tdddwList);
			names.add("recorder");
			lists.add(recorderList);
			names.add("comment");
			lists.add(commentList);
			names.add("others");
			lists.add(othersList);

			param += ParamsUtil.listToParams(lists, names);
			// serverUrl =
			System.out.println("-------" + param);
			saveTask = new SaveTask();
			saveTask.execute();
		}
	}

	public String getStringFrom(EditText et) {
		return et.getText().toString();
	}

	public void backClick(View v) {
		finish();
	}

	public void getValue(String record) {

		String day = txt_day.getText().toString();
		dayList.add(day);
		String time = getStringFrom(txt_time);
		timeList.add(time);
		String jldy = getStringFrom(txt_jldy);
		jldyList.add(jldy);
		String zlsc_a = getStringFrom(txt_zlsc_a);
		zlsc_aList.add(zlsc_a);
		String zlsc_v = getStringFrom(txt_zlsc_v);
		zlsc_vList.add(zlsc_v);
		String tdddw = getStringFrom(txt_tdddw);
		tdddwList.add(tdddw);

		recorderList.add(record);
		String comment = getStringFrom(txt_comment);
		commentList.add(comment);
		String others = getStringFrom(txt_others);
		othersList.add(others);
	}

	private class SaveTask extends AsyncTask<String, Void, String> {
		String req;
		JSONObject rejs;

		@Override
		protected String doInBackground(String... params) {
			Log.e("param++++++++++", param);
			req = HttpRequestUtil.PostHttp(
					serverUrl + "/services/base/rc/save", param,
					(OilApplication) getApplication());
			Log.v("req====", req);
			return req;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				rejs = JSONUtil.stringToJson(req);

				if (JSONUtil.stringToJson(req).getInt("status") == 200) {
					Toast.makeText(getApplicationContext(), "保存成功", 1000)
							.show();
					dialog.dismiss();
//					Intent intent = new Intent(RcCreatActivity.this,
//							RcListActivity.class);
//					startActivity(intent);
					MainActivity.ma.finish();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							rejs.getString("message"), 1000).show();
					dialog.dismiss();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), req, 1000).show();
			} catch (Exception e) {
				// TODO: handle exception
				dialog.dismiss();
				if(!StringUtil.isBlank(req)) {
					Toast.makeText(getApplicationContext(), req, 1000).show();
				} else {
					Toast.makeText(getApplicationContext(), "发生未知错误", 1000).show();
				}
			}
			// txt_comment.setText(params);
			// et_show.setText(req);
			super.onPostExecute(result);
		}

	}

	protected void onDestroy() {
		if (saveTask != null && saveTask.getStatus() == AsyncTask.Status.RUNNING) {
			saveTask.cancel(true); // 如果Task还在运行，则先取消它
		}
		super.onDestroy();
	}
}
