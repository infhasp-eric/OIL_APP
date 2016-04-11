package com.oil.dialog;

import com.oil.activity.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CheckDialog extends Dialog implements OnClickListener{
	Context context;
	private String url;
    public CheckDialog(Context context,String url) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.url = url;
    }
    public CheckDialog(Context context, int theme,String url){
        super(context, theme);
        this.context = context;
        this.url = url;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.check_dialog);
        initView();
    }
    
    private void initView(){
    	Button bt_check = (Button) findViewById(R.id.bt_check);
    	Button bt_cancel = (Button) findViewById(R.id.bt_cancel);
    	bt_check.setOnClickListener(this);
    	bt_cancel.setOnClickListener(this);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		super.cancel();
    		return true;
    	}
    	return false;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_check:
			 Uri uri = Uri.parse(url);   
		        //通过Uri获得编辑框里的//地址，加上http://是为了用户输入时可以不要输入  
		     Intent intent = new Intent(Intent.ACTION_VIEW,uri);    
		        //建立Intent对象，传入uri  
		     context.startActivity(intent);    
			break;
		case R.id.bt_cancel:
			super.cancel();
			break;
		default:
			break;
		}
	}
}
