package com.anjoyo.meituan.adapter;

import java.util.ArrayList;

import me.storm.volley.data.RequestManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.domain.Wenzhang;
import com.jsk.imgtxt.WenzhangDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Wenzhang> allList;
	private Intent intent;
	String action;
	ImageLoader imageLoader = RequestManager.getImageLoader();

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public MainAdapter(Context context, ArrayList<Wenzhang> allList) {
		this.context = context;
		this.allList = allList;
		System.out.println("allList.size()" + allList.size());

		intent = new Intent(context, WenzhangDetailActivity.class);
	}

	public void setList(ArrayList<Wenzhang> allList) {
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
					R.layout.main_listitem, null);
			holderView = new HolderView();
			holderView.image = (NetworkImageView) convertView.findViewById(R.id.image);
			holderView.relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.rl_merchant_item);
			holderView.detail = (TextView) convertView.findViewById(R.id.detail);
			holderView.title = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		final Wenzhang Wenzhang = allList.get(position);

		holderView.image.setImageUrl(Wenzhang.getwenzhang_picture(), imageLoader);
		holderView.title.setText(Wenzhang.getwenzhang_title());
		holderView.detail.setText(Wenzhang.getDetail());
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
		private NetworkImageView image;
		private TextView title;
		private TextView detail;
		private RelativeLayout relativeLayout;
	}
}
