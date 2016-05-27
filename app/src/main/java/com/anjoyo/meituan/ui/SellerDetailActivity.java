package com.anjoyo.meituan.ui;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import me.storm.volley.data.RequestManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.adapter.CommentAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Comment;
import com.anjoyo.meituan.domain.Good;
import com.anjoyo.meituan.domain.Seller;
import com.anjoyo.meituan.ui.ProductDetailActivity.MyTransferListener;
import com.anjoyo.meituan.utils.FtpUtils;
import com.anjoyo.meituan.utils.Logger;
import com.anjoyo.meituan.utils.MyShare;
import com.anjoyo.meituan.utils.Rotate3dAnimation;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SplitNetImagePath;
import com.anjoyo.meituan.utils.Utils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SellerDetailActivity extends BaseActivity implements
		OnClickListener {
	
	private static final int CONFIG_DOWN_SUC = 100;
	private static final int CONFIG_DOWN_FAIL = 101;
	
	private TextView name_textView, detail_comment;
	private TextView link_Tv;
	private TextView address_Tv;
	private ImageView detail_button, detail_share;
	private NetworkImageView image1, image2, image3;
	private RatingBar ratingBar;
	private ListView comment_listview;
	private MyShare myShare;
	private Seller datas;
	private String[] strings;
	public static List<Comment> userComments;
	private String netImagePath;
	private Bitmap[] bitmaps;
	public static List<Good> detail_goods;
	private Button commentBtn;
	CommentAdapter merchantCommentAdapter;
	private TextView backTv;
	private ImageView backIV;
	ImageLoader imageLoader = RequestManager.getImageLoader();

	// 点赞
	private Button dianzan, dianzan_sel;
	private TextView textView;
	private android.view.animation.Animation animation;
	private RelativeLayout layout;
	private TextView mTvLikeVount;
	
	String commentString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.seller_detail);
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);// 获得IWXAPI实例
		super.onCreate(savedInstanceState);
	}

	void init() {
		// TODO Auto-generated method stub
		name_textView = (TextView) findViewById(R.id.name_textView);
		detail_comment = (TextView) findViewById(R.id.detail_comment);
		backIV = (ImageView) findViewById(R.id.detail_button);
		backTv = (TextView) findViewById(R.id.imgView_mei);
		backIV.setOnClickListener(this);
		backTv.setOnClickListener(this);
		detail_button = (ImageView) findViewById(R.id.detail_button);
		detail_share = (ImageView) findViewById(R.id.detail_share);
		image1 = (NetworkImageView) findViewById(R.id.image1);
		image2 = (NetworkImageView) findViewById(R.id.image2);
		image3 = (NetworkImageView) findViewById(R.id.image3);
		// photo_imageskip=(ImageView)findViewById(R.id.photo_imageskip);
		link_Tv = (TextView) findViewById(R.id.seller_link);
		address_Tv = (TextView) findViewById(R.id.seller_address);
		commentBtn = (Button) findViewById(R.id.say_something);
		commentBtn.setOnClickListener(this);

		ratingBar = (RatingBar) findViewById(R.id.details_ratingBar);
		comment_listview = (ListView) findViewById(R.id.comment_listview);
		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		image3.setOnClickListener(this);
		link_Tv.setOnClickListener(this);
		address_Tv.setOnClickListener(this);

		// 从上一个页面接收数据
		Intent intent = getIntent();
		datas = new Seller();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("MerchantAll");
		intentFilter.addAction("MerchantSale");
		String fromMerchant = intent.getAction();

		if ("MerchantAll".equals(fromMerchant)) {
			int allPosition = intent.getIntExtra("allPosition", 0);
			datas = MerchantActivity.saleList.get(allPosition);
		}

		detail_share.setOnClickListener(this);
		Logger.i("merchantDetail:" + datas.getSeller_name());
		name_textView.setText(datas.getSeller_name());
		ratingBar.setNumStars((int) datas.getSeller_rank());
		address_Tv.setText(datas.getSeller_address());
		link_Tv.setText(datas.getSeller_phone());
		// detail_comment.setText(4+""); //假数据
		detail_comment.setVisibility(View.GONE);
		strings = SplitNetImagePath
				.splitNetImagePath(datas.getSeller_picture());
		
//		image1.setBackgroundResource(R.drawable.ppp);
//		image2.setBackgroundResource(R.drawable.ppp);
//		image3.setBackgroundResource(R.drawable.ppp);
		image1.setImageUrl(datas.getSeller_picture(), imageLoader);
		image2.setImageUrl(datas.getSeller_picture2(), imageLoader);
		image3.setImageUrl(datas.getSeller_picture3(), imageLoader);

		// 评论列表
		String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/comment"
				+ datas.getSeller_id() + ".txt";
		File file = new File(filePath);
		if (null != file && file.exists() && file.length() > 0) {
			commentString = Utils.ReadTxtFile(filePath);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				FtpUtils.ftpconnectDownload("comment" + datas.getSeller_id()
						+ ".txt",
						"//Data/"+ AppContext.getConfigration(SellerDetailActivity.this, "wenjianjia") + "/kehuapp/Comment/comment" + datas.getSeller_id()
								+ ".txt", new MyTransferListener());
			}
		}).start();
