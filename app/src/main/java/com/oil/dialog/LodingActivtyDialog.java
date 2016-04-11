package com.oil.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;

import com.oil.activity.R;

/**
 * 异步进度Dialog
 * 
 * @author East
 * 
 */
public class LodingActivtyDialog extends AlertDialog {
	private Thread thread;
	private AsyncTask task;
	
	public LodingActivtyDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loding_activity);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(thread != null) {
				thread.interrupt();
				super.cancel();
				return true;
			} else if (task != null){
				task.cancel(true);
				super.cancel(); 
			}
		}
		return false;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public AsyncTask getTask() {
		return task;
	}

	public void setTask(AsyncTask task) {
		this.task = task;
	}

}
