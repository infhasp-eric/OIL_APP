package com.oil.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpGetDataByGet {
	public static String getDataFromServer(String path, String session){
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			URL url = new URL(path);
			System.out.println("path=====" + path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Cookie", session);
			if(conn.getResponseCode() == 200){
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while((line = br.readLine()) != null){
					sb.append(line);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String getDataFromServer(String path){
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if(conn.getResponseCode() == 200){
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while((line = br.readLine()) != null){
					sb.append(line);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
