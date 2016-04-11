package com.oil.adapter;

import java.util.Map;

import android.view.View;
import android.view.ViewGroup;

public interface NoticeAdapter {
	public void addItem(Map<String, Object> map);

	public int getCount();
}
