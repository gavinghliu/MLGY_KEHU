package com.jsk.imgtxt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnalysisJSON {

	public static List<Content> getProvinceCities(String json){
		List<Content> list = new ArrayList<Content>();
		
		try {
			JSONArray jsonArray = new JSONArray(json);
			int count = jsonArray.length();
			for(int i=0; i < count; i++){
				JSONObject object = jsonArray.getJSONObject(i);
				String detail = object.getString("detail");
				boolean img = false;
				if(detail.indexOf(".jpg") != -1 || detail.indexOf(".png") != -1){
					img = true;
				}
				Content content = new Content(detail, img);
				
				if (detail.indexOf(".gif") != -1 ) {
					content.setGif(true);
				}
				
				list.add(content);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} 
		
		return list;
	}
}
