package com.oil.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.oil.domain.JointProper;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.Urls;

@SuppressLint("NewApi")
public class InsulationAdd extends FinalActivity {
	@ViewInject(id=R.id.bt_back, click="backClick") Button bt_back;
	@ViewInject(id=R.id.bt_save, click="saveClick") Button bt_save;
	@ViewInject(id=R.id.bt_add, click="addClick") Button bt_add;
	@ViewInject(id=R.id.lin_month_1, click="lin1Click") LinearLayout lin_month_1;
	@ViewInject(id=R.id.lin_month_2, click="lin2Click") LinearLayout lin_month_2;
	@ViewInject(id=R.id.lin_month_3, click="lin3Click") LinearLayout lin_month_3;
	@ViewInject(id=R.id.lin_month_4, click="lin4Click") LinearLayout lin_month_4;
	@ViewInject(id=R.id.lin_month_5, click="lin5Click") LinearLayout lin_month_5;
	@ViewInject(id=R.id.lin_month_6, click="lin6Click") LinearLayout lin_month_6;
	@ViewInject(id=R.id.lin_month_7, click="lin7Click") LinearLayout lin_month_7;
	@ViewInject(id=R.id.lin_month_8, click="lin8Click") LinearLayout lin_month_8;
	@ViewInject(id=R.id.lin_month_9, click="lin9Click") LinearLayout lin_month_9;
	@ViewInject(id=R.id.lin_month_10, click="lin10Click") LinearLayout lin_month_10;
	@ViewInject(id=R.id.lin_month_11, click="lin11Click") LinearLayout lin_month_11;
	@ViewInject(id=R.id.lin_month_12, click="lin12Click") LinearLayout lin_month_12;
	@ViewInject(id=R.id.lin_amonth1) LinearLayout lin_amonth_1;
	@ViewInject(id=R.id.lin_amonth2) LinearLayout lin_amonth_2;
	@ViewInject(id=R.id.lin_amonth3) LinearLayout lin_amonth_3;
	@ViewInject(id=R.id.lin_amonth4) LinearLayout lin_amonth_4;
	@ViewInject(id=R.id.lin_amonth5) LinearLayout lin_amonth_5;
	@ViewInject(id=R.id.lin_amonth6) LinearLayout lin_amonth_6;
	@ViewInject(id=R.id.lin_amonth7) LinearLayout lin_amonth_7;
	@ViewInject(id=R.id.lin_amonth8) LinearLayout lin_amonth_8;
	@ViewInject(id=R.id.lin_amonth9) LinearLayout lin_amonth_9;
	@ViewInject(id=R.id.lin_amonth10) LinearLayout lin_amonth_10;
	@ViewInject(id=R.id.lin_amonth11) LinearLayout lin_amonth_11;
	@ViewInject(id=R.id.lin_amonth12) LinearLayout lin_amonth_12;
	@ViewInject(id=R.id.img1) ImageView img1;
	@ViewInject(id=R.id.img2) ImageView img2;
	@ViewInject(id=R.id.img3) ImageView img3;
	@ViewInject(id=R.id.img4) ImageView img4;
	@ViewInject(id=R.id.img5) ImageView img5;
	@ViewInject(id=R.id.img6) ImageView img6;
	@ViewInject(id=R.id.img7) ImageView img7;
	@ViewInject(id=R.id.img8) ImageView img8;
	@ViewInject(id=R.id.img9) ImageView img9;
	@ViewInject(id=R.id.img10) ImageView img10;
	@ViewInject(id=R.id.img11) ImageView img11;
	@ViewInject(id=R.id.img12) ImageView img12;
	
