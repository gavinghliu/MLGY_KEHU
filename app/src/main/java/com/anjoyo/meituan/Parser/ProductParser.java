package com.anjoyo.meituan.Parser;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.Seller;

public class ProductParser {
	public ArrayList<Product> getMerchant(Object res){
		ArrayList<Product> merchant= null;
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("products");
			if (array != null && !array.equals("[]")) {
				merchant=new ArrayList<Product>();
				for(int i=0;i<array.length();i++){
					Product product=new Product();
					JSONObject object2 = array.optJSONObject(i);
					
					product.setProduct_id(object2.optInt("product_id"));  
					product.setProduct_name(object2.optString("product_name"));
					product.setProduct_picture(object2.getString("product_picture"));
					product.setProduct_rank(object2.getInt("product_rank"));
					product.setKind(object2.getString("product_kind"));
					product.setProductCommentCount(object2.getInt("comment_count"));
					product.setProduct_price(object2.getString("product_price"));
					product.setIslike(object2.getInt("product_islike"));
					product.setLikeCount(object2.getInt("product_like_count"));
					product.setProduct_des(object2.getString("product_des"));
					if (object2.has("product_picture2")) {
						product.setProduct_picture2(object2.getString("product_picture2"));
					}
					if (object2.has("product_picture3")) {
						product.setProduct_picture3(object2.getString("product_picture3"));
					}
					
					if (object2.has("service_time")) {
						product.setTime(object2.getString("service_time"));
					}
					if (object2.has("is_fav")) {
						product.setIsFav(object2.getInt("is_fav"));
					}
					
					if (object2.has("bug_url")) {
						product.setBuyUrl(object2.getString("bug_url"));
					}
					
					if (object2.has("has_comment")) {
						product.setHasComment(object2.getInt("has_comment") == 1);
					}
					
					if (object2.has("product_bianma")) {
						product.setBianma(object2.getString("product_bianma"));
					}
					
					if (object2.has("product_danju")) {
						product.setDanju(object2.getString("product_danju"));
					}
					merchant.add(product);
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return merchant;
	}
	
}
