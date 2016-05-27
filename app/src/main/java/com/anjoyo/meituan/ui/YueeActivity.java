package com.anjoyo.meituan.ui;

import java.util.ArrayList;
import java.util.List;

import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.Parser.GroupBuyLikeParser;
import com.anjoyo.meituan.adapter.CollectAdapter;
import com.anjoyo.meituan.adapter.CollectSellerAdapter;
import com.anjoyo.meituan.adapter.MySimpleCursorAdapter2;
import com.anjoyo.meituan.adapter.ProductAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Beautician;
import com.anjoyo.meituan.domain.UnGetProduct;
import com.anjoyo.meituan.domain.UnPayService;
import com.anjoyo.meituan.domain.Yuer;
import com.anjoyo.meituan.myview.MyListView;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class YueeActivity extends BaseActivity implements OnClickListener {
	private ImageView imageview_back;
	private TextView imageview_back2;
	private Button button_product, button_seller, button_other;
	private LinearLayout line_groupbuy, line_seller, line_other;
	private MyListView listview;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private TextView text4;
	private TextView text5;
	private TextView text6;
	private TextView textview;
	private MyListView xListView;
	private MyListView xListView2;
	private MyListView xListView3;
	private GroupBuyLikeParser parser;
	private CollectSellerAdapter adapter2;
	private CollectAdapter collectAdapter;
	public static ArrayList<Beautician> mList = new ArrayList<Beautician>();
	ProductAdapter mAdapter;
	private SocketUtils socketUtil;
	boolean first = false;
	boolean second = false;
	boolean third = false;
	String curSime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	void init() {
		socketUtil = new SocketUtils(this);
		curSime = getIntent().getStringExtra("sime");
		setContentView(R.layout.yuer_activity);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);
		button_product = (Button) findViewById(R.id.button_product);
		button_seller = (Button) findViewById(R.id.button_service);
		button_other = (Button) findViewById(R.id.button_other);
		line_groupbuy = (LinearLayout) findViewById(R.id.line_product);
		line_seller = (LinearLayout) findViewById(R.id.line_seller);
		line_other = (LinearLayout) findViewById(R.id.line_other);
		listview = (MyListView) findViewById(R.id.listview_collect);
		textview = (TextView) findViewById(R.id.textview_collect);
		xListView = (MyListView) findViewById(R.id.xlistview);
		xListView2 = (MyListView) findViewById(R.id.xlistview2);
		xListView3 = (MyListView) findViewById(R.id.xlistview3);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		text4 = (TextView) findViewById(R.id.text4);
		text5 = (TextView) findViewById(R.id.text5);
		text6 = (TextView) findViewById(R.id.text6);
		
		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
		button_product.setOnClickListener(this);
		button_seller.setOnClickListener(this);
		button_other.setOnClickListener(this);
		
		text1.setText("客人卡号");
		text2.setText("客人姓名");
		text3.setText("服务名称");
		text4.setText("剩余次数");
		text5.setText("最后使用时间");
		text6.setText("服务代码");

		AppContext appContext = (AppContext) getApplicationContext();
		
		listview.setVisibility(View.GONE);
		textview.setVisibility(View.GONE);
		xListView.setVisibility(View.VISIBLE);
		xListView2.setVisibility(View.GONE);
		xListView3.setVisibility(View.GONE);
//		btnMllc = (Button) findViewById(R.id.btn_mllc);
//		btnMllc.setOnClickListener(this);
		String qianzhui = "khy1_";

		String requestString = qianzhui + appContext.getUser().getUsername()
				+ "_" + curSime;
		onCreateDialog();
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
										dialog.dismiss();
										first = true;
										
										String res = respone;
										ArrayList<Yuer> yuerList = Yuer.parseYuer(res);	
										
											List<String> list = new ArrayList<String>();
										
											for (int i = 0; i < yuerList.size(); i++) {
												list.add(yuerList.get(i).toListString());
											}
											
											ListAdapter adapter = new MySimpleCursorAdapter2(YueeActivity.this, list);
											xListView.setAdapter(adapter);
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
						Toast.makeText(YueeActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		
		case R.id.button_service:
			button_seller.setTextColor(getResources().getColor(R.color.black));
			button_product.setTextColor(getResources().getColor(
					R.color.textgray));
			button_other
					.setTextColor(getResources().getColor(R.color.textgray));
			line_seller.setVisibility(View.VISIBLE);
			line_groupbuy.setVisibility(View.INVISIBLE);
			line_other.setVisibility(View.INVISIBLE);
			xListView.setVisibility(View.GONE);
			xListView2.setVisibility(View.VISIBLE);
			xListView3.setVisibility(View.GONE);
			
			text1.setText("客人卡号");
			text2.setText("客人姓名");
			text3.setText("货品名称");
			text4.setText("数量");
			text5.setText("单位");
			text6.setText("产品代码");
			if (second) {
				return;
			}
			
			String qianzhui = "khy2_";
			final AppContext appContext = (AppContext) getApplicationContext();

			String requestString = qianzhui + appContext.getUser().getUsername()
					+ "_" + curSime;
			onCreateDialog();
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
											second = true;
											dialog.dismiss();
											
											String res = respone;
											List<String> list = new ArrayList<String>();
											
											ArrayList<UnGetProduct> ungetList = UnGetProduct.parseQuestion(res);
											for (int i = 0; i < ungetList.size(); i++) {
												list.add(ungetList.get(i).toListString());
											}
											
											ListAdapter adapter = new MySimpleCursorAdapter2(YueeActivity.this, list);
											xListView2.setAdapter(adapter);
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
							Toast.makeText(YueeActivity.this, "网络请求超时",
									Toast.LENGTH_SHORT).show();
							
						}
					});
			

			break;
		case R.id.button_product:
			button_product.setTextColor(getResources().getColor(R.color.black));
			button_seller.setTextColor(getResources()
					.getColor(R.color.textgray));
			button_other
					.setTextColor(getResources().getColor(R.color.textgray));
			line_groupbuy.setVisibility(View.VISIBLE);
			line_seller.setVisibility(View.INVISIBLE);
			line_other.setVisibility(View.INVISIBLE);

			xListView.setVisibility(View.VISIBLE);
			xListView2.setVisibility(View.GONE);
			xListView3.setVisibility(View.GONE);
			
			text1.setText("客人卡号");
			text2.setText("客人姓名");
			text3.setText("服务名称");
			text4.setText("剩余次数");
			text5.setText("最后使用时间");
			text6.setText("服务代码");
			if (first) {
				return;
			}
			
			break;
		case R.id.button_other:
			button_other.setTextColor(getResources().getColor(R.color.black));
			button_product.setTextColor(getResources().getColor(
					R.color.textgray));
			button_seller.setTextColor(getResources()
					.getColor(R.color.textgray));
			line_groupbuy.setVisibility(View.INVISIBLE);
			line_seller.setVisibility(View.INVISIBLE);
			line_other.setVisibility(View.VISIBLE);

			xListView.setVisibility(View.GONE);
			xListView2.setVisibility(View.GONE);
			xListView3.setVisibility(View.VISIBLE);
			
			text1.setText("客户代码");
			text2.setText("客户");
			text3.setText("日期");
			text4.setText("项目");
			text5.setText("未结算金额");
			text6.setText("项目代码");
			
			if (third) {
				return;
			}
		qianzhui = "khy3_";
		final AppContext appContext2 = (AppContext) getApplicationContext();

		 requestString = qianzhui + appContext2.getUser().getUsername()
					+ "_" + curSime;
			onCreateDialog();
			// 发送请求
			socketUtil.sendRequestForTxtFile(SocketUtils.ZHUCHE_REQUEST + requestString,
					SocketUtils.ZHUCHE_REQUEST3 + qianzhui + appContext2.getUser().getUsername(),
					new SocketListener() {

						@Override
						public void downLoadSuccess(final String respone) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									 runOnUiThread(new Runnable() {
										@Override
										public void run() {
											dialog.dismiss();
											third = true;
											String res = respone;
											
											List<String> list = new ArrayList<String>();
											
											ArrayList<UnPayService> unpayList = UnPayService.parseQuestion(res);		
											for (int i = 0; i < unpayList.size(); i++) {
												list.add(unpayList.get(i).toListString());
											}
											
											ListAdapter adapter = new MySimpleCursorAdapter2(YueeActivity.this, list);
											xListView3.setAdapter(adapter);
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
							Toast.makeText(YueeActivity.this, "网络请求超时",
									Toast.LENGTH_SHORT).show();
							
						}
					});
			break;
//		case R.id.btn_mllc:
//			Intent intent = new Intent(PeopleDetailActivity.this, HistoryActivity.class);
//			intent.putExtra("product_id", datas.getbeautician_id());
//			startActivity(intent);
		default:
			break;
		}

	}

}
