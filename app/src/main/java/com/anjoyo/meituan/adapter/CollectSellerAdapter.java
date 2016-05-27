package com.anjoyo.meituan.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.domain.Seller;
import com.anjoyo.meituan.ui.ProductDetailActivity;
import com.anjoyo.meituan.utils.SplitNetImagePath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollectSellerAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Seller> allList;
	private Intent intent;
	Bitmap pngBM;// 网络图片
	Bitmap[] bitmaps;

	public CollectSellerAdapter(Context context, ArrayList<Seller> allList) {
		this.context = context;
		this.allList = allList;
		System.out.println("allList.size()" + allList.size());

		intent = new Intent(context, ProductDetailActivity.class);

		bitmaps = new Bitmap[allList.size()];
		ImageCache1 imageCache = new ImageCache1();
		imageCache.start();
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

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.merchant_listitem, null);
			holderView = new HolderView();
			holderView.price_merchant = (TextView) convertView
					.findViewById(R.id.address_merchant);
			holderView.name_merchant = (TextView) convertView
					.findViewById(R.id.name_merchant);
			holderView.image_merchant = (ImageView) convertView
					.findViewById(R.id.image_merchant);
			holderView.ratingBar = (RatingBar) convertView
					.findViewById(R.id.ratingBar1);
			holderView.relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.rl_merchant_item);

			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		holderView.price_merchant.setText(allList.get(position)
				.getSeller_address());
		holderView.name_merchant
				.setText(allList.get(position).getSeller_name());

		holderView.image_merchant.setImageResource(R.drawable.test);
		holderView.ratingBar.setRating((float) 4.3);

		holderView.image_merchant.setImageBitmap(bitmaps[position]);

		final int getPosition = position;
		holderView.relativeLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setAction("collect");
				intent.putExtra("collectposition", getPosition);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class HolderView {

		private ImageView image_merchant;
		private TextView name_merchant, price_merchant;
		private RatingBar ratingBar;
		private RelativeLayout relativeLayout;
	}

	class ImageCache1 extends Thread {

		public void run() {
			for (int i = 0; i < allList.size(); i++) {
				String netPictruePath = allList.get(i).getSeller_picture();
				String[] strings = SplitNetImagePath
						.splitNetImagePath(netPictruePath);
				// 显示第一张图片，为默认图片
				String pictruePath = strings[0];
				// 把网络地址转换为BitMap
				URL picUrl;
				try {
					picUrl = new URL(pictruePath);
					pngBM = BitmapFactory.decodeStream(picUrl.openStream());
					bitmaps[i] = pngBM;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
