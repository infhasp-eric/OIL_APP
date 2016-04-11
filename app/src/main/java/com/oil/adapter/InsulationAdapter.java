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
import android.widget.TextView;
import com.oil.activity.R;
import com.oil.domain.InsulationRecord;
import com.oil.utils.ParamsUtil;

public class InsulationAdapter extends BaseAdapter {
	public List<InsulationRecord> list;

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private Date date;
	private Context context;

	public InsulationAdapter(List<InsulationRecord> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
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
					R.layout.insulation_list_item, null);
			holder.tv_linename = (TextView) convertView
					.findViewById(R.id.get_line);
			holder.tv_well = (TextView) convertView.findViewById(R.id.get_well);
			holder.tv_createby = (TextView) convertView
					.findViewById(R.id.get_createby);
			holder.tv_createtime = (TextView) convertView
					.findViewById(R.id.get_createtime);
			holder.tv_examine = (TextView) convertView
					.findViewById(R.id.get_examine);
			holder.tv_year = (TextView) convertView.findViewById(R.id.get_year);
			holder.tv_state = (TextView) convertView
					.findViewById(R.id.get_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_createby.setText(list.get(position).getCreateBy());
		date = new Date(Long.parseLong(list.get(position).getCreateTime()));
		holder.tv_createtime.setText(format.format(date));

		holder.tv_examine.setText(list.get(position).getExamine());
		holder.tv_linename.setText(list.get(position).getLine_name() + "|"
				+ list.get(position).getStart_end() + "|"
				+ list.get(position).getLine_standard());

		if("0".equals(list.get(position).getState())){
			holder.tv_state.setText("未审核");
		}else if("1".equals(list.get(position).getState())){
			holder.tv_state.setText("通过");
		}else{
			holder.tv_state.setText("不通过");
		}
		
		Drawable btnDrawable = ParamsUtil.getBackground(context,Integer.parseInt(list.get(position).getState())); 
		holder.tv_state.setBackgroundDrawable(btnDrawable);
		holder.tv_well.setText(list.get(position).getWell());
		holder.tv_year.setText(list.get(position).getYear());
		return convertView;
	}

	class ViewHolder {
		private TextView tv_linename, tv_createby, tv_examine, tv_createtime,
				tv_state, tv_well, tv_year;
	}
}
