package com.oil.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.oil.domain.High_Dca_Information_Create_Bean;
import com.oil.utils.AdapterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class High_DCA_Information_Create extends FinalActivity {
	@ViewInject(id = R.id.high_dca_information_create_sp_pl)
	Spinner sp_pl;
	@ViewInject(id = R.id.high_dca_information_create_sp_section)
	Spinner sp_section;
	@ViewInject(id = R.id.high_dca_information_create_sp_spec)
	Spinner sp_spec;
	@ViewInject(id = R.id.high_dca_information_create_back_btn, click = "backlistener")
	Button back;
	@ViewInject(id = R.id.high_dca_information_create_next, click = "nextlistener")
	Button next;
	private Thread newThread;
	private String status;
	private String[] plList;
	private int[] pl_idList;
	private String[] sectionList;
	private int[] section_idList;
	private String[] specList;
	private int[] spec_idList;
	private ArrayAdapter<String> plAdapter;
	private ArrayAdapter<String> sectionAdapter;
	private ArrayAdapter<String> specAdapter;
	private Integer pl_id;
	private Integer section_id;
	private Integer spec_id;
	private ProgressDialog dialog = null;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					plList = new String[plArray.length()];
					pl_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						plList[i] = plArray.getJSONObject(i).getString("name");
						pl_idList[i] = plArray.getJSONObject(i).getInt("id");
					}
					plAdapter = new ArrayAdapter<String>(
							High_DCA_Information_Create.this,
							R.layout.spinner_style, plList);
					plAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_pl.setAdapter(plAdapter);
					sp_pl.setSelection(0, true);
					pl_id = pl_idList[0];
					sp_pl.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (msg.what == 2) {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					sectionList = new String[plArray.length()];
					section_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						sectionList[i] = plArray.getJSONObject(i).getString(
								"name");
						section_idList[i] = plArray.getJSONObject(i).getInt(
								"id");
					}
					sectionAdapter = new ArrayAdapter<String>(
							High_DCA_Information_Create.this,
							R.layout.spinner_style, sectionList);
					sectionAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_section.setAdapter(sectionAdapter);
					sp_section.setSelection(0, true);
					section_id = section_idList[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (msg.what == 3) {
				try {
					JSONArray plArray = (JSONArray) msg.obj;
					specList = new String[plArray.length()];
					spec_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						specList[i] = plArray.getJSONObject(i)
								.getString("name");
						spec_idList[i] = plArray.getJSONObject(i).getInt("id");
					}
					specAdapter = new ArrayAdapter<String>(
							High_DCA_Information_Create.this,
							R.layout.spinner_style, specList);
					specAdapter
							.setDropDownViewResource(R.layout.spinner_drop_down);
					sp_spec.setAdapter(specAdapter);
					sp_spec.setSelection(0, true);
					spec_id = spec_idList[0];
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.high_dca_information_create);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.setCancelable(false);
		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					High_DCA_Information_Create.this.finish();
				}
				return false;
			}
		});
		
		// 管线下拉列表点击事件
		sp_pl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				AdapterUtil.updateAdapter(sp_section, sectionAdapter,
						High_DCA_Information_Create.this);
				AdapterUtil.updateAdapter(sp_spec, specAdapter,
						High_DCA_Information_Create.this);
				section_id = null;
				spec_id = null;
				sectionList = null;
				section_idList = null;
				if (pl_idList != null) {
					pl_id = pl_idList[arg2];

					plRefresh(Urls.URL + "services/base/section/get", "pl_id="
							+ pl_id, 2);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// seltion_idList = listener.getIdList();

		// 管段下拉列表点击时间
		sp_section.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				AdapterUtil.updateAdapter(sp_spec, specAdapter,
						High_DCA_Information_Create.this);
				spec_id = null;
				specList = null;
				spec_idList = null;
				if (section_idList != null) {
					section_id = section_idList[arg2];
					plRefresh(Urls.URL + "services/base/spec/get",
							"pl_section_id=" + section_id, 3);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		// sp_pl.setOnClickListener();

		sp_spec.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (spec_idList != null) {
					spec_id = spec_idList[arg2];
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		// 加载管线列表内容
		plRefresh(Urls.URL + "services/base/pipeline/get", "", 1);
	}

	@Override
	protected void onDestroy() {

		ThreadKILL.killthread(newThread);

		super.onDestroy();

	}

	public void plRefresh(final String url, final String params, final int type) {
		newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				String repl = HttpRequestUtil.sendGet(url, params,
						(OilApplication) getApplication());
				if (repl.equals("error")) {
					Message msg = mHandler.obtainMessage();
					msg.obj = "请检查网络";
					mHandler.sendMessage(msg);
				} else {
					try {
						JSONObject reJson = JSONUtil.stringToJson(repl);
						status = reJson.getString("status");
						if (status.equals("200")) {
							JSONArray plArray = reJson.getJSONArray("data");
							Message msg = mHandler.obtainMessage();
							msg.what = type;
							msg.obj = plArray;
							mHandler.sendMessage(msg);
						} else {
							String message = reJson.getString("message");
							Message msg = mHandler.obtainMessage();
							msg.obj = message;
							mHandler.sendMessage(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		});
		newThread.start(); // 启动线程

	}

	public void backlistener(View V) {
		finish();
	}

	public void nextlistener(View V) {
		if(newThread != null && newThread.isAlive()) {
			Toast.makeText(getApplicationContext(), "正在加载，请稍等...", Toast.LENGTH_SHORT).show();
			return;
		}
		if (pl_id == null) {
			Toast.makeText(getApplicationContext(), "请选择管线名称", Toast.LENGTH_SHORT).show();
		} else if (section_id == null) {
			Toast.makeText(getApplicationContext(), "请选择起止段落", Toast.LENGTH_SHORT).show();

		} else if (spec_id == null) {
			Toast.makeText(getApplicationContext(), "请选择管线规格", Toast.LENGTH_SHORT).show();
		} else {
			High_Dca_Information_Create_Bean bean = new High_Dca_Information_Create_Bean();
			bean.setPl(pl_id);
			bean.setSection(section_id);
			bean.setSpec(spec_id);
			Intent intent = new Intent(High_DCA_Information_Create.this,
					High_DCA_Information_Create_NEXT.class);
			intent.putExtra("params", bean.toString());
			startActivity(intent);
			finish();

		}

	}
}
