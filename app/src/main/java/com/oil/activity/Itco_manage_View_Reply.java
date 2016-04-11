package com.oil.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oil.adapter.Itco_Reply_Adapter;
import com.oil.dialog.LodingActivtyDialog;
import com.oil.utils.FileUtils;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;
import com.oil.utils.Utility;

public class Itco_manage_View_Reply extends FinalActivity {
	private static final int FILE_SELECT_CODE1 = 0;
	@ViewInject(id = R.id.itco_manage_view_reply_audit_opinion)
	EditText audit_opinion_et;
	@ViewInject(id = R.id.itco_manage_view_reply_reply_content)
	EditText reply_content_et;
	@ViewInject(id = R.id.itco_manage_view_reply_listview)
	ListView listview;
	@ViewInject(id = R.id.itco_manage_view_reply_addfile, click = "addfile_ck")
	LinearLayout addfile_btn;
	@ViewInject(id = R.id.itco_manage_view_reply_btn, click = "reply_ck")
	Button reply_btn;
	@ViewInject(id = R.id.itco_manage_view_reply_send, click = "send_ck")
	Button send;
	@ViewInject(id = R.id.itco_manage_view_reply_back_btn, click = "back_ck")
	Button back_btn;
	@ViewInject(id = R.id.reply_layout)
	LinearLayout reply_layout;

