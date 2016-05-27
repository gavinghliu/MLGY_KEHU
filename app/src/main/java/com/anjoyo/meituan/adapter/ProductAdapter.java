package com.anjoyo.meituan.adapter;

import java.util.ArrayList;

import me.storm.volley.data.RequestManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.ui.ProductDetailActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Product> allList;
	private Intent intent;
	String action;
	ImageLoader imageLoader = RequestManager.getImageLoader();

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ProductAdapter(Context context, ArrayList<Product> allList) {
		this.context = context;
		this.allList = allList;
		System.out.println("allList.size()" + allList.size());

		intent = new Intent(context, ProductDetailActivity.class);
	}
	
	public void setList(ArrayList<Product> allList) {
		this.allList = allList;
	}

	public int getCount() {
		return allList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.merchant_listitem, null);
			holderView = new HolderView();
			holderView.name_merchant = (TextView) convertView
					.findViewById(R.id.name_merchant);
			holderView.image_merchant = (NetworkImageView) convertView
					.findViewById(R.id.image_merchant);
			holderView.ratingBar = (RatingBar) convertView
					.findViewById(R.id.ratingBar1);
			holderView.relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.rl_merchant_item);
			holderView.commentCount = (TextView) convertView.findViewById(R.id.address_merchant);

			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		final Product product = allList.get(position);
		holderView.name_merchant
				.setText(product.getProduct_name());

//		if (1 == product.getProduct_id()) {
//			holderView.image_merchant.setImageResource(R.drawable.p1);
//		} else if (2 == product.getProduct_id()) {
//			holderView.image_merchant.setImageResource(R.drawable.p2);
//		}
		holderView.image_merchant.setImageUrl(product.getProduct_picture(), imageLoader);
		holderView.ratingBar.setRating(product.getProduct_rank());
		holderView.commentCount.setText("有" + product.getProductCommentCount() + "条评论");
//		holderView.image_merchant.setImageBitmap(bitmaps[position]);

		final int getPosition = position;
		holderView.relativeLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				intent.setAction(action);
				intent.putExtra("allPosition", getPosition);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class HolderView {

		private NetworkImageView image_merchant;
		private TextView name_merchant;
		private RatingBar ratingBar;
		private RelativeLayout relativeLayout;
		private TextView commentCount;
	}

}
