package com.oil.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.oil.domain.GetIdName;

public class FilterUtil {

	public static String[] filterName(List<GetIdName> list) {
		String[] name = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			name[i] = list.get(i).getName();
		}
		return name;
	}

	public static String[] getvalue(HashMap<String, String[]> mapPU,
			String position) {
		Iterator itMapPU = mapPU.entrySet().iterator();
		while (itMapPU.hasNext()) {
			Map.Entry entry = (Map.Entry) itMapPU.next();
			if (position.equals(entry.getKey().toString())) {
				return (String[]) entry.getValue();
			}
		}
		return null;
	}

	public static boolean isRepeatPosition(HashMap<String, String[]> mapPU,
			String position) {
		Iterator itMapPU = mapPU.entrySet().iterator();
		while (itMapPU.hasNext()) {
			Map.Entry entry = (Map.Entry) itMapPU.next();
			if (position.equals(entry.getKey().toString())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isRepeatMonth(HashMap<String, String> map,
			String month) {
		Iterator itMapPU = map.entrySet().iterator();
		while (itMapPU.hasNext()) {
			Map.Entry entry = (Map.Entry) itMapPU.next();
			if (month.equals(entry.getKey().toString())) {
				return true;
			}
		}
		return false;
	}

	public static String getValuePot(HashMap<String, String> map, String month) {
		Iterator itMapPU = map.entrySet().iterator();
		while (itMapPU.hasNext()) {
			Map.Entry entry = (Map.Entry) itMapPU.next();
			if (month.equals(entry.getKey().toString())) {
				return entry.getValue().toString();
			}
		}
		return "";
	}

	public static String getPath(Context context, Uri uri) {

		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

}
