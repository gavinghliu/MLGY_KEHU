package com.anjoyo.meituan.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anjoyo.meituan.Parser.ProductParser;
import com.anjoyo.meituan.adapter.HistoryAdapter;
import com.anjoyo.meituan.adapter.SellerAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Beautician;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.Seller;
import com.anjoyo.meituan.myview.ExpandTabView;
import com.anjoyo.meituan.myview.ViewLeft;
import com.anjoyo.meituan.myview.ViewRight;
import com.anjoyo.meituan.myview.ViewService;
import com.anjoyo.meituan.utils.Logger;
import com.anjoyo.meituan.utils.ScrollLayout;
import com.anjoyo.meituan.utils.ScrollLayout.OnViewChangeListener;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.meituan.utils.XListView;
import com.anjoyo.meituan.utils.XListView.IXListViewListener;
import com.anjoyo.mlgy.R;

@SuppressLint("HandlerLeak")
public class HistoryActivity extends BaseActivity implements
		OnViewChangeListener, OnClickListener {
	private RadioButton button_allmerchant, button_salemerchant,
			button_service;
	private ScrollLayout scrollLayout;
	private int viewCount;
	private int mCurSel;
	private RadioButton[] buttons;
	private ExpandTabView expandTabView, expandTabViewSeller,
			expandTabViewService;
	private ArrayList<View> mViewArray = new ArrayList<View>();
	private ArrayList<View> mViewArraySellr = new ArrayList<View>();
	private ArrayList<View> mViewArrayService = new ArrayList<View>();
	private ViewLeft viewLeft;
	// private ViewMiddle viewMiddle;
	private ViewRight viewRight;
	private ViewService viewService;
	public static ArrayList<Product> allList = new ArrayList<Product>();   //产品列表
	public static ArrayList<Product> saleList = null;		//其他列表
	public static ArrayList<Product> serviceList = null;  //服务列表
	private SellerAdapter merchantSaleAdapter;
	private HistoryAdapter productAdapter;
	private HistoryAdapter serviceAdapter;
	public static XListView aListView, sListView, serviceListView;
	int currenViewPostion;
	private RelativeLayout backLayout;
	private SocketUtils socketUtil;
	private String curSime;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.history_activity);
		super.onCreate(savedInstanceState);
		socketUtil = new SocketUtils(this);
		curSime = getIntent().getStringExtra("sime");
		initMerchantHeader();
		initProductListView();
		initPageScroll();
		initClassifyView();
		backLayout = (RelativeLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(this);
	}

	@Override
	void init() {
	}

	/*
	 * 初始化分类栏
	 */
	private void initClassifyView() {
		expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view_product);
		viewLeft = new ViewLeft(this);
		viewRight = new ViewRight(this);

		mViewArray.add(viewLeft);
		ArrayList<String> mTextArray = new ArrayList<String>();
		mTextArray.add("全部");
		int[] photo = { R.drawable.ic_category_all };
		expandTabView.setValue(mTextArray, mViewArray, photo);
		expandTabView.setTitle(viewLeft.getShowText(), 1);
		expandTabView.setPhoto(photo[0], 0);

		expandTabViewService = (ExpandTabView) findViewById(R.id.expandtab_view_service);
		viewService = new ViewService(this);
		mViewArrayService.add(viewService);
		mTextArray = new ArrayList<String>();
		mTextArray.add("全部");
		int[] photo3 = { R.drawable.ic_category_all };
		expandTabViewService.setValue(mTextArray, mViewArrayService, photo3);
		expandTabViewService.setTitle(viewService.getShowText(), 1);
		expandTabViewService.setPhoto(photo3[0], 0);

		viewService.setOnSelectListener(new ViewService.OnSelectListener() {

			public void getValue(String distance, String showText) {
				onRefreshService(viewRight, distance, showText);
			}
		});

		expandTabViewSeller = (ExpandTabView) findViewById(R.id.expandtab_view_seller);

		mViewArraySellr.add(viewRight);
		mTextArray = new ArrayList<String>();
		mTextArray.add("全部");
		int[] photo2 = { R.drawable.ic_category_all };
		expandTabViewSeller.setValue(mTextArray, mViewArraySellr, photo2);
		expandTabViewSeller.setTitle(viewRight.getShowText(), 1);
		expandTabViewSeller.setPhoto(photo2[0], 0);

		viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

			public void getValue(String distance, String showText) {
				onRefreshProduct(viewLeft, distance, showText);
			}
		});

		viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {

			public void getValue(String distance, String showText) {
				onRefreshSeller(viewRight, distance, showText);
			}
		});

		expandTabView.setVisibility(View.VISIBLE);
		expandTabViewSeller.setVisibility(View.GONE);
		expandTabViewService.setVisibility(View.GONE);
	}

	/*
	 * 刷新分类栏左侧的图片
	 */
	private void onRefreshPhoto(View view, int showPhoto) {
		expandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabView.getPhoto(position).equals(showPhoto)) {
			expandTabView.setPhoto(showPhoto, position);
		}
	}

	private void onRefreshProduct(View view, String kind, String showText) {

		expandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
			expandTabView.setTitle(showText, position);
		}
		ArrayList<Product> productList = new ArrayList<Product>();
		for (int i = 0; i < allList.size(); i++) {
			Product product = allList.get(i);
//			if (kind.equals("1") || product.getKind().equals(kind)) {
//				productList.add(product);
//			}
		}
		productAdapter.setList(productList);
		productAdapter.notifyDataSetChanged();
	}

	private void onRefreshSeller(View view, String kind, String showText) {

		expandTabViewSeller.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabViewSeller.getTitle(position).equals(showText)) {
			expandTabViewSeller.setTitle(showText, position);
		}

		ArrayList<Seller> sellerList = new ArrayList<Seller>();
