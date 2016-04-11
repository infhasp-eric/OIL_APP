package com.oil.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.oil.domain.CathodeMonth;
import com.oil.utils.HttpRequestUtil;
import com.oil.utils.Urls;

public class CathodeProtectionAdd extends Activity {
	
	private EditText ysdt,ssd,wdtd,ljtd,jztd,fljctd,jcgxtd,qttd,scmaxa,scmina,yavga,scmaxv,scminv,yavgv,tddmax,tddmin,sdl,tdbhl,sbwhl,yxfx,tbr,shr;
	private CathodeMonth cm;
	private String[] trans = new String[5];
	private ProgressDialog dialog = null;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			dialog.dismiss();
			try {
				Toast.makeText(getApplicationContext(),new JSONObject(msg.obj.toString()).getString("message"),Toast.LENGTH_SHORT).show();
				finish();
				RecordCreate.instance.finish();
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cathode_month_add);
		Intent intent = getIntent();
		trans = intent.getBundleExtra("bd").getStringArray("trans");
		
		findViewById(R.id.cathode_month_back_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		initWeidgt();
		
		findViewById(R.id.cathode_month_save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(judgeNull()){
					dialog = new ProgressDialog(CathodeProtectionAdd.this);
					dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					dialog.setMessage("数据上传中，请稍后...");
					dialog.show();
					dialog.setCancelable(false);
					cm =new CathodeMonth();
					cm.setPl(trans[0]);
					cm.setSection(trans[1]);
					cm.setSpec(trans[2]);
					cm.setJinzhan(trans[3]);
					cm.setrMonth(trans[4]);
					cm.setComment(yxfx.getText().toString());
					cm.setCreatedBy(tbr.getText().toString());
					cm.setAuditor(shr.getText().toString());
					cm.setEstimated(ysdt.getText().toString());
					cm.setActual(ssd.getText().toString());
					cm.setWdtd(wdtd.getText().toString());
					cm.setLjtd(ljtd.getText().toString());
					cm.setJztd(jztd.getText().toString());
					cm.setFljctd(fljctd.getText().toString());
					cm.setJcgxtd(jcgxtd.getText().toString());
					cm.setQttd(qttd.getText().toString());
					cm.setoMaxA(scmaxa.getText().toString());
					cm.setoMinA(scmina.getText().toString());
					cm.setoAvgA(yavga.getText().toString());
					cm.setoMaxV(scmaxv.getText().toString());
					cm.setoMinV(scminv.getText().toString());
					cm.setoAvgV(yavgv.getText().toString());
					cm.setTtdVMax(tddmax.getText().toString());
					cm.setTtdVMin(tddmin.getText().toString());
					cm.setSdl(sdl.getText().toString());
					cm.setBhl(tdbhl.getText().toString());
					cm.setSbwhl(sbwhl.getText().toString());
					StrarSubThread(cm);
				}else{
					Toast.makeText(getApplicationContext(),"请填写填报人与审核人！",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	protected void StrarSubThread(final CathodeMonth cMonth) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.obj = HttpRequestUtil.PostHttp(Urls.CPSRMRS,cMonth.toString(),(OilApplication)getApplication());
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void initWeidgt() {
		ysdt = (EditText) findViewById(R.id.should_apply_electric);
		ssd = (EditText) findViewById(R.id.actual_apply_electric);
		wdtd = (EditText) findViewById(R.id.external_power_cut);
		ljtd = (EditText) findViewById(R.id.thunderstrike_power_cut);
		jztd = (EditText) findViewById(R.id.mechanical_failure_power_cut);
		fljctd = (EditText) findViewById(R.id.lightning_protection_power_cut);
		jcgxtd = (EditText) findViewById(R.id.detect_pipe_power_cut);
		qttd = (EditText) findViewById(R.id.other_power_cut);
		scmaxa = (EditText) findViewById(R.id.output_max_a);
		scmina = (EditText) findViewById(R.id.output_min_a);
		yavga = (EditText) findViewById(R.id.output_a_average);
		scmaxv = (EditText) findViewById(R.id.output_max_v);
		scminv = (EditText) findViewById(R.id.output_min_v);
		yavgv = (EditText) findViewById(R.id.output_v_average);
		tddmax = (EditText) findViewById(R.id.potential_max);
		tddmin = (EditText) findViewById(R.id.potential_min);
		sdl = (EditText) findViewById(R.id.transmission_rate);
		tdbhl = (EditText) findViewById(R.id.poweron_protection_rate);
		sbwhl = (EditText) findViewById(R.id.equipment_usability_rate);
		yxfx = (EditText) findViewById(R.id.run_analysis);
		tbr = (EditText) findViewById(R.id.cathode_creater);
		shr = (EditText) findViewById(R.id.cathode_verifier);
	}

	protected boolean judgeNull() {
		if(/*!"".equals(ysdt.getText().toString())
				&&!"".equals(ssd.getText().toString())
				&&!"".equals(wdtd.getText().toString())
				&&!"".equals(ljtd.getText().toString())
				&&!"".equals(jztd.getText().toString())
				&&!"".equals(fljctd.getText().toString())
				&&!"".equals(jcgxtd.getText().toString())
				&&!"".equals(qttd.getText().toString())
				&&!"".equals(scmaxa.getText().toString())
				&&!"".equals(scmina.getText().toString())
				&&!"".equals(yavga.getText().toString())
				&&!"".equals(scmaxv.getText().toString())
				&&!"".equals(scminv.getText().toString())
				&&!"".equals(yavgv.getText().toString())
				&&!"".equals(tddmax.getText().toString())
				&&!"".equals(tddmin.getText().toString())
				&&!"".equals(sdl.getText().toString())
				&&!"".equals(tdbhl.getText().toString())
				&&!"".equals(sbwhl.getText().toString())
				&&!"".equals(yxfx.getText().toString())&&*/
				!"".equals(tbr.getText().toString())
				&&!"".equals(shr.getText().toString())){
			return true;
		}else{
			return false;
		}
	}
	
}
