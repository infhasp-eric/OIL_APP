package com.oil.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oil.activity.R;
import com.oil.utils.ParamsUtil;

@SuppressLint("NewApi")
public class AskNoticeAdapter extends BaseAdapter implements NoticeAdapter {
	private Context context; // 运行上下文
	public List<Map<String, Object>> listItems; // 列表信息集合
	private LayoutInflater listContainer; // 视图容器
	private boolean[] hasChecked; // 记录商品选中状态

	public final class ListItemView { // 自定义控件集合
		public TextView txt_title;
		public TextView txt_data;//日期
		public TextView txt_author;//创建人
		public TextView txt_verify;//状态
	}

	public AskNoticeAdapter(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		hasChecked = new boolean[getCount()];
	}

	public int getCount() {
		// TODO Auto-generated method stub
		if(listItems != null) 
		return listItems.size();
		else 
			return 0;
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
		new AlertDialog.Builder(context)
				.setTitle("物品详情：" + listItems.get(clickID).get("info"))
				.setMessage(listItems.get(clickID).get("detail").toString())
				.setPositiveButton("确定", null).show();
	}

	/**
	 * ListView Item设置
	 */
	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.ask_notice_layout, null);
			// 获取控件对象
			listItemView.txt_title = (TextView) convertView
					.findViewById(R.id.txt_title);
			listItemView.txt_data = (TextView) convertView
					.findViewById(R.id.txt_data);
			listItemView.txt_author = (TextView) convertView
					.findViewById(R.id.txt_author);
			listItemView.txt_verify = (TextView) convertView
					.findViewById(R.id.txt_verify);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// Log.e("image", (String) listItems.get(position).get("title")); //测试
		// Log.e("image", (String) listItems.get(position).get("info"));

		// 设置文字和图片
		listItemView.txt_title.setText((String) listItems.get(position).get(
				"title"));
		listItemView.txt_data.setText((String) listItems.get(position).get(
				"data"));
		listItemView.txt_author.setText((String) listItems.get(position).get(
				"author"));
		listItemView.txt_verify.setText((String) listItems.get(position).get(
				"verify"));
		listItemView.txt_verify.setBackgroundDrawable(ParamsUtil.getBackground(context, 
				listItems.get(position).get("verify").toString()));

		return convertView;
	}
	
	@Override
	public void addItem(Map<String, Object> map){
		listItems.add(map);
	}
}
