package com.oil.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oil.dialog.LodingActivtyDialog;
import com.oil.utils.FileUtils;
import com.oil.utils.Urls;

@SuppressLint("NewApi")
public class High_DCA_Information_Create_NEXT_FileDialog extends FinalActivity {
	private static final int FILE_SELECT_CODE1 = 1;
	private static final int FILE_SELECT_CODE2 = 2;
	private static final int FILE_SELECT_CODE3 = 3;
	private static final int FILE_SELECT_CODE4 = 4;
	private static final int FILE_SELECT_CODE5 = 5;
	private static final int FILE_SELECT_CODE6 = 6;
	private static final int FILE_SELECT_CODE7 = 7;
	private static final int FILE_SELECT_CODE8 = 8;
	private static final int FILE_SELECT_CODE9 = 9;
	private static final int FILE_SELECT_CODE10 = 10;
	private static final int FILE_SELECT_CODE11 = 11;
	private static final int FILE_SELECT_CODE12 = 12;

	private int CODE1 = 0;
	private int CODE2 = 0;
	private int CODE3 = 0;
	private int CODE4 = 0;
	private int CODE5 = 0;
	private int CODE6 = 0;
	private int CODE7 = 0;
	private int CODE8 = 0;
	private int CODE9 = 0;
	private int CODE10 = 0;
	private int CODE11 = 0;
	private int CODE12 = 0;
	private LodingActivtyDialog dialog;
	public static String paramssss;
	// 按钮注册
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_one_btn, click = "one_ck")
	Button one_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_two_btn, click = "two_ck")
	Button two_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_three_btn, click = "three_ck")
	Button three_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_four_btn, click = "four_ck")
	Button four_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_five_btn, click = "five_ck")
	Button five_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_six_btn, click = "six_ck")
	Button six_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_seven_btn, click = "seven_ck")
	Button seven_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_eight_btn, click = "eight_ck")
	Button eight_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_nine_btn, click = "nine_ck")
	Button nine_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_ten_btn, click = "ten_ck")
	Button ten_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_eleven_btn, click = "eleven_ck")
	Button eleven_btn;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_twelve_btn, click = "twelve_ck")
	Button twelve_btn;
	// 完成按钮
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_finish_btn, click = "finish_ck")
	Button finish_btn;
	// 路径textview
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_one_tx)
	TextView one_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_two_tx)
	TextView two_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_three_tx)
	TextView three_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_four_tx)
	TextView four_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_five_tx)
	TextView five_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_six_tx)
	TextView six_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_seven_tx)
	TextView seven_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_eight_tx)
	TextView eight_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_nine_tx)
	TextView nine_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_ten_tx)
	TextView ten_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_eleven_tx)
	TextView eleven_tx;
	@ViewInject(id = R.id.high_dca_information_create_next_filedialog_twelve_tx)
	TextView twelve_tx;
	String one_filepath, two_filepath, three_filepath, four_filepath,
			five_filepath, six_filepath, seven_filepath, eight_filepath,
			nine_filepath, ten_filepath, eleven_filepath, twelve_filepath;
	public static List<File> list = null;
	File file1, file2, file3, file4, file5, file6, file7, file8, file9, file10,
			file11, file12;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.high_dca_information_create_next_filedialog);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		list = new ArrayList<File>();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE1:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				one_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", one_filepath);
				one_tx.setText(one_filepath);
				file1 = new File(one_filepath);
				CODE1 = 1;

			}
			break;
		case FILE_SELECT_CODE2:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				two_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", two_filepath);
				two_tx.setText(two_filepath);
				file2 = new File(two_filepath);
				CODE2 = 2;

			}
			break;
		case FILE_SELECT_CODE3:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				three_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", three_filepath);
				three_tx.setText(three_filepath);
				file3 = new File(three_filepath);
				CODE3 = 3;

			}
			break;
		case FILE_SELECT_CODE4:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				four_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", four_filepath);
				four_tx.setText(four_filepath);
				file4 = new File(four_filepath);
				CODE4 = 4;

			}
			break;
		case FILE_SELECT_CODE5:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				five_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", five_filepath);
				five_tx.setText(five_filepath);
				file5 = new File(five_filepath);
				CODE5 = 5;
			}
			break;
		case FILE_SELECT_CODE6:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				six_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", six_filepath);
				six_tx.setText(six_filepath);
				file6 = new File(six_filepath);
				CODE6 = 6;
			}
			break;
		case FILE_SELECT_CODE7:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				seven_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", seven_filepath);
				seven_tx.setText(seven_filepath);
				file7 = new File(seven_filepath);
				CODE7 = 7;
			}
			break;
		case FILE_SELECT_CODE8:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				eight_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", eight_filepath);
				eight_tx.setText(eight_filepath);
				file8 = new File(eight_filepath);
				CODE8 = 8;
			}
			break;
		case FILE_SELECT_CODE9:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				nine_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", nine_filepath);
				nine_tx.setText(nine_filepath);
				file9 = new File(nine_filepath);
				CODE9 = 9;
			}
			break;
		case FILE_SELECT_CODE10:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				ten_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", ten_filepath);
				ten_tx.setText(ten_filepath);
				file10 = new File(ten_filepath);
				CODE10 = 10;
			}
			break;
		case FILE_SELECT_CODE11:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				eleven_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", eleven_filepath);
				eleven_tx.setText(eleven_filepath);
				file11 = new File(eleven_filepath);
				CODE11 = 11;
			}
			break;
		case FILE_SELECT_CODE12:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				twelve_filepath = FileUtils.getPath(this, uri);
				Log.d("warning_filepath++++", twelve_filepath);
				twelve_tx.setText(twelve_filepath);
				file12 = new File(twelve_filepath);
				CODE12 = 12;
			}
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void one_ck(View v) {
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

	public void two_ck(View v) {
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

	public void three_ck(View v) {
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

	public void four_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE4);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void five_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE5);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void six_ck(View v) {
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

	public void seven_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE7);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void eight_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE8);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void nine_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE9);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void ten_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE10);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void eleven_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE11);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void twelve_ck(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "选择要上传的文件"),
					FILE_SELECT_CODE12);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "请安装一个文件管理器.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void finish_ck(View v) {
		Log.v("CODE1===", "==" + CODE1);
		Log.v("CODE2===", "==" + CODE2);
		Log.v("CODE3===", "==" + CODE3);
		Log.v("CODE4===", "==" + CODE4);
		Log.v("CODE5===", "==" + CODE5);

		System.out.println("++++++++++++++++++++++++++++" + list.size());
		for (int i = 0; i < list.size(); i++)
			System.out.println("list.get(i)=========" + list.get(i));
		dialog = new LodingActivtyDialog(
				High_DCA_Information_Create_NEXT_FileDialog.this);
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		UploadFileTask fileTask = new UploadFileTask();
		fileTask.execute();
	}

	private class UploadFileTask extends AsyncTask<String, Void, String> {
		private String status;
		private JSONObject jsonObject;

		@Override
		protected String doInBackground(String... params) {
			if (CODE1 == 1) {
				list.add(file1);
			} else {
				list.add(null);
			}

			if (CODE2 == 2) {
				list.add(file2);
			} else {
				list.add(null);
			}
			if (CODE3 == 3) {
				list.add(file3);
			} else {
				list.add(null);
			}
			if (CODE4 == 4) {
				list.add(file4);
			} else {
				list.add(null);
			}
			if (CODE5 == 5) {
				list.add(file5);
			} else {
				list.add(null);
			}
			if (CODE6 == 6) {
				list.add(file6);
			} else {
				list.add(null);
			}
			if (CODE7 == 7) {
				list.add(file7);
			} else {
				list.add(null);
			}
			if (CODE8 == 8) {
				list.add(file8);
			} else {
				list.add(null);
			}
			if (CODE9 == 9) {
				list.add(file9);
			} else {
				list.add(null);
			}
			if (CODE10 == 10) {
				list.add(file10);
			} else {
				list.add(null);
			}
			if (CODE11 == 11) {
				list.add(file11);
			} else {
				list.add(null);
			}
			if (CODE12 == 12) {
				list.add(file12);
			} else {
				list.add(null);
			}

			for (File f : list) {
				System.out.println("list==============" + f);
			}
			try {
				System.out.println("开始上传");
				String str = uploadFiles(list);
				jsonObject = new JSONObject(str);
				status = jsonObject.getString("status");

				list.clear();
				Log.e("===================", status);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			try {
				if (status.equals("200")) {
					Toast.makeText(getApplicationContext(), "文件上传成功", 1).show();
					JSONArray data = jsonObject.getJSONArray("data");
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < data.length(); i++) {
						if (data.getString(i) != null
								&& !data.getString(i).equals("null")
								&& !data.getString(i).equals(null)
								&& !data.getString(i).equals("")) {
							if (i == data.length() - 1) {
								buffer.append(data.getString(i));
							} else {
								buffer.append(data.getString(i) + ";");
							}

						} else {
							System.out.println(i);
							if (i == data.length() - 1) {
								buffer.append("");
							} else {
								buffer.append("" + ";");
							}

						}

					}
					Log.v("String===========", buffer.toString());
					dialog.cancel();
					// Intent intent = new Intent(
					// High_DCA_Information_Create_NEXT_FileDialog.this,
					// High_DCA_Information_Create_NEXT.class);
					// intent.putExtra("filenames", buffer.toString());
					paramssss = buffer.toString();
					// startActivity(intent);
					finish();

				} else if (status.equals("") || status.equals(null)
						|| status.equals("null")) {
					// Intent intent = new Intent(
					// High_DCA_Information_Create_NEXT_FileDialog.this,
					// High_DCA_Information_Create_NEXT.class);
					// intent.putExtra("filenames", "");
					// startActivity(intent);
					dialog.cancel();
					paramssss = "";
					finish();
				} else {
					dialog.cancel();
					Toast.makeText(getApplicationContext(), "文件上传失败", 1).show();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}

	public static String uploadFiles(List<File> files) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		URL uri = new URL(Urls.URL + "services/uploadMFile");
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		// conn.setReadTimeout(5 * 1000);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null) {
			for (File f : files) {
				Log.e("filepath", f + "=====================");
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ (f == null ? "" : f.getName()) + "\"" + LINEND);
				sb1.append("Content-Type: multipart/form-data; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				if (f != null) {
					InputStream is = new FileInputStream(f);
					byte[] buffer = new byte[4096];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
					is.close();
				}
				outStream.write(LINEND.getBytes());
			}
		}
		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		int success = conn.getResponseCode();
		// System.out.println(success);
		// System.out.println(conn.getResponseMessage());
		InputStream in = conn.getInputStream();
		InputStreamReader isReader = new InputStreamReader(in);
		BufferedReader bufReader = new BufferedReader(isReader);
		String line = null;
		String data = "";
		// System.out.println("开始读取");
		while ((line = bufReader.readLine()) != null) {
			System.out.println(line);
			data += line;
		}
		outStream.close();
		conn.disconnect();
		return data;
	}
}
