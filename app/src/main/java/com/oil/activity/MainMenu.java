package com.oil.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.Utils;
import com.oil.ResideMenu.ResideMenu;
import com.oil.ResideMenu.ResideMenuItem;
import com.oil.adapter.MyViewPagerAdapter;
import com.oil.dialog.CheckDialog;
import com.oil.dialog.CustiomViewPager;
import com.oil.dialog.DialogActivity;
import com.oil.fragment.BaseFragment;
import com.oil.fragment.HighFragment;
import com.oil.fragment.OneFragment;
import com.oil.fragment.StudyFragment;
import com.oil.fragment.TemporaryFragment;
import com.oil.utils.GetSessionService;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.Urls;

public class MainMenu extends FragmentActivity implements OnClickListener {
	private RadioButton studyBtn, baseBtn, oneBtn, tempBtn, highBtn;
	private ViewPager vp;
	private ArrayList<Fragment> fragList;
	private FragmentManager fm;
	public static Activity MM;
	private CheckDialog checkDialog;
	StudyFragment study;
	BaseFragment base;
	TemporaryFragment temp;
	OneFragment one;
	HighFragment high;
	private static ResideMenu resideMenu;
	
	private Handler tostHand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String url = msg.obj.toString();
			System.out.println("=======运行到这里==================");
			checkDialog = new CheckDialog(MainMenu.this,R.style.CheckDialog, url);
			checkDialog.show();
			checkDialog.setCancelable(false);
		}
	};

    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_menu);
		
		Utils.logStringCache = Utils.getLogText(getApplicationContext());
		/*PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(MainMenu.this, "api_key"));*/

		MM = this;
		fm = getSupportFragmentManager();
		setUpMenu();
		initWidget();
		addListener();
		checkEdition();
//		vp.setFocusable(false);
	}
	
	private void checkEdition() {
		Thread checkTh = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String res = HttpRequestUtil.getDataFromServer(Urls.URL + "services/upgrade/info");
					JSONObject json = new JSONObject(res);
					JSONObject data = json.getJSONArray("data").getJSONObject(0);
					String web_ver = data.getString("version");
					String app_ver = getVersionName();
					if(!app_ver.equals(web_ver)) {
						Message msg = tostHand.obtainMessage();
						msg.obj = data.get("update_path");
						tostHand.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		checkTh.start();
	}

	private void initWidget() {
		studyBtn = (RadioButton) findViewById(R.id.menu_study);
		baseBtn = (RadioButton) findViewById(R.id.menu_base);
		oneBtn = (RadioButton) findViewById(R.id.menu_one);
		highBtn = (RadioButton) findViewById(R.id.menu_high);
		tempBtn = (RadioButton) findViewById(R.id.menu_temp);
		studyBtn.setOnClickListener(this);
		baseBtn.setOnClickListener(this);
		oneBtn.setOnClickListener(this);
		highBtn.setOnClickListener(this);
		tempBtn.setOnClickListener(this);

		vp = (ViewPager) findViewById(R.id.menu_vp);
		fragList = new ArrayList<Fragment>();
		study = new StudyFragment();
		base = new BaseFragment();
		temp = new TemporaryFragment();
		one = new OneFragment();
		high = new HighFragment();
		fragList.add(study);
		fragList.add(base);
		fragList.add(temp);
		fragList.add(one);
		fragList.add(high);

		vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),
				fragList));
		vp.setOffscreenPageLimit(5);
		vp.setCurrentItem(0);
		studyBtn.setChecked(true);
//		 vp.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.menu_study:
			vp.setCurrentItem(0);
			break;
		case R.id.menu_base:
			vp.setCurrentItem(1);
			break;
		case R.id.menu_temp:
			vp.setCurrentItem(2);
			break;
		case R.id.menu_one:
			vp.setCurrentItem(3);
			break;
		case R.id.menu_high:
			vp.setCurrentItem(4);
			break;
		}
	}

	private void addListener() {
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int id) {
				switch (id) {
				case 0:
					studyBtn.setChecked(true);
					break;
				case 1:
					baseBtn.setChecked(true);
					break;
				case 2:
					tempBtn.setChecked(true);
					break;
				case 3:
					oneBtn.setChecked(true);
					break;
				case 4:
					highBtn.setChecked(true);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
//		vp.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				return true;
//			}
//		});
	}

	// 返回按钮监听 两秒内双加返回两次则退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			exitBy2Click();
			return true;
		}
		// else if (keyCode == KeyEvent.KEYCODE_MENU) {
		// super.openOptionsMenu();
		// return true;
		// }
		return false;
	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if(resideMenu != null && resideMenu.isOpened()) {
			resideMenu.closeMenu();
		} else {
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);

		} else {
			((OilApplication) getApplication()).setJSESSIONID(null);
			finish();
			System.exit(0);
		}
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// 在这里做你想做的事情

		super.onOptionsMenuClosed(menu);
	}

	@Override
	public void onDestroy() {
		Utils.setLogText(getApplicationContext(), Utils.logStringCache);
		stopService(LoginActivity.getSessionIntent);
		stopService(LoginActivity.gpsIntent);
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU){
			Intent intent = new Intent(MainMenu.this,DialogActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
	private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(getApplicationContext());
        resideMenu.setBackground(R.color.black);
        resideMenu.attachToActivity(this);
        //resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.8f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        //resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // create menu items;
        //为某区域禁止滑动
        ViewPager ignored_view = (ViewPager) findViewById(R.id.menu_vp);
        LinearLayout lin_raido = (LinearLayout) findViewById(R.id.lin_radio);
        resideMenu.addIgnoredView(ignored_view); 
        resideMenu.addIgnoredView(lin_raido);
        
        // You can disable a direction by sett
        
    }
	
	/**
	 * 为侧边菜单设置菜单项列表
	 * @param resideList
	 */
	public static void setResideMenuItem(List<ResideMenuItem> resideList) {
        	resideMenu.setMenuItems(resideList, ResideMenu.DIRECTION_RIGHT);
	}
	
	/**
	 * 打开侧边菜单
	 */
	public static void openMenu() {
		resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
	}
	
	/**
	 * 关闭侧边菜单
	 */
	public static void closeMenu() {
		resideMenu.closeMenu();
	}
	
	/**
	 * 获取当前应用banbenhao
	 * @return
	 * @throws Exception
	 */
	private String getVersionName() throws Exception{
	    // 获取packagemanager的实例
	    android.content.pm.PackageManager packageManager = getPackageManager();
	    // getPackageName()是你当前类的包名，0代表是获取版本信息
	    android.content.pm.PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
	    String version = packInfo.versionName;
	    return version;
	}
}
