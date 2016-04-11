package com.oil.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.oil.ResideMenu.ResideMenu;
import com.oil.ResideMenu.ResideMenuItem;
import com.oil.activity.HighStaticCreateOneAct;
import com.oil.activity.High_DCA_Information_Create;
import com.oil.activity.MainMenu;
import com.oil.activity.R;
import com.oil.activity.SearchAct;
import com.oil.adapter.MyViewPagerAdapter;
import com.oil.dialog.CustiomViewPager;

public class HighFragment extends Fragment implements OnClickListener{
	private RadioButton rbDynamic,rbStatic;
	private CustiomViewPager vp;
	private Button create;
	private ArrayList<Fragment> list;
	private boolean judge = true,isSearch = false;
	private String searchData = new String();
	private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    View highLayout;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		highLayout = LayoutInflater.from(getActivity()).inflate(R.layout.high_consquence_tabhost, null);
		init(highLayout);
		setUpMenu();
		rbDynamic.setChecked(true);
		rbDynamic.setOnClickListener(this);
		rbStatic.setOnClickListener(this);

		return highLayout;
	}

	private void init(View v) {
		rbDynamic = (RadioButton) v.findViewById(R.id.high_radio_dynamic);
		rbStatic = (RadioButton) v.findViewById(R.id.high_radio_static);
		create = (Button) v.findViewById(R.id.high_cons_create);
		vp = (CustiomViewPager) v.findViewById(R.id.highc_vp);
		
		list = new ArrayList<Fragment>();
		HighDynamicFragment hDynamic = new HighDynamicFragment();
		HighStaticFragment hStatic = new HighStaticFragment();
		list.add(hDynamic);
		list.add(hStatic);
		
		vp.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), list));
		vp.setOffscreenPageLimit(2);
		vp.setCurrentItem(0);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					rbDynamic.setChecked(true);
					break;
				case 1:
					rbStatic.setChecked(true);
					break;
				}
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.high_radio_dynamic:
			judge = true;
			vp.setCurrentItem(0);
			break;

		case R.id.high_radio_static:
			judge = false;
			vp.setCurrentItem(1);
			break;
			
		/*case R.id.high_cons_create:
			Intent intent = new Intent();
			if(judge){
				intent.setClass(getActivity(),High_DCA_Information_Create.class);
			}else{
				intent.setClass(getActivity(),HighStaticCreateOneAct.class);
			}
			startActivity(intent);
			break;*/
		}
	}
	
	//展开侧边菜单
		private void setUpMenu() { 
	        itemCalendar = new ResideMenuItem(getActivity(), R.drawable.bt_create, "新建");
	        itemSettings = new ResideMenuItem(getActivity(), R.drawable.bt_query, "查询");

	        //com.oil.dialog.CustiomViewPager ignored_view = (com.oil.dialog.CustiomViewPager) getActivity().findViewById(R.id.highc_vp);  
	        
	        //resideMenu.addIgnoredView(ignored_view);
	        
	        itemSettings.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),SearchAct.class);
					intent.putExtra("dateType",3);
					intent.putExtra("type",10);
					startActivityForResult(intent,200);
					MainMenu.closeMenu();
				}
			});
	        
	        itemCalendar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					if(judge){
						intent.setClass(getActivity(),High_DCA_Information_Create.class);
					}else{
						intent.setClass(getActivity(),HighStaticCreateOneAct.class);
					}
					startActivity(intent);
					MainMenu.closeMenu();
				}
			});
	        //itemSettings.setOnClickListener(getActivity());
	        
	        final List<ResideMenuItem> resideList = new ArrayList<ResideMenuItem>();
	        resideList.add(itemCalendar);
	        resideList.add(itemSettings);
	        
	        // You can disable a direction by sett
	        create.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	            	//Toast.makeText(getActivity().getApplicationContext(), "点击了弹出", 100).show();
	                MainMenu.setResideMenuItem(resideList);
	                MainMenu.openMenu();
	            }
	        });
	    }

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if(resultCode == 20){
				isSearch = true;
				Intent intent = new Intent();
				intent.setAction("actionSearch");
				intent.putExtra("isSearch",isSearch);
				intent.putExtra("which",judge);
				intent.putExtra("searchData",data.getStringExtra("searchData"));
				getActivity().sendBroadcast(intent);
				Log.e("Tag",data.getStringExtra("searchData"));
			}
		}
		
		@Override
		public void onResume() {
			super.onResume();
			
		}
		
}
