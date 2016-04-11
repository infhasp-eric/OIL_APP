package com.oil.dialog;

import java.lang.reflect.Field;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

public class MyDatePickerDialog extends DatePickerDialog {

	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.setTitle(year+"年"+(monthOfYear + 1) + "月" );
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.setTitle(year+"年"+(month + 1) + "月" );
	}
	
	@Override
	public void show() {
		super.show();
		DatePicker dp = findDatePicker((ViewGroup) this.getWindow().getDecorView());
		if(dp != null){
			Class c = dp.getClass();
			Field field;
			try {
				try {
					if(getAndroidSDKVersion() > 4.0){
						field = c.getDeclaredField("");
						field.setAccessible(true);
						LinearLayout l = (LinearLayout) field.get(dp);
						l.setVisibility(View.GONE);
					}else{
						field = c.getDeclaredField("");
						field.setAccessible(true);
						LinearLayout l = (LinearLayout) field.get(dp);
						l.setVisibility(View.GONE);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}
	
	private DatePicker findDatePicker(ViewGroup group){
		if (group != null) {  
            for (int i = 0, j = group.getChildCount(); i < j; i++) {  
                View child = group.getChildAt(i);  
                if (child instanceof DatePicker) {  
                    return (DatePicker) child;  
                } else if (child instanceof ViewGroup) {  
                    DatePicker result = findDatePicker((ViewGroup) child);  
                    if (result != null)  
                        return result;  
                }   
            }  
        }  
		return null;
	}
	
	private  int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}
}
