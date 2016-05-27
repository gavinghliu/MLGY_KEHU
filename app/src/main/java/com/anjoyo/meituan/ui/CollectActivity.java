package com.anjoyo.meituan.ui;

import java.util.ArrayList;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.Parser.GroupBuyLikeParser;
import com.anjoyo.meituan.Parser.ProductParser;
import com.anjoyo.meituan.adapter.CollectAdapter;
import com.anjoyo.meituan.adapter.CollectSellerAdapter;
import com.anjoyo.meituan.adapter.ProductAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.myview.MyListView;
import com.anjoyo.meituan.utils.XListView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectActivity extends BaseActivity implements OnClickListener {
	private ImageView imageview_back;
	private TextView imageview_back2;
	private Button button_product, button_seller, button_other;
	private LinearLayout line_groupbuy, line_seller, line_other;
	private MyListView listview;
	private TextView textview;
	private XListView xListView;
	private GroupBuyLikeParser parser;
	private CollectSellerAdapter adapter2;
	private CollectAdapter collectAdapter;
	public static ArrayList<Product> mList = new ArrayList<Product>();
	ProductAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	void init() {
		setContentView(R.layout.collect_activity);
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
		xListView = (XListView) findViewById(R.id.xlistview);

		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
		button_product.setOnClickListener(this);
		button_seller.setOnClickListener(this);
		button_other.setOnClickListener(this);

		AppContext appContext = (AppContext) getApplicationContext();
		User user = appContext.getUser();
		String username = user.getUsername();
		mAdapter = new ProductAdapter(CollectActivity.this, mList);
		mAdapter.setAction("collect");
		listview.setAdapter(mAdapter);
		
		ProductParser productParer = new ProductParser();
		String res = "{'products' : [{product_id:1,product_name:收藏产品1,is_fav:1,product_like_count:100,product_islike:0,comment_count:5,product_kind:2,product_picture:'',product_des:产品描述~~,product_rank:4,product_price:1000},"
				+ "{product_id:1,product_name:收藏产品2,is_fav:0,comment_count:2,product_like_count:100,product_islike:0,product_kind:3,product_picture:'',product_des:产品描述~~,product_rank:3,product_price:1500},"
				+ "{product_id:1,product_name:收藏产品4,is_fav:0,product_like_count:100,product_islike:1,comment_count:123,product_kind:4,product_picture:'',product_des:产品描述~~,product_rank:1,product_price:1400}]}";

		ArrayList<Product> product = productParer.getMerchant(res);
		mList.clear();
		mList.addAll(product);
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
		listview.setVisibility(View.VISIBLE);
		textview.setVisibility(View.GONE);
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

			ProductParser productParer = new ProductParser();
			String res = "{'products' : [{product_id:1,product_name:收藏服务1,is_fav:1,product_like_count:100,product_islike:0,comment_count:5,product_kind:2,product_picture:'',product_des:产品描述~~,product_rank:4,product_price:1000},"
					+ "{product_id:1,product_name:收藏服务2,is_fav:0,comment_count:2,product_like_count:100,product_islike:0,product_kind:3,product_picture:'',product_des:产品描述~~,product_rank:3,product_price:1500},"
					+ "{product_id:1,product_name:收藏服务4,is_fav:0,product_like_count:100,product_islike:1,comment_count:123,product_kind:4,product_picture:'',product_des:产品描述~~,product_rank:1,product_price:1400}]}";

			ArrayList<Product> product = productParer.getMerchant(res);

			if (product != null && !product.isEmpty()) {
				mList.clear();
				mList.addAll(product);
				textview.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				xListView.setVisibility(View.GONE);
			} else {
				textview.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				xListView.setVisibility(View.GONE);
			}
			mAdapter.setList(mList);
			mAdapter.notifyDataSetChanged();

			// listview.setPullLoadEnable(false);
			// listview.setPullRefreshEnable(false);

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

			productParer = new ProductParser();
			res = "{'products' : [{product_id:1,product_name:收藏产品1,is_fav:1,product_like_count:100,product_islike:0,comment_count:5,product_kind:2,product_picture:'',product_des:产品描述~~,product_rank:4,product_price:1000},"
					+ "{product_id:1,product_name:收藏产品2,is_fav:0,comment_count:2,product_like_count:100,product_islike:0,product_kind:3,product_picture:'',product_des:产品描述~~,product_rank:3,product_price:1500},"
					+ "{product_id:1,product_name:收藏产品4,is_fav:0,product_like_count:100,product_islike:1,comment_count:123,product_kind:4,product_picture:'',product_des:产品描述~~,product_rank:1,product_price:1400}]}";

			product = productParer.getMerchant(res);

			if (product != null && !product.isEmpty()) {
				mList.clear();
				mList.addAll(product);
				textview.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				xListView.setVisibility(View.GONE);
			} else {
				textview.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				xListView.setVisibility(View.GONE);
			}
			mAdapter.setList(mList);
			mAdapter.notifyDataSetChanged();

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

			if (collectAdapter != null) {
				xListView.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				textview.setVisibility(View.GONE);
			} else {
				xListView.setVisibility(View.GONE);
				listview.setVisibility(View.GONE);
				textview.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}

	}

}