	@ViewInject(id=R.id.et_month_b1) EditText et_month_b1;
	@ViewInject(id=R.id.et_month_m1) EditText et_month_m1;
	@ViewInject(id=R.id.et_month_b2) EditText et_month_b2;
	@ViewInject(id=R.id.et_month_m2) EditText et_month_m2;
	@ViewInject(id=R.id.et_month_b3) EditText et_month_b3;
	@ViewInject(id=R.id.et_month_m3) EditText et_month_m3;
	@ViewInject(id=R.id.et_month_b4) EditText et_month_b4;
	@ViewInject(id=R.id.et_month_m4) EditText et_month_m4;
	@ViewInject(id=R.id.et_month_b5) EditText et_month_b5;
	@ViewInject(id=R.id.et_month_m5) EditText et_month_m5;
	@ViewInject(id=R.id.et_month_b6) EditText et_month_b6;
	@ViewInject(id=R.id.et_month_m6) EditText et_month_m6;
	@ViewInject(id=R.id.et_month_b7) EditText et_month_b7;
	@ViewInject(id=R.id.et_month_m7) EditText et_month_m7;
	@ViewInject(id=R.id.et_month_b8) EditText et_month_b8;
	@ViewInject(id=R.id.et_month_m8) EditText et_month_m8;
	@ViewInject(id=R.id.et_month_b9) EditText et_month_b9;
	@ViewInject(id=R.id.et_month_m9) EditText et_month_m9;
	@ViewInject(id=R.id.et_month_b10) EditText et_month_b10;
	@ViewInject(id=R.id.et_month_m10) EditText et_month_m10;
	@ViewInject(id=R.id.et_month_b11) EditText et_month_b11;
	@ViewInject(id=R.id.et_month_m11) EditText et_month_m11;
	@ViewInject(id=R.id.et_month_b12) EditText et_month_b12;
	@ViewInject(id=R.id.et_month_m12) EditText et_month_m12;
	@ViewInject(id=R.id.et_position) EditText et_position;
	
	private boolean open1,open2,open3,open4,open5,open6,open7,open8,open9,open10,open11,open12;
	
