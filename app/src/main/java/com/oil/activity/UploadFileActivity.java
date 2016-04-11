package com.oil.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.oil.utils.HttpRequestUtil;
import com.oil.utils.Urls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
/**
 * Activity 上传的界面
 * @author spring sky
 * Email:vipa1888@163.com
 * QQ:840950105
 * MyName:石明政
 *
 */
public class UploadFileActivity extends Activity implements OnClickListener{
    private static final String TAG = "uploadImage";
    private Button selectImage,uploadImage,bt_login;
    private ImageView imageView;
    
    private String picPath = null;
    private List<File> files = new ArrayList<File>();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
        
        selectImage = (Button) this.findViewById(R.id.selectImage);
        uploadImage = (Button) this.findViewById(R.id.uploadImage);
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        bt_login = (Button) this.findViewById(R.id.bt_login);
        
        imageView = (ImageView) this.findViewById(R.id.imageView);
        
        
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.selectImage:
            /***
             * 这个是调用android内置的intent，来过滤图片文件   ，同时也可以过滤其他的  
             */
        	//HttpRequestUtil.HttpURLConnection_Post(Urls.URL + "authentication", (OilApplication)getApplication());
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
            break;
        case R.id.uploadImage:
            if(files.size()!=0)
            {
            	String request = "";
				try {
					request = HttpRequestUtil.uploadFiles(files);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                uploadImage.setText(request + "");
            }
            break;
        case R.id.bt_login :
        	System.out.println("-------------我是分割线-----------------");
        	HttpRequestUtil.HttpURLConnection_Post(Urls.URL + "authentication", (OilApplication)getApplication());
        	
        	break;
        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK)
        {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径  
             */
            Uri uri = data.getData();
            Log.e(TAG, "uri = "+ uri);
            try {
                String[] pojo = {MediaStore.Images.Media.DATA};
                
                Cursor cursor = managedQuery(uri, pojo, null, null,null);
                if(cursor!=null)
                {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
                     * 如果是图片格式的话，那么才可以   
                     */
                    if(path.endsWith("jpg")||path.endsWith("png"))
                    {
                        picPath = path;
                        files.add(new File(picPath));
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        imageView.setImageBitmap(bitmap);
                    }else{alert();}
                }else{alert();}
                
            } catch (Exception e) {
            }
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void alert()
    {
        Dialog dialog = new AlertDialog.Builder(this)
        .setTitle("提示")
        .setMessage("您选择的不是有效的图片")
        .setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                            int which) {
                        picPath = null;
                    }
                })
        .create();
        dialog.show();
    }
    
}