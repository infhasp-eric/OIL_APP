package com.oil.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.oil.ResideMenu.ResideMenuItem;
import com.oil.activity.MainMenu;
import com.oil.activity.R;
import com.oil.activity.SearchKeyAct;
import com.oil.adapter.MyViewPagerAdapter;
import com.oil.dialog.CustiomViewPager;
import com.oil.dialog.DialogActivity;

public class StudyFragment extends Fragment implements OnClickListener {

	private RadioButton rbStu, rbNot;
	private CustiomViewPager vp;
	private ArrayList<Fragment> list;
	private Button Menu,Menu2;
    private ResideMenuItem itemSettings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.noti_stu_tabhost, null);
		init(v);
		setUpMenu();
//		if(!judgeMenu){
//			resideMenu.setVisibility(View.GONE);
//		}
		Menu.setVisibility(View.GONE);
		rbNot.setChecked(true);
		rbNot.setOnClickListener(this);
		rbStu.setOnClickListener(this);
		Menu.setOnClickListener(this);
		Menu2.setOnClickListener(this);
		return v;
	}

	private void init(View v) {
		rbNot = (RadioButton) v.findViewById(R.id.tab_notification);
		rbStu = (RadioButton) v.findViewById(R.id.tab_study);
		vp = (CustiomViewPager) v.findViewById(R.id.tab_viewpager);
		Menu = (Button) v.findViewById(R.id.tab_menu);
		Menu2= (Button) v.findViewById(R.id.tab_menu2);
		//Menu2.setFocusable(true);
		list = new ArrayList<Fragment>();
		StuFragment study = new StuFragment();
		NotiFragment noti = new NotiFragment();
		list.add(noti);
		list.add(study);

		vp.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), list));
		vp.setOffscreenPageLimit(2);
		vp.setCurrentItem(0);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					rbNot.setChecked(true);
					Menu.setVisibility(View.GONE);
					Menu2.setVisibility(View.VISIBLE);
					Menu2.setFocusableInTouchMode(true);
					Menu.setFocusable(false);
					Menu2.setFocusable(true);
					break;
				case 1:
					rbStu.setChecked(true);
					Menu.setVisibility(View.VISIBLE);
					Menu2.setVisibility(View.GONE);
					Menu.setFocusable(true);
					Menu2.setFocusable(false);
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
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tab_notification:
			//MainMenu.closeMenu();
			Menu.setVisibility(View.GONE);
			vp.setCurrentItem(0);
			break;

		case R.id.tab_study:
//			resideMenu.setVisibility(View.VISIBLE);
			Menu.setVisibility(View.VISIBLE);
			vp.setCurrentItem(1);
			break;
		case R.id.tab_menu:
			System.out.println("菜单");
//			startActivity(new Intent(getActivity(), DialogActivity.class));
			final List<ResideMenuItem> resideList = new ArrayList<ResideMenuItem>();
	        resideList.add(itemSettings);
	        MainMenu.setResideMenuItem(resideList);
			MainMenu.openMenu();
			break;
		case R.id.tab_menu2:
			System.out.println("系统菜单");
			Intent intent = new Intent(getActivity(),DialogActivity.class);
			startActivity(intent);
			break;	
			
			
		}
	}
	
	private void setUpMenu() {
        // create menu items;
        //添加按钮
        itemSettings = new ResideMenuItem(getActivity(), R.drawable.bt_query, "查询");

        itemSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),SearchKeyAct.class);
				intent.putExtra("actionSearchKey",3);
				startActivity(intent);
				MainMenu.closeMenu();
			}
		});
        
        //itemSettings.setOnClickListener(getActivity());
        // You can disable a direction by setting ->
    }
}