	private ProgressDialog dialog = null;
	private String param = "";
	private List<Map<String, String>> pamList = new ArrayList<Map<String,String>>();
	private List<String> positionl = new ArrayList<String>();
	private String serverUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.insulation_potential);
		
		Bundle bundle = getIntent().getBundleExtra("bundle");
		param = bundle.getString("param");
		
		serverUrl = Urls.IJPMRS;
		
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("正在保存，请稍后...");
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					InsulationAdd.this.finish();
				}
				return false;
			}
		});
	}
	
	public void backClick(View v) {
		finish();
	}
	
	public void saveClick(View v) {
		if(getValue()) {
			dialog.show();
			for(Map<String, String> map : pamList) {
				param += ParamsUtil.mapToParams(map);
			}
			param += ParamsUtil.listToParams(positionl, "position");
			SavaTask task = new SavaTask();
			System.out.println(param);
			task.execute();
		}
	}
	
	public void addClick(View v) {
		getValue();
		emptyValue();
	}
	
	public boolean getValue() {
		if(et_position.getText().toString().length() > 20) {
			Toast.makeText(getApplicationContext(), "位置长度不能大于20，请重新确认", 100).show();
			return false;
		}
		Map<String, String> bMap = getBValue();
		pamList.add(bMap);
		Map<String, String> mMap = getMValue();
		pamList.add(mMap);
		positionl.add(et_position.getText().toString());
		return true;
	}
	
	public void emptyValue() {
		et_position.setText("");
		et_month_b1.setText("");
		et_month_b2.setText("");
		et_month_b3.setText("");
		et_month_b4.setText("");
		et_month_b5.setText("");
		et_month_b6.setText("");
		et_month_b7.setText("");
		et_month_b8.setText("");
		et_month_b9.setText("");
		et_month_b10.setText("");
		et_month_b11.setText("");
		et_month_b12.setText("");
		et_month_m1.setText("");
		et_month_m2.setText("");
		et_month_m3.setText("");
		et_month_m4.setText("");
		et_month_m5.setText("");
		et_month_m6.setText("");
		et_month_m7.setText("");
		et_month_m8.setText("");
		et_month_m9.setText("");
		et_month_m10.setText("");
		et_month_m11.setText("");
		et_month_m12.setText("");
	}
	
	public Map<String, String> getBValue(){
		Map<String, String> valueMap = new HashMap<String, String>();
		String month_1 = et_month_b1.getText().toString();
		String month_2 = et_month_b2.getText().toString();
		String month_3 = et_month_b3.getText().toString();
		String month_4 = et_month_b4.getText().toString();
		String month_5 = et_month_b5.getText().toString();
		String month_6 = et_month_b6.getText().toString();
		String month_7 = et_month_b7.getText().toString();
		String month_8 = et_month_b8.getText().toString();
		String month_9 = et_month_b9.getText().toString();
		String month_10 = et_month_b10.getText().toString();
		String month_11 = et_month_b11.getText().toString();
		String month_12 = et_month_b12.getText().toString();
		valueMap.put("month_1", month_1);
		valueMap.put("month_2", month_2);
		valueMap.put("month_3", month_3);
		valueMap.put("month_4", month_4);
		valueMap.put("month_5", month_5);
		valueMap.put("month_6", month_6);
		valueMap.put("month_7", month_7);
		valueMap.put("month_8", month_8);
		valueMap.put("month_9", month_9);
		valueMap.put("month_10", month_10);
		valueMap.put("month_11", month_11);
		valueMap.put("month_12", month_12);
		return valueMap;
	}
	
	public Map<String, String> getMValue(){
		Map<String, String> valueMap = new HashMap<String, String>();
		String month_1 = et_month_m1.getText().toString();
		String month_2 = et_month_m2.getText().toString();
		String month_3 = et_month_m3.getText().toString();
		String month_4 = et_month_m4.getText().toString();
		String month_5 = et_month_m5.getText().toString();
		String month_6 = et_month_m6.getText().toString();
		String month_7 = et_month_m7.getText().toString();
		String month_8 = et_month_m8.getText().toString();
		String month_9 = et_month_m9.getText().toString();
		String month_10 = et_month_m10.getText().toString();
		String month_11 = et_month_m11.getText().toString();
		String month_12 = et_month_m12.getText().toString();
		valueMap.put("month_1", month_1);
		valueMap.put("month_2", month_2);
		valueMap.put("month_3", month_3);
		valueMap.put("month_4", month_4);
		valueMap.put("month_5", month_5);
		valueMap.put("month_6", month_6);
		valueMap.put("month_7", month_7);
		valueMap.put("month_8", month_8);
		valueMap.put("month_9", month_9);
		valueMap.put("month_10", month_10);
		valueMap.put("month_11", month_11);
		valueMap.put("month_12", month_12);
		return valueMap;
	}
	
	public void lin1Click(View v) {
		if(open1) {
			lin_amonth_1.setVisibility(View.GONE);
			img1.setImageResource(R.drawable.oo);
			open1 = false;
		} else {
			lin_amonth_1.setVisibility(View.VISIBLE);
			img1.setImageResource(R.drawable.oo_down);
			open1 = true;
		}
	}
	
	public void lin2Click(View v) {
		if(open2) {
			lin_amonth_2.setVisibility(View.GONE);
			img2.setImageResource(R.drawable.oo);
			open2 = false;
		} else {
			lin_amonth_2.setVisibility(View.VISIBLE);
			img2.setImageResource(R.drawable.oo_down);
			open2 = true;
		}
	}
	
	public void lin3Click(View v) {
		if(open3) {
			lin_amonth_3.setVisibility(View.GONE);
			img3.setImageResource(R.drawable.oo);
			open3 = false;
		} else {
			lin_amonth_3.setVisibility(View.VISIBLE);
			img3.setImageResource(R.drawable.oo_down);
			open3 = true;
		}
	}
	
	public void lin4Click(View v) {
		if(open4) {
			lin_amonth_4.setVisibility(View.GONE);
			img4.setImageResource(R.drawable.oo);
			open4 = false;
		} else {
			lin_amonth_4.setVisibility(View.VISIBLE);
			img4.setImageResource(R.drawable.oo_down);
			open4 = true;
		}
	}
	
	public void lin5Click(View v) {
		if(open5) {
			lin_amonth_5.setVisibility(View.GONE);
			img5.setImageResource(R.drawable.oo);
			open5 = false;
		} else {
			lin_amonth_5.setVisibility(View.VISIBLE);
			img5.setImageResource(R.drawable.oo_down);
			open5 = true;
		}
	}
	
	public void lin6Click(View v) {
		if(open6) {
			lin_amonth_6.setVisibility(View.GONE);
			img6.setImageResource(R.drawable.oo);
			open6 = false;
		} else {
			lin_amonth_6.setVisibility(View.VISIBLE);
			img6.setImageResource(R.drawable.oo_down);
			open6 = true;
		}
	}
	
	public void lin7Click(View v) {
		if(open7) {
			lin_amonth_7.setVisibility(View.GONE);
			img7.setImageResource(R.drawable.oo);
			open7 = false;
		} else {
			lin_amonth_7.setVisibility(View.VISIBLE);
			img7.setImageResource(R.drawable.oo_down);
			open7 = true;
		}
	}
	
	public void lin8Click(View v) {
		if(open8) {
			lin_amonth_8.setVisibility(View.GONE);
			img8.setImageResource(R.drawable.oo);
			open8 = false;
		} else {
			lin_amonth_8.setVisibility(View.VISIBLE);
			img8.setImageResource(R.drawable.oo_down);
			open8 = true;
		}
	}
	
	public void lin9Click(View v) {
		if(open9) {
			lin_amonth_9.setVisibility(View.GONE);
			img9.setImageResource(R.drawable.oo);
			open9 = false;
		} else {
			lin_amonth_9.setVisibility(View.VISIBLE);
			img9.setImageResource(R.drawable.oo_down);
			open9 = true;
		}
	}
	
	public void lin10Click(View v) {
		if(open10) {
			lin_amonth_10.setVisibility(View.GONE);
			img10.setImageResource(R.drawable.oo);
			open10 = false;
		} else {
			lin_amonth_10.setVisibility(View.VISIBLE);
			img10.setImageResource(R.drawable.oo_down);
			open10 = true;
		}
	}
	
	public void lin11Click(View v) {
		if(open11) {
			lin_amonth_11.setVisibility(View.GONE);
			img11.setImageResource(R.drawable.oo);
			open11 = false;
		} else {
			lin_amonth_11.setVisibility(View.VISIBLE);
			img11.setImageResource(R.drawable.oo_down);
			open11 = true;
		}
	}
	
	public void lin12Click(View v) {
		if(open12) {
			lin_amonth_12.setVisibility(View.GONE);
			img12.setImageResource(R.drawable.oo);
			open12 = false;
		} else {
			lin_amonth_12.setVisibility(View.VISIBLE);
			img12.setImageResource(R.drawable.oo_down);
			open12 = true;
		}
	}
	
	public class SavaTask extends AsyncTask<String, Void, String> {
		JSONObject rejs;
		String req;

		@Override
		protected String doInBackground(String... params) {
			req = HttpRequestUtil.PostHttp(serverUrl, param,
					(OilApplication) getApplication());

			return req;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				rejs = JSONUtil.stringToJson(result);
				if (rejs.getString("status").equals("200")) {
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "保存成功", 1000)
							.show();
//					Intent intent = new Intent();
					InsulationCreate.ins.finish();
					InsulationElecActivity.iea.finish();
//					intent.setClass(InsulationAdd.this,
//							InsulationJoint.class);
//					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),rejs.getString("message"), Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), req, Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
			super.onPostExecute(result);
		}

	}
}
