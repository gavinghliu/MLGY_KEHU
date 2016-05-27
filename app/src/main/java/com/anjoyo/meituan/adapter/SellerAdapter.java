package com.anjoyo.meituan.adapter;

import java.util.ArrayList;

import me.storm.volley.data.RequestManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.domain.Seller;
import com.anjoyo.meituan.ui.SellerDetailActivity;
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

public class SellerAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Seller> allList;
	private Intent intent;
	ImageLoader imageLoader = RequestManager.getImageLoader();

	public SellerAdapter(Context context, ArrayList<Seller> allList) {
		this.context = context;
		this.allList = allList;
		System.out.println("allList.size()" + allList.size());

		intent = new Intent(context, SellerDetailActivity.class);
	}
	
	public void setList(ArrayList<Seller> allList) {
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

		final Seller seller = allList.get(position);
		holderView.name_merchant
				.setText(seller.getSeller_name());

		holderView.image_merchant.setImageUrl(seller.getSeller_picture(), imageLoader);
		
		holderView.ratingBar.setRating(seller.getSeller_rank());
		holderView.commentCount.setText("有" + seller.getCommentCount() + "条评论");


		final int getPosition = position;
		holderView.relativeLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				intent.setAction("MerchantAll");
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
