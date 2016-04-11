package com.oil.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.oil.domain.GetIdName;
import com.oil.utils.AdapterUtil;
import com.oil.utils.FilterUtil;
import com.oil.utils.HttpGetDataByGet;
import com.oil.utils.JSONParse;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class HighStaticCreateOneAct extends Activity {
	
	private Spinner pipe,section,spec;
	private List<GetIdName> list = new ArrayList<GetIdName>();
	private List<GetIdName> listS = new ArrayList<GetIdName>();
	private List<GetIdName> listSp = new ArrayList<GetIdName>();
	private ArrayAdapter<String> adapter;
	private String[] lineName,sectionName,specName;
	private Integer plId,sectionId,specId;
	public static HighStaticCreateOneAct highStaticInstance = null;
	private ProgressDialog dialog = null;
	private Thread spinnerThread;
	private StringBuffer sb = new StringBuffer();
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				try {
					list = JSONParse.parseIdName(msg.obj.toString());
					lineName = new String[list.size()];
					lineName = FilterUtil.filterName(list);
					adapter = new ArrayAdapter<String>(HighStaticCreateOneAct.this,
							R.layout.spinner_style, lineName);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					pipe.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(msg.what == 2){
				try {
					listS = JSONParse.parseIdName(msg.obj.toString());
					sectionName = new String[listS.size()];
					sectionName = FilterUtil.filterName(listS);
					adapter = new ArrayAdapter<String>(HighStaticCreateOneAct.this,R.layout.spinner_style,sectionName);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					section.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(msg.what == 3){
				try {
					listSp = JSONParse.parseIdName(msg.obj.toString());
					specName = new String[listSp.size()];
					specName = FilterUtil.filterName(listSp);
					adapter = new ArrayAdapter<String>(HighStaticCreateOneAct.this,R.layout.spinner_style,specName);
					adapter.setDropDownViewResource(R.layout.spinner_drop_down);
					spec.setAdapter(adapter);
					dialog.dismiss();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "请检查您的网络",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_high_consquence_one);
		highStaticInstance =this;
		initWidget();
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("数据加载中，请稍后...");
		dialog.show();
		dialog.setCancelable(false);
		startSubThread(Urls.PL,1);
		AdapterUtil.updateAdapter(pipe, adapter, highStaticInstance);
		AdapterUtil.updateAdapter(section, adapter, highStaticInstance);
		AdapterUtil.updateAdapter(spec, adapter, highStaticInstance);
		
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				 if (arg1 == KeyEvent.KEYCODE_BACK && arg2.getAction() == KeyEvent.ACTION_DOWN) {
		                dialog.dismiss();
		                HighStaticCreateOneAct.this.finish();
		            }
		            return false;
			}
		});
		
		findViewById(R.id.high_consquence_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		pipe.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(section, adapter, highStaticInstance);
				AdapterUtil.updateAdapter(spec, adapter, highStaticInstance);
				try {
					for(int i = 0; i < list.size(); i++){
						if(list.get(i).getName().equals(parent.getItemAtPosition(position).toString())){
							plId = list.get(i).get_id();
							startSubThread(Urls.SECTION + plId, 2);
							break;
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		section.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AdapterUtil.updateAdapter(spec, adapter, highStaticInstance);
				try {
					for(int i = 0; i < listS.size(); i++){
						if(listS.get(i).getName().equals(parent.getItemAtPosition(position).toString())){
							sectionId = listS.get(i).get_id();
							startSubThread(Urls.SPEC + sectionId, 3);
							break;
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		spec.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					for(int i = 0; i < listSp.size(); i++){
						if(listSp.get(i).getName().equals(parent.getItemAtPosition(position).toString())){
							specId = listSp.get(i).get_id();
							break;
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"请检查您的网络",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		findViewById(R.id.high_consquence_next).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 判断三个管线id是否都有复制
				if(spinnerThread != null && spinnerThread.isAlive()) {
					Toast.makeText(getApplicationContext(), "正在加载，请稍等...", 200).show();
					return;
				}
				if (plId == null || plId.intValue() <= 0) {
					Toast.makeText(getApplicationContext(), "请选择管线", 200).show();
					return;
				} else if (sectionId == null || sectionId.intValue() <= 0) {
					Toast.makeText(getApplicationContext(), "请选择管段", 200).show();
					return;
				} else if (specId == null || specId.intValue() <= 0) {
					Toast.makeText(getApplicationContext(), "请选择管线规格", 200).show();
					return;
				}
				Intent intent =new Intent(HighStaticCreateOneAct.this,HighStaticCreateTwoAct.class);
				sb.append("pl="+plId);
				sb.append("&section="+sectionId);
				sb.append("&spec="+specId);
				intent.putExtra("str",sb.toString());
				sb = new StringBuffer();
				startActivity(intent);
			}
		});
	}
	private void initWidget() {
		pipe = (Spinner) findViewById(R.id.pipe_high_consquence);
		section = (Spinner) findViewById(R.id.para_high_consquence);
		spec = (Spinner) findViewById(R.id.spec_high_consquence);
	}
	private void startSubThread(final String str, final int i) {
		spinnerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = HttpGetDataByGet.getDataFromServer(str,((OilApplication)getApplication()).getJSESSIONID());
				msg.what = i;
				handler.sendMessage(msg);
			}
		});
		spinnerThread.start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ThreadKILL.killthread(spinnerThread);
	}
}
