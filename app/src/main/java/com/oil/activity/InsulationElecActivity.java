package com.oil.activity;

import com.oil.domain.JointProper;
import com.oil.utils.StringUtil;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 绝缘法兰通电点电位
 * @author Administrator
 *
 */
public class InsulationElecActivity extends FinalActivity {
	@ViewInject(id=R.id.bt_back, click="backClick") Button bt_back;
	@ViewInject(id=R.id.bt_next, click="nextClick") Button bt_next;
	@ViewInject(id=R.id.et_month1) EditText et_month1;
	@ViewInject(id=R.id.et_month2) EditText et_month2;
	@ViewInject(id=R.id.et_month3) EditText et_month3;
	@ViewInject(id=R.id.et_month4) EditText et_month4;
	@ViewInject(id=R.id.et_month5) EditText et_month5;
	@ViewInject(id=R.id.et_month6) EditText et_month6;
	@ViewInject(id=R.id.et_month7) EditText et_month7;
	@ViewInject(id=R.id.et_month8) EditText et_month8;
	@ViewInject(id=R.id.et_month9) EditText et_month9;
	@ViewInject(id=R.id.et_month10) EditText et_month10;
	@ViewInject(id=R.id.et_month11) EditText et_month11;
	@ViewInject(id=R.id.et_month12) EditText et_month12;
	
	private String[] trans = new String[7];
	private JointProper proper = new JointProper();
	public static InsulationElecActivity iea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_insulation_elec);
		iea = this;
		Bundle bundle = getIntent().getBundleExtra("bundle");
		trans = bundle.getStringArray("proper");
		proper.setPl(trans[0]);
		proper.setSection(trans[1]);
		proper.setSpec(trans[2]);
		proper.setWells(trans[3]);
		proper.setM_year(trans[4]);
		proper.setCreateBy(trans[5]);
		proper.setAuditor(trans[6]);
	}
	
	public void backClick(View v) {
		finish();
	}
	
	public void nextClick(View v) {
		String month_1 = et_month1.getText().toString();
		if(StringUtil.isBlank(month_1)) {
			Toast.makeText(getApplicationContext(), "请填写1月份通电点电位", Toast.LENGTH_SHORT).show();
			return;
		}
		String month_2 = et_month2.getText().toString();
		String month_3 = et_month3.getText().toString();
		String month_4 = et_month4.getText().toString();
		String month_5 = et_month5.getText().toString();
		String month_6 = et_month6.getText().toString();
		String month_7 = et_month7.getText().toString();
		String month_8 = et_month8.getText().toString();
		String month_9 = et_month9.getText().toString();
		String month_10 = et_month10.getText().toString();
		String month_11 = et_month11.getText().toString();
		String month_12 = et_month12.getText().toString();
		proper.setMonth_1(month_1);
		proper.setMonth_2(month_2);
		proper.setMonth_3(month_3);
		proper.setMonth_4(month_4);
		proper.setMonth_5(month_5);
		proper.setMonth_6(month_6);
		proper.setMonth_7(month_7);
		proper.setMonth_8(month_8);
		proper.setMonth_9(month_9);
		proper.setMonth_10(month_10);
		proper.setMonth_11(month_11);
		proper.setMonth_12(month_12);
		
		Intent intent = new Intent(InsulationElecActivity.this, InsulationAdd.class);
		Bundle bundle = new Bundle();
		bundle.putString("param", proper.toString());
		intent.putExtra("bundle", bundle);
		startActivity(intent);
	}

}
