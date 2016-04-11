package com.oil.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.oil.activity.R;

public class AdapterUtil {
	
	public static void updateAdapter(Spinner sp, ArrayAdapter<String> adapter, Context ctx) {
		String[] sList = new String[] { "" };
		adapter = new ArrayAdapter<String>(ctx,
				R.layout.spinner_style, sList);
		adapter.setDropDownViewResource(R.layout.spinner_drop_down);
		sp.setAdapter(adapter);
	}
	
	public static void updateSearchAdapter(Spinner sp,int count, ArrayAdapter<String> adapter, Context ctx) {
		String[] sList = null;
		if(count == 1){
			sList = new String[] { "全部管线" };
		}else if(count == 2){
			sList = new String[] { "全部管段" };
		}else if(count == 3){
			sList = new String[] { "全部规格" };
		}
		try {
			adapter = new ArrayAdapter<String>(ctx,R.layout.spinner_style, sList);
			adapter.setDropDownViewResource(R.layout.spinner_drop_down);
			sp.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