//		String commentres = "{'comments' : [{comment_name:杨小姐,comment_time:2012-11-02,comment_content:去过最好的店，没有之一~~,comment_rank:2},"
//				+ "{comment_name:朱小姐,comment_time:2012-11-03,comment_content:装修不错哦~,comment_rank:4},"
//				+ "{comment_name:马小姐,comment_time:2012-11-03,comment_content:环境优美，很舒服,comment_rank:4}]}";
		if (null != commentString) {
			userComments = Comment.parseComment(commentString);
			merchantCommentAdapter = new CommentAdapter(this, userComments);
			comment_listview.setAdapter(merchantCommentAdapter);
		}
		
		detail_button.setOnClickListener(this);
		// photo_imageskip.setOnClickListener(this);

		layout = (RelativeLayout) findViewById(R.id.layout);
		dianzan = (Button) findViewById(R.id.pro_dianzan);
		dianzan_sel = (Button) findViewById(R.id.pro_dianzan_sel);
		dianzan.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.tv_one);
		animation = AnimationUtils.loadAnimation(SellerDetailActivity.this,
				R.anim.nn);
		mTvLikeVount = (TextView) findViewById(R.id.tv_like_count);
		mTvLikeVount.setText(datas.getLikeCount() + "");
		
		if (1 == datas.getIslike()) {
			dianzan.setVisibility(View.GONE);
			dianzan_sel.setVisibility(View.VISIBLE);
		}

	}



	@Override
	protected void onResume() {
		super.onResume();
		if (null != merchantCommentAdapter) {
			merchantCommentAdapter.notifyDataSetChanged();
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.detail_share:
			shareToWechat();
			break;
		case R.id.say_something:
			AppContext appContext = (AppContext) this.getApplicationContext();
			if (null == appContext.getUser()) {
				Toast.makeText(SellerDetailActivity.this, "请先登录~",
						Toast.LENGTH_LONG).show();
				return;
			}
			Intent intent = new Intent(this, CommentActivity.class);
			intent.putExtra("seller_id", datas.getSeller_id());
			startActivity(intent);
			break;
		case R.id.detail_button:
		case R.id.imgView_mei:
			finish();
			break;
		// case R.id.detail_favorite:
		// startActivity(new Intent(this,.class));
		// break;
		case R.id.image1:
			intent = new Intent(SellerDetailActivity.this,
					SpaceImageDetailActivity.class);
			intent.putExtra("images",
					datas.getSeller_picture());
			int[] location = new int[2];
			image1.getLocationOnScreen(location);
			intent.putExtra("locationX", location[0]);
			intent.putExtra("locationY", location[1]);

			intent.putExtra("width", 30);
			intent.putExtra("height", 30);
			startActivity(intent);
			overridePendingTransition(0, 0);
			break;

		case R.id.image2:
			intent = new Intent(SellerDetailActivity.this,
					SpaceImageDetailActivity.class);
			intent.putExtra("images",
					datas.getSeller_picture2());
			location = new int[2];
			image3.getLocationOnScreen(location);
			intent.putExtra("locationX", location[0]);
			intent.putExtra("locationY", location[1]);

			intent.putExtra("width", 30);
			intent.putExtra("height", 30);
			startActivity(intent);
			overridePendingTransition(0, 0);
			break;
		case R.id.image3:
			intent = new Intent(SellerDetailActivity.this,
					SpaceImageDetailActivity.class);
			intent.putExtra("images",
					datas.getSeller_picture3());
			location = new int[2];
			image3.getLocationOnScreen(location);
			intent.putExtra("locationX", location[0]);
			intent.putExtra("locationY", location[1]);

			intent.putExtra("width", 30);
			intent.putExtra("height", 30);
			startActivity(intent);
			overridePendingTransition(0, 0);
			break;
		case R.id.seller_link:
			intent = new Intent();
			intent.setAction(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + datas.getSeller_phone()));
			startActivity(intent);
			break;
		case R.id.pro_dianzan:
			textView.setVisibility(View.VISIBLE);
			textView.startAnimation(animation);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					textView.setVisibility(View.GONE);
				}
			}, 1000);
			// 获取布局的中心点位置，作为旋转的中心点
			float centerX = layout.getWidth() / 2f;
			float centerY = layout.getHeight() / 2f;
			// 构建3D旋转动画对象，旋转角度为0到90度，这使得赞将会从可见变为不可见
			final Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90,
					centerX, centerY, 310.0f, true);
			// 动画持续时间500毫秒
			rotation.setDuration(500);
			// 动画完成后保持完成的状态
			rotation.setFillAfter(true);
			rotation.setInterpolator(new AccelerateInterpolator());
			// 设置动画的监听器
			rotation.setAnimationListener(new TurnToImageView());
			layout.startAnimation(rotation);

			dianzan_sel.setClickable(false);
			mTvLikeVount.setText(Integer.parseInt(mTvLikeVount.getText().toString()) + 1 + "");
			break;
		case R.id.seller_address:
			
			intent = new Intent(this,SellerLocationActivity.class);
			intent.putExtra("latitude", datas.getLatitude());
			intent.putExtra("longitude", datas.getLongitude());
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	// *************************************************************************
	// share to wechat Method
	// *************************************************************************

	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001; // 支持分享到朋友圈的微信版本
	public static final String APP_ID = "wxf7f986ad87da0b2f";
	private static final int THUMB_SIZE = 150;

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	private void shareToWechat() {
		api.registerApp(APP_ID);

		if (!api.isWXAppInstalled()) {
			Toast.makeText(this, "没有安装微信~", Toast.LENGTH_LONG).show();
			return;
		}

		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
			Toast.makeText(this, "微信版本太旧不支持分享~", Toast.LENGTH_LONG).show();
		}

		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.test);
		WXImageObject imgObj = new WXImageObject(bmp);

		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
				THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);

		// String text = "share our application";
		// WXTextObject textObj = new WXTextObject();
		// textObj.text = text;
		//
		// WXMediaMessage msg = new WXMediaMessage(textObj);
		// msg.mediaObject = textObj;
		// msg.description = text;
		//
		// SendMessageToWX.Req req = new SendMessageToWX.Req();
		// req.transaction = String.valueOf(System.currentTimeMillis());
		// req.message = msg;
		// req.scene = SendMessageToWX.Req.WXSceneTimeline;
		// api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	class TurnToImageView implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		/**
		 * 当ListView的动画完成后，还需要再启动ImageView的动画，让ImageView从不可见变为可见
		 */
		@Override
		public void onAnimationEnd(Animation animation) {
			// 获取布局的中心点位置，作为旋转的中心点
			float centerX = layout.getWidth() / 2f;
			float centerY = layout.getHeight() / 2f;
			// 将赞隐藏
			dianzan.setVisibility(View.GONE);
			// 将已赞显示
			dianzan_sel.setVisibility(View.VISIBLE);
			// 构建3D旋转动画对象，旋转角度为270到360度，这使得赞将会从不可见变为可见
			final Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360,
					centerX, centerY, 310.0f, false);
			// 动画持续时间500毫秒
			rotation.setDuration(500);
			// 动画完成后保持完成的状态
			rotation.setFillAfter(true);
			rotation.setInterpolator(new AccelerateInterpolator());
			layout.startAnimation(rotation);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
	
	public class MyTransferListener implements FTPDataTransferListener {

		// 传输放弃时触发
		@Override
		public void aborted() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_FAIL);
		}

		// 文件传输完成时，触发
		@Override
		public void completed() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_SUC);
		}

		// 传输失败时触发
		@Override
		public void failed() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_FAIL);
		}

		// 文件开始上传或下载时触发
		@Override
		public void started() {
		}

		// 显示已经传输的字节数
		@Override
		public void transferred(int arg0) {// 以下用于显示进度的
		}
	}
	
	// 定义一个Handler
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CONFIG_DOWN_SUC:
				String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/comment"
						+ datas.getSeller_id() + ".txt";
				File file = new File(filePath);
				if (null != file && file.exists() && file.length() > 0) {
					commentString = Utils.ReadTxtFile(filePath);
				}
				if (null != commentString) {
					userComments = Comment.parseComment(commentString);
				}
				// comments=SplitNetImagePath.splitNetImagePath(datas.getProduct_comment());
				merchantCommentAdapter = new CommentAdapter(
						SellerDetailActivity.this, userComments);
				comment_listview.setAdapter(merchantCommentAdapter);
				merchantCommentAdapter.notifyDataSetChanged();
				break;

			case CONFIG_DOWN_FAIL:
				break;
			}
		}
	};
}
