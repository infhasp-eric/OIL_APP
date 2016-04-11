package com.oil.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.oil.activity.R;
import com.oil.domain.PipeRecord;
import com.oil.utils.ParamsUtil;

public class PipeMeasureAdapter extends BaseAdapter {

	public List<PipeRecord> list;

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private Date date;
	private Context context;

	public PipeMeasureAdapter(List<PipeRecord> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			context = parent.getContext();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.pipe_record_list_item, null);
			// holder.tl = (TableLayout)
			// convertView.findViewById(R.id.background_tl);
			holder.pipeName = (TextView) convertView
					.findViewById(R.id.pipe_measure_get_name);

			holder.month = (TextView) convertView
					.findViewById(R.id.pipe_measure_get_year);
			holder.station = (TextView) convertView
					.findViewById(R.id.pipe_measure_get_well);
			holder.state = (TextView) convertView
					.findViewById(R.id.pipe_measure_get_state);
			holder.createTime = (TextView) convertView
					.findViewById(R.id.pipe_measure_get_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.pipeName.setText(list.get(position).getPipeName() + "|"
				+ list.get(position).getSection() + "|"
				+ list.get(position).getSpec());

		holder.station.setText(list.get(position).getWell());

		StringBuffer sb = new StringBuffer(list.get(position).getMonth());
		sb.insert(4, "-");
		holder.month.setText(sb.toString());

		date = new Date(Long.parseLong(list.get(position).getCreateTime()));
		holder.createTime.setText(format.format(date));

		if ("0".equals(list.get(position).getState())) {
			holder.state.setText("未审核");
		} else if ("1".equals(list.get(position).getState())) {
			holder.state.setText("通过");
		} else {
			holder.state.setText("不通过");
		}

		Drawable btnDrawable = ParamsUtil.getBackground(context,
				Integer.parseInt(list.get(position).getState()));
		holder.state.setBackgroundDrawable(btnDrawable);
		/*
		 * if(position%2 == 0){ holder.tl.setBackgroundColor(R.color.jichu_bg);
		 * }else{ holder.tl.setBackgroundColor(Color.WHITE); }
		 */

		return convertView;
	}

	class ViewHolder {
		TextView pipeName, section, spec, station, month, createTime, state;
		TableLayout tl;
	}

	public void addItem(PipeRecord pr) {
		// TODO Auto-generated method stub
		list.add(pr);
	}

}
