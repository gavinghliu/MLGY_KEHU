package com.anjoyo.meituan.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.crypto.Mac;

import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.ui.ProductDetailActivity;
import com.anjoyo.meituan.utils.SplitNetImagePath;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Product> allList;
	private Intent intent;
	Bitmap pngBM;// 网络图片
	Bitmap[] bitmaps;
	String mAction;

	public HistoryAdapter(Context context, ArrayList<Product> allList) {
		this.context = context;
		this.allList = allList;
		System.out.println("allList.size()" + allList.size());

		intent = new Intent(context, ProductDetailActivity.class);

//		bitmaps = new Bitmap[allList.size()];
//		ImageCache1 imageCache = new ImageCache1();
//		imageCache.start();
//		try {
//			imageCache.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void setList(ArrayList<Product> allList) {
		this.allList = allList;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return allList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setAction(String action) {
		mAction = action;
	}
	
	public String getAction() {
		return mAction;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.merchant_listitem, null);
			holderView = new HolderView();
			holderView.name_merchant = (TextView) convertView
					.findViewById(R.id.name_merchant);
			holderView.image_merchant = (ImageView) convertView
					.findViewById(R.id.image_merchant);
			holderView.ratingBar = (RatingBar) convertView
					.findViewById(R.id.ratingBar1);
			holderView.relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.rl_merchant_item);
			holderView.commentCount = (TextView) convertView.findViewById(R.id.address_merchant);
			holderView.serviceTime = (TextView) convertView.findViewById(R.id.service_time);
			holderView.hasComment = (TextView) convertView.findViewById(R.id.service_comment);

			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		final Product product = allList.get(position);
		holderView.name_merchant
				.setText(product.getProduct_name());

		if (1 == product.getProduct_id()) {
			holderView.image_merchant.setImageResource(R.drawable.p1);
		} else if (2 == product.getProduct_id()) {
			holderView.image_merchant.setImageResource(R.drawable.p2);
		}else if (21 == product.getProduct_id()) {
			holderView.image_merchant.setImageResource(R.drawable.p21);
		} else if (22 == product.getProduct_id()) {
			holderView.image_merchant.setImageResource(R.drawable.p22);
		} else if (23 == product.getProduct_id()) {
			holderView.image_merchant.setImageResource(R.drawable.p23);
		}
		
		holderView.ratingBar.setRating(product.getProduct_rank());
		holderView.commentCount.setText("有" + product.getProductCommentCount() + "条评论");
//		holderView.image_merchant.setImageBitmap(bitmaps[position]);
		
		holderView.serviceTime.setVisibility(View.GONE);
		if (null != product.getTime()) {
			holderView.serviceTime.setVisibility(View.VISIBLE);
			holderView.serviceTime.setText(product.getTime());
		}
		
		holderView.hasComment.setVisibility(View.VISIBLE);
		if (product.isHasComment()) {
			holderView.hasComment.setText("已评论");
			holderView.hasComment.setTextColor(Color.rgb(163, 153, 153));
		} else {
			holderView.hasComment.setText("未评论");
			holderView.hasComment.setTextColor(Color.rgb(255, 153, 0));
		}

		final int getPosition = position;
		holderView.relativeLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				intent.setAction(mAction);
				intent.putExtra("allPosition", getPosition);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class HolderView {

		private ImageView image_merchant;
		private TextView name_merchant;
		private RatingBar ratingBar;
		private RelativeLayout relativeLayout;
		private TextView commentCount;
		private TextView serviceTime;
		private TextView hasComment;
	}

}