//		for (int i = 0; i < saleList.size(); i++) {
//			Seller seller = saleList.get(i);
//			if (kind.equals("1") || seller.getKind().equals(kind)) {
//				sellerList.add(seller);
//			}
//		}
		merchantSaleAdapter.setList(sellerList);
		merchantSaleAdapter.notifyDataSetChanged();
	}

	private void onRefreshService(View view, String kind, String showText) {

		expandTabViewService.onPressBack();
		int position = getPositon(view);
		if (position >= 0
				&& !expandTabViewService.getTitle(position).equals(showText)) {
			expandTabViewService.setTitle(showText, position);
		}
//		ArrayList<Beautician> productList = new ArrayList<Beautician>();
		for (int i = 0; i < serviceList.size(); i++) {
			Product product = serviceList.get(i);
//			if (kind.equals("1") || product.getKind().equals(kind)) {
//				productList.add(product);
//			}
		}
//		serviceAdapter.setList(productList);
		serviceAdapter.notifyDataSetChanged();
	}

	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}

		for (int i = 0; i < mViewArraySellr.size(); i++) {
			if (mViewArraySellr.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}

	public void onBackPressed() {

		if (!expandTabView.onPressBack()) {
			finish();
		}
	}

	// 初始化textView,button控件
	private void initMerchantHeader() {
		button_allmerchant = (RadioButton) findViewById(R.id.button_allmerchant);
		button_salemerchant = (RadioButton) findViewById(R.id.button_salemerchant);
		button_service = (RadioButton) findViewById(R.id.button_service);
	}

	private void initProductListView() {
		if (null == allList) {
			return;
		}
		String qianzhui = "khl1_";
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
										dialog.dismiss();
										aListView = (XListView) findViewById(R.id.listview_merchantall);
										ProductParser productParer = new ProductParser();
										String res = respone;
										ArrayList<Product> product = productParer.getMerchant(res);
								
										if (product != null && !product.isEmpty()) {
											allList.clear();
											allList.addAll(product);
										}
										AppContext appContext = (AppContext) getApplicationContext();
										appContext.setProduct(allList);
										productAdapter = new HistoryAdapter(HistoryActivity.this, allList);
										productAdapter.setAction("HistoryProduct");
										aListView.setAdapter(productAdapter);
										aListView.setPullLoadEnable(false);
										aListView.setPullRefreshEnable(false);
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
						Toast.makeText(HistoryActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});

	}

	private void initServiceListView() {
		
		String qianzhui = "khl2_";
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
										dialog.dismiss();
										
										serviceListView = (XListView) findViewById(R.id.listview_service);
										serviceList = new ArrayList<Product>();
										ProductParser productParer = new ProductParser();
										String res = respone;
										ArrayList<Product> product = productParer.getMerchant(res);

										if (product != null && !product.isEmpty()) {
											serviceList.clear();
											serviceList.addAll(product);
										}
										serviceAdapter = new HistoryAdapter(HistoryActivity.this, serviceList);
										serviceAdapter.setAction("HistoryService");
										serviceListView.setAdapter(serviceAdapter);

										serviceListView.setPullLoadEnable(false);
										serviceListView.setPullRefreshEnable(false);
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
						Toast.makeText(HistoryActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
		
	}

	private void onLoad(XListView listView) {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	private void initMerchantSaleListView() {
		String qianzhui = "khl3_";
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
										dialog.dismiss();
										
//										sListView = (XListView) findViewById(R.id.listview_service);
										saleList = new ArrayList<Product>();
//										ProductParser productParer = new ProductParser();
//										String res = respone;
//										ArrayList<Product> product = productParer.getMerchant(res);
//
//										if (product != null && !product.isEmpty()) {
//											saleList.clear();
//											saleList.addAll(product);
//										}
//										serviceAdapter = new HistoryAdapter(HistoryActivity.this, saleList);
//										serviceAdapter.setAction("HistoryService");
//										sListView.setAdapter(serviceAdapter);
//
//										sListView.setPullLoadEnable(false);
//										sListView.setPullRefreshEnable(false);
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
						Toast.makeText(HistoryActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
		
	

	}

	/**
	 * 初始化水平滚动翻页 RadioButton
	 */
	private void initPageScroll() {
		currenViewPostion = 0;
		scrollLayout = (ScrollLayout) findViewById(R.id.scrollView);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.merchant_header);

		viewCount = scrollLayout.getChildCount();
		Logger.i("MerActivity:   viewCount" + viewCount);
		buttons = new RadioButton[viewCount];

		for (int i = 0; i < viewCount; i++) {
			buttons[i] = (RadioButton) relativeLayout.getChildAt(i * 2 + 1);
			buttons[i].setTag(i);
			buttons[i].setChecked(false);
			buttons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int pos = (Integer) (v.getTag());
					currenViewPostion = pos;
					switch (pos) {
					case 0:
						expandTabView.setVisibility(View.VISIBLE);
						expandTabViewSeller.setVisibility(View.GONE);
						expandTabViewService.setVisibility(View.GONE);
						// 刷新全部商家和优惠商家的radioButton下端的图片
						button_allmerchant
								.setCompoundDrawablesWithIntrinsicBounds(0, 0,
										0, R.drawable.bottom_divider);
						button_salemerchant
								.setCompoundDrawablesWithIntrinsicBounds(0, 0,
										0, R.color.green);
						button_service.setCompoundDrawablesWithIntrinsicBounds(
								0, 0, 0, R.color.green);
						break;
					case 1:
						if (serviceList == null || serviceList.size() == 0) {
							initServiceListView();
						} else {
							serviceListView = (XListView) findViewById(R.id.listview_service);
							serviceAdapter = new HistoryAdapter(HistoryActivity.this, serviceList);
							serviceAdapter.setAction("HistoryService");
							serviceListView.setAdapter(serviceAdapter);

							serviceListView.setPullLoadEnable(false);
							serviceListView.setPullRefreshEnable(false);
						}
						
						expandTabViewSeller.setVisibility(View.VISIBLE);
						expandTabView.setVisibility(View.GONE);
						expandTabViewService.setVisibility(View.GONE);
						button_salemerchant
								.setCompoundDrawablesWithIntrinsicBounds(0, 0,
										0, R.color.green);
						button_allmerchant
								.setCompoundDrawablesWithIntrinsicBounds(0, 0,
										0, R.color.green);
						button_service.setCompoundDrawablesWithIntrinsicBounds(
								0, 0, 0, R.drawable.bottom_divider);
						break;
					case 2:
						if (saleList == null) {
							initMerchantSaleListView();
						}
						
						expandTabViewSeller.setVisibility(View.GONE);
						expandTabView.setVisibility(View.GONE);
						expandTabViewService.setVisibility(View.VISIBLE);
						button_salemerchant
								.setCompoundDrawablesWithIntrinsicBounds(0, 0,
										0, R.color.green);
						button_allmerchant
								.setCompoundDrawablesWithIntrinsicBounds(0, 0,
										0, R.drawable.bottom_divider);
						button_service.setCompoundDrawablesWithIntrinsicBounds(
								0, 0, 0, R.color.green);
						break;
					}
					scrollLayout.snapToScreen(pos);
				}
			});
		}

		// 设置第一显示屏
		mCurSel = 0;
		buttons[mCurSel].setChecked(true);
		scrollLayout
				.SetOnViewChangeListener(new ScrollLayout.OnViewChangeListener() {
					public void OnViewChange(int viewIndex) {
						// 切换列表视图-如果列表数据为空：加载数据
						currenViewPostion = viewIndex;
						switch (viewIndex) {
						case 0:
							expandTabView.setVisibility(View.VISIBLE);
							expandTabViewSeller.setVisibility(View.GONE);
							expandTabViewService.setVisibility(View.GONE);
							button_allmerchant
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.drawable.bottom_divider);
							button_salemerchant
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.color.green);
							button_service
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.color.green);
							break;
						case 1:
							if (serviceList == null || serviceList.size() == 0) {
								initServiceListView();
							} else {
								serviceListView = (XListView) findViewById(R.id.listview_service);
								serviceAdapter = new HistoryAdapter(HistoryActivity.this, serviceList);
								serviceAdapter.setAction("HistoryService");
								serviceListView.setAdapter(serviceAdapter);

								serviceListView.setPullLoadEnable(false);
								serviceListView.setPullRefreshEnable(false);
							}
							
							expandTabViewService.setVisibility(View.VISIBLE);
							expandTabView.setVisibility(View.GONE);
							expandTabViewSeller.setVisibility(View.GONE);
							button_salemerchant
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.color.green);
							button_allmerchant
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.color.green);
							button_service
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.drawable.bottom_divider);
							break;
						case 2:
							if (saleList == null) {
								initMerchantSaleListView();
							}
							
							expandTabViewSeller.setVisibility(View.VISIBLE);
							expandTabView.setVisibility(View.GONE);
							expandTabViewService.setVisibility(View.GONE);
							button_salemerchant
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.drawable.bottom_divider);
							button_allmerchant
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.color.green);
							button_service
									.setCompoundDrawablesWithIntrinsicBounds(0,
											0, 0, R.color.green);
							break;
						}
					}
				});
	}

	public void OnViewChange(int view) {
	}

	@Override
	protected void onResume() {
		super.onResume();
//		serviceAdapter.notifyDataSetChanged();
//		productAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back_layout:
			finish();
			break;

		default:
			break;
		}
	}
}
