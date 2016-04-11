package com.oil.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.oil.activity.R;

public class BaseFragment extends Fragment {
	
	private LinearLayout Layout1, Layout2, Layout3, Layout4, Layout5, Layout6,
	Layout7, Layout8, Layout9;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		View baseLayout = LayoutInflater.from(getActivity()).inflate(R.layout.activity_basedata, null);
		init(baseLayout);
		return baseLayout;
	}

	private void init(View v) {
		Layout1 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main1);
		Layout2 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main2);
		Layout3 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main3);
		Layout4 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main4);
		Layout5 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main5);
		Layout6 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main6);
		Layout7 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main7);
		Layout8 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main8);
		Layout9 = (LinearLayout) v.findViewById(R.id.jichuziliao_Layout_main9);
		// 管线保护点位测量记录
		Layout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 1);
				startActivity(intent);
			}
		});
		// 管道保护点位曲线图
		Layout2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 2);
				startActivity(intent);
			}
		});
		// 绝缘接头性能测量记录
		Layout3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 3);
				startActivity(intent);
			}
		});
		// 阴极保护站运行月综合记录
		Layout4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 4);
				startActivity(intent);
			}
		});
		// 阴极保护站运行记录
		Layout5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 5);
				startActivity(intent);
			}
		});
		// 集输气管线附属设施维护记录
		Layout6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 6);
				startActivity(intent);
			}
		});
		// 管线巡检工作记录
		Layout7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 7);
				startActivity(intent);
			}
		});
		// 阀室阀井巡检工作记录
		Layout8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 8);
				startActivity(intent);
			}
		});
		// 阀室阀井维护保养工作记录
		Layout9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BaseDataFragment.class);
				intent.putExtra("type", 9);
				startActivity(intent);
			}
		});
		
	}
}
