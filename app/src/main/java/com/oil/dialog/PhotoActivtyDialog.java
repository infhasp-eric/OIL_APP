package com.oil.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.oil.activity.R;

/**
 * 异步进度Dialog
 * 
 * @author East
 * 
 */
public class PhotoActivtyDialog extends Activity {
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bigphoto);
		ImageView imageView = (ImageView) findViewById(R.id.bigimg);
		Intent intent = getIntent();
		if (intent != null) {
			bitmap = intent.getParcelableExtra("bitmap");
			imageView.setImageBitmap(bitmap);
		}

	}

}
