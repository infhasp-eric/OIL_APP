package com.oil.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.dialog.LodingActivtyDialog;
import com.oil.utils.FileUtils;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.JSONUtil;
import com.oil.utils.StringUtils;
import com.oil.utils.ThreadKILL;
import com.oil.utils.Urls;

@SuppressLint("SimpleDateFormat")
public class Itco_create_Next extends FinalActivity {
	private static final int FILE_SELECT_CODE1 = 1;
	private static final int FILE_SELECT_CODE2 = 2;
	private static final int FILE_SELECT_CODE3 = 3;
	private static final int FILE_SELECT_CODE4 = 4;
	private static final int FILE_SELECT_CODE5 = 5;

	private static final int FILE_SELECT_CODE6 = 6;//探管文件
	@ViewInject(id = R.id.itco_create_next_back_btn, click = "backlistener") Button back;
	@ViewInject(id = R.id.itco_create_next_bt_save, click = "savelistener")	Button save;
	// 选择文件按钮
	@ViewInject(id = R.id.itco_create_next_notice_choosefile, click = "notice_choosefilelistener") Button notice_choosefile;
	@ViewInject(id = R.id.itco_create_next_warning_choosefile, click = "warning_choosefilelistener") Button warning_choosefile;
	@ViewInject(id = R.id.itco_create_next_bt_addfile, click = "addfilelistener") Button addfile;
	// 输入框
	@ViewInject(id = R.id.itco_create_next_et_address) EditText address_et;
	@ViewInject(id = R.id.itco_create_next_et_longitude) EditText longitude_et;
	@ViewInject(id = R.id.itco_create_next_et_latitude)	EditText latitude_et;
	@ViewInject(id = R.id.itco_create_next_et_content) EditText content_et;
	@ViewInject(id = R.id.itco_create_next_et_remark) EditText remark_et;
	@ViewInject(id = R.id.itco_create_next_et_keyword) EditText keyword_et;
	// 文件路径
	@ViewInject(id = R.id.itco_create_next_notice_filepath)
	TextView notice_filepath_tx;
	@ViewInject(id = R.id.itco_create_next_warning_filepath)
	TextView warning_filepath_tx;
	// 问题类型spinner
	@ViewInject(id = R.id.itco_create_next_problemtype)
	Spinner problemtype_spinner;
	@ViewInject(id = R.id.addlayout)
	LinearLayout addlLayout;

	@ViewInject(id = R.id.itco_create_next_pic_choosefile, click = "pic_btn_ck")
	Button pic_btn;
	@ViewInject(id = R.id.itco_create_next_pic)
	ImageView iv;
	/*@ViewInject(id = R.id.itco_create_next_gps_choosefile, click = "gpspic_btn_ck")
	Button gpspic_btn;*/
	@ViewInject(id = R.id.itco_create_next_gpspic) ImageView gpsiv;
	@ViewInject(id = R.id.lin_ma_remark) LinearLayout lin_ma_remark;//探管选择文件
	@ViewInject(id = R.id.bt_ma_remark_file, click="ma_remarkChoseFile") Button bt_ma_remark_file;
	@ViewInject(id = R.id.txt_ma_remark_file) TextView txt_ma_remark_file;
	@ViewInject(id = R.id.et_ef_length) EditText et_ef_length;//影响管线长度
	@ViewInject(id = R.id.et_pl_no) EditText et_pl_no;//管线桩号
	@ViewInject(id = R.id.et_risk) EditText et_risk;//主要存在风险
	@ViewInject(id = R.id.et_link_name) EditText et_link_name;//通讯录姓名
	@ViewInject(id = R.id.et_link_duty) EditText et_link_duty;//职务
	@ViewInject(id = R.id.et_link_method) EditText et_link_method;//联系方式
	@ViewInject(id = R.id.et_unit_name) EditText et_unit_name;//单位名称
	@ViewInject(id = R.id.et_unit_address) EditText et_unit_address;//单位地址
	@ViewInject(id = R.id.et_unit_post) EditText et_unit_post;//单位邮编
	
	private String ef_length, pl_no, risk, link_name, link_duty, link_method, unit_name,
	unit_address, unit_post;

