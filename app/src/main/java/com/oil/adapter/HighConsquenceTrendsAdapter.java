package com.oil.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.oil.activity.R;
import com.oil.domain.PipeRecord;
import com.oil.utils.ParamsUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HighConsquenceTrendsAdapter extends BaseAdapter {

	public List<PipeRecord> list = new ArrayList<PipeRecord>();

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private Date date;
	private Context context;

	public HighConsquenceTrendsAdapter(List<PipeRecord> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			context = parent.getContext();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.high_trends_list_item, null);
			holder.pl = (TextView) convertView
					.findViewById(R.id.high_trends_get_name);
			holder.status = (TextView) convertView
					.findViewById(R.id.high_trends_get_state);
			holder.createTime = (TextView) convertView
					.findViewById(R.id.high_trends_get_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.pl.setText(list.get(position).getPipeName() + "|"
				+ list.get(position).getSection() + "|"
				+ list.get(position).getSpec());

		date = new Date(Long.parseLong(list.get(position).getCreateTime()));
		holder.createTime.setText(format.format(date));

		if("0".equals(list.get(position).getState())){
			holder.status.setText("未审核");
		}else if("1".equals(list.get(position).getState())){
			holder.status.setText("通过");
		}else{
			holder.status.setText("不通过");
		}
		
		Drawable btnDrawable = ParamsUtil.getBackground(context,Integer.parseInt(list.get(position).getState())); 
		holder.status.setBackgroundDrawable(btnDrawable);

		return convertView;
	}

	class ViewHolder {
		TextView pl, section, spec, createTime, status;
	}

}
