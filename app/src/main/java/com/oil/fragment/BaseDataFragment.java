package com.oil.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.oil.ResideMenu.ResideMenu;
import com.oil.ResideMenu.ResideMenuItem;
import com.oil.activity.FMaintCreateActivity;
import com.oil.activity.InsulationCreate;
import com.oil.activity.MainActivity;
import com.oil.activity.PlCurveCreatActivity;
import com.oil.activity.R;
import com.oil.activity.RecordCreate;
import com.oil.activity.SearchAct;
import com.oil.activity.VMaintCreateActivity;
import com.oil.activity.VPatrolCreateActivity;

public class BaseDataFragment extends FragmentActivity implements View.OnClickListener{

	private View parentView;
	public static BaseDataFragment ctx;
	private ResideMenu resideMenu;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    private  boolean isSearch = false,isCreate = false;
    private Fragment fragment;
    private TextView tvTitle;
    private int type;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pl_curve_list);
        tvTitle = (TextView) findViewById(R.id.textView1);
        
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        
        ctx = this;
        setUpMenu();
        uploadList(type,"");
    }
	
	private void uploadList(int type,String data) {
		Bundle bundle = new Bundle();
        switch (type) {
		case 1:
			fragment = new PipeProtectionFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("管道保护电位测试记录");
			changeFragment(fragment);
			break;
		case 2:
			fragment = new PlCurveFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("管道保护电位曲线图");
			changeFragment(fragment);
			break;
		case 3:
			fragment = new InsulationFunctionFragemnt();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("绝缘接头（法兰）性能测试记录");
			changeFragment(fragment);
			break;
		case 4:
			fragment = new CathodeMonthFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("阴极保护站月综合记录");
			changeFragment(fragment);
			break;
		case 5:
			fragment = new CathodeRuntimeFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("阴极保护站运行记录");
			changeFragment(fragment);
			break;
		case 6:
			fragment = new FMaintFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("集输气管线附属设施台账");
			changeFragment(fragment);
			break;
		case 7:
			fragment = new PipePollingFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("管线巡检工作记录");
			changeFragment(fragment);
			break;
		case 8:
			fragment = new VpListFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("阀室、阀井巡检工作记录");
			changeFragment(fragment);
			break;
		case 9:
			fragment = new VmListFragment();
			bundle.putBoolean("isSearch",isSearch);
			bundle.putString("searchData",data);
			fragment.setArguments(bundle);
			tvTitle.setText("阀室、阀井维护保养工作记录");
			changeFragment(fragment);
			break;
		default:
			break;
		}
	}

	private void setUpMenu() {
		findViewById(R.id.bt_back).setOnClickListener(this);
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.color.black);
        resideMenu.attachToActivity(this);
        //resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.8f);

        // create menu items;
        itemCalendar = new ResideMenuItem(this, R.drawable.bt_query, "查询");
        itemSettings = new ResideMenuItem(this, R.drawable.bt_create, "新建");

        itemCalendar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BaseDataFragment.this,SearchAct.class);
				if(type == 3){
					intent.putExtra("dateType",1);
				}else if(type == 1||type == 2||type == 4||type == 5||type == 7){
					intent.putExtra("dateType",2);
				}else if(type == 6||type == 8||type == 9){
					intent.putExtra("dateType",3);
				}
				intent.putExtra("type",type);
				startActivityForResult(intent,100);
				resideMenu.closeMenu();
			}
		});
        itemSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isCreate = true;
				Intent intent = new Intent();
				switch (type) {
				case 1:
					intent = new Intent(BaseDataFragment.this,RecordCreate.class);
					intent.putExtra("judge", 1);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(BaseDataFragment.this,PlCurveCreatActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(BaseDataFragment.this,InsulationCreate.class);
					startActivity(intent);
					break;
				case 4:
					intent = new Intent(BaseDataFragment.this,RecordCreate.class);
					intent.putExtra("judge", 2);
					startActivity(intent);
					break;
				case 5:
					 intent = new Intent(BaseDataFragment.this, MainActivity.class);
					startActivity(intent);
					break;
				case 6:
					intent = new Intent(BaseDataFragment.this,FMaintCreateActivity.class);
					startActivity(intent);
					break;
				case 7:
					intent = new Intent(BaseDataFragment.this,RecordCreate.class);
					intent.putExtra("judge", 3);
					startActivity(intent);
					break;
				case 8:
					intent = new Intent(BaseDataFragment.this,VPatrolCreateActivity.class);
					startActivity(intent);
					break;
				case 9:
					intent = new Intent(BaseDataFragment.this,VMaintCreateActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
				isSearch = false;
				resideMenu.closeMenu();
			}
		});

        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        findViewById(R.id.bt_creat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }
	
	private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bt_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("result","resultcode="+resultCode);
		if(resultCode == 20){
			isSearch = true;
			Log.v("Tag","searchbasedata="+data.getStringExtra("searchData"));
			uploadList(type,data.getStringExtra("searchData"));
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isSearch&&isCreate){
			uploadList(type,"");
			isCreate = false;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(resideMenu.isOpened()) {
				resideMenu.closeMenu();
			} else {
				finish();
			}
			return true;
		}
		// else if (keyCode == KeyEvent.KEYCODE_MENU) {
		// super.openOptionsMenu();
		// return true;
		// }
		return false;
	}

}
