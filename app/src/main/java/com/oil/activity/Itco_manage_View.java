package com.oil.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.adapter.Itco_manage_View_list_Adapter;
import com.oil.dialog.LodingActivtyDialog;
import com.oil.layout.MyListView;
import com.oil.utils.DateFormaterUtil;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.ParamsUtil;
import com.oil.utils.PreviewUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.Urls;

public class Itco_manage_View extends FinalActivity {
	@ViewInject(id = R.id.itco_manage_view_pl_sc_spec)
	TextView pl_sc_spec;
	@ViewInject(id = R.id.itco_manage_view_status)
	TextView stasus;
	@ViewInject(id = R.id.itco_manage_view_time)
	TextView time;
	@ViewInject(id = R.id.itco_manage_view_address)
	TextView address;
	@ViewInject(id = R.id.itco_manage_view_gps)
	TextView gps;
	@ViewInject(id = R.id.itco_manage_view_problemtype)
	TextView problemtype;
	@ViewInject(id = R.id.itco_manage_view_content_et)
	EditText content;
	@ViewInject(id = R.id.itco_manage_view_remark_et)
	EditText remark;
	@ViewInject(id = R.id.itco_manage_view_back_btn, click = "Backlistener")
	Button back;
	@ViewInject(id = R.id.itco_manage_view_notice_downloadfile, click = "Notice_downloadfilelistener")
	Button notice_downloadfile;
	@ViewInject(id = R.id.itco_manage_view_warning_downloadfile, click = "Warning_downloadfilelistener")
	Button warning_downloadfile;
	@ViewInject(id = R.id.itco_manage_view_next, click = "next_ck")
	Button next_btn;
	@ViewInject(id = R.id.itco_manage_view_downloadlist)
	MyListView listview;
	// 隐藏的控件
	@ViewInject(id = R.id.itco_manage_view_notice_layout)
	RelativeLayout notice_layout;
	@ViewInject(id = R.id.itco_manage_view_warning_layout)
	RelativeLayout warning_layout;
	@ViewInject(id = R.id.itco_manage_view_next_layout)
	RelativeLayout next_layout;
	@ViewInject(id = R.id.itco_manage_view_notice_radiobtn_yes)
	RadioButton notice_radiobtn_yes;
	@ViewInject(id = R.id.itco_manage_view_notice_radiobtn_no)
	RadioButton notice_radiobtn_no;
	@ViewInject(id = R.id.itco_manage_view_warning_radiobtn_yes)
	RadioButton warning_radiobtn_yes;
	@ViewInject(id = R.id.itco_manage_view_warning_radiobtn_no)
	RadioButton warning_radiobtn_no;
	@ViewInject(id = R.id.itco_manage_view_notice_path)
	TextView notice_path;
	@ViewInject(id = R.id.itco_manage_view_warning_path)
	TextView warning_path;
	@ViewInject(id = R.id.itco_manage_view_pic_layout)
	LinearLayout pic_layout;
	@ViewInject(id = R.id.itco_manage_view_gps_pic_layout)
	LinearLayout gpspic_layout;
	@ViewInject(id = R.id.itco_manage_view_pic_path)
	TextView pic_path;
	@ViewInject(id = R.id.itco_manage_view_pic_down_btn, click = "downpic_ck")
	Button downpic;
	@ViewInject(id = R.id.itco_manage_view_gps_pic_path)
	TextView gpspic_path;
	@ViewInject(id = R.id.itco_manage_view_gps_pic_down_btn, click = "gpsdownpic_ck")
	Button gpsdownpic;
	@ViewInject(id = R.id.itco_manage_view_keyword)
	TextView keyword;
	@ViewInject(id = R.id.itco_manage_view_reporter)
	TextView reporter;
	@ViewInject(id = R.id.itco_manage_view_verifier)
	TextView verifier;
	@ViewInject(id=R.id.verify_desc) EditText verifier_desc;
	@ViewInject(id=R.id.lin_verifier) LinearLayout lin_verifier;
	@ViewInject(id=R.id.lin_verify_desc) LinearLayout lin_verify_desc;
	@ViewInject(id=R.id.txt_code_no) TextView txt_code_no;
	@ViewInject(id=R.id.txt_ef_length) TextView txt_ef_length;
	@ViewInject(id=R.id.txt_pl_no) TextView txt_pl_no;
	@ViewInject(id=R.id.et_risk) EditText et_risk;
	@ViewInject(id=R.id.txt_link_name) TextView txt_link_name;
	@ViewInject(id=R.id.txt_link_duty) TextView txt_link_duty;
	@ViewInject(id=R.id.txt_link_method) TextView txt_link_method;
	@ViewInject(id=R.id.txt_unit_name) TextView txt_unit_name;
	@ViewInject(id=R.id.txt_unit_address) TextView txt_unit_address;
	@ViewInject(id=R.id.txt_unit_post) TextView txt_unit_post;
	@ViewInject(id=R.id.rb_ma_remark_no) RadioButton rb_ma_remark_no;
	@ViewInject(id=R.id.rb_ma_remark_yes) RadioButton rb_ma_remark_yes;
	@ViewInject(id=R.id.rl_ma_remark) RelativeLayout rl_ma_remark;
	@ViewInject(id=R.id.txt_rb_ma_remark) TextView txt_rb_ma_remark;
	@ViewInject(id=R.id.bt_ma_remark, click="downMaRemark") Button bt_ma_remark;
	@ViewInject(id=R.id.txt_event_level) TextView txt_event_level;
	@ViewInject(id=R.id.lin_level) LinearLayout lin_level;
	
