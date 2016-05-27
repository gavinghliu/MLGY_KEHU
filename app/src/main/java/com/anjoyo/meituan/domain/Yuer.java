package com.anjoyo.meituan.domain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Yuer {
	private int surplus_time;
	private String guest_name;
	private String service_name;
	private String card_num;
	private String last_use_time;
	private String service_code;
	
	public String toListString() {
		return card_num + "&  " + guest_name + "&" + service_name  + "&   " + surplus_time + "&" + last_use_time + "&" + service_code ;
	}
	
//	{'balances’ : [
//
//		{
//		card_num:123134,
//		guest_name:梁总,
//		service_name:半年服务,
//		surplus_time:19,
//		last_use_time:2016-12-12
//
//		},
//		
	
	public static ArrayList<Yuer> parseYuer(String res) {
//		res = "{'balances': [{card_num:12090001,guest_name:何总,service_name:13800礼包结肠SPA服务,surplus_time:'12',last_use_time:'2015-12-10'},{card_num:0012090001,guest_name:何总,service_name:13800礼包水娃娃服务,surplus_time:'48',last_use_time:'2015-12-10'},{card_num:0012090001,guest_name:何总,service_name:NC眼部＋NC脸部服务,surplus_time:'1',last_use_time:''},{card_num:0012090001,guest_name:何总,service_name:防癌美胸服务,surplus_time:'1',last_use_time:''},{card_num:0012090001,guest_name:何总,service_name:四季养芳淋巴排毒服务,surplus_time:'7',last_use_time:''}]}";
		String fileString = res;
		
		ArrayList<Yuer> questionList = new ArrayList<Yuer>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("balances");
			if (array != null && !array.equals("[]")) {
				questionList = new ArrayList<Yuer>();
				for (int i = 0; i < array.length(); i++) {
					Yuer question = new Yuer();
					JSONObject object2 = array.optJSONObject(i);

					question.setSurplus_time(object2.optInt("surplus_time"));
					question.setGuest_name(object2.optString("guest_name"));
					question.setService_name(object2.optString("service_name"));
					question.setCard_num(object2.getString("card_num"));
					question.setService_code(object2.getString("service_code"));
					if (object2.has("last_use_time")) {
						question.setLast_use_time(object2.optString("last_use_time"));
						if (object2.optString("last_use_time").trim().length() == 0) {
							question.setLast_use_time("无限期");
						}
					} else {
						question.setLast_use_time("无限期");
					}
					questionList.add(question);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String s = e.getMessage();
			if (s.contains("No value for")) {
				s = "缺少key值--" + s.substring("No value for".length());
			} else if (s.contains("cannot be converted to JSONObject")){
				s = "当前选择的不是配置文件,请仔细查看";
			} else if (s.contains("Unterminated object at character")) {
				int startIndex = s.indexOf("Unterminated object at character") + "Unterminated object at character".length();
				int endIndex =  s.indexOf("of");
				String numberString = s.substring(startIndex, endIndex).trim();
				s = "第" + numberString + "个字符有错误：";
				
				int rang = 15;
				int number = Integer.parseInt(numberString);
				int errStartIndex = number;
				int errEndIndex = errStartIndex + rang;
				if (errStartIndex < rang) {
					errStartIndex = 0;
				} else {
					errStartIndex -= rang;
				}
				
				if (errEndIndex > fileString.length()) {
					errEndIndex = fileString.length() - 1;
				}
				
				s += "在\n" + fileString.substring(errStartIndex, errEndIndex) + "\n中的" +  fileString.substring(number - 4, number - 1)+ "前面";
			} else if (s.contains("Unterminated array at character")) {
				int startIndex = s.indexOf("Unterminated array at character") + "Unterminated array at character".length();
				int endIndex =  s.indexOf("of");
				String numberString = s.substring(startIndex, endIndex).trim();
				s = "第" + numberString + "个字符有错误：";
				
				int rang = 25;
				int number = Integer.parseInt(numberString);
				int errStartIndex = number;
				int errEndIndex = errStartIndex + rang;
				if (errStartIndex < rang) {
					errStartIndex = 0;
				} else {
					errStartIndex -= rang;
				}
				
				if (errEndIndex > fileString.length()) {
					errEndIndex = fileString.length() - 1;
				}
				
				s += "在\n" + fileString.substring(errStartIndex, errEndIndex) + "\n中的" +  fileString.substring(number - 4, number)+ "后面";
			
			} 
			Log.d("test", s);
		}

		return questionList;
	}



	public int getSurplus_time() {
		return surplus_time;
	}



	public void setSurplus_time(int surplus_time) {
		this.surplus_time = surplus_time;
	}



	public String getGuest_name() {
		return guest_name;
	}



	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}



	public String getService_name() {
		return service_name;
	}



	public void setService_name(String service_name) {
		this.service_name = service_name;
	}



	public String getCard_num() {
		return card_num;
	}



	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}



	public String getLast_use_time() {
		return last_use_time;
	}



	public void setLast_use_time(String last_use_time) {
		this.last_use_time = last_use_time;
	}

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}
	

}
