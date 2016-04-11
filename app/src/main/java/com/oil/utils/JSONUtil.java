package com.oil.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.oil.domain.InsulationRecord;
import com.oil.domain.Record;

public class JSONUtil {
	
	public static JSONObject stringToJson(String param) {
		JSONObject allresult = null;

		try {
			allresult = new JSONObject(param);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allresult;
	}
	
	public static JSONArray stringToJsonArray(String params) {
		JSONArray array = null;
		try {
			array = new JSONArray(params);
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}

	public static List<InsulationRecord> parseInsulation(String content){
		List<InsulationRecord> list = null;
		InsulationRecord ir;
		try {
			list = new ArrayList<InsulationRecord>();
			JSONArray arr = new JSONObject(content).getJSONArray("data");
			for(int i = 0; i < arr.length();i++){
				JSONObject subData = arr.getJSONObject(i);
				ir = new InsulationRecord();
				ir.setId(subData.getInt("id"));
				ir.setLine_name(subData.getString("pl_name"));
				ir.setStart_end(subData.getString("pl_section_name"));
				ir.setLine_standard(subData.getString("pl_spec_name"));
				ir.setWell(subData.getString("jinzhan"));
				ir.setYear(subData.getString("m_year"));
				ir.setCreateBy(subData.getString("created_by"));
				ir.setExamine(subData.getString("auditor"));
				ir.setCreateTime(subData.getString("create_time"));
				ir.setState(subData.getString("status"));
				list.add(ir);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Record> parseLinename(String content){
		List<Record> list = new ArrayList<Record>();
		try {
			JSONArray arr = new JSONObject(content).getJSONArray("data");
			Record lr;
			for(int i = 0;i < arr.length() ;i++){
				lr = new Record();
				JSONObject subName = arr.getJSONObject(i);
				lr.setId(Integer.parseInt(subName.getString("id")));
				lr.setName(subName.getString("name"));
				list.add(lr);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] parseSpec(String content) {
		String[] spec;
		try {
			JSONArray arr = new JSONObject(content).getJSONArray("data");
			spec = new String[arr.length()];
			for(int i = 0;i < arr.length() ;i++){
				JSONObject subName = arr.getJSONObject(i);
				spec[i] = subName.getString("name");
			}
			return spec;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
