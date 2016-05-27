package com.anjoyo.meituan.adapter;

import java.util.List;

import com.anjoyo.meituan.domain.Comment;
import com.anjoyo.mlgy.R;
import com.anjoyo.mlgy.R.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
private Context context;
private List<Comment> comment;

	public CommentAdapter(Context context,List<Comment> comment){
		this.context=context;
		this.comment = comment;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comment.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comment.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView  holderView=null;
		if(convertView==null){
			holderView=new HolderView();
			convertView=LayoutInflater.from(context).inflate(R.layout.merchant__details_comment_item,null);
			holderView.comment_content=(TextView) convertView.findViewById(R.id.commemt_content);
			holderView.nameTv = (TextView) convertView.findViewById(R.id.commemt_name);
			holderView.timeTv = (TextView) convertView.findViewById(R.id.commemt_time);
			holderView.ratingBar=(RatingBar) convertView.findViewById(R.id.ratingBar1);
			convertView.setTag(holderView);
			
		}else{
			holderView=(HolderView) convertView.getTag();
			
		}
		Comment tempComment = comment.get(position);
		holderView.comment_content.setText(tempComment.getContent());
		holderView.nameTv.setText(tempComment.getName());
		holderView.timeTv.setText(tempComment.getTime());
		holderView.ratingBar.setNumStars(tempComment.getRank());
		return convertView;
	}
	
	class  HolderView  {
		private TextView    comment_content;
		private RatingBar   ratingBar;
		private TextView    nameTv;
		private TextView    timeTv;
	}

}
