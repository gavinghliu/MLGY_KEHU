package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UnPayService {
	private int unpay_money;
	private String project;
	private String guest_name;
	private String guest_num;
	private String tiem;
	private String projectCode;
	
	public String getProjectCode() {
		return projectCode;
	}


	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}


	public String toListString() {
		return guest_num + "&" + guest_name + "&" + tiem  + "&" + project + "&   " + unpay_money + "& " + projectCode ;
	}


	public int getUnpay_money() {
		return unpay_money;
	}

	public void setUnpay_money(int unpay_money) {
		this.unpay_money = unpay_money;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getGuest_name() {
		return guest_name;
	}

	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}

	public String getGuest_num() {
		return guest_num;
	}

	public void setGuest_num(String guest_num) {
		this.guest_num = guest_num;
	}

	public String getTiem() {
		return tiem;
	}

	public void setTiem(String tiem) {
		this.tiem = tiem;
	}

	public static ArrayList<UnPayService> parseQuestion(String res) {
		ArrayList<UnPayService> questionList = new ArrayList<UnPayService>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("unpay");
			if (array != null && !array.equals("[]")) {
				questionList = new ArrayList<UnPayService>();
				for (int i = 0; i < array.length(); i++) {
					UnPayService question = new UnPayService();
					JSONObject object2 = array.optJSONObject(i);

					question.setUnpay_money(object2.optInt("unpay_money"));
					question.setGuest_num(object2.optString("card_num"));
					question.setGuest_name(object2.optString("guest_name"));
					question.setProject(object2.getString("project"));
					question.setProjectCode(object2.getString("projectCode"));
					if (object2.has("time")) {
						question.setTiem(object2.optString("time"));
					}
					questionList.add(question);
				}
			}
//			{'unpay’ : [
			//
//					{
//					guest_num:123134,
//					guest_name:梁总,
//					project:面膜服务,
//					unpay_money:1932,
//					tiem:2014-01-23
//					},
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return questionList;
	}
}
