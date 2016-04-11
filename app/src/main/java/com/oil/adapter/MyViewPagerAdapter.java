package com.oil.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragList;
	
	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragList) {
		super(fm);
		this.fragList = fragList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragList.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

}
