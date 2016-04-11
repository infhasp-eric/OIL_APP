package com.oil.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.adapter.ReplieAdapter;
import com.oil.layout.MyListView;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ReplyType;
import com.oil.utils.StringUtil;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

public class TmpNoticeDetailActivity extends FinalActivity {
	@ViewInject(id=R.id.bt_back, click="backClick") Button bt_back;
	@ViewInject(id=R.id.bt_reply, click="replyClick") Button bt_reply;
	@ViewInject(id=R.id.txt_author) TextView txt_author;
	@ViewInject(id=R.id.web_show) WebView web_show;
	@ViewInject(id=R.id.txt_postil) TextView txt_postil;//批注
	@ViewInject(id=R.id.list_replies) MyListView list_replies;
	@ViewInject(id=R.id.lin_file) LinearLayout lin_file;
	@ViewInject(id=R.id.lin_reply) LinearLayout lin_reply;
	@ViewInject(id=R.id.txt_file) TextView txt_file;
	@ViewInject(id=R.id.txt_title) TextView txt_title;
	@ViewInject(id=R.id.bt_down, click="downFile") Button bt_down;
	@ViewInject(id=R.id.dialog_rl) RelativeLayout rl;
	@ViewInject(id=R.id.dialog_pro) RelativeLayout dialog_pro;
	
	private String id;
	private String serverUrl;
	private List<Map<String, Object>> listItems;
	private String result;
	private ReplieAdapter replieAdapter;
	private ScrollView sv;
	
	private Thread getDate = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			result = HttpRequestUtil.sendGet(serverUrl + "services/tmp_notice_detail", "id=" + id, (OilApplication)getApplication());
			try {
				JSONObject json = new JSONObject(result);
				if(json.getInt("status") == 200) {
					getHandler.sendMessage(getHandler.obtainMessage());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});
	private Handler getHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			try {
				JSONObject json = new JSONObject(result);
				JSONObject data = json.getJSONObject("data");
				JSONObject notice = data.getJSONObject("notice");
				txt_author.setText(notice.getString("author"));
				txt_postil.setText(notice.getString("postil").equals("null")?"无":notice.getString("postil"));
				txt_title.setText(notice.getString("title"));
				
				web_show.getSettings().setSupportZoom(true);
				web_show.setBackgroundColor(Color.WHITE);
			    //v.loadUrl("about:blank");

				web_show.clearCache(true);
				web_show.getSettings().setDefaultTextEncodingName("utf-8");
			    //v.loadData(new String(Temp).replaceAll("#", "%23").replaceAll("%",
			    //        "%25").replaceAll("\'", "%27"), "text/html", "utf-8");
			    //v.loadData(Temp, "text/html", "utf-8");
			    web_show.loadDataWithBaseURL(null,notice.getString("content"), "text/html", "utf-8",null);
				
				/*web_show.setScrollContainer(false);
				web_show.setScrollbarFadingEnabled(false);
				web_show.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
				WebSettings settings = web_show.getSettings();
				settings.setDefaultTextEncodingName("UTF-8") ;
				settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				settings.setBuiltInZoomControls(false); // 设置显示缩放按钮
				settings.setSupportZoom(false); // 支持缩放
				web_show.loadDataWithBaseURL("", notice.getString("content"), "text/html", "utf-8", null);
				*///web_show.setTextSize((float) 18.5);
				//web_show.setText(Html.fromHtml(notice.getString("content")));
				Integer status = notice.getInt("status");
				if(status == 1) {
					//状态为打开
					lin_reply.setVisibility(View.VISIBLE);
				} else {
					//状态为关闭
					lin_reply.setVisibility(View.GONE);
				}
				if(StringUtil.isBlank(notice.getString("path")) || notice.getString("path").equals("null")) {
					lin_file.setVisibility(View.GONE);
				} else {
					txt_file.setText(notice.getString("path"));
					fileUrl = notice.getString("path");
					lin_file.setVisibility(View.VISIBLE);
				}
				updateList(data.getJSONArray("replies"));
				replieAdapter = new ReplieAdapter(TmpNoticeDetailActivity.this,listItems);
				list_replies.setAdapter(replieAdapter);
				// 更新
				list_replies.invalidate();
				sv.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dialog_pro.setVisibility(View.GONE);
		}
	};
	private String fileUrl;
	private boolean isSecond = false;
	
	public static TmpNoticeDetailActivity instan;
	
	private boolean fileDown;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(fileDown){
				Toast.makeText(getApplicationContext(),"下载成功！",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(),"下载失败，文件已存在！",Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tmp_notice_detail);
		sv = (ScrollView) findViewById(R.id.tmp_notice_scroll);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		instan = this;
		
		serverUrl = Urls.URL;
		/*获取Bundle*/
        Bundle bundle = this.getIntent().getExtras();
        
        /*通过key值从bundle中取值*/
        id = bundle.getString("id");
        
        getDate.start();
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(isSecond) {
			refresh();
		}
	}
	
	private void refresh() {
        //Toast.makeText(getApplicationContext(), "保存成功", 1000).show();
		isSecond = false;
        finish();
        Intent intent = new Intent(TmpNoticeDetailActivity.this, TmpNoticeDetailActivity.class);
        Bundle bundle = new Bundle();
		bundle.putString("id", id);
		//bundle.put
		intent.putExtras(bundle);
        startActivity(intent);
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
	
	public void updateList(JSONArray jarray) {
		listItems = new ArrayList<Map<String,Object>>();
		try {
			for(int i = 0; i < jarray.length(); i++) {
				JSONObject json = jarray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("replier", json.getString("replier"));
				map.put("content", json.getString("content"));
				map.put("filename", json.getString("path") + "");
				listItems.add(map);
				//System.out.println(i + "==========第i条回复");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(rl.isShown()){
				rl.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public void backClick(View v) {
		finish();
	}
	
	public void replyClick(View v) {
		Intent intent = new Intent(TmpNoticeDetailActivity.this, NotiStuReply.class);
		intent.putExtra("id",id + "");
		System.out.println(intent.getExtras().get("id") + "=================");
		intent.putExtra("ReplyType", ReplyType.TMP_NOTICE);
		startActivity(intent);
		isSecond = true;
	}
	
	public void downFile(View v) {
		try {
			//通过调用浏览器进行下载
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.SAVEFILE + fileUrl));
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void onDestroy() {
		ThreadKILL.killthread(getDate);
		super.onDestroy();
	}
}
