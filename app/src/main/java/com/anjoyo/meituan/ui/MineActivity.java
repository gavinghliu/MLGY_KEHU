package com.anjoyo.meituan.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.UnGetProduct;
import com.anjoyo.meituan.domain.UnPayService;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.domain.Yuer;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.mlgy.R;

public class MineActivity extends BaseActivity implements OnClickListener {

//	private ImageView iv_mine1;// 通知
	private LinearLayout lL_mine, lL_mine2;// //账户
	private RelativeLayout rL_mine;// 我的美团卷
	private RelativeLayout rL_mine1;
	private LinearLayout lL_mine3;// 每日推荐
	private LinearLayout lL_mine4;//待付款
	private LinearLayout lL_mine5;//已付款
	private LinearLayout lL_mine6;// 抽奖单
	private LinearLayout historyLayout; //服务历史
	private LinearLayout lL_mine7;
	private LinearLayout qiaoqiaohuaLayout; //悄悄话
	private LinearLayout yuyueLayout; //悄悄话
	private Button button_register;
	private TextView textview_username;
	private AppContext appContext;
	private User user;
	private SocketUtils socketUtil;
	
//	private List<Yuer> yuerList = new ArrayList<Yuer>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplicationContext();
		user = appContext.getUser();
		if (user != null) {
			String username = user.getUsername();
			lL_mine.setVisibility(View.GONE);
			textview_username.setText(username);
			lL_mine2.setVisibility(View.VISIBLE);
		}
		socketUtil = new SocketUtils(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.iV_mine1:// 美团通知
//			Intent intent = new Intent(MineActivity.this,
//					NotificationActivity.class);
//			startActivity(intent);
//
//			break;
		case R.id.lL_mine:
		case R.id.button_register:// 登录
			Intent intent1 = new Intent(MineActivity.this, LoginActivity.class);
			startActivityForResult(intent1, 12);

			break;
		case R.id.rL_mine:
			if (user != null) {
				Intent intent2 = new Intent(MineActivity.this, YueeActivity.class);
				intent2.putExtra("sime", appContext.getSIME());
				startActivity(intent2);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.rL_mine1:
			if (user != null) {
				Intent intent3 = new Intent(MineActivity.this,
						CollectActivity.class);
				startActivity(intent3);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.lL_mine3:// 每日推荐
			Intent intent4 = new Intent(MineActivity.this,
					RecommendedActivity.class);
			startActivity(intent4);
			break;
		case R.id.lL_mine4:
			if (user != null) {
				Intent intent5 = new Intent(MineActivity.this,
						ObligationActivity.class);
				startActivity(intent5);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.lL_mine5:
			if (user != null) {
				Intent intent6 = new Intent(MineActivity.this, PaidActivity.class);
				startActivity(intent6);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.lL_mine6:// 抽奖单
			if (user != null) {
				Intent intent7 = new Intent(MineActivity.this,
						LotteryActivity.class);
				startActivity(intent7);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.lL_mine7: //客户查询
			Intent intent8 = new Intent(MineActivity.this,
					QueryActivity.class);
			startActivity(intent8);
			break;
		case R.id.history:
			if (user != null) {
				Intent intent9 = new Intent(MineActivity.this,
						HistoryActivity.class);
				intent9.putExtra("sime", appContext.getSIME());
				startActivity(intent9);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.lL_mine2:
			startActivityForResult(new Intent(this, AccountActivity.class), 11);
			break;
		case R.id.qiaoqiaohua_layout:
			if (user != null) {
				Intent intent9 = new Intent(MineActivity.this,
						QuestionListActivity.class);
				startActivity(intent9);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.layout_yuyue:
			if (user != null) {
				Intent intent9 = new Intent(MineActivity.this,
						YuyueActivity.class);
				startActivity(intent9);
			} else {
				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
			}
				break;
		default:
			break;
		}

	}

	@Override
	void init() {
		setContentView(R.layout.mine_activity);
//		iv_mine1 = (ImageView) findViewById(R.id.iV_mine1);
		lL_mine = (LinearLayout) findViewById(R.id.lL_mine);
		rL_mine = (RelativeLayout) findViewById(R.id.rL_mine);
		rL_mine1 = (RelativeLayout) findViewById(R.id.rL_mine1);
		lL_mine3 = (LinearLayout) findViewById(R.id.lL_mine3);
		lL_mine4 = (LinearLayout) findViewById(R.id.lL_mine4);
		lL_mine5 = (LinearLayout) findViewById(R.id.lL_mine5);
		lL_mine6 = (LinearLayout) findViewById(R.id.lL_mine6);
		historyLayout = (LinearLayout) findViewById(R.id.history);
		
		lL_mine7 = (LinearLayout) findViewById(R.id.lL_mine7);
		lL_mine7.setVisibility(View.GONE);
		if(null != user && user.getType() == 1) {
			lL_mine7.setVisibility(View.VISIBLE);
		}
		
		lL_mine2 = (LinearLayout) findViewById(R.id.lL_mine2);
		button_register = (Button) findViewById(R.id.button_register);
		textview_username = (TextView) findViewById(R.id.textview_username);
		qiaoqiaohuaLayout = (LinearLayout) findViewById(R.id.qiaoqiaohua_layout);
		yuyueLayout = (LinearLayout) findViewById(R.id.layout_yuyue);
//		iv_mine1.setOnClickListener(this);
		lL_mine.setOnClickListener(this);
		rL_mine.setOnClickListener(this);
		rL_mine1.setOnClickListener(this);
		lL_mine3.setOnClickListener(this);
		lL_mine4.setOnClickListener(this);
		lL_mine5.setOnClickListener(this);
		lL_mine6.setOnClickListener(this);
		lL_mine7.setOnClickListener(this);
		button_register.setOnClickListener(this);
		lL_mine2.setOnClickListener(this);
		historyLayout.setOnClickListener(this);
		qiaoqiaohuaLayout.setOnClickListener(this);
		yuyueLayout.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 10000) {
			finish();
		}
		
		if (resultCode == 12) {
			if (data != null) {
				String username = data.getStringExtra("username");
				lL_mine.setVisibility(View.GONE);
				textview_username.setText(username);
				lL_mine2.setVisibility(View.VISIBLE);
			} 
		}else if (resultCode == 11) {
			lL_mine2.setVisibility(View.GONE);
			lL_mine.setVisibility(View.VISIBLE);
		}
		user = appContext.getUser();

	}
	
	private void initYuer() {
		String qianzhui = "khy1_";
		final AppContext appContext = (AppContext) getApplicationContext();

		String requestString = qianzhui + appContext.getUser().getUsername()
				+ "_" + appContext.getSIME();
//		onCreateDialog();
		// 发送请求
		socketUtil.sendRequestForTxtFile(SocketUtils.ZHUCHE_REQUEST + requestString,
				SocketUtils.ZHUCHE_REQUEST3 + qianzhui + appContext.getUser().getUsername(),
				new SocketListener() {

					@Override
					public void downLoadSuccess(final String respone) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
//										dialog.dismiss();
										
										String res = respone;
										ArrayList<Yuer> yuerList = Yuer.parseYuer(res);		
										for (Yuer yuer : yuerList) {
											Log.d("test", yuer.getGuest_name());
										}
									}
								});
							}
						});
					}

					@Override
					public void downLoadFail() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								}
						});
					}
					
					@Override
					public void timeOut() {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						Toast.makeText(MineActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
	}
	
	void yuyue() {
		
		String qianzhui = "khyy_";
		final AppContext appContext = (AppContext) getApplicationContext();

//		808462khyy+手机号+手机码+年+月+日+时间+分店代码
		String requestString = qianzhui + appContext.getUser().getUsername()
				+ "_" + appContext.getSIME() + "_2015" + "_04" + "_08" + "_11:00" + "_22";
		socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString,
				SocketUtils.ZHUCHE_REQUEST2 + qianzhui + appContext.getUser().getUsername(),
				new SocketListener() {

					@Override
					public void downLoadSuccess(final String respone) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Log.d("yueyue", respone);
										if (respone.contains("保存预约信息成功")) {
											
										}
										Toast.makeText(MineActivity.this, respone, Toast.LENGTH_SHORT).show();
									}
								});
							}
						});
					}

					@Override
					public void downLoadFail() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								}
						});
					}
					
					@Override
					public void timeOut() {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						Toast.makeText(MineActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
	}
	
	private void initUnpay() {
		String qianzhui = "khy3_";
		final AppContext appContext = (AppContext) getApplicationContext();

		String requestString = qianzhui + appContext.getUser().getUsername()
				+ "_" + appContext.getSIME();
//		onCreateDialog();
		// 发送请求
		socketUtil.sendRequestForTxtFile(SocketUtils.ZHUCHE_REQUEST + requestString,
				SocketUtils.ZHUCHE_REQUEST3 + qianzhui + appContext.getUser().getUsername(),
				new SocketListener() {

					@Override
					public void downLoadSuccess(final String respone) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
//										dialog.dismiss();
										
										String res = respone;
										ArrayList<UnPayService> unpayList = UnPayService.parseQuestion(res);		
										for (UnPayService unpay : unpayList) {
											Log.d("test", unpay.getGuest_name());
										}
									}
								});
							}
						});
					}

					@Override
					public void downLoadFail() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								}
						});
					}
					
					@Override
					public void timeOut() {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						Toast.makeText(MineActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
	}

	
	private void initUnget() {
		String qianzhui = "khy2_";
		final AppContext appContext = (AppContext) getApplicationContext();

		String requestString = qianzhui + appContext.getUser().getUsername()
				+ "_" + appContext.getSIME();
//		onCreateDialog();
		// 发送请求
		socketUtil.sendRequestForTxtFile(SocketUtils.ZHUCHE_REQUEST + requestString,
				SocketUtils.ZHUCHE_REQUEST3 + qianzhui + appContext.getUser().getUsername(),
				new SocketListener() {

					@Override
					public void downLoadSuccess(final String respone) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
//										dialog.dismiss();
										
										String res = respone;
										ArrayList<UnGetProduct> ungetList = UnGetProduct.parseQuestion(res);		
										for (UnGetProduct unget : ungetList) {
											Log.d("test", unget.getGuest_name());
										}
									}
								});
							}
						});
					}

					@Override
					public void downLoadFail() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								}
						});
					}
					
					@Override
					public void timeOut() {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						Toast.makeText(MineActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
	}
}
