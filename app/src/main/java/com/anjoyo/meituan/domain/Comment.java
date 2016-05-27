package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Comment {

	private String name;
	private String time;
	private int rank;
	private String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public static List<Comment> parseComment(String res) {
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("comments");
			if (array != null && !array.equals("[]")) {
				commentList  =new ArrayList<Comment>();
				for(int i=0;i<array.length();i++){
					Comment comment = new Comment();
					JSONObject object2 = array.optJSONObject(i);
					
					comment.setName(object2.optString("comment_name"));  
					comment.setTime(object2.optString("comment_time"));
					comment.setContent(object2.optString("comment_content"));
					comment.setRank(object2.getInt("comment_rank"));
					commentList.add(comment);
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
				
				if (errEndIndex > res.length()) {
					errEndIndex = res.length() - 1;
				}
				
				s += "在\n" + res.substring(errStartIndex, errEndIndex) + "\n中的" +  res.substring(number - 4, number - 1)+ "前面";
			} else if (s.contains(" literal value at character")) {
				int startIndex = s.indexOf(" literal value at character") + " literal value at character".length();
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
				
				if (errEndIndex > res.length()) {
					errEndIndex = res.length() - 1;
				}
				
				s += "在\n" + res.substring(errStartIndex, errEndIndex) + "\n中的" +  res.substring(number - 4, number)+ "后面";
			} 
			Log.d("log",s);
		}
			
		return commentList;

	}

}
