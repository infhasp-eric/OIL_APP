package com.oil.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oil.activity.R;
import com.oil.utils.ParamsUtil;

public class High_DCA_Information_List_Adapter extends BaseAdapter {
	private Context context;
	public List<Map<String, Object>> listdatas;
	private String status = null;
	private LayoutInflater inflater;
	private boolean[] hasChecked;

	public High_DCA_Information_List_Adapter(Context context,
			List<Map<String, Object>> listdatas) {
		this.context = context;
		this.listdatas = listdatas;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		try {
			return listdatas.size();
		} catch (NullPointerException e) {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.high_dca_information_list_item, null);
			holder = new ViewHolder();
			holder.pl_name = (TextView) convertView
					.findViewById(R.id.high_dca_information_list_item_pl_name);

			holder.creat_time = (TextView) convertView
					.findViewById(R.id.high_dca_information_list_item_create_time);

			holder.state = (TextView) convertView
					.findViewById(R.id.high_dca_information_list_item_status);
			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}
		holder.pl_name.setText((String) listdatas.get(position).get("pl_name")
				+ "|" + (String) listdatas.get(position).get("pl_section_name")
				+ "|" + (String) listdatas.get(position).get("pl_spec_name"));
		holder.creat_time.setText((String) listdatas.get(position).get(
				"create_time"));
		try {
			status = (String) listdatas.get(position).get("status");
		} catch (Exception e) {
			status = null;
		}
		if(status.equals("0")){
			holder.state.setText("未审核");
		}else if(status.equals("1")){
			holder.state.setText("通过");
		}else{
			holder.state.setText("不通过");
		}
		
		Drawable btnDrawable = ParamsUtil.getBackground(context,Integer.parseInt(status)); 
		holder.state.setBackgroundDrawable(btnDrawable);

		return convertView;
	}

	class ViewHolder {
		TextView pl_name;
		TextView creat_time;
		TextView state;

	}
}
