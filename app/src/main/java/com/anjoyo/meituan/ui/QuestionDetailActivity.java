package com.anjoyo.meituan.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Question;
import com.anjoyo.meituan.utils.SocketUtils;import com.anjoyo.mlgy.R;

import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionDetailActivity extends BaseActivity implements
		OnClickListener {
	private ImageView imageview_back;
	private TextView imageview_back2;
	private TextView questionContent;
	private TextView questionAnswer;
	private Question datas;
	private Button btnDone;
	private Button btnNotDone;
	private SocketUtils socketUtil;
	private int questionId;
	
	private EditText unDoneEt;
	private Button unDoneBtn2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	void init() {
		setContentView(R.layout.question_details);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);
		questionAnswer = (TextView) findViewById(R.id.answer_content);
		questionContent = (TextView) findViewById(R.id.question_content);
		btnDone = (Button) findViewById(R.id.btn_done);
		btnNotDone = (Button) findViewById(R.id.btn_not_done);
		
		unDoneBtn2 = (Button) findViewById(R.id.btn_not_done2);
		unDoneEt = (EditText) findViewById(R.id.btn_not_done_et);
		
		unDoneBtn2.setOnClickListener(this);
		
		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
		
		int allPosition = getIntent().getIntExtra("allPosition", 0);
		questionId = getIntent().getIntExtra("questionId", 0);
		datas = QuestionListActivity.mList.get(allPosition);
		
		questionContent.setText("   " + datas.getQuestion_content());
		if (null != datas.getQuestion_answer()) {
			questionAnswer.setText("" + datas.getQuestion_answer());
		} else {
			questionAnswer.setText("" + "暂未回答~");
		}
		
		if (datas.isDone()) {
			btnDone.setText("已解决");
			btnDone.setOnClickListener(null);
			btnDone.setBackgroundColor(Color.GRAY);
			btnDone.setClickable(false);
			btnNotDone.setVisibility(View.GONE);
		} else {
			btnDone.setText("确认解决");
			btnDone.setOnClickListener(this);
			btnNotDone.setVisibility(View.VISIBLE);
		}
		
		btnNotDone.setOnClickListener(this);
		socketUtil = new SocketUtils(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_not_done:
			
			btnNotDone.setVisibility(View.GONE);
			unDoneBtn2.setVisibility(View.VISIBLE);
			unDoneEt.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_not_done2:
			String qianzhui = "khq1_";
			final AppContext appContext = (AppContext) getApplicationContext();
			int type = appContext.getUser().getType();
			
			if (1 == type) {
				qianzhui = "ygq1_";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			//khq1+手机号+手机码+悄悄话ID+是否确认解决+提交时间+ 地理位置经度+地理位置纬度+内容
			String requestString = qianzhui + appContext.getUser().getUsername()
					+ "_" + appContext.getSIME() + "_" + datas.getQuestion_id() + "_" + "否" + "_"+
					dateFormat.format(new Date()) + "_" + 0 + "_" + 0 + "_" + unDoneEt.getText().toString();
			
			// 发送请求
			socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString,null);
			int allPosition = getIntent().getIntExtra("allPosition", 0);
			QuestionListActivity.mList.get(allPosition).setDone(true);
			unDoneBtn2.setText("已提交");
			unDoneBtn2.setBackgroundColor(Color.GRAY);
			unDoneBtn2.setOnClickListener(null);
			unDoneBtn2.setClickable(false);
			finish();
			break;
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		case R.id.btn_done:
			 qianzhui = "khq1_";
			 AppContext appContext2 = (AppContext) getApplicationContext();
			 type = appContext2.getUser().getType();
			
			if (1 == type) {
				qianzhui = "ygq1_";
			}
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			//khq1+手机号+手机码+悄悄话ID+是否确认解决+提交时间+ 地理位置经度+地理位置纬度+内容
			 requestString = qianzhui + appContext2.getUser().getUsername()
					+ "_" + appContext2.getSIME() + "_" + datas.getQuestion_id() + "_" + "是" + "_"+
					dateFormat.format(new Date())  + "_" + 0 + "_" + 0 + "_" + "确认解决";
			
			// 发送请求
			socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString,null);
			 allPosition = getIntent().getIntExtra("allPosition", 0);
			QuestionListActivity.mList.get(allPosition).setDone(true);
			btnDone.setText("已解决");
			btnDone.setBackgroundColor(Color.GRAY);
			btnDone.setOnClickListener(null);
			btnDone.setClickable(false);
			break;
		default:
			break;
		}

	}

}
