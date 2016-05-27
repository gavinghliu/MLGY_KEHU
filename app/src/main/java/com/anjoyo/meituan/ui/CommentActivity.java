package com.anjoyo.meituan.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Comment;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CommentActivity extends BaseActivity implements OnClickListener {

	ImageView mStart1;
	ImageView mStart2;
	ImageView mStart3;
	ImageView mStart4;
	ImageView mStart5;
	EditText mCommentTv;
	Button mCommentBtn;
	View mBackLayout;
	int selectRank;
	int productId;
	int sellerId;
	private SocketUtils socketUtil;
	private String bianma;
	private String danju;

	@Override
	void init() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_activity);

		mStart1 = (ImageView) findViewById(R.id.rank1);
		mStart2 = (ImageView) findViewById(R.id.rank2);
		mStart3 = (ImageView) findViewById(R.id.rank3);
		mStart4 = (ImageView) findViewById(R.id.rank4);
		mStart5 = (ImageView) findViewById(R.id.rank5);
		mCommentBtn = (Button) findViewById(R.id.comment_button);
		mCommentTv = (EditText) findViewById(R.id.comment_content);
		mBackLayout = findViewById(R.id.comment_back);
		
		socketUtil = new SocketUtils(this);

		mStart1.setOnClickListener(this);
		mStart2.setOnClickListener(this);
		mStart3.setOnClickListener(this);
		mStart4.setOnClickListener(this);
		mStart5.setOnClickListener(this);
		mCommentBtn.setOnClickListener(this);
		mBackLayout.setOnClickListener(this);
		selectRank = 5;
		productId = getIntent().getIntExtra("product_id", -1);
		sellerId = getIntent().getIntExtra("seller_id", -1);
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
			AppContext appContext = (AppContext) getApplicationContext();
			User user = appContext.getUser();
			
			String qianzhui = "ygpl_";
			if (user.getType() == 2) {
				qianzhui = "khpl_";
			} 
			
			
			if (selectRank < 4) {
				Intent intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("comment", mCommentTv.getText().toString());
				if (-1 != productId) {
					intent.putExtra("outId", productId);
					intent.putExtra("bianma", bianma);
					intent.putExtra("danju", danju);
				}
				if (-1 != sellerId) {
					intent.putExtra("outId", sellerId);
				}
				intent.putExtra("rank", selectRank);
				startActivity(intent);
				finish();
				return;
			}
			
			Comment newComment = new Comment();
			newComment.setName(appContext.getUser().getUsername());
			newComment.setContent(mCommentTv.getText().toString());
			newComment.setTime(Utils.getStringTodayFormat("yyyy-MM-dd"));
			newComment.setRank(selectRank);
			if (-1 != productId) {
				ProductDetailActivity.userComments.add(0,newComment);
			}
			
			if (-1 != sellerId) {
				SellerDetailActivity.userComments.add(0,newComment);
			}
			Intent intent = new Intent();
			intent.putExtra("hasComment", true);
			setResult(RESULT_OK, intent);
//			if (selectRank >= 4) {
//				Toast.makeText(this, "谢谢支持，欢迎下次光临~", Toast.LENGTH_LONG).show();
//			}
			onCreateDialog();
//			808462khpl+手机号+手机码+评论的文章或通知id+评论人名称+ 评论时间+评分+评论内容+货品编号+ 单据号
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String requestString = qianzhui + user.getUsername() + "_" +
									appContext.getSIME() + "_" +
									(productId == -1 ? sellerId : productId ) + "_" + 
									user.getUsername() +  "_" + 
									dateFormat.format(new Date()) + "_" + 
									selectRank + "_" + 
									newComment.getContent() + "_" +
									bianma + "_" + 
									danju;
									
			socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString, SocketUtils.ZHUCHE_REQUEST2 + qianzhui + user.getUsername(),  new SocketListener() {
				
				@Override
				public void downLoadSuccess(final String respone) {
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							if (dialog.isShowing()) {
								dialog.dismiss();
							}

							if (respone.contains("保存成功")) {
								finish();
							}
							Toast.makeText(CommentActivity.this, respone,
									Toast.LENGTH_SHORT).show();
						}
					});
				
				}
				
				@Override
				public void downLoadFail() {
					
				}
				
				@Override
				public void timeOut() {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					Toast.makeText(CommentActivity.this, "网络请求超时",
							Toast.LENGTH_SHORT).show();
					
				}
			});
			
			
			break;
		case R.id.rank1:
			selectRank = 1;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank2:
			selectRank = 2;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank3:
			selectRank = 3;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_off);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank4:
			selectRank = 4;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_off);
			break;

		case R.id.rank5:
			selectRank = 5;
			mStart1.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart2.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart3.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart4.setBackgroundResource(R.drawable.rate_star_medium_on);
			mStart5.setBackgroundResource(R.drawable.rate_star_medium_on);
			break;
		default:
			break;
		}

	}

}
