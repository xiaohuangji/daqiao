package com.tg.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.HttpException;

public class MCPUtil {

	public static String BASE_API_URL = "http://118.186.218.43/api/";
	public static final String APP_SECRET = "9f738a3934abf88b1dca8e8092043fbd";
	public static final String APP_ID = "1001";

	private static Map<String, String> params = new HashMap<String, String>();

	private static String ticket = "BUjVQQVVgV20DM109DTBQOw0_UmFTZVIzDmcPM1VjAWA.";

	private static String userSecretKey = "0cc23bcfe33f62326aef583e3691c262";

	public static void main(String[] args) throws HttpException, IOException {
		params.put("t", ticket);
		params.put("userSecretKey", userSecretKey);
		params.put("guideId", "10000007");
		params.put("scenic", "test");
		params.put("startTime", System.currentTimeMillis() + "");
		params.put("endTime", System.currentTimeMillis() + "");
		putOtherParam(true);
		String result = POST("inviteEvent/invite");
		System.out.println(result);

	}

	public static String POST(String url) throws HttpException, IOException {

		HttpURLConnection connection = null;
		BufferedReader reader = null;

		try {
			URL hurl = new URL(BASE_API_URL + url);

			connection = (HttpURLConnection) hurl.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.connect();

			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());

			StringBuffer sbb = new StringBuffer("");
			for (String key : params.keySet()) {
				sbb.append(key);
				sbb.append("=");
				sbb.append(params.get(key));
				sbb.append("&");
			}
			out.writeBytes(sbb.toString());

			out.flush();
			out.close();

			// 读取响应
			GZIPInputStream gin = new GZIPInputStream(
					connection.getInputStream());
			reader = new BufferedReader(new InputStreamReader(gin, "utf-8"));

			String lines;
			StringBuffer sbs = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sbs.append(lines);
			}
			reader.close();
			return sbs.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// logger.error("appstore receipt error:",e);
		}
		return "ERROR";
	}

	private static void putOtherParam(boolean NeedTicket) {

		if (NeedTicket && !ticket.isEmpty()) {
			params.put("t", ticket);
		}
		params.put("app_id", APP_ID);
		params.put("v", "1.0"); // TODO
		params.put("call_id", String.valueOf(System.currentTimeMillis()));
		params.put("gz", "compression");
		params.put("sig",
				getSig(params, APP_SECRET, NeedTicket ? userSecretKey : null));

	}

	private static String getSig(Map<String, String> params,
			String appSecretKey, String userSecretKey) {

		Vector<String> vecSig = new Vector<String>();
		for (String key : params.keySet()) {
			String value = params.get(key);
			vecSig.add(key + "=" + value);
		}

		String[] nameValuePairs = new String[vecSig.size()];
		vecSig.toArray(nameValuePairs);

		for (int i = 0; i < nameValuePairs.length; i++) {
			for (int j = nameValuePairs.length - 1; j > i; j--) {
				if (nameValuePairs[j].compareTo(nameValuePairs[j - 1]) < 0) {
					String temp = nameValuePairs[j];
					nameValuePairs[j] = nameValuePairs[j - 1];
					nameValuePairs[j - 1] = temp;
				}
			}
		}
		StringBuffer nameValueStringBuffer = new StringBuffer();
		for (int i = 0; i < nameValuePairs.length; i++) {
			nameValueStringBuffer.append(nameValuePairs[i]);
		}
		nameValueStringBuffer.append(appSecretKey);
		if (userSecretKey != null && !userSecretKey.isEmpty()) {
			nameValueStringBuffer.append(userSecretKey);
		}

		String sig = MD5Util.md5(nameValueStringBuffer.toString());
		return sig;

	}
}
