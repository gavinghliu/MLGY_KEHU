package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Beautician {
	private String beautician_id;
	private String beautician_name;
	private String beautician_picture;
	private float beautician_rank;
	private String beautician_des;
	private int beauticianCommentCount;
	private String tel;
	private ArrayList<Comment> commentList;


	public int getbeauticianCommentCount() {
		return beauticianCommentCount;
	}

	public void setbeauticianCommentCount(int beauticianCommentCount) {
		this.beauticianCommentCount = beauticianCommentCount;
	}

	public String getbeautician_des() {
		return beautician_des;
	}

	public void setbeautician_des(String beautician_des) {
		this.beautician_des = beautician_des;
	}

	public String getbeautician_id() {
		return beautician_id;
	}

	public void setbeautician_id(String beautician_id) {
		this.beautician_id = beautician_id;
	}

	public String getbeautician_name() {
		return beautician_name;
	}

	public void setbeautician_name(String beautician_name) {
		this.beautician_name = beautician_name;
	}

	public String getbeautician_picture() {
		return beautician_picture;
	}

	public void setbeautician_picture(String beautician_picture) {
		this.beautician_picture = beautician_picture;
	}


	public float getbeautician_rank() {
		return beautician_rank;
	}

	public void setbeautician_rank(float beautician_rank) {
		this.beautician_rank = beautician_rank;
	}

	public String getPTel() {
		return tel;
	}

	public void setTel(String proCode) {
		this.tel = proCode;
	}
	
	//"{'beauticians': [{beautician_id: 2,beautician_name:'xxx美容师',comment_count: 2,beautician_picture: '',beautician_des: '美容师介绍',beautician_rank: 3,beautician_tel: 13800000000}]}";
	static public ArrayList<Beautician> getMerchant(Object res) {
		ArrayList<Beautician> merchant = null;
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("beauticians");
			if (array != null && !array.equals("[]")) {
				merchant = new ArrayList<Beautician>();
				for (int i = 0; i < array.length(); i++) {
					Beautician beautician = new Beautician();
					JSONObject object2 = array.optJSONObject(i);

					beautician
							.setbeautician_id(object2.optString("beautician_id"));
					beautician.setbeautician_name(object2
							.optString("beautician_name"));
					beautician.setbeautician_picture(object2
							.getString("beautician_picture"));
					beautician.setbeautician_rank(object2
							.getInt("beautician_rank"));
					beautician.setbeauticianCommentCount(object2
							.getInt("comment_count"));
					beautician.setbeautician_des(object2
							.getString("beautician_des"));
					beautician.setTel(object2
							.getString("beautician_tel"));
					
					JSONArray commentArray = object2.getJSONArray("beautician_comment");
					ArrayList<Comment> cList = new ArrayList<Comment>();
					if (commentArray != null && !commentArray.equals("[]")) {
						for (int j = 0; j < commentArray.length(); j++) {
							Comment comment = new Comment();
							JSONObject object3 = commentArray.optJSONObject(j);
							comment.setName(object3.optString("comment_name"));  
							comment.setTime(object3.optString("comment_time"));
							comment.setContent(object3.optString("comment_content"));
							comment.setRank(object3.getInt("comment_rank"));
							cList.add(comment);
						}
					}
					beautician.setCommentList(cList);
					
					merchant.add(beautician);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return merchant;
	}

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}
}