	private int id;
	private LodingActivtyDialog Dialog;
	private List<Map<String, Object>> listdatas;
	// private LinearLayout layout;
	private List<File> list = null;
	private File addfile;
	private String filename;
	private String add_filepath;
	private String reply_content;
	private String path;
	private Itco_Reply_Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.itco_manage_view_reply);
		audit_opinion_et.setSingleLine(false);
		audit_opinion_et.setHorizontallyScrolling(false);
		// layout = (LinearLayout) findViewById(R.id.addfile_layout);
		list = new ArrayList<File>();
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras(); // 获取intent里面的bundle对象
		id = bundle.getInt("id");
		Dialog = new LodingActivtyDialog(Itco_manage_View_Reply.this);
		Dialog.show();
		Dialog.setCanceledOnTouchOutside(false);
		
		Dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					Dialog.dismiss();
					Itco_manage_View_Reply.this.finish();
				}
				return false;
			}
		});
		
		GetContentTask contentTask = new GetContentTask();
		contentTask.execute();
	}

	/**
	 * 
	 * 获取附件返回路径
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case FILE_SELECT_CODE1:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				add_filepath = FileUtils.getPath(this, uri);
				// TextView textView = new
				// TextView(Itco_manage_View_Reply.this);
				// layout.addView(textView);
				// layout.setOrientation(LinearLayout.VERTICAL);
				Log.d("warning_filepath++++", add_filepath);
				// textView.setText(add_filepath);

				addfile = new File(add_filepath);
				if (addfile != null) {
					Toast.makeText(getApplicationContext(), "附件添加成功", 1).show();
				} else {
					Toast.makeText(getApplicationContext(), "附件添加失败，请重新添加", 1)
							.show();
				}

				// list.add(addfile);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void addfile_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE1);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void reply_ck(View v) {
		reply_layout.setVisibility(View.VISIBLE);
		reply_btn.setVisibility(View.GONE);

	}

	public void send_ck(View v) {

		if (!StringUtils.isEmpty(reply_content_et.getText().toString())) {
			Dialog.show();
			RrplyTask Task = new RrplyTask();
			Task.execute();
			
		} else {
			Toast.makeText(getApplicationContext(), "请输入回复内容", 1).show();
		}
	}

	public void back_ck(View v) {
		finish();
	}

	private class GetContentTask extends AsyncTask<String, Void, String> {
		private int status;
		private JSONObject data;
		private JSONObject event;

		@Override
		protected String doInBackground(String... params) {
			String json = HttpRequestUtil.sendGet(Urls.URL
					+ "services/event/detail", "id=" + id,
					(OilApplication) getApplication());
			Log.e("&&&&&&&&&&&&&&&&&&", json);
			try {
				JSONObject jsonObject = new JSONObject(json);
				status = jsonObject.getInt("status");
				if (status == 200) {

					data = jsonObject.getJSONObject("data");
					event = data.getJSONObject("e");

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			if (status == 200) {
				Dialog.cancel();
				reply_btn.setVisibility(View.VISIBLE);
				reply_layout.setVisibility(View.GONE);
				try {
					audit_opinion_et.setText((String) event
							.getString("message"));
					Adddatas(data.getJSONArray("replies"));
					for (int i = 0; i < listdatas.size(); i++) {
						try {
							Log.e("", "" + listdatas.get(i).get("filepath"));
						} catch (Exception e) {
							Log.e("", "没有附件");
						}
						Log.e("", "" + listdatas.get(i).get("replier"));
						Log.e("", "" + listdatas.get(i).get("content"));
					}
					if (listdatas.size() != 0) {
						adapter = new Itco_Reply_Adapter(
								Itco_manage_View_Reply.this, listdatas);
						listview.setAdapter(adapter);
						Utility.setListViewHeightBasedOnChildren(listview);
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			super.onPostExecute(result);
		}

	}

	private class RrplyTask extends AsyncTask<String, Void, String> {
		private int rp_status;
		private String msg;
		private int addfilestatus;

		@Override
		protected String doInBackground(String... params) {
			if (/* list.size() != 0 */
			addfile != null) {
				try {
					String addfilejson = HttpRequestUtil.uploadFile(addfile);
					Log.e("list++++++++", "" + list.size());
					System.out.println("_____addfilejson_____________________"
							+ addfilejson);

					JSONObject jsonObject = new JSONObject(addfilejson);
					addfilestatus = jsonObject.getInt("status");
					if (addfilestatus == 200) {
						JSONArray str = jsonObject.getJSONArray("data");
						System.out.println("___________str_______________"
								+ str);
						StringBuffer buffer = new StringBuffer();
						for (int i = 0; i < str.length(); i++) {
							Log.v("str.getString(i)", str.getString(i));
							buffer.append("&fileName=" + str.getString(i));
						}
						filename = buffer.toString();
					} else {
						filename = "&fileName=" + "";
					}

				} catch (IOException e2) {
					filename = "&fileName=" + "";
				} catch (JSONException e) {
					filename = "&fileName=" + "";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				filename = "&fileName=" + "";

			}

			reply_content = reply_content_et.getText().toString();
			StringBuffer buffer = new StringBuffer();
			buffer.append("id=");
			buffer.append(id);
			buffer.append("&msg_content=");
			buffer.append(reply_content);

			String param = buffer.toString() + filename;
			System.out.println("++++++++++++++++++++++++++++++" + param);
			String json = HttpRequestUtil.PostHttp(Urls.URL
					+ "services/event/reply ", param,
					(OilApplication) getApplication());
			try {
				JSONObject jsonObject = new JSONObject(json);
				rp_status = jsonObject.getInt("status");
				msg = jsonObject.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", json);

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			if (rp_status == 200) {
				Dialog.cancel();
				Log.e("+++", "此处一运行");
				reply_content_et.setText("");
				add_filepath = "";
				Toast.makeText(getApplicationContext(), msg, 1).show();
				GetContentTask contentTask = new GetContentTask();
				contentTask.execute();
			} else if (rp_status == 202) {
				Dialog.cancel();
				Toast.makeText(getApplicationContext(), msg, 1).show();
			} else {
				Dialog.cancel();
				Toast.makeText(getApplicationContext(), "请检查网络后再试", 1).show();
			}
			super.onPostExecute(result);
		}

	}

	public void Adddatas(JSONArray jarray) {

		listdatas = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < jarray.length(); i++) {

				JSONObject json = jarray.getJSONObject(i);
				try {
					path = (String) json.getJSONArray("fileNames").getString(0);
				} catch (Exception e) {
					path = "";
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("filepath", path + "");
				map.put("replyer", json.getString("replier") + "");
				map.put("content", json.getString("content") + "");
				listdatas.add(map);
				System.out.println(i + "==========第i条回复");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
