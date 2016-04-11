package com.oil.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.util.Log;
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
import com.oil.utils.StringUtil;
import com.oil.utils.Urls;

/**
 * 管道保护电位曲线图
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class PlCurveDetailActivity extends FinalActivity {

	@ViewInject(id=R.id.et_no) EditText et_no;
	@ViewInject(id=R.id.et_p_early) EditText et_p_early;
	@ViewInject(id=R.id.et_p_end) EditText et_p_end;
	@ViewInject(id=R.id.et_measurer) EditText et_measurer;
	@ViewInject(id=R.id.et_remark) EditText et_remark;
	@ViewInject(id=R.id.bt_add, click="addClick") Button bt_add;
	@ViewInject(id=R.id.bt_back, click="backClick") Button bt_back;
	@ViewInject(id=R.id.bt_save, click="saveClick") Button bt_save;
	
	PostAsync post;
	List<String> noList = new ArrayList<String>();
	List<String> p_earlyList = new ArrayList<String>();
	List<String> p_endList = new ArrayList<String>();
	List<String> measurerList = new ArrayList<String>();
	List<String> remarkList = new ArrayList<String>();
	private ProgressDialog dialog;
	String params;
	private Integer clickNum = 0; 
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pl_curve_detail);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		 /*获取Bundle*/
        Bundle bundle = this.getIntent().getExtras();
        
        /*通过key值从bundle中取值*/
        params = bundle.getString("params");
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
	
	/**
	 * 将数据添加进list中
	 * @param v
	 */
	public void addClick(View v) {
		boolean isSuc = getValue();
		if(!isSuc) {
			return;
		}
		et_no.setText("");
		et_p_early.setText("");
		et_p_end.setText("");
		et_measurer.setText("");
		et_remark.setText("");
	}
	
	/**
	 * 返回点击事件
	 * @param v
	 */
	public void backClick(View v) {
		finish();
	}
	
	/**
	 * 保存点击事件
	 * @param v
	 */
	public void saveClick(View v) {
		if(clickNum == 0) {
			boolean judge = getValue();
			if(!judge){
				return;
			}
		}
		if(noList.size() == 0) {
			Toast.makeText(PlCurveDetailActivity.this.getApplicationContext(), "没有填写数据，不能保存", 1000).show();
			dialog.cancel();
			return;
		}
		
		clickNum++;
		dialog = new ProgressDialog(PlCurveDetailActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据上传中，请稍后...");
		dialog.show();
		dialog.setCancelable(false);
		post = new PostAsync();
		post.execute();
	}
	
	public boolean getValue(){
		boolean isSuc = false;
		String no = et_no.getText().toString();
		if(StringUtil.isBlank(no)) {
			Toast.makeText(getApplicationContext(), "请填写编号", 1000).show();
			return isSuc;
		}
		String p_early = et_p_early.getText().toString();
		if(StringUtil.isBlank(p_early)) {
			Toast.makeText(getApplicationContext(), "请填写上旬电位", 1000).show();
			return isSuc;
		}
		String p_end = et_p_end.getText().toString();
		if(StringUtil.isBlank(p_end)) {
			Toast.makeText(getApplicationContext(), "请填写下旬电位", 1000).show();
			return isSuc;
		}
		noList.add(no);
		p_earlyList.add(p_early);
		p_endList.add(p_end);
		String measurer = et_measurer.getText().toString();
		measurerList.add(measurer);
		String remark = et_remark.getText().toString();
		remarkList.add(remark);
		isSuc = true;
		return isSuc;
	}
	
	public class PostAsync extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			List<List<String>> lists = new ArrayList<List<String>>();
			List<String> names = new ArrayList<String>();
			lists.add(noList);
			names.add("no");
			lists.add(p_earlyList);
			names.add("p_early");
			lists.add(p_endList);
			names.add("p_end");
			lists.add(measurerList);
			names.add("measurer");
			lists.add(remarkList);
			names.add("remark");
			
			params += ParamsUtil.listToParams(lists, names);
			Log.v("++++++", params);
			String req = HttpRequestUtil.PostHttp(Urls.URL + "services/base/pl_curve/save", params, (OilApplication)getApplication());
			
			return req;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			try {
				JSONObject rejs = JSONUtil.stringToJson(result);
				if(rejs.getString("status").equals("200")) {
					Toast.makeText(getApplicationContext(),"保存成功", 1000).show();
					//PlCurveListActivity.pcl.finish();
					PlCurveCreatActivity.pcc.finish();
					//Intent intent = new Intent(getApplicationContext(), PlCurveListActivity.class);
					//startActivity(intent);
					
					finish();
					dialog.cancel();
				} else {
					dialog.cancel();
					Toast.makeText(getApplicationContext(), rejs.getString("status")+rejs.getString("message"), 1000).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				dialog.cancel();
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "发生未知错误", 1000).show();
			} catch (Exception e) {
				e.printStackTrace();
				dialog.cancel();
				Toast.makeText(getApplicationContext(), result, 1000).show();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		//结束异步请求
		if (post != null && post.getStatus() == AsyncTask.Status.RUNNING) {
			post.cancel(true); // 如果Task还在运行，则先取消它
		}
		super.onDestroy();
	}
}
