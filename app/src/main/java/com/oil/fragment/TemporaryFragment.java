package com.oil.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.oil.ResideMenu.ResideMenu;
import com.oil.ResideMenu.ResideMenuItem;
import com.oil.activity.AskNoticeCreatActivity;
import com.oil.activity.Itco_create_main;
import com.oil.activity.MainMenu;
import com.oil.activity.OilApplication;
import com.oil.activity.R;
import com.oil.activity.SearchAct;
import com.oil.activity.SearchKeyAct;
import com.oil.activity.SearchNotice;
import com.oil.dialog.CustiomViewPager;
import com.oil.utils.HttpRequestUtil;

public class TemporaryFragment extends Fragment implements OnClickListener {
	private RadioButton layout1, layout2;
	private FragmentManager fm;
	private NoticeListFragment nlf;
	private Button bt_creat;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;
    private boolean isSearch = false;
    private String searchData = new String();
    private int type = 1;
    private final List<ResideMenuItem> resideList = new ArrayList<ResideMenuItem>();
    private Intent intent;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_tmp_notice, null);
		layout1 = (RadioButton) view.findViewById(R.id.work);
		layout2 = (RadioButton) view.findViewById(R.id.instrus);
		bt_creat = (Button) view.findViewById(R.id.temp_create);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		
		intent = new Intent(getActivity(),SearchNotice.class);
		HttpRequestUtil.getSession((OilApplication) getActivity()
				.getApplication());

		setFragmentFlag(1);
		setUpMenu();
		layout1.setChecked(true);
		layout2.setChecked(false);
		resideList.add(itemSettings);
		bt_creat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainMenu.setResideMenuItem(resideList);
				if(type == 1){
					itemCalendar.setVisibility(View.GONE);
					itemSettings.setVisibility(View.VISIBLE);
				}else{
					itemCalendar.setVisibility(View.VISIBLE);
					itemSettings.setVisibility(View.VISIBLE);
				}
				MainMenu.openMenu();
			}
		});
		
		return view;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.work:
			bt_creat.setVisibility(View.VISIBLE);
			resideList.clear();
			setFragmentFlag(1);
			resideList.add(itemSettings);
			layout1.setChecked(true);
			layout2.setChecked(false);
			break;
		case R.id.instrus:
			bt_creat.setVisibility(View.VISIBLE);
			resideList.clear();
			setFragmentFlag(2);
			resideList.add(itemCalendar);
			resideList.add(itemSettings);
			layout1.setChecked(false);
			layout2.setChecked(true);
			break;
		default:
			break;
		}
	}

	private void setFragmentFlag(int i) {
		Log.e("Tag","setFragmentFlag");
		type = i;
		intent.putExtra("actionSearchKey",i);
		Bundle bundle = new Bundle();
		nlf = new NoticeListFragment();
		bundle.putInt("type", i);
		nlf.setArguments(bundle);
		getChildFragmentManager()
        .beginTransaction()
        .replace(R.id.content, nlf, "fragment")
        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .commit();
	}
	
	private void setUpMenu() {
        //添加按钮
        itemCalendar = new ResideMenuItem(getActivity(), R.drawable.bt_create, "新建");
        itemSettings = new ResideMenuItem(getActivity(), R.drawable.bt_query, "查询");

        itemSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(intent);
				MainMenu.closeMenu();
			}
		});
        
        itemCalendar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),AskNoticeCreatActivity.class);
				startActivity(intent);
				MainMenu.closeMenu();
			}
		});
    }
}