	// 各种变量
	private int id;
	private String listid;
	private LodingActivtyDialog Dialog;
	private int status;
	private ArrayList<Map<String, Object>> listdatas;
	private String fileUrl;
	private ImageView ivScene, ivSatellite;
	Handler UIhandler;
	String notice_filepath, warn_filepath, pic, gpspic, ma_remarkpath;
	private Bitmap bitmapScene, bitmapSate;
	private ImageView fullImage;
	private RelativeLayout fullRl;
	private boolean fullFlag = false;

	private Handler imageHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (bitmapScene != null && msg.what == 1) {
				ivScene.setImageBitmap(bitmapScene);
			} else if (bitmapSate != null && msg.what == 2) {
				ivSatellite.setImageBitmap(bitmapSate);
			} else {
				Toast.makeText(getApplicationContext(), "图片预览失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.itco_manage_view);

		ivScene = (ImageView) findViewById(R.id.extra_picture_scene);
		ivSatellite = (ImageView) findViewById(R.id.extra_picture_satellite);
		fullImage = (ImageView) findViewById(R.id.extra_pic_full);
		fullRl = (RelativeLayout) findViewById(R.id.full_rl);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras(); // 获取intent里面的bundle对象
		listid = bundle.getString("listid");
		String[] ids;
		try {
			ids = listid.split("&");
		} catch (Exception e) {
			ids = new String[] { listid };
		}
		id = Integer.valueOf(ids[0]);
		warning_radiobtn_no.setChecked(true);
		notice_radiobtn_no.setChecked(true);
		Dialog = new LodingActivtyDialog(Itco_manage_View.this);
		Dialog.show();
		Dialog.setCanceledOnTouchOutside(false);
		
		Dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					Dialog.dismiss();
					Itco_manage_View.this.finish();
				}
				return false;
			}
		});
		
		GetTask task = new GetTask();
		task.execute();

		UIhandler = new Handler() {
			public void handleMessage(Message msg) {
				Itco_manage_View_list_Adapter adapter = new Itco_manage_View_list_Adapter(
						Itco_manage_View.this, listdatas);
				System.out.println(listdatas.size() + "=========================");
				listview.setAdapter(adapter);
			}
		};

		/*Dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK
						&& arg2.getAction() == KeyEvent.ACTION_DOWN) {
					Dialog.dismiss();
					Itco_manage_View.this.finish();
				}
				return false;
			}
		});*/

		ivScene.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (bitmapScene != null && !fullFlag) {
					fullImage.setImageBitmap(bitmapScene);
					fullRl.setVisibility(View.VISIBLE);
					fullRl.setClickable(true);
					fullFlag = true;
				}
			}
		});

		ivSatellite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (bitmapSate != null && !fullFlag) {
					fullImage.setImageBitmap(bitmapSate);
					fullRl.setVisibility(View.VISIBLE);
					fullRl.setClickable(true);
					fullFlag = true;
				}
			}
		});

		fullRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (fullFlag) {
					fullRl.setVisibility(View.GONE);
					fullRl.setClickable(true);
					fullFlag = false;
				}
			}
		});

	}

	public void Backlistener(View V) {
		finish();
	}

	public void next_ck(View V) {
		Intent intent = new Intent(Itco_manage_View.this,
				Itco_manage_View_Reply.class);

		Bundle bundle = new Bundle(); // 创建Bundle对象
		bundle.putInt("id", id); // 装入数据
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void Notice_downloadfilelistener(View V) {

		try {
			fileUrl = notice_filepath;

			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "文件路径为空", 1).show();
		}

	}

	public void Warning_downloadfilelistener(View V) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(warn_filepath));
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "文件路径为空", 1).show();
		}

	}

	/**
	 * 下载现场示意图
	 * 
	 * @param V
	 */
	public void downpic_ck(View V) {

		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Urls.SAVEFILE + pic));
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载现场卫星示意图
	 * 
	 * @param V
	 */
	public void gpsdownpic_ck(View V) {

		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Urls.SAVEFILE + gpspic));
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downMaRemark(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.SAVEFILE + ma_remarkpath));
		startActivity(intent);
	}

	private class GetTask extends AsyncTask<String, Void, String> {
		JSONObject event;
		JSONObject data;

		@Override
		protected String doInBackground(String... params) {

			String json = HttpRequestUtil.sendGet(Urls.URL
					+ "services/event/detail", "id=" + listid,
					(OilApplication) getApplication());
			Log.e("详情", json);
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
				try {
					time.setText(DateFormaterUtil.getDateToString(Long
							.parseLong(event.getString("create_time"))));
					gps.setText(event.getString("longitude") + ","
							+ event.getString("latitude"));
					content.setText(event.getString("content"));
					remark.setText(event.getString("remark"));
					problemtype.setText(event.getString("typeName"));
					address.setText(event.getString("position_start"));
					pl_sc_spec.setText(event.getString("pl_name") + "|"
							+ event.getString("pl_section_name") + "|"
							+ event.getString("pl_spec_name"));
					stasus.setText(ParamsUtil.getVerifyByStatus(event
							.getInt("status")));
					pic = event.getString("scene_file");
					keyword.setText(event.getString("keyword"));
					if ("null".equals(event.getString("reporter"))) {
						reporter.setText("");
					} else {
						reporter.setText(event.getString("reporter"));
					}
					txt_code_no.setText(event.getString("code_no"));
					txt_ef_length.setText(event.getString("ef_length"));
					txt_pl_no.setText(event.getString("pl_no"));
					et_risk.setText(event.getString("risk"));
					txt_link_name.setText(event.getString("link_name"));
					txt_link_duty.setText(event.getString("link_duty"));
					txt_link_method.setText(event.getString("link_method"));
					txt_unit_address.setText(event.getString("unit_address"));
					txt_unit_name.setText(event.getString("unit_name"));
					txt_unit_post.setText(event.getString("unit_post"));
					txt_event_level.setText(event.getString("level_name"));
					
					if(event.getInt("status") != 0) {
						verifier_desc.setText(event.getString("message"));
						if ("null".equals(event.getString("verify"))) {
							verifier.setText("");
						} else {
							verifier.setText(event.getString("verify"));
						}
					} else {
						lin_verifier.setVisibility(View.GONE);
						lin_verify_desc.setVisibility(View.GONE);
					}
					
					if(event.getInt("status") != 1) {
						lin_level.setVisibility(View.GONE);
					}
					
					try {
						if (!pic.equals(null) && !pic.equals("")
								&& !pic.equals("null")) {
							pic_path.setText(StringUtils.getFileName(pic));
							downpic.setVisibility(View.VISIBLE);
							new Thread(new Runnable() {
								@Override
								public void run() {
									Message msg = imageHandler.obtainMessage();
									bitmapScene = PreviewUtil
											.getNetBitmap(Urls.SAVEFILE + pic);
									msg.what = 1;
									imageHandler.sendMessage(msg);
								}
							}).start();
						} else {
							pic_path.setText("无现场示意图");
							downpic.setVisibility(View.GONE);
						}

					} catch (Exception e) {
					}
					gpspic = event.getString("moon_file");

					try {
						Log.d("gpspic", "+++++++" + gpspic);
						if (!gpspic.equals(null) && !gpspic.equals("")
								&& !gpspic.equals("null")) {
							gpspic_path.setText(StringUtils.getFileName(gpspic));
							gpsdownpic.setVisibility(View.VISIBLE);
							new Thread(new Runnable() {
								@Override
								public void run() {
									Message msg = imageHandler.obtainMessage();
									bitmapSate = PreviewUtil
											.getNetBitmap(Urls.SAVEFILE
													+ gpspic);
									msg.what = 2;
									imageHandler.sendMessage(msg);
								}
							}).start();
						} else {
							gpspic_path.setText("无卫星示意图");
							gpsdownpic.setVisibility(View.GONE);
						}

					} catch (Exception e) {
					}

					if (event.getInt("status") == 1) {
						next_layout.setVisibility(View.VISIBLE);
					} else {
						next_layout.setVisibility(View.GONE);
					}
					Log.e("send_notice", "" + event.getString("send_notice"));
					if (event.getBoolean("send_notice")) {
						notice_radiobtn_yes.setChecked(true);
						notice_layout.setVisibility(View.VISIBLE);

						notice_filepath = event.getString("notice_file");
						try {
							notice_path.setText(StringUtils.getFileName(notice_filepath));
						} catch (Exception e) {
							notice_radiobtn_yes.setChecked(true);
							notice_path.setText("暂时无法下载");
							notice_downloadfile.setVisibility(View.GONE);
						}

					} else {
						notice_radiobtn_no.setChecked(true);
						notice_layout.setVisibility(View.GONE);
					}
					if (event.getBoolean("is_warn")) {
						warning_radiobtn_yes.setChecked(true);

						warning_layout.setVisibility(View.VISIBLE);
						warn_filepath = event.getString("warn_file");
						try {
							warning_path.setText(StringUtils.getFileName(warn_filepath));
						} catch (Exception e) {
							e.printStackTrace();
							warning_radiobtn_yes.setChecked(true);
							warning_path.setText("暂时无法下载");
							warning_downloadfile.setVisibility(View.GONE);
							
						}
					} else {
						warning_radiobtn_no.setChecked(true);
						warning_layout.setVisibility(View.GONE);
					}
					
					if(event.getBoolean("is_ma_remark")) {
						rb_ma_remark_yes.setChecked(true);
						rl_ma_remark.setVisibility(View.VISIBLE);
						txt_rb_ma_remark.setText(StringUtils.getFileName(event.getString("ma_remark")));
						ma_remarkpath = event.getString("ma_remark");
					} else {
						rb_ma_remark_no.setChecked(true);
					}
					Addpath(data.getJSONArray("ealist"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			super.onPostExecute(result);
		}

	}

	public void Addpath(JSONArray jarray) {
		listdatas = new ArrayList<Map<String, Object>>();
		try {
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject json = jarray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("path", json.getString("path") + "");
				map.put("filename", StringUtils.getFileName(json.getString("path")) + "");
				listdatas.add(map);
			}

			UIhandler.sendMessage(UIhandler.obtainMessage());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (fullFlag && keyCode == KeyEvent.KEYCODE_BACK) {
			fullRl.setVisibility(View.GONE);
			fullFlag = false;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

}
