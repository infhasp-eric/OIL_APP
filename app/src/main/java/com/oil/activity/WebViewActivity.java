package com.oil.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oil.utils.SimpleClient;
import com.oil.utils.Urls;

public class WebViewActivity extends FinalActivity {
	WebView mywebview;
	private SharedPreferences pre;
	private Handler listHand;
	private String session;
	private String url;
	CookieManager cookieManager;
	private LinearLayout ll;
	
	private Handler openHand = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			CookieSyncManager.createInstance(WebViewActivity.this);
			CookieManager cookieManager = CookieManager.getInstance();
			// Cookie sessionCookie = Utils.appCookie;
			//将session放入请求中
			cookieManager.setCookie(url, "JSESSIONID=" + session);
			Log.e("Tag","sessionCoo ="+session);
			//System.out.println(session);
			CookieSyncManager.getInstance().sync();
			
			/*.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});*/
			mywebview.loadUrl(url);
		}
	};
	
	private Handler tostHand = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			Toast.makeText(getApplicationContext(), msg.obj.toString(), 1).show();
			finish();
		}
	};
	/*
	private Handler finHand = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
		}
	};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web_view);
		
		ll = (LinearLayout) findViewById(R.id.la_show);
		mywebview = (WebView) findViewById(R.id.webshow);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		System.out.println(url + "======================");

//		mywebview =(WebView) findViewById(R.id.web_show);
		
		/*LoadWebViewTask task = new LoadWebViewTask();
		task.execute();*/

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				openWebView();
			}
		}).start();
		
		WebSettings settings = mywebview.getSettings();
		//设置支持javascript
		settings.setJavaScriptEnabled(true);
		//设置自适应
		settings.setUseWideViewPort(true);
		// 设置可以支持缩放 
		settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(true); 
		// 设置出现缩放工具 
		settings.setBuiltInZoomControls(true);
		//扩大比例的缩放
		settings.setUseWideViewPort(true);
		
		mywebview.setWebChromeClient(new WebChromeClient() {
			
			public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                    view.loadUrl(url);
                    return false;
            }
			
			public void onProgressChanged(WebView view, int progress) {
				// Activity和Webview根据加载程度决定进度条的进度大小
				// 当加载到100%的时候 进度条自动消失
				setTitle("Loading...");
				setProgress(progress * 100);
				if(progress == 100){
					ll.setVisibility(View.GONE);
				}
			}
		});
		
	}

	private void openWebView() {
		if(getSession()) {
			System.out.println("session====" + session);
			openHand.sendMessage(openHand.obtainMessage());
		} else {
			Message msg = tostHand.obtainMessage();
			msg.obj = "网络不给力，请稍后再试";
			tostHand.sendMessage(msg);
		}
	}
	
	//获取web端session值
	public boolean getSession() {
		pre = getSharedPreferences("longinvalue", MODE_WORLD_WRITEABLE);
		NameValuePair username = new NameValuePair() {

			@Override
			public String getValue() {
				return pre.getString("name", "");
			}

			@Override
			public String getName() {
				return "j_username";
			}
		};
		NameValuePair j_password = new NameValuePair() {

			@Override
			public String getValue() {
				return pre.getString("pass", "");
			}

			@Override
			public String getName() {
				return "j_password";
			}
		};
		NameValuePair rmbUser = new NameValuePair() {

			@Override
			public String getValue() {
				return "on";
			}

			@Override
			public String getName() {
				return "rmbUser";
			}
		};

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(rmbUser);
		params.add(j_password);
		params.add(username);

		/*
		 * if(op.getWEBJSSIONID() == null) { Map<String, String> params1 = new
		 * HashMap<String, String>(); params1.put("j_username",
		 * op.getUsername()); params1.put("j_password", op.getPassword());
		 * HttpRequestUtil.HttpURLConnection_Post(Urls.URL + "authentication",
		 * (OilApplication)getApplication()); //String re =
		 * HttpRequestUtil.submitPostData(params1, "utf-8",Urls.URL +
		 * "authentication"); //String re =
		 * HttpRequestUtil.PostHttpLogin(Urls.URL + "authentication",
		 * (OilApplication) getApplication()); }
		 */

		try {
			SimpleClient.getHttpClient();
			session = SimpleClient.doPost(Urls.URL +  "authentication",params);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
//	// 返回按钮监听 两秒内双加返回两次则退出程序
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			Intent intent=new Intent(WebViewActivity.this,MainMenu.class);
//			startActivity(intent);
//			finish();
//			return true;
//		}
//		// else if (keyCode == KeyEvent.KEYCODE_MENU) {
//		// super.openOptionsMenu();
//		// return true;
//		// }
//		return false;
//	}
}