	// 定义各种变量
	private LinearLayout notice_layout;
	private LinearLayout warning_layout;
	private RadioGroup notice_RG;
	private RadioGroup warning_RG;
	private RadioGroup rg_ma_remark;
	private RadioButton notice_btn_yes;
	private RadioButton notice_btn_no;
	private RadioButton warning_btn_yes;
	private RadioButton warning_btn_no;
	private RadioButton rb_ma_remark_yes;
	private RadioButton rb_ma_remark_no;
	private String address;// 地段
	private String longitude;// 经度
	private String latitude;// 纬度
	private String content;// 内容
	private String remark;// 备注
	private String keyword;// 关键字
	private LodingActivtyDialog Dialog;
	private int status;
	private String[] problemtypelist;
	private int[] problemtypelist_idList;
	private ArrayAdapter<String> problemtypeAdapter;
	private Handler mHandler;
	private int pt_id;
	private String add_filepath;
	private String warning_filepath;
	private String notice_filepath;
	private String ma_remarkPath;//探管文件路径
	private String ma_remark;
	private String warning_json;
	private String notice_json;
	private File notice_file, warning_file;
	private String notice_filename, warning_filename, picdownpath,
			gpspicdownpath;
	private String filename;
	private int send_notice = 0;
	private int is_warn = 0;
	private int is_ma_remark = 0;
	private int i = 0;
	private String Nowtime;
	private List<File> list = null;
	private int addfilestatus, no_status, wa_status, pic_status;
	private String picPath, gpspicPath;
	private int desiredWidth, desiredHeight;
	private Thread newThread, saveThr, savePicsThr, uploadThr;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(Dialog != null && Dialog.isShowing()) {
				Dialog.cancel();
			}
			Toast.makeText(getApplicationContext(), msg.obj + "", 1).show();
		};
	};
	
	Handler success = new Handler() {
		public void handleMessage(Message msg) {
			if(Dialog != null && Dialog.isShowing()) {
				Dialog.cancel();
			}
			Toast.makeText(getApplicationContext(), "保存成功", 1).show();
			Intent intent = new Intent();
			intent.setAction("actionCreate");
			intent.putExtra("isRefresh",true);
			intent.putExtra("which",false);
			sendBroadcast(intent);
			Itco_create_main.icm.finish();
			finish();
		}
	};
	
	Handler uploadHand, uploadValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.itco_create_next);
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		
		Dialog = new LodingActivtyDialog(Itco_create_Next.this);

		desiredWidth = (int) (wm.getDefaultDisplay().getWidth() * 0.8);
		desiredHeight = (int) (wm.getDefaultDisplay().getHeight() * 0.3);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		getview();
		warning_btn_no.setChecked(true);
		notice_btn_no.setChecked(true);
		rb_ma_remark_no.setChecked(true);
		// 获取当前时间
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		Nowtime = dateFormat.format(now);
		// 实例化list
		list = new ArrayList<File>();

		// notice RadioGroup监听事件
		notice_RG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == notice_btn_yes.getId()) {
					notice_layout.setVisibility(View.VISIBLE);
					send_notice = 1;
				} else if (checkedId == notice_btn_no.getId()) {
					notice_layout.setVisibility(View.GONE);
					send_notice = 0;
					notice_filepath_tx.setText("");
				}
			}
		});
		
		/**
		 * 探管文件选择
		 */
		rg_ma_remark.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb_ma_remark_yes.getId()) {
					lin_ma_remark.setVisibility(View.VISIBLE);
					is_ma_remark = 1;
				} else if (checkedId == rb_ma_remark_no.getId()) {
					lin_ma_remark.setVisibility(View.GONE);
					is_ma_remark = 0;
					txt_ma_remark_file.setText("");
				}
			}
		});
		
		// warning RadioGroup监听事件
		warning_RG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == warning_btn_yes.getId()) {
					warning_layout.setVisibility(View.VISIBLE);
					is_warn = 1;
				} else if (checkedId == warning_btn_no.getId()) {
					warning_layout.setVisibility(View.GONE);
					is_warn = 0;
					warning_filepath_tx.setText("");
				}
			}
		});
		// 问题类型下拉菜单监听事件
		problemtype_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						pt_id = problemtypelist_idList[position];
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		// 加载问题类型列表内容
		newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 这里写入子线程需要做的工作
				PTRefresh();
			}
		});
		newThread.start(); // 启动线程

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// super.handleMessage(msg);
				problemtypeAdapter = new ArrayAdapter<String>(
						Itco_create_Next.this, R.layout.spinner_style,
						problemtypelist);
				problemtypeAdapter
						.setDropDownViewResource(R.layout.spinner_drop_down);
				problemtype_spinner.setAdapter(problemtypeAdapter);
				problemtype_spinner.setSelection(0, true);
				// int ptlist = problemtypelist_idList[0];
				problemtype_spinner.invalidate();
			}
		};

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			ThreadKILL.killthread(saveThr);
			ThreadKILL.killthread(savePicsThr);
			ThreadKILL.killthread(uploadThr);
			
			if(Dialog.isShowing()){
				Dialog.cancel();
			}else {
				finish();
			}
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		ThreadKILL.killthread(newThread);
		ThreadKILL.killthread(saveThr);
		ThreadKILL.killthread(savePicsThr);
		ThreadKILL.killthread(uploadThr);
		super.onDestroy();
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
				notice_filepath = FileUtils.getPath(this, uri);
				Log.d("notice_filepath++++", notice_filepath);
				notice_filepath_tx.setText(notice_filepath.substring(
						notice_filepath.lastIndexOf("/") + 1,
						notice_filepath.length()));
			}
			break;
		case FILE_SELECT_CODE6:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				ma_remarkPath = FileUtils.getPath(this, uri);
				Log.d("maremark++++", ma_remarkPath);
				txt_ma_remark_file.setText(ma_remarkPath.substring(
						ma_remarkPath.lastIndexOf("/") + 1,
						ma_remarkPath.length()));
			}
			break;	
		
		case FILE_SELECT_CODE2:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				warning_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", warning_filepath);
				warning_filepath_tx.setText(warning_filepath.substring(
						warning_filepath.lastIndexOf("/") + 1,
						warning_filepath.length()));
			}
			break;
		case FILE_SELECT_CODE3:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				add_filepath = FileUtils.getPath(this, uri);
				TextView textView = new TextView(Itco_create_Next.this);
				addlLayout.addView(textView);
				addlLayout.setOrientation(LinearLayout.VERTICAL);
