package com.anjoyo.meituan.adapter;

import java.util.ArrayList;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.domain.Question;
import com.anjoyo.meituan.ui.ProductDetailActivity;
import com.anjoyo.meituan.ui.QuestionDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Question> allList;
	private Intent intent;

	public QuestionAdapter(Context context, ArrayList<Question> allList) {
		this.context = context;
		this.allList = allList;
		System.out.println("allList.size()" + allList.size());
		intent = new Intent(context, QuestionDetailActivity.class);
	}

	public void setList(ArrayList<Question> allList) {
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

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.question_listitem, null);
			holderView = new HolderView();
			holderView.tvJiejue = (TextView) convertView
					.findViewById(R.id.tv_jiejue);
			holderView.tvQuestion = (TextView) convertView
					.findViewById(R.id.tv_question);
			convertView.findViewById(R.id.address_merchant);
			holderView.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			holderView.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_merchant_item);

			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		final Question question = allList.get(position);
		holderView.tvJiejue.setVisibility(View.VISIBLE);
		if (question.isDone()) {
			holderView.tvJiejue.setText("已解决");
			holderView.tvJiejue.setTextColor(Color.rgb(163, 153, 153));
		} else {
			holderView.tvJiejue.setText("未解决");
			holderView.tvJiejue.setTextColor(Color.rgb(255, 153, 0));
		}
		holderView.tvQuestion.setText(question.getQuestion_content());
		holderView.tvTime.setText(question.getQuestion_time());

		final int getPosition = position;
		holderView.relativeLayout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				intent.putExtra("allPosition", getPosition);
				intent.putExtra("questionID", question.getQuestion_id());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class HolderView {
		private RelativeLayout relativeLayout;
		private TextView tvQuestion;
		private TextView tvJiejue;
		private TextView tvTime;
	}

}
