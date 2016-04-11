package com.oil.adapter;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.activity.NotiStuContentActivity;
import com.oil.activity.R;
import com.oil.domain.ReplyInfo;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.Urls;

public class ReplyAdapter extends BaseAdapter {
	
	private boolean result;
	
	private List<ReplyInfo> list;
	private Context context;
//	private ProgressDialog dialog = null;
	private RelativeLayout rl;
	private Button ver,can;
	private TextView textPath,textUploadTime;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private String time = new String();
	private Date date;
	
	public ReplyAdapter(List<ReplyInfo> list) {
		super();
		this.list = list;
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(result){
//				if(dialog.isShowing()){
//					dialog.dismiss();
//				}
				rl.setVisibility(View.GONE);
				Toast.makeText(context.getApplicationContext(),"下载成功！",Toast.LENGTH_SHORT).show();
			}else{
//				if(dialog.isShowing()){
//					dialog.dismiss();
//				}
				rl.setVisibility(View.GONE);
				Toast.makeText(context.getApplicationContext(),"下载失败！",Toast.LENGTH_SHORT).show();
			}
		}
	};
	
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
		final int finalPosition;
		ViewHolder holder;
		context = parent.getContext();
		if(convertView == null){
			rl = (RelativeLayout) NotiStuContentActivity.instan.findViewById(R.id.dialog_rl);
			ver = (Button) NotiStuContentActivity.instan.findViewById(R.id.dialog_verify);
			can = (Button) NotiStuContentActivity.instan.findViewById(R.id.dialog_cancel);
			textPath = (TextView) NotiStuContentActivity.instan.findViewById(R.id.dialog_filename);
			textUploadTime = (TextView) NotiStuContentActivity.instan.findViewById(R.id.dialog_fileinfo);
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_list_item,null);
			holder.name = (TextView) convertView.findViewById(R.id.reply_name);
			holder.content = (TextView) convertView.findViewById(R.id.reply_content);
			holder.downloadBtn = (ImageButton) convertView.findViewById(R.id.reply_btn);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.content.setText(list.get(position).getContent());
		finalPosition = position;
		
		String str = list.get(position).getPath();
		if(!"null".equals(str)){
			holder.downloadBtn.setVisibility(View.VISIBLE);
		}else{
			holder.downloadBtn.setVisibility(View.INVISIBLE);
		}
		
		holder.downloadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!"null".equals(list.get(finalPosition).getPath())){
					time = list.get(finalPosition).getPath();
					Log.e("Tag","time = " + time);
					String formatTime = time.substring(time.lastIndexOf("/")+1,time.indexOf("."));
					date = new Date(Long.parseLong(formatTime));
					textPath.setText(time.substring(time.lastIndexOf("/")+1,time.length()));
					textUploadTime.setText(format.format(date)+ "所上传附件");
					rl.setClickable(true);
					rl.setVisibility(View.VISIBLE);
				}
			}
		});
		
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.SAVEFILE + time));
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		can.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				rl.setClickable(false);
				rl.setVisibility(View.GONE);
			}
		});
		
		return convertView;
	}

	class ViewHolder{
		TextView name,content;
		ImageButton downloadBtn;
	}
	
}
