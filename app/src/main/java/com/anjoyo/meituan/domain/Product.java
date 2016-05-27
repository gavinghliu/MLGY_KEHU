package com.anjoyo.meituan.domain;

public class Product {
	private int product_id;
	private String kind;
	private String product_name;
	private String product_picture;
	private String product_price;
	private String product_picture2;
	private String product_picture3;
	private float product_rank;
	private String product_des;
	private int productCommentCount;
	private String time;
	private boolean hasComment;
	private int islike;
	private int likeCount;
	private int isFav;
	private String buyUrl;
	private String bianma;
	private String danju;

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
	
	public boolean isHasComment() {
		return hasComment;
	}

	public void setHasComment(boolean hasComment) {
		this.hasComment = hasComment;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getProductCommentCount() {
		return productCommentCount;
	}

	public void setProductCommentCount(int productCommentCount) {
		this.productCommentCount = productCommentCount;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public String getProduct_des() {
		return product_des;
	}

	public void setProduct_des(String product_des) {
		this.product_des = product_des;
	}

	public String getProduct_price() {
		return product_price;
	}

	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}


	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_picture() {
		return product_picture;
	}

	public void setProduct_picture(String product_picture) {
		this.product_picture = product_picture;
	}


	public float getProduct_rank() {
		return product_rank;
	}

	public void setProduct_rank(float product_rank) {
		this.product_rank = product_rank;
	}

	public String getProduct_picture2() {
		return product_picture2;
	}

	public void setProduct_picture2(String product_price2) {
		this.product_picture2 = product_price2;
	}

	public String getProduct_picture3() {
		return product_picture3;
	}

	public void setProduct_picture3(String product_price3) {
		this.product_picture3 = product_price3;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	public String getBianma() {
		return bianma;
	}

	public void setBianma(String bianma) {
		this.bianma = bianma;
	}

	public String getDanju() {
		return danju;
	}

	public void setDanju(String danju) {
		this.danju = danju;
	}

}
