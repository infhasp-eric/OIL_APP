package com.oil.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oil.activity.R;
import com.oil.domain.NotiStu;

public class NotiStuAdapter extends BaseAdapter {
	public List<NotiStu> list ;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date date;

	public NotiStuAdapter(List<NotiStu> list) {
		super();
		this.list = list;
	}

	@Override
	public int getCount() {
		if(list != null) {
			return list.size();
		} else {
			return 0;
		}
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_stu_list_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.noti_title);
			holder.content = (TextView) convertView.findViewById(R.id.noti_content);
			holder.time = (TextView) convertView.findViewById(R.id.noti_time);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(list.get(position).getTitle());
//		holder.content.setMovementMethod(ScrollingMovementMethod.getInstance());
		Spanned str = Html.fromHtml(list.get(position).getContent());
		holder.content.setText(str);
		
		date = new Date(Long.parseLong(list.get(position).getTime()));
		holder.time.setText(format.format(date));
		return convertView;
	}
	class ViewHolder{
		TextView title,content,time;
	}
}
