package com.oil.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.activity.R;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

public class Itco_manage_View_list_Adapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> listdatas;
	private LayoutInflater inflater;

	public Itco_manage_View_list_Adapter(Context context,
			List<Map<String, Object>> listdatas) {
		this.context = context;
		this.listdatas = listdatas;
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
			convertView = inflater.inflate(R.layout.itco_manage_view_list_item,
					null);
			holder=new ViewHolder();
			holder.button = (Button) convertView
					.findViewById(R.id.itco_manage_view_list_item_bt_down);
			holder.path_tx = (TextView) convertView
					.findViewById(R.id.itco_manage_view_list_item_path);
			convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String fileName = (String) listdatas.get(position).get("filename");
			String path = (String) listdatas.get(position).get("path");
			if (!fileName.equals(null) || !fileName.equals("")) {
				try {
					holder.path_tx.setText(StringUtils.PathtoDate(fileName));
				} catch (Exception e) {
					holder.path_tx.setText(fileName);
				}

				MyListener listener = new MyListener(position, path);
				holder.button.setOnClickListener(listener);
			
		}

		return convertView;
	}
	class ViewHolder {
		Button button;
		TextView path_tx;
	}
	public class MyListener implements OnClickListener {
		String fileUrl;
		int postion;
		String path;

		public MyListener(int position, String path) {
			this.path = path;
			this.postion = position;
		}

		@Override
		public void onClick(View v) {
			try {
				fileUrl = path;
				System.out.println("path|" + path);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
				intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				context.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, "文件路径为空", 1).show();
			}

		}

	}

}
