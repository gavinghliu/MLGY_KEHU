package com.jsk.imgtxt;

import java.io.Serializable;

public class Content implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String details;
	private boolean img; 
	private boolean title;
	private boolean yangzheng;
	private boolean shiping;
	private String answer;
	
	private boolean gif;
	
	public Content(String details, boolean img){
		this.details = details;
		this.img = img;
		setTitle(false);
		setYangzheng(false);
		setShiping(false);
	}
	
	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	public boolean isImg() {
		return img;
	}
	
	public void setImg(boolean img) {
		this.img = img;
	}

	public boolean isYangzheng() {
		return yangzheng;
	}

	public void setYangzheng(boolean yangzheng) {
		this.yangzheng = yangzheng;
	}

	public boolean isShiping() {
		return shiping;
	}

	public void setShiping(boolean shiping) {
		this.shiping = shiping;
	}

	public boolean isTitle() {
		return title;
	}

	public void setTitle(boolean title) {
		this.title = title;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean getGif() {
		return gif;
	}

	public void setGif(boolean gif) {
		this.gif = gif;
	}
	
	
	
}
