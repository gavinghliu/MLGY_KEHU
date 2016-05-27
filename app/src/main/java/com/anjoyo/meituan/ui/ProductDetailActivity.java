package com.anjoyo.meituan.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.meituan.adapter.CommentAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Comment;
import com.anjoyo.meituan.domain.Good;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.utils.ExcelRead;
import com.anjoyo.meituan.utils.FtpUtils;
import com.anjoyo.meituan.utils.Logger;
import com.anjoyo.meituan.utils.MyShare;
import com.anjoyo.meituan.utils.Rotate3dAnimation;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.meituan.utils.SplitNetImagePath;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import me.storm.volley.data.RequestManager;

public class ProductDetailActivity extends BaseActivity implements
		OnClickListener, SocketListener {

	private static final int CONFIG_DOWN_SUC = 100;
	private static final int CONFIG_DOWN_FAIL = 101;

	private TextView name_textView, detail_comment;
	private TextView price_Tv;
	private TextView des_Tv;
	private ImageView detail_share;
	private NetworkImageView image1, image2, image3;
	private RatingBar ratingBar;
	private ListView comment_listview;
	private MyShare myShare;
	private Product datas;
	private String[] strings;
	public static List<Comment> userComments;
	private String netImagePath;
	private Bitmap[] bitmaps;
	public static List<Good> detail_goods;
	ImageLoader imageLoader = RequestManager.getImageLoader();

	private Button commentBtn;
	CommentAdapter merchantCommentAdapter;
	private TextView backTv;
	private ImageView backIV;
	private TextView servicerTextView;
	private Button serviceDetailBtn;
	public ProgressDialog dialog;
	private SocketUtils socketUtil;

	// 点赞
	private Button dianzan, dianzan_sel;
	private TextView textView;
	private android.view.animation.Animation animation;
	private RelativeLayout layout;
	private TextView mTvLikeVount;

	Button btnJump;
	ImageView btnFav;
	String commentString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.merchant__details);
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);// 获得IWXAPI实例
		super.onCreate(savedInstanceState);
	}

	void init() {
		name_textView = (TextView) findViewById(R.id.name_textView);

		backIV = (ImageView) findViewById(R.id.detail_button);
		backTv = (TextView) findViewById(R.id.imgView_mei);
		backIV.setOnClickListener(this);
		backTv.setOnClickListener(this);
		detail_comment = (TextView) findViewById(R.id.detail_comment);
		dialog = new ProgressDialog(this);
		detail_share = (ImageView) findViewById(R.id.detail_share);
		image1 = (NetworkImageView) findViewById(R.id.image1);
		image2 = (NetworkImageView) findViewById(R.id.image2);
		image3 = (NetworkImageView) findViewById(R.id.image3);

		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		image3.setOnClickListener(this);

		price_Tv = (TextView) findViewById(R.id.price_textView);
		des_Tv = (TextView) findViewById(R.id.product_des);
		serviceDetailBtn = (Button) findViewById(R.id.sevice_detail);
		serviceDetailBtn.setVisibility(View.GONE);
		commentBtn = (Button) findViewById(R.id.say_something);
		commentBtn.setOnClickListener(this);
		commentBtn.setVisibility(View.GONE);

		ratingBar = (RatingBar) findViewById(R.id.details_ratingBar);
		comment_listview = (ListView) findViewById(R.id.comment_listview);
		servicerTextView = (TextView) findViewById(R.id.servicer);

		// 从上一个页面接收数据
		Intent intent = getIntent();
		datas = new Product();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("MerchantAll");
		intentFilter.addAction("MerchantSale");
		String fromMerchant = intent.getAction();

		servicerTextView.setVisibility(View.GONE);
		if ("MerchantAll".equals(fromMerchant)) {
			int allPosition = intent.getIntExtra("allPosition", 0);
			datas = MerchantActivity.allList.get(allPosition);
			detail_share.setVisibility(View.GONE);
		} else if ("Service".equals(fromMerchant)) {
			int allPosition = intent.getIntExtra("allPosition", 0);
			detail_share.setVisibility(View.GONE);
			datas = MerchantActivity.serviceList.get(allPosition);
		} else if ("HistoryService".equals(fromMerchant)) {
			int allPosition = intent.getIntExtra("allPosition", 0);
			datas = HistoryActivity.serviceList.get(allPosition);
			detail_share.setVisibility(View.VISIBLE);
			servicerTextView.setVisibility(View.VISIBLE);
			serviceDetailBtn.setVisibility(View.VISIBLE);
			commentBtn.setVisibility(View.VISIBLE);
			serviceDetailBtn.setOnClickListener(this);
		} else if ("HistoryProduct".equals(fromMerchant)) {
			int allPosition = intent.getIntExtra("allPosition", 0);
			datas = HistoryActivity.allList.get(allPosition);
			detail_share.setVisibility(View.VISIBLE);
			commentBtn.setVisibility(View.VISIBLE);
			serviceDetailBtn.setVisibility(View.GONE);
		} else if ("collect".equals(fromMerchant)) {
			int allPosition = intent.getIntExtra("allPosition", 0);
			detail_share.setVisibility(View.GONE);
			datas = CollectActivity.mList.get(allPosition);
		}

		String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/comment"
				+ datas.getProduct_id() + ".txt";
		File file = new File(filePath);
		if (null != file && file.exists() && file.length() > 0) {
			commentString = Utils.ReadTxtFile(filePath);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				FtpUtils.ftpconnectDownload("comment" + datas.getProduct_id()
						+ ".txt",
						"//Data/"+ AppContext.getConfigration(ProductDetailActivity.this, "wenjianjia") + "/kehuapp/Comment/comment" + datas.getProduct_id()
								+ ".txt", new MyTransferListener());
			}
		}).start();

		image1.setImageUrl(datas.getProduct_picture(), imageLoader);
		image2.setImageUrl(datas.getProduct_picture2(), imageLoader);
		image3.setImageUrl(datas.getProduct_picture3(), imageLoader);
		detail_share.setOnClickListener(this);
		Logger.i("merchantDetail:" + datas.getProduct_name());
		name_textView.setText(datas.getProduct_name());
		ratingBar.setNumStars((int) datas.getProduct_rank());
		price_Tv.setText("价格" + datas.getProduct_price());
		des_Tv.setText(datas.getProduct_des());
		// detail_comment.setText(4+""); //假数据
		detail_comment.setVisibility(View.GONE);
		strings = SplitNetImagePath.splitNetImagePath(datas
				.getProduct_picture());

		if (null != commentString) {
			userComments = Comment.parseComment(commentString);
			merchantCommentAdapter = new CommentAdapter(this, userComments);
			comment_listview.setAdapter(merchantCommentAdapter);
		}
		// comments=SplitNetImagePath.splitNetImagePath(datas.getProduct_comment());

		// detail_button.setOnClickListener(this);
		// photo_imageskip.setOnClickListener(this);
		socketUtil = new SocketUtils(this);

		layout = (RelativeLayout) findViewById(R.id.layout);
		dianzan = (Button) findViewById(R.id.pro_dianzan);
		dianzan_sel = (Button) findViewById(R.id.pro_dianzan_sel);
		dianzan.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.tv_one);
		animation = AnimationUtils.loadAnimation(ProductDetailActivity.this,
				R.anim.nn);

		mTvLikeVount = (TextView) findViewById(R.id.tv_like_count);
		mTvLikeVount.setText(datas.getLikeCount() + "");

		if (1 == datas.getIslike()) {
			dianzan.setVisibility(View.GONE);
			dianzan_sel.setVisibility(View.VISIBLE);
		}
		btnJump = (Button) findViewById(R.id.btn_jump);
		btnJump.setOnClickListener(this);
		btnFav = (ImageView) findViewById(R.id.btn_favorite);
		btnFav.setOnClickListener(this);
		btnFav.setVisibility(View.GONE);
		if (datas.getIsFav() == 1) {
			btnFav.setBackgroundResource(R.drawable.ic_action_favorite_on);
		} else {
			btnFav.setBackgroundResource(R.drawable.ic_action_favorite_off);
		}
	}

	// @SuppressLint("HandlerLeak")
	// Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// super.handleMessage(msg);
	// Bitmap[] pngBM = (Bitmap[]) msg.obj;
	// Logger.i("pngBM" + pngBM.length);
	// image1.setImageBitmap(pngBM[0]);
	// image2.setImageBitmap(pngBM[1]);
	// image3.setImageBitmap(pngBM[2]);
	// Logger.i("image" + pngBM[0]);
	// Logger.i("image" + pngBM[1]);
	// Logger.i("image" + pngBM[2]);
	// }
	// };

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
			// myShare = new MyShare(this);
			// myShare.share("商家分享");
			shareToWechat();
			break;
		case R.id.say_something:
			AppContext appContext = (AppContext) this.getApplicationContext();
			if (null == appContext.getUser()) {
				Toast.makeText(ProductDetailActivity.this, "请先登录~",
						Toast.LENGTH_LONG).show();
				return;
			}
			Intent intent = new Intent(this, CommentActivity.class);
			intent.putExtra("product_id", datas.getProduct_id());
			intent.putExtra("bianma", datas.getBianma());
			intent.putExtra("danju", datas.getDanju());
			startActivityForResult(intent, 10001);
			break;
		case R.id.detail_button:
		case R.id.imgView_mei:
			finish();
			break;
		case R.id.sevice_detail:
			intent = new Intent(ProductDetailActivity.this, GridActivity.class);
			intent.putExtra("procode", datas.getProduct_id());
			startActivity(intent);
			break;
		case R.id.image1:
			intent = new Intent(ProductDetailActivity.this,
					SpaceImageDetailActivity.class);
			intent.putExtra("images", datas.getProduct_picture());
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
			intent = new Intent(ProductDetailActivity.this,
					SpaceImageDetailActivity.class);
			intent.putExtra("images", datas.getProduct_picture2());
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
			intent = new Intent(ProductDetailActivity.this,
					SpaceImageDetailActivity.class);
			intent.putExtra("images", datas.getProduct_picture3());
			location = new int[2];
			image3.getLocationOnScreen(location);
			intent.putExtra("locationX", location[0]);
			intent.putExtra("locationY", location[1]);

			intent.putExtra("width", 30);
			intent.putExtra("height", 30);
			startActivity(intent);
			overridePendingTransition(0, 0);
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
			int count = Integer.parseInt(mTvLikeVount.getText().toString());
			count++;
			mTvLikeVount.setText(count + "");
			break;
		case R.id.btn_jump:
			if (null == datas.getBuyUrl()) {
				break;
			}
			Uri uri = Uri.parse(datas.getBuyUrl());
			// "http://item.taobao.com/item.htm?spm=a230r.1.14.1.0TAOja&id=39544631377&ns=1#detail"
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			break;
		case R.id.btn_favorite:
			datas.setIsFav(datas.getIsFav() == 1 ? 0 : 1);
			intent = getIntent();
			String fromMerchant = intent.getAction();
			int allPosition = intent.getIntExtra("allPosition", 0);
			if ("MerchantAll".equals(fromMerchant)) {
				MerchantActivity.allList.get(allPosition).setIsFav(
						datas.getIsFav());
			} else if ("Service".equals(fromMerchant)) {
				MerchantActivity.serviceList.get(allPosition).setIsFav(
						datas.getIsFav());
			} else if ("HistoryService".equals(fromMerchant)) {
				HistoryActivity.serviceList.get(allPosition).setIsFav(
						datas.getIsFav());
			} else if ("HistoryProduct".equals(fromMerchant)) {
				HistoryActivity.allList.get(allPosition).setIsFav(
						datas.getIsFav());
			}

			if (datas.getIsFav() == 0) {
				btnFav.setBackgroundResource(R.drawable.ic_action_favorite_off);
				Toast.makeText(ProductDetailActivity.this, "已取消收藏",
						Toast.LENGTH_LONG).show();
			} else {
				btnFav.setBackgroundResource(R.drawable.ic_action_favorite_on);
				Toast.makeText(ProductDetailActivity.this, "已收藏",
						Toast.LENGTH_LONG).show();
			}

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

	public static void inputstreamtofile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode)
			return;
		if (requestCode == 10001) {
			if (data.getBooleanExtra("hasComment", false)) {
				String fromMerchant = getIntent().getAction();
				if ("HistoryService".equals(fromMerchant)) {
					int allPosition = getIntent().getIntExtra("allPosition", 0);
					HistoryActivity.serviceList.get(allPosition).setHasComment(
							true);
					HistoryActivity.serviceList.get(allPosition)
							.setProductCommentCount(
									HistoryActivity.serviceList
											.get(allPosition)
											.getProductCommentCount() + 1);
				} else if ("HistoryProduct".equals(fromMerchant)) {
					int allPosition = getIntent().getIntExtra("allPosition", 0);
					HistoryActivity.allList.get(allPosition)
							.setHasComment(true);
					HistoryActivity.allList.get(allPosition)
							.setProductCommentCount(
									HistoryActivity.allList.get(allPosition)
											.getProductCommentCount() + 1);
				}
			}
		}
	}

	public void onCreateDialog() {// 显示网络连接Dialog
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View v = inflater
				.inflate(R.layout.selfdef_progress_dialog_layout, null);
		// dialog.setTitle("请稍候。。。");
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
		// dialog.setMessage("请稍候。。。");
		dialog.show();
		dialog.setContentView(v);
	}
	
	@Override
	public void downLoadSuccess(String respone) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				File file = new File(SocketUtils.QUERY_XLS_FILE_PATH);
				Intent i = new Intent();
				i.setClass(ProductDetailActivity.this, ExcelRead.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", file.getAbsolutePath());
				i.putExtras(bundle);
				startActivity(i);
			}
		});
	}

	@Override
	public void downLoadFail() {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}

				Toast.makeText(ProductDetailActivity.this, "查询失败",
						Toast.LENGTH_LONG).show();
			}
		});
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
						+ datas.getProduct_id() + ".txt";
				File file = new File(filePath);
				if (null != file && file.exists() && file.length() > 0) {
					commentString = Utils.ReadTxtFile(filePath);
				}
				if (null != commentString) {
					userComments = Comment.parseComment(commentString);
				}
				// comments=SplitNetImagePath.splitNetImagePath(datas.getProduct_comment());
				merchantCommentAdapter = new CommentAdapter(
						ProductDetailActivity.this, userComments);
				comment_listview.setAdapter(merchantCommentAdapter);
				merchantCommentAdapter.notifyDataSetChanged();
				break;

			case CONFIG_DOWN_FAIL:
				break;
			}
		}
	};

	@Override
	public void timeOut() {
		// TODO Auto-generated method stub
		
	}

}
