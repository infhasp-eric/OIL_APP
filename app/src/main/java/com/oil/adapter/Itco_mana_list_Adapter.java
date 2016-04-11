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

public class Itco_mana_list_Adapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	public List<Map<String, Object>> listdatas;
	private String status = null;

	public Itco_mana_list_Adapter(Context context,
			List<Map<String, Object>> listItems) {
		this.context = context;
		this.listdatas = listItems;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		try {
			return listdatas.size();
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.itco_mana_list_item, null);
			holder = new ViewHolder();
			holder.time = (TextView) convertView
					.findViewById(R.id.itco_item_time);
			holder.address = (TextView) convertView
					.findViewById(R.id.itco_item_address);
			holder.reporter = (TextView) convertView
					.findViewById(R.id.itco_item_reporter);
			holder.state = (TextView) convertView
					.findViewById(R.id.itco_item_state);
			holder.plname = (TextView) convertView
					.findViewById(R.id.itco_item_plname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.plname
				.setText((String) listdatas.get(position).get("pl_name")
						+ "|"
						+ (String) listdatas.get(position).get(
								"pl_section_name")
						+ "|"
						+ (String) listdatas.get(position).get(
								"pl_spec_name"));
		holder.time.setText((String) listdatas.get(position).get(
				"discover_date"));
		holder.address.setText((String) listdatas.get(position).get(
				"position_start"));
		holder.reporter.setText((String) listdatas.get(position).get(
				"created_by"));
		status = (String) listdatas.get(position).get("status");
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

	public class ViewHolder {
		TextView time;
		TextView address;
		TextView reporter;
		TextView state;
		TextView plname;

	}
}
