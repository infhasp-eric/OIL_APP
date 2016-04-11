package com.oil.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.dialog.LodingActivtyDialog;
import com.oil.layout.LineBreakLayout;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class AskNoticeCreatActivity extends FinalActivity {

	@ViewInject(id=R.id.bt_save, click="saveClick") Button bt_save;
	@ViewInject(id=R.id.bt_back, click="backClick") Button bt_back;
	@ViewInject(id=R.id.et_title) EditText et_title;
	@ViewInject(id=R.id.et_content) EditText et_content;
	@ViewInject(id=R.id.sp_type) Spinner sp_type;
	@ViewInject(id=R.id.bt_file, click="choseFile") Button bt_file;
	@ViewInject(id=R.id.rd_y) RadioButton rd_y;
	@ViewInject(id=R.id.rd_n) RadioButton rd_n;
	@ViewInject(id=R.id.rg_warn) RadioGroup rg_warn;
	@ViewInject(id=R.id.lin_check) LineBreakLayout lin_check;
	@ViewInject(id=R.id.lin_path) LinearLayout lin_path;
	@ViewInject(id=R.id.txt_path) TextView txt_path;
	
	private ArrayAdapter<String> typeAdapter;
	private String[] typeList = {"学习培训","临时信息申请","工程配合","请、销假申请","其他"};
	private Thread getThr;
	private Thread uploadThr;
	private Handler postHand;
	private Handler getHand;
	private String result;
	private List<CheckBox> checkList = new ArrayList<CheckBox>();
//	private List<Integer> idList = new ArrayList<Integer>();
	private AskNoticeCreatActivity asn;
	private Map<String, Integer> leaders = new HashMap<String, Integer>();
	private String ask_type = typeList[0];
	private String send = "1";;
	private String fileName = "";
	private String filePath = "";
	public static final int FILE_SELECT_CODE = 1;
	private ProgressDialog dialog = null;
	private LodingActivtyDialog lodingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ask_notice_creat);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.show();
		dialog.setCancelable(false);
		
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					dialog.dismiss();
					AskNoticeCreatActivity.this.finish();
				}
				return false;
			}
		});
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		asn = this;
		typeAdapter = new ArrayAdapter<String>(asn,R.layout.spinner_style, typeList);
		typeAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
		sp_type.setAdapter(typeAdapter);
		sp_type.setSelection(0, true);
		
		rd_y.setChecked(true);
		
		rg_warn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				if(rd_y.getId() == checkedId) {
					send = "1";
					//Toast.makeText(getApplicationContext(), "选择了是", 1000).show();
				} else {
					//Toast.makeText(getApplicationContext(), "选择了否", 1000).show();
					send = "0";
				}
			}
		});
		
		getThr = new Thread(new Runnable() {
			
			@Override
			public void run() {
				result = HttpRequestUtil.getDataFromServer(Urls.URL + "services/findLeader");
				System.out.println(result);
				try {
					JSONObject json = new JSONObject(result);
					if(json.getInt("status") == 200) {
						Message msg = getHand.obtainMessage();
						msg.what = 1;
						getHand.sendMessage(msg);
					}else{
						Message msg = getHand.obtainMessage();
						msg.what = 2;
						getHand.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = getHand.obtainMessage();
					msg.what = 3;
					getHand.sendMessage(msg);
				}
			}
		});
		getThr.start();
		
		getHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					try {
						JSONObject json = new JSONObject(result);
						JSONArray array = json.getJSONArray("data");
						for(int i = 0; i < array.length(); i++) {
							JSONObject leader = array.getJSONObject(i);
							System.out.println(leader.getString("receiver"));
							leaders.put(leader.getString("receiver"), leader.getInt("user_id"));
						}
						upList();
						dialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(msg.what == 2){
					JSONObject json;
					try {
						json = new JSONObject(result);
						String message = json.getString("message");
						Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else if(msg.what == 3){
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		sp_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ask_type = typeList[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				ask_type = typeList[0];
			}
			
		});
		

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				filePath = FilterUtil.getPath(this, uri);
				if (filePath != null) {
					lin_path.setVisibility(View.VISIBLE);
					txt_path.setText(filePath.substring(filePath.lastIndexOf("/")+1,filePath.length()));
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	
	public void backClick(View v) {
		finish();
	}
	
	public void saveClick(View v) {
		final Map<String, String> map = new HashMap<String, String>();
		String title = et_title.getText().toString();
		if(StringUtil.isBlank(title)) {
			Toast.makeText(getApplicationContext(), "请填写标题", Toast.LENGTH_SHORT).show();
			return;
		}
		map.put("title", title);
		String content = et_content.getText().toString();
		if(StringUtil.isBlank(content)) {
			Toast.makeText(getApplicationContext(), "请填写内容", Toast.LENGTH_SHORT).show();
			return;
		}
		map.put("content", content);
		String users = "";
		for(CheckBox cb : checkList) {
			if(cb.isChecked()) {
				if(!StringUtil.isBlank(users)) {
					users += ",";
				}
				users += leaders.get(cb.getText());
			}
		}
		if(StringUtil.isBlank(users)) {
			Toast.makeText(getApplicationContext(), "请选择技术干部", Toast.LENGTH_SHORT).show();
			return;
		}
		map.put("users", users);
		lodingDialog = new LodingActivtyDialog(AskNoticeCreatActivity.this);
		lodingDialog.show();
		lodingDialog.setCanceledOnTouchOutside(false);

		postHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				
				map.put("ask_type", ask_type);
				map.put("send", send);
				map.put("fileName", fileName);
				System.out.println(ParamsUtil.mapToParams(map));
				//Toast.makeText(getApplicationContext(), users, 1000).show();
				String result = HttpRequestUtil.PostHttp(Urls.URL + "services/ask_notice_save", ParamsUtil.mapToParams(map), (OilApplication)getApplication());
				System.out.println(result);
				try {
					JSONObject json = new JSONObject(result);
					if(json.getInt("status") == 200) {
						lodingDialog.cancel();
						Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
						
//						NoticeListActivity.nl.finish();
//						
//						Intent intent = new Intent(AskNoticeCreatActivity.this, NoticeListActivity.class);
//						Bundle bundle = new Bundle();
//						bundle.putInt("type", 2);
//						intent.putExtras(bundle);
//						startActivity(intent);
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
					lodingDialog.cancel();
					Toast.makeText(getApplicationContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
				}
				
				Intent intent = new Intent();
				intent.setAction("actionUpdate");
				intent.putExtra("updateType",2);
				sendBroadcast(intent);
			}
		};
		
		if(!StringUtils.isEmpty(filePath)) {
			uploadThr = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						String res = HttpRequestUtil.uploadFile(new File(filePath));
						System.out.println(res);
						JSONObject json = new JSONObject(res);
						if(json.getInt("status") == 200) {
							JSONArray array = json.getJSONArray("data");
							fileName = array.getString(0);
							postHand.sendMessage(postHand.obtainMessage());
						} else {
							lodingDialog.cancel();
							Toast.makeText(getApplicationContext(), "文件上传失败", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			
			uploadThr.start();
		} else {
			postHand.sendMessage(postHand.obtainMessage());
		}
		
	}
	
	public void choseFile(View v) {
		Intent intent = new Intent();
		intent.setType("*/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent,
					"Select a File to Upload"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(),
				"Please install a File Manager.",
				Toast.LENGTH_SHORT).show();
		}
	}
	
	public void upList() {
		if(leaders.size() > 0) {
			Set<String> names = leaders.keySet();
			
			//System.out.println("--------------------" + names.size() +"----------------------");
			for (String s : names) {
				System.out.println(s);
		        CheckBox cb = new CheckBox(this);
		        checkList.add(cb);
		        cb.setText(s);
		        lin_check.addView(cb);
		    }
		}
		
	}
	
	protected void onDestroy() {
		ThreadKILL.killthread(uploadThr);
		super.onDestroy();
	}
}
