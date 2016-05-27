package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Wenzhang {
	private int wenzhang_id;
	private String wenzhang_title;
	private String wenzhang_picture;
	private String video;
	private String yangzheng;
	private String detail;
	private String answer;
	private String shareUrl;
	private String shareDes;
	private String adPic_url;
	
	public void setPic_url(String adPic) {
		adPic_url = adPic;
	} 
	
	public String getPic_url () {
		return adPic_url;
	}

	public int getwenzhang_id() {
		return wenzhang_id;
	}

	public void setwenzhang_id(int wenzhang_id) {
		this.wenzhang_id = wenzhang_id;
	}

	public String getwenzhang_title() {
		return wenzhang_title;
	}

	public void setwenzhang_title(String wenzhang_name) {
		this.wenzhang_title = wenzhang_name;
	}


	public String getwenzhang_picture() {
		return wenzhang_picture;
	}

	public void setwenzhang_picture(String wenzhang_picture) {
		this.wenzhang_picture = wenzhang_picture;
	}


	public static ArrayList<Wenzhang> parsewenzhang(String res) {
		ArrayList<Wenzhang> wenzhangList = new ArrayList<Wenzhang>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("wenzhangs");
			if (array != null && !array.equals("[]")) {
				wenzhangList = new ArrayList<Wenzhang>();
				for (int i = 0; i < array.length(); i++) {
					Wenzhang wenzhang = new Wenzhang();
					JSONObject object2 = array.optJSONObject(i);

					wenzhang.setwenzhang_title(object2.optString("wenzhang_title"));
					wenzhang.setwenzhang_id(object2.getInt("wenzhang_id"));
					wenzhang.setwenzhang_picture(object2.getString("wenzhang_picture"));
					if (object2.has("detail")) {
						wenzhang.setDetail(object2.getString("detail"));
					}
					
					if (object2.has("wenzhang_video")) {
						wenzhang.setVideo(object2.getString("wenzhang_video"));
					}
					
					if (object2.has("wenzhang_yanzhang")) {
						wenzhang.setYangzheng(object2.getString("wenzhang_yanzhang"));
						wenzhang.setAnswer(object2.getString("answer"));
					}
					
					if (object2.has("share_url")) {
						wenzhang.setShareUrl(object2.getString("share_url"));
						wenzhang.setShareDes(object2.getString("share_des"));
					}
					
					if (object2.has("adpic_url")) {
						wenzhang.setPic_url(object2.getString("adpic_url"));
					}
					wenzhangList.add(wenzhang);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return wenzhangList;

	}

	public String getYangzheng() {
		return yangzheng;
	}

	public void setYangzheng(String yangzheng) {
		this.yangzheng = yangzheng;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getShareDes() {
		return shareDes;
	}

	public void setShareDes(String shareDes) {
		this.shareDes = shareDes;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

}
