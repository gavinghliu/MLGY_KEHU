package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServiceHistoryDetail {
	private String liucheng;
	private String shuoming;
	private String shijian;
	
	public static ArrayList<ServiceHistoryDetail> parseDetail(String res) {
//		res = "{'detail' : [{liucheng:冥想,shuoming:'放松身心，让人心神愉悦',shijian:5} ,{liucheng:'头部松筋(后面)',shuoming:改善睡眠，增强记忆力，改善头部供氧供血',shijian:15}]}";
		ArrayList<ServiceHistoryDetail> questionList = new ArrayList<ServiceHistoryDetail>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("detail");
			if (array != null && !array.equals("[]")) {
				questionList = new ArrayList<ServiceHistoryDetail>();
				for (int i = 0; i < array.length(); i++) {
					ServiceHistoryDetail question = new ServiceHistoryDetail();
					JSONObject object2 = array.optJSONObject(i);

					question.setLiucheng(object2.optString("liucheng"));
					question.setShuoming(object2.optString("shuoming"));
					question.setShijian(object2.optString("shijian"));
					questionList.add(question);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return questionList;
	}

	public String getLiucheng() {
		return liucheng;
	}

	public void setLiucheng(String liucheng) {
		this.liucheng = liucheng;
	}

	public String getShuoming() {
		return shuoming;
	}

	public void setShuoming(String shuoming) {
		this.shuoming = shuoming;
	}

	public String getShijian() {
		return shijian;
	}

	public void setShijian(String shijian) {
		this.shijian = shijian;
	}




}
