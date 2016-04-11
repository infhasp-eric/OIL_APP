package com.oil.dialog;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

/**
 * 仅选择年月
 * @author Eric
 *
 */
public class CustomerDatePickerDialog extends DatePickerDialog {
	public CustomerDatePickerDialog(Context context,  
            OnDateSetListener callBack, int year, int monthOfYear,  
            int dayOfMonth) {  
        super(context, callBack, year, monthOfYear, dayOfMonth);  
    }  
  
    @Override  
    public void onDateChanged(DatePicker view, int year, int month, int day) {  
        super.onDateChanged(view, year, month, day);
    }
    
    public static DatePicker findDatePicker(ViewGroup group) {  
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
}
