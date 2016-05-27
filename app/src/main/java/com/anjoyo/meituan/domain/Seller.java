package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Seller {
	private int seller_id;
	private String seller_name;
	private String seller_address;
	private String seller_phone;
	private String seller_picture;
	private String seller_picture2;
	private String seller_picture3;
	private float seller_rank;
	private String kind;
	private double latitude;
	private double longitude;
	private int commentCount;
	private int islike;
	private int likeCount;
	private int isFav;

	public int getIsFav() {
		return isFav;
	}

	public void setIsFav(int isFav) {
		this.isFav = isFav;
	}
	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getIslike() {
		return islike;
	}

	public void setIslike(int islike) {
		this.islike = islike;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getSeller_address() {
		return seller_address;
	}

	public void setSeller_address(String seller_address) {
		this.seller_address = seller_address;
	}

	public String getSeller_phone() {
		return seller_phone;
	}

	public void setSeller_phone(String seller_phone) {
		this.seller_phone = seller_phone;
	}

	public String getSeller_picture() {
		return seller_picture;
	}

	public void setSeller_picture(String seller_picture) {
		this.seller_picture = seller_picture;
	}

	public float getSeller_rank() {
		return seller_rank;
	}

	public void setSeller_rank(float seller_rank) {
		this.seller_rank = seller_rank;
	}

	public static ArrayList<Seller> parseSeller(String res) {
		ArrayList<Seller> sellerList = new ArrayList<Seller>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("sellers");
			if (array != null && !array.equals("[]")) {
				sellerList = new ArrayList<Seller>();
				for (int i = 0; i < array.length(); i++) {
					Seller seller = new Seller();
					JSONObject object2 = array.optJSONObject(i);

					seller.setSeller_name(object2.optString("seller_name"));
					seller.setSeller_address(object2.optString("seller_address"));
					seller.setSeller_phone(object2.optString("seller_phone"));
					seller.setSeller_rank(object2.getInt("seller_rank"));
					seller.setSeller_id(object2.getInt("seller_id"));
					seller.setCommentCount(object2.getInt("comment_count"));
					seller.setIslike(object2.getInt("seller_islike"));
					seller.setLikeCount(object2.getInt("seller_like_count"));
//					seller.setCommentCount(object.getInt("comment_count"));
					seller.setLatitude(object2.getDouble("latitude"));
					seller.setLongitude(object2.getDouble("longitude"));
					seller.setSeller_picture(object2.getString("seller_picture"));
					if (object2.has("is_fav")) {
						seller.setIsFav(object2.getInt("is_fav"));
					}
					if (object2.has("seller_picture2")) {
						seller.setSeller_picture2(object2.getString("seller_picture2"));
					}
					if (object2.has("seller_picture3")) {
						seller.setSeller_picture3(object2.getString("seller_picture3"));
					}
					
					seller.setKind(object2.getString("seller_kind"));
					sellerList.add(seller);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sellerList;

	}

	public String getSeller_picture2() {
		return seller_picture2;
	}

	public void setSeller_picture2(String seller_picture2) {
		this.seller_picture2 = seller_picture2;
	}

	public String getSeller_picture3() {
		return seller_picture3;
	}

	public void setSeller_picture3(String seller_picture3) {
		this.seller_picture3 = seller_picture3;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
