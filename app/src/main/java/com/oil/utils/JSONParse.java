package com.oil.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.oil.domain.GetIdName;
import com.oil.domain.NotiStu;
import com.oil.domain.PipeRecord;
import com.oil.domain.ReplyInfo;

public class JSONParse {
	public static List<PipeRecord> parsePipeMeasureRecord(String content) {
		List<PipeRecord> list = new ArrayList<PipeRecord>();
		PipeRecord record;
		JSONArray arr;
		try {
			System.out.println(content + "-====================");
			arr = new JSONObject(content).getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject subData = arr.getJSONObject(i);
				record = new PipeRecord();
				record.set_id(subData.getString("id"));
				record.setPipeName(subData.getString("pl_name"));
				record.setSection(subData.getString("pl_section_name"));
				record.setSpec(subData.getString("pl_spec_name"));
				record.setWell(subData.getString("jinzhan"));
				record.setMonth(subData.getString("c_month"));
				record.setCreateTime(subData.getString("create_time"));
				record.setState(subData.getString("status"));
				list.add(record);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<PipeRecord> parseCathodeMonthRecord(String content) {
		List<PipeRecord> list = new ArrayList<PipeRecord>();
		PipeRecord record;
		JSONArray arr;
		try {
			arr = new JSONObject(content).getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject subData = arr.getJSONObject(i);
				record = new PipeRecord();
				record.set_id(subData.getString("id"));
				record.setPipeName(subData.getString("pl_name"));
				record.setSection(subData.getString("pl_section_name"));
				record.setSpec(subData.getString("pl_spec_name"));
				record.setWell(subData.getString("jinzhan"));
				record.setMonth(subData.getString("r_month"));
				record.setCreater(subData.getString("created_by"));
				record.setVerifier(subData.getString("auditor"));
				record.setCreateTime(subData.getString("create_time"));
				record.setState(subData.getString("status"));
				list.add(record);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<PipeRecord> parsePipePollingRecord(String content) {
		List<PipeRecord> list = new ArrayList<PipeRecord>();
		PipeRecord record;
		JSONArray arr;
		try {
			arr = new JSONObject(content).getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject subData = arr.getJSONObject(i);
				record = new PipeRecord();
				record.set_id(subData.getString("id"));
				record.setPipeName(subData.getString("pl_name"));
				record.setSection(subData.getString("pl_section_name"));
				record.setSpec(subData.getString("pl_spec_name"));
				record.setWell(subData.getString("jinzhan"));
				record.setMonth(subData.getString("p_month"));
				record.setCreateTime(subData.getString("create_time"));
				record.setState(subData.getString("status"));
				record.setProWell(subData.getString("save_jinzhan"));
				list.add(record);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<PipeRecord> parseHighStaticRecord(String content) {
		List<PipeRecord> list = new ArrayList<PipeRecord>();
		PipeRecord record;
		JSONArray arr;
		try {
			arr = new JSONObject(content).getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject subData = arr.getJSONObject(i);
				record = new PipeRecord();
				record.set_id(subData.getString("id"));
				record.setPipeName(subData.getString("pl_name"));
				record.setSection(subData.getString("pl_section_name"));
				record.setSpec(subData.getString("pl_spec_name"));
				record.setCreateTime(subData.getString("create_time"));
				record.setState(subData.getString("status"));
				list.add(record);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<NotiStu> parseNotiStu(String content) {
		List<NotiStu> list = new ArrayList<NotiStu>();
		NotiStu record;
		JSONArray arr;
		try {
			arr = new JSONObject(content).getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject subData = arr.getJSONObject(i);
				record = new NotiStu();
				record.setId(subData.getString("id"));
				record.setTitle(subData.getString("title"));
				record.setContent(subData.getString("content"));
				record.setTime(subData.getString("create_time"));
				list.add(record);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static NotiStu parseNotiStuDetail(String content) {
		List<ReplyInfo> list = new ArrayList<ReplyInfo>();
		NotiStu record = new NotiStu();
		ReplyInfo info;
		JSONObject obj;
		try {
			obj = new JSONObject(content).getJSONObject("data");
			JSONArray arr = obj.getJSONArray("replies");
			JSONObject notice = obj.getJSONObject("notice");
			record.setTitle(notice.getString("title"));
			record.setContent(notice.getString("content"));
			record.setTime(notice.getString("create_time"));
			record.setPath(notice.getString("path"));
			if (arr.length() != 0) {
				for (int i = 0; i < arr.length(); i++) {
					JSONObject subData = arr.getJSONObject(i);
					info = new ReplyInfo();
					info.setName(subData.getString("replier"));
					info.setContent(subData.getString("content"));
					info.setPath(subData.getString("path"));
					list.add(info);
				}
				record.setComment(list);
			} else {
				record.setComment(list);
			}
			return record;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<GetIdName> parseIdName(String content) {
		List<GetIdName> list = new ArrayList<GetIdName>();
		try {
			JSONArray arr = new JSONObject(content).getJSONArray("data");
			GetIdName lr;
			for (int i = 0; i < arr.length(); i++) {
				lr = new GetIdName();
				JSONObject subName = arr.getJSONObject(i);
				lr.set_id(Integer.parseInt(subName.getString("id")));
				lr.setName(subName.getString("name"));
				list.add(lr);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String parseImageResult(List<String> str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.size(); i++) {
			int j = i % 5;
			if (j == 0) {
				sb.append("&pic1=" + str.get(i));
			} else if (j == 1) {
				sb.append("&pic2=" + str.get(i));
			} else if (j == 2) {
				sb.append("&pic3=" + str.get(i));
			} else if (j == 3) {
				sb.append("&pic4=" + str.get(i));
			} else if (j == 4) {
				sb.append("&pic5=" + str.get(i));
			}
		}
		return sb.toString();
	}
}
