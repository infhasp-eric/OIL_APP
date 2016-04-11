package com.oil.adapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.activity.R;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtils;

public class Itco_Reply_Adapter extends BaseAdapter {
	Context context;
	private List<Map<String, Object>> listdatas;
	private LayoutInflater inflater;
	private String path;
	private boolean fileDown;
	private MyButtonListener buttonListener;

	public Itco_Reply_Adapter(Context context,
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
			convertView = inflater
					.inflate(
							R.layout.itco_manage_information_view_reply_list_item,
							null);
			holder = new ViewHolder();
			holder.download = (Button) convertView
					.findViewById(R.id.itco_manage_information_view_reply_list_item_download_btn);
			holder.reply_content = (TextView) convertView
					.findViewById(R.id.itco_manage_information_view_reply_list_item_reply_content);
			holder.replyer = (TextView) convertView
					.findViewById(R.id.itco_manage_information_view_reply_list_item_replyer);

			holder.path = (TextView) convertView
					.findViewById(R.id.itco_manage_information_view_reply_list_item_path);
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.itco_manage_information_view_reply_list_item_downlayout);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.reply_content.setText((String) listdatas.get(position).get(
				"content"));
		holder.replyer.setText((String) listdatas.get(position).get("replyer"));
		path = (String) listdatas.get(position).get("filepath");
		if (path.equals(null) || path.equals("") || path.equals("null")) {
			holder.layout.setVisibility(View.GONE);

		} else {

			holder.layout.setVisibility(View.VISIBLE);
			try {
				holder.path.setText(StringUtils.PathtoDate(path));
			} catch (Exception e) {
				String str=path;
				holder.path.setText(str.substring(path.lastIndexOf("/")+1));
			}
			
			buttonListener = new MyButtonListener(position, path);
			holder.download.setOnClickListener(buttonListener);
		}
		return convertView;
	}

	public class MyButtonListener implements OnClickListener {
		int position;
		String path;

		public MyButtonListener(int position, String path) {
			super();
			this.position = position;
			this.path = path;
		}

		@Override
		public void onClick(View v) {

			try {
				final String fileUrl = path;
				boolean sdCardExist = Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
				if (sdCardExist) {
					final File gen = Environment.getExternalStorageDirectory();// 获取根目录
					Log.e("Tag", gen.getPath());
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							int start = fileUrl.lastIndexOf("/");
							if (start < 0) {
								start = fileUrl.lastIndexOf("\\");
							}
							Log.e("START", "" + fileUrl);
							HttpRequestUtil.getFile(fileUrl, context);
						}
					}).start();
				} else {

				}

			} catch (Exception e) {
				Toast.makeText(context, "文件路径为空", 1).show();
			}

		}
	}

}

class ViewHolder {
	TextView replyer;
	TextView reply_content;
	TextView path;
	RelativeLayout layout;
	Button download;

}
