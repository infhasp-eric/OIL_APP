package com.oil.adapter;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oil.activity.R;
import com.oil.adapter.RcListAdapter.ListItemView;
import com.oil.utils.ParamsUtil;

public class VmListAdapter extends BaseAdapter {

	private Context context; // 运行上下文
	public static List<Map<String, Object>> listItems; // 商品信息集合
	private LayoutInflater listContainer; // 视图容器
	private boolean[] hasChecked; // 记录商品选中状态

	public final class ListItemView { // 自定义控件集合
		public TextView pl_name;
		public TextView section_name;
		public TextView spec_name;
		public TextView valve_name;
		public TextView check_date;
		public TextView create_time;
		public TextView status;
		public TextView txt_no;
	}

	public VmListAdapter(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		hasChecked = new boolean[getCount()];
	}

	public int getCount() {
		// TODO Auto-generated method stub
		if (listItems != null) {
			return listItems.size();
		} else {
			return 0;
		}
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 记录勾选了哪个物品
	 * 
	 * @param checkedID
	 *            选中的物品序号
	 */
	private void checkedChange(int checkedID) {
		hasChecked[checkedID] = !hasChecked[checkedID];
	}

	/**
	 * 判断物品是否选择
	 * 
	 * @param checkedID
	 *            物品序号
	 * @return 返回是否选中状态
	 */
	public boolean hasChecked(int checkedID) {
		return hasChecked[checkedID];
	}

	/**
	 * 显示物品详情
	 * 
	 * @param clickID
	 */
	private void showDetailInfo(int clickID) {
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.vm_list_layout, null);
			// 获取控件对象
			listItemView.pl_name = (TextView) convertView
					.findViewById(R.id.pl_name);

			listItemView.valve_name = (TextView) convertView
					.findViewById(R.id.valve_name);
			listItemView.check_date = (TextView) convertView
					.findViewById(R.id.check_date);
			listItemView.create_time = (TextView) convertView
					.findViewById(R.id.create_time);
			listItemView.status = (TextView) convertView
					.findViewById(R.id.status);
			listItemView.txt_no = (TextView) convertView
					.findViewById(R.id.txt_no);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// Log.e("image", (String) listItems.get(position).get("title")); //测试
		// Log.e("image", (String) listItems.get(position).get("info"));
		// 设置文字和图片
		try {
			listItemView.pl_name.setText((String) listItems.get(position).get(
					"pl_name")
					+ "|"
					+ (String) listItems.get(position).get("pl_section_name")
					+ "|"
					+ (String) listItems.get(position).get("pl_spec_name"));
		} catch (Exception e) {
			listItemView.pl_name.setText("");
		}

		try {
			listItemView.valve_name.setText((String) listItems.get(position)
					.get("valve_name"));
		} catch (Exception e) {
			listItemView.valve_name.setText("");
		}
		try {
			listItemView.check_date.setText((String) listItems.get(position)
					.get("check_date"));
		} catch (Exception e) {
			listItemView.check_date.setText("");
		}
		try {

			listItemView.create_time.setText((String) listItems.get(position)
					.get("create_time"));
		} catch (Exception e) {
			listItemView.create_time.setText("");
		}
		try {

			listItemView.status.setText((String) listItems.get(position).get(
					"verify"));
			Drawable btnDrawable = ParamsUtil.getBackground(context, (String) listItems.get(position).get(
					"verify")); 
			listItemView.status.setBackgroundDrawable(btnDrawable);
		} catch (Exception e) {
			listItemView.status.setText("");
		}

	

		return convertView;
	}
	
	public static void addItem(Map<String, Object> item) {
		listItems.add(item);
	}
}
