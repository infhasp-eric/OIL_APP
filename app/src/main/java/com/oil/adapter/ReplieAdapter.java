package com.oil.adapter;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.activity.NotiStuContentActivity;
import com.oil.activity.R;
import com.oil.activity.TmpNoticeDetailActivity;
import com.oil.adapter.VmListAdapter.ListItemView;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

public class ReplieAdapter extends BaseAdapter {
	private boolean result;
	private Context context; // 运行上下文
	public List<Map<String, Object>> listItems; // 商品信息集合
	private LayoutInflater listContainer; // 视图容器
	private boolean[] hasChecked; // 记录商品选中状态
	private String time = new String();
	private Date date;
	private TextView textPath,textUploadTime;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private RelativeLayout rl;
	private Button ver,can;

	public final class ListItemView { // 自定义控件集合
		public TextView txt_replier;
		public TextView txt_content;
		public ImageButton reply_btn;
	}

	public ReplieAdapter(Context context, List<Map<String, Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		hasChecked = new boolean[getCount()];
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
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
		new AlertDialog.Builder(context)
				.setTitle("物品详情：" + listItems.get(clickID).get("info"))
				.setMessage(listItems.get(clickID).get("detail").toString())
				.setPositiveButton("确定", null).show();
	}

	/**
	 * ListView Item设置
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.e("method", "getView");
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			rl = (RelativeLayout) TmpNoticeDetailActivity.instan.findViewById(R.id.dialog_rl);
			ver = (Button) TmpNoticeDetailActivity.instan.findViewById(R.id.dialog_verify);
			can = (Button) TmpNoticeDetailActivity.instan.findViewById(R.id.dialog_cancel);
			textPath = (TextView) TmpNoticeDetailActivity.instan.findViewById(R.id.dialog_filename);
			textUploadTime = (TextView) TmpNoticeDetailActivity.instan.findViewById(R.id.dialog_fileinfo);
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.reply_list_item, null);
			// 获取控件对象
			listItemView.txt_replier = (TextView) convertView
					.findViewById(R.id.reply_name);
			listItemView.txt_content = (TextView) convertView
					.findViewById(R.id.reply_content);
			listItemView.reply_btn = (ImageButton) convertView
					.findViewById(R.id.reply_btn);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// Log.e("image", (String) listItems.get(position).get("title")); //测试
		// Log.e("image", (String) listItems.get(position).get("info"));

		// 设置文字和图片
		listItemView.txt_replier.setText((String) listItems.get(position).get(
				"replier"));
		listItemView.txt_content.setText((String) listItems.get(position).get(
				"content"));

		if (StringUtil
				.isBlank((String) listItems.get(position).get("filename"))
				|| ((String) listItems.get(position).get("filename"))
						.equals("null")) {
			listItemView.reply_btn.setVisibility(View.INVISIBLE);
		} else {
			listItemView.reply_btn.setVisibility(View.VISIBLE);
			listItemView.reply_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					time = (String) listItems.get(position).get("filename");
					Log.e("Tag","time = " + time);
					int farstInd = 1;
					if(time.lastIndexOf("/") > 0) {
						farstInd += time.lastIndexOf("/");
					} else {
						farstInd += time.lastIndexOf("\\");
					}
					String formatTime = time.substring(farstInd,time.indexOf("."));
					date = new Date(Long.parseLong(formatTime));
					textPath.setText(time.substring(farstInd,time.length()));
					textUploadTime.setText(format.format(date)+ "所上传附件");
					rl.setVisibility(View.VISIBLE);
					}
				});
		}
		
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
				rl.setVisibility(View.GONE);
			}
		});

		return convertView;
	}

}