//				addlLayout.setGravity(Gravity.CENTER);
				Log.d("warning_filepath++++", add_filepath);
				textView.setText(add_filepath.substring(
						add_filepath.lastIndexOf("/") + 1,
						add_filepath.length()));
				File addfile = new File(add_filepath);
				list.add(addfile);
			}
			break;
		case FILE_SELECT_CODE4:
			if (resultCode == Activity.RESULT_OK) {
				/**
				 * 当选择的图片不为空的话，在获取到图片的途径
				 */
				Uri uri = data.getData();
				try {
					String[] pojo = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(uri, pojo, null, null, null);
					if (cursor != null) {
						ContentResolver cr = this.getContentResolver();
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String path = cursor.getString(colunm_index);
						Log.v("paht____________________________", path);
						/***
						 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，
						 * 你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
						 */
						if (path.endsWith("jpg") || path.endsWith("png")
								|| path.endsWith("jpeg")) {
							picPath = path;
							Bitmap destBitmap = BitmapFactory.decodeStream(cr
									.openInputStream(uri));
							Bitmap bitmap;
							if (destBitmap.getWidth() > desiredWidth
									|| destBitmap.getHeight() > desiredHeight) {
								bitmap = Bitmap.createScaledBitmap(destBitmap,
										desiredWidth, desiredHeight, true);
								destBitmap.recycle();
							} else {
								bitmap = destBitmap;
							}
							iv.setImageBitmap(bitmap);
						} else {
							alert();
						}
					} else {
						alert();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			break;
		case FILE_SELECT_CODE5:
			if (resultCode == Activity.RESULT_OK) {
				/**
				 * 当选择的图片不为空的话，在获取到图片的途径
				 */
				Uri uri = data.getData();
				try {
					String[] pojo = { MediaStore.Images.Media.DATA };

					Cursor cursor = managedQuery(uri, pojo, null, null, null);
					if (cursor != null) {
						ContentResolver cr = this.getContentResolver();
						int colunm_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String path = cursor.getString(colunm_index);
						/***
						 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，
						 * 你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
						 */
						if (path.endsWith("jpg") || path.endsWith("png")
								|| path.endsWith("jpeg")) {
							gpspicPath = path;
							Bitmap destBitmap = BitmapFactory.decodeStream(cr
									.openInputStream(uri));
							Bitmap bitmap;
							if (destBitmap.getWidth() > desiredWidth
									|| destBitmap.getHeight() > desiredHeight) {
								bitmap = Bitmap.createScaledBitmap(destBitmap,
										desiredWidth, desiredHeight, true);
								destBitmap.recycle();
							} else {
								bitmap = destBitmap;
							}
							gpsiv.setImageBitmap(bitmap);
						} else {
							gpsalert();
						}
					} else {
						gpsalert();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 返回按钮监听
	 */
	public void backlistener(View v) {
		finish();
	}

	/**
	 * 添加多个附件
	 */
	public void addfilelistener(View v) {
		System.out.println("点击了添加附件");
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE3);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 通知选择附件按钮监听
	 */
	public void notice_choosefilelistener(View v) {
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

	/**
	 * 警告标志选择附件按钮监听
	 */
	public void warning_choosefilelistener(View v) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE2);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}
	
	/**
	 * 警告标志选择附件按钮监听
	 */
	public void ma_remarkChoseFile(View v) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE6);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 现场示意图选择附件按钮监听
	 */
	public void pic_btn_ck(View v) {

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, FILE_SELECT_CODE4);
	}

	/**
	 * 卫星示意图选择附件按钮监听
	 */
	public void gpspic_btn_ck(View v) {

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, FILE_SELECT_CODE5);
	}

	/**
	 * 保存按钮监听
	 * 
	 */
	public void savelistener(View v) {
		address = address_et.getText().toString();
		longitude = longitude_et.getText().toString();
		latitude = latitude_et.getText().toString();
		content = content_et.getText().toString();
		remark = remark_et.getText().toString();
		if (StringUtils.isEmpty(address)){
			Toast.makeText(Itco_create_Next.this, "请填写地段", Toast.LENGTH_SHORT).show();
		}else if(StringUtils.isEmpty(longitude)){
			Toast.makeText(Itco_create_Next.this, "请填写经度", Toast.LENGTH_SHORT).show();
		}else if(StringUtils.isEmpty(latitude)){
			Toast.makeText(Itco_create_Next.this, "请填写纬度", Toast.LENGTH_SHORT).show();
		}else if(StringUtils.isEmpty(content)){
			Toast.makeText(Itco_create_Next.this, "请填写内容", Toast.LENGTH_SHORT).show();
		}else if(StringUtils.isEmpty(remark)) {
			Toast.makeText(Itco_create_Next.this, "请填写备注", Toast.LENGTH_SHORT).show();
		} else {
			Dialog.setThread(newThread);
			Dialog.show();
			Dialog.setCanceledOnTouchOutside(false);
			//提交警示标志等附件内容
			uploadHand = new Handler() {
				public void handleMessage(Message msg) {
					savePicsThr = new SavePics();
					savePicsThr.start();
				};
			};
			uploadValues = new Handler() {
				public void handleMessage(Message msg) {
					uploadThr = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							System.out.println(getValue());
							String req = HttpRequestUtil.PostHttp(Urls.URL
									+ "services/event/save", getValue(),
									(OilApplication) getApplication());
							JSONObject rejs = JSONUtil.stringToJson(req);
							try {
								status = rejs.getInt("status");
								System.out.println("status=====" + status);
								if(status == 200) {
									success.sendMessage(success.obtainMessage());
								} else {
									Message msg = handler.obtainMessage();
									msg.obj = "保存失败，请重新登陆";
									handler.sendMessage(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								Message msg = handler.obtainMessage();
								msg.obj = "保存失败，请稍后再试";
								handler.sendMessage(msg);
							} catch (Exception e) {
								status = 0;
								Message msg = handler.obtainMessage();
								msg.obj = "保存失败，请稍后再试";
								handler.sendMessage(msg);
								e.printStackTrace();
							}
						}
					});
					uploadThr.start();
				};
			};
			saveThr = new SaveFileList();
			saveThr.start();
			/*task = new SaveHttpTask();
			task.execute();*/
		}
	}

	/**
	 * 弹出提示框
	 */
	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

	/**
	 * 弹出提示框
	 */
	private void gpsalert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						gpspicPath = null;
					}
				}).create();
		dialog.show();
	}

	/**
	 * 初始化控件
	 */
	private void getview() {
		notice_RG = (RadioGroup) findViewById(R.id.notice_RG);
		warning_RG = (RadioGroup) findViewById(R.id.warning_RG);
		notice_layout = (LinearLayout) findViewById(R.id.itco_create_next_notice_layout);
		warning_layout = (LinearLayout) findViewById(R.id.itco_create_next_warning_layout);
		notice_btn_yes = (RadioButton) findViewById(R.id.itco_create_next_notice_radiobtn_yes);
		notice_btn_no = (RadioButton) findViewById(R.id.itco_create_next_notice_radiobtn_no);
		warning_btn_yes = (RadioButton) findViewById(R.id.itco_create_next_warning_radiobtn_yes);
		warning_btn_no = (RadioButton) findViewById(R.id.itco_create_next_warning_radiobtn_no);
		rg_ma_remark = (RadioGroup) findViewById(R.id.rg_ma_remark);
		rb_ma_remark_no = (RadioButton) findViewById(R.id.rb_ma_remark_no);
		rb_ma_remark_yes = (RadioButton) findViewById(R.id.rb_ma_remark_yes);
	}

	/**
	 * 问题类型下拉菜单更新操作
	 */
	public void PTRefresh() {
		String repl = HttpRequestUtil.sendGet(
				Urls.URL + "/services/event/type", "",
				(OilApplication) getApplication());

		if (repl.equals("error")) {
			Message message2 = handler.obtainMessage();
			message2.obj = "服务器错误";
			handler.sendMessage(message2);
		} else {
			try {
				JSONObject reJson = JSONUtil.stringToJson(repl);
				status = reJson.getInt("status");
				if (status == 200) {
					JSONArray plArray = reJson.getJSONArray("data");
					problemtypelist = new String[plArray.length()];
					problemtypelist_idList = new int[plArray.length()];
					for (int i = 0; i < plArray.length(); i++) {
						problemtypelist[i] = plArray.getJSONObject(i)
								.getString("name");
						problemtypelist_idList[i] = plArray.getJSONObject(i)
								.getInt("id");
					}
					mHandler.sendMessage(mHandler.obtainMessage());
				} else {
					String message = reJson.getString("message");
					Message message2 = handler.obtainMessage();
					message2.obj = message;
					handler.sendMessage(message2);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * 保存文件列表的线程
	 * @author Administrator
	 *
	 */
	private class SaveFileList extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println("线程开始-------------------------------");
				//11111上传文件批量文件，单独放在线程中，若有报错则终止
				String addfilejson = HttpRequestUtil.uploadFiles(list);
				JSONObject jsonObject = new JSONObject(addfilejson);
				addfilestatus = jsonObject.getInt("status");
				if (addfilestatus == 200) {
					JSONArray str = jsonObject.getJSONArray("data");
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < str.length(); i++) {
						System.out.println("__________________________" + str);
						buffer.append("&fileName=" + str.getString(i));
					}
					filename = buffer.toString();
				}
				uploadHand.sendMessage(uploadHand.obtainMessage());
			} catch (IOException e2) {
				e2.printStackTrace();
				Message msg = handler.obtainMessage();
				msg.obj = "上传文件失败，请重试";
				handler.sendMessage(msg);
			} catch (JSONException e) {
				e.printStackTrace();
				Message msg = handler.obtainMessage();
				msg.obj = "发生未知错误，请稍后再试";
				handler.sendMessage(msg);
			}
		}
	}
	
	private class SavePics extends Thread {
		@Override
		public void run() {
			if (send_notice == 1 && !StringUtils.isEmpty(notice_filepath)) {
				try {
					notice_file = new File(notice_filepath);
					Log.d("notice_json", notice_filepath);
					notice_json = HttpRequestUtil.uploadFile(notice_file);
					Log.d("notice_json", notice_json);
					JSONObject noticejson = new JSONObject(notice_json);
					no_status = noticejson.getInt("status");
					if (no_status == 200) {
						JSONArray data = noticejson.getJSONArray("data");
						notice_filename = data.getString(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = "文件提交失败，请重试";
					handler.sendMessage(msg);
					return;
				}

			} else {
				notice_filename = "";
			}

			if (is_warn == 1 && !StringUtils.isEmpty(warning_filepath)) {

				try {
					warning_file = new File(warning_filepath);
					warning_json = HttpRequestUtil.uploadFile(warning_file);
					Log.d("warning_json", warning_json);
					JSONObject warningjson = new JSONObject(warning_json);
					wa_status = warningjson.getInt("status");
					if (wa_status == 200) {
						JSONArray data = warningjson.getJSONArray("data");
						warning_filename = data.getString(0);
						Log.d("warning_filename", warning_filename);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = "文件提交失败，请重试";
					handler.sendMessage(msg);
					return;
				}
			} else {
				warning_filename = "";
			}

			if (!StringUtils.isEmpty(picPath)) {
				File file = new File(picPath);
				try {
					String json = HttpRequestUtil.uploadFile(file);
					JSONObject jsonObject = new JSONObject(json);
					pic_status = jsonObject.getInt("status");
					if (pic_status == 200) {
						JSONArray data = jsonObject.getJSONArray("data");
						picdownpath = data.getString(0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = "文件提交失败，请重试";
					handler.sendMessage(msg);
					return;
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = "文件提交失败，请重试";
					handler.sendMessage(msg);
					return;
				}
			} else {
				picdownpath = "";
			}
			if(!StringUtils.isEmpty(ma_remarkPath)) {
				File file = new File(ma_remarkPath);
				try {
					String json = HttpRequestUtil.uploadFile(file);
					JSONObject jsonObject = new JSONObject(json);
					pic_status = jsonObject.getInt("status");
					if (pic_status == 200) {
						JSONArray data = jsonObject.getJSONArray("data");
						ma_remark = data.getString(0);
					}
				} catch(Exception e) {
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = "文件提交失败，请重试";
					handler.sendMessage(msg);
					return;
				}
			} else {
				ma_remark = "";
			}
			uploadValues.sendMessage(uploadValues.obtainMessage());
		}
	}
	
	private String getValue() {
		StringBuffer buffer = new StringBuffer();
		address = address_et.getText().toString();
		longitude = longitude_et.getText().toString();
		latitude = latitude_et.getText().toString();
		content = content_et.getText().toString();
		remark = remark_et.getText().toString();
		keyword = keyword_et.getText().toString();
		
		ef_length = et_ef_length.getText().toString(); 
		buffer.append("&ef_length=");
		buffer.append(ef_length);
		pl_no = et_pl_no.getText().toString();
		buffer.append("&pl_no=");
		buffer.append(pl_no);
		risk = et_risk.getText().toString();
		buffer.append("&risk=");
		buffer.append(risk);
		link_name = et_link_name.getText().toString();
		buffer.append("&link_name=");
		buffer.append(link_name);
		link_duty = et_link_duty.getText().toString();
		buffer.append("&link_duty=");
		buffer.append(link_duty);
		link_method = et_link_method.getText().toString();
		buffer.append("&link_method=");
		buffer.append(link_method);
		unit_name = et_unit_name.getText().toString();
		buffer.append("&unit_name=");
		buffer.append(unit_name);
		unit_address = et_unit_address.getText().toString();
		buffer.append("&unit_address=");
		buffer.append(unit_address);
		unit_post = et_unit_post.getText().toString();
		buffer.append("&unit_post=");
		buffer.append(unit_post);
		
		buffer.append("&position_start=");
		buffer.append(address);
		buffer.append("&longitude=");
		buffer.append(longitude);
		buffer.append("&latitude=");
		buffer.append(latitude);
		buffer.append("&content=");
		buffer.append(content);
		buffer.append("&remark=");
		buffer.append(remark);
		buffer.append("&type_id=");
		buffer.append(pt_id);
		buffer.append("&send_notice=");
		buffer.append(send_notice);
		buffer.append("&is_warn=");
		buffer.append(is_warn);
		buffer.append("&notice_file=");
		buffer.append(notice_filename);
		buffer.append("&warn_file=");
		buffer.append(warning_filename);
		buffer.append("&keyword=");
		buffer.append(keyword);
		buffer.append("&fileName=");
		buffer.append(filename);
		buffer.append("&scene_file=");
		buffer.append(picdownpath);
		buffer.append("&is_ma_remark=");
		buffer.append(is_ma_remark);
		buffer.append("&ma_remark=");
		buffer.append(ma_remark);
		
		String data_params = buffer.toString();
		/* 获取Bundle */
		Bundle bundle = Itco_create_Next.this.getIntent().getExtras();

		/* 通过key值从bundle中取值 */
		String param = bundle.getString("params");
		param += data_params;
		return param;
	}

}
