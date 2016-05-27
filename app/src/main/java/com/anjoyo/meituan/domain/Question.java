package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class Question {
	private String question_id;
	private String question_time;
	private String question_content;
	private String question_answer;
	private boolean isDone;
	

	public static ArrayList<Question> parseQuestion(String res) {
//		res = "{'questions' : [{question_id:2,question_answer:sadsds,question_time:'2015-03-11 13:21:58',question_done:处理中,question_content:2},{question_id:2,question_answer:sdfsd,question_time:'2015-03-11 13:08:26',question_done:处理中,question_content:2}]}";
		String fileString = res;
		ArrayList<Question> questionList = new ArrayList<Question>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("questions");
			if (array != null && !array.equals("[]")) {
				questionList = new ArrayList<Question>();
				for (int i = 0; i < array.length(); i++) {
					Question question = new Question();
					JSONObject object2 = array.optJSONObject(i);

					question.setQuestion_id(object2.optString("question_id"));
					question.setQuestion_time(object2.optString("question_time"));
					question.setQuestion_content(object2.optString("question_content"));
					question.setDone(object2.getString("question_done").equals("是"));
					if (object2.has("question_answer")) {
						question.setQuestion_answer(object2.optString("question_answer"));
					} else {
						question.setQuestion_answer(null);
					}
					questionList.add(question);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return questionList;
	}
	
	public String getQuestion_answer() {
		return question_answer;
	}

	public void setQuestion_answer(String question_answer) {
		this.question_answer = question_answer;
	}


	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_time() {
		return question_time;
	}

	public void setQuestion_time(String question_time) {
		this.question_time = question_time;
	}

	public String getQuestion_content() {
		return question_content;
	}

	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
