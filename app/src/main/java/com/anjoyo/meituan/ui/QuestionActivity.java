package com.anjoyo.meituan.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.mlgy.R;

public class QuestionActivity extends BaseActivity implements OnClickListener {

	EditText mCommentTv;
	Button mCommentBtn;
	View mBackLayout;

	int rank = 0;
	int outId;
	private SocketUtils socketUtil;
	
	private String bianma;
	private String danju;

	@Override
	void init() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity);

		mCommentBtn = (Button) findViewById(R.id.comment_button);
		mCommentTv = (EditText) findViewById(R.id.comment_content);
		mBackLayout = findViewById(R.id.comment_back);
		socketUtil = new SocketUtils(this);
		mCommentBtn.setOnClickListener(this);
		mBackLayout.setOnClickListener(this);
		mCommentTv.setText(getIntent().getStringExtra("comment"));
		rank = getIntent().getIntExtra("rank", 0);
		outId = getIntent().getIntExtra("outId", 0);
		bianma  = getIntent().getStringExtra("bianma");
		danju  = getIntent().getStringExtra("danju");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.comment_back:
			finish();
			break;
		case R.id.comment_button:
			String qianzhui = "khqq_";
			final AppContext appContext = (AppContext) getApplicationContext();
			int type = appContext.getUser().getType();

			if (1 == type) {
				qianzhui = "ygqq_";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// khqq+手机号+手机码+反馈产品或通知id+ 反馈时间+评分+反馈内容
			// khqq+手机号+手机码+反馈产品或通知id+悄悄话人名称+ 反馈时间+评分+反馈内容

			String requestString = qianzhui + appContext.getUser().getUsername() + "_" + appContext.getSIME() + "_"
					+ outId + "_" + appContext.getUser().getUsername() + "_" + dateFormat.format(new Date()) + "_"
					+ rank + "_" + mCommentTv.getText().toString() + "_" + bianma + "_" + danju;

			// 发送请求
			socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString, null);
			Toast.makeText(this, "谢谢支持，欢迎下次光临~", Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			break;
		}

	}

}
