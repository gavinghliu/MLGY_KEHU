package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UnGetProduct {
	private int number;
	private String card_num;
	private String unit;
	private String guest_name;
	private String product_name;
	private String product_code;
	
	public String toListString() {
		return card_num + "&  " + guest_name + "&" + product_name  + "& " + number + "& " + unit+ "& " + product_code;
	}

	public static ArrayList<UnGetProduct> parseQuestion(String res) {
		ArrayList<UnGetProduct> questionList = new ArrayList<UnGetProduct>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("untake");
			if (array != null && !array.equals("[]")) {
				questionList = new ArrayList<UnGetProduct>();
				for (int i = 0; i < array.length(); i++) {
					UnGetProduct question = new UnGetProduct();
					JSONObject object2 = array.optJSONObject(i);

					question.setNumber(object2.optInt("number"));
					question.setCard_num(object2.optString("card_num"));
					question.setGuest_name(object2.optString("guest_name"));
					question.setProduct_name(object2.getString("product_name"));
					question.setProduct_code(object2.getString("product_code"));
					if (object2.has("unit")) {
						question.setUnit(object2.optString("unit"));
					} 
					questionList.add(question);
				}
			}
//			{'untake’ : [
			//
//					{
//					card_num:123134,
//					guest_name:梁总,
//					product_name:半年服务,
//					number:19,
//					unit:瓶
//					},
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return questionList;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getGuest_name() {
		return guest_name;
	}

	public void setGuest_name(String guest_name) {
		this.guest_name = guest_name;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	
	

}
