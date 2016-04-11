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
import com.oil.domain.PipeRecord;
import com.oil.utils.ParamsUtil;

public class PipePollingAdapter extends BaseAdapter {

	public List<PipeRecord> list;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date date;
	private Context context;

	public PipePollingAdapter(List<PipeRecord> list) {
		super();
		this.list = list;
	}
	
	@Override
	public int getCount() {
		if(list != null) 
		return list.size();
		else 
			return 0;
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
		if(convertView == null){
			holder = new ViewHolder();
			context = parent.getContext();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pipe_polling_list_item, null);
			holder.pipeName = (TextView) convertView.findViewById(R.id.pipe_polling_get_name);
			holder.month = (TextView) convertView.findViewById(R.id.pipe_polling_get_year);
			holder.station = (TextView) convertView.findViewById(R.id.pipe_polling_get_well);
			holder.state = (TextView) convertView.findViewById(R.id.pipe_polling_get_state);
			holder.createTime = (TextView) convertView.findViewById(R.id.pipe_polling_get_time);
			holder.proSta = (TextView) convertView.findViewById(R.id.pipe_polling_get_protect_station);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.pipeName.setText(list.get(position).getPipeName()+"|"+list.get(position).getSection()+"|"+list.get(position).getSpec());
		holder.station.setText(list.get(position).getWell());
		holder.proSta.setText(list.get(position).getProWell());
		
		StringBuffer sb = new StringBuffer(list.get(position).getMonth());
		sb.insert(4,"-");
		holder.month.setText(sb.toString());
		
		date = new Date(Long.parseLong(list.get(position).getCreateTime()));
		holder.createTime.setText(format.format(date));
		
		if("0".equals(list.get(position).getState())){
			holder.state.setText("未审核");
		}else if("1".equals(list.get(position).getState())){
			holder.state.setText("通过");
		}else{
			holder.state.setText("不通过");
		}
		
		Drawable btnDrawable = ParamsUtil.getBackground(context,Integer.parseInt(list.get(position).getState())); 
		holder.state.setBackgroundDrawable(btnDrawable);
		
		return convertView;
	}

	class ViewHolder{
		TextView pipeName,station,month,proSta,createTime,state;
	}

}
