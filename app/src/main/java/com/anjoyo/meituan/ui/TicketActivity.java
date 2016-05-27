package com.anjoyo.meituan.ui;

import com.anjoyo.mlgy.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TicketActivity extends BaseActivity implements OnClickListener {
	private ImageView imageview_back;
	private TextView imageview_back2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	void init() {
		setContentView(R.layout.ticket_activity);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);

		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		default:
			break;
		}

	}

}
