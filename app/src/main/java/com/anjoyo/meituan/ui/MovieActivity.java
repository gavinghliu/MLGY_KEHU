package com.anjoyo.meituan.ui;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.adapter.GroupBuyLikeAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Good;
import com.anjoyo.meituan.myview.MyListView;

public class MovieActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private MyListView listviewlistView_todayNewOrder;
	private int flag_;// 辨别从哪一个页面跳转过来的标记
	String[] strings = { "美食", "电影", "周边游", "", "KTV", "酒店", "足底按摩" };
	private AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_new_order);

		appContext = (AppContext) getApplicationContext();

		Intent intent = getIntent();
		flag_ = intent.getIntExtra("flag", 1);

		this.initView();
		switchAdapter(flag_);
	}

	public void initView() {
		listviewlistView_todayNewOrder = (MyListView) findViewById(R.id.listView_todayNewOrder);
		listviewlistView_todayNewOrder.setOnItemClickListener(this);
		// 标题
		TextView textView_categories = (TextView) findViewById(R.id.tv_categories);
		textView_categories.setText(strings[flag_ - 1]);
		// 分组栏
		TextView textView_main_title = (TextView) findViewById(R.id.textView_main_title);
		textView_main_title.setText(strings[flag_ - 1]);
		// 返回
		RelativeLayout relativeLayout_groupbuydetails_left = (RelativeLayout) findViewById(R.id.relativeLayout_groupbuydetails_left);
		relativeLayout_groupbuydetails_left.setOnClickListener(this);
		// 地图
		ImageButton imagebuttton_map = (ImageButton) findViewById(R.id.imagebutton_map_order);
		imagebuttton_map.setOnClickListener(this);
		// 搜索
		ImageButton imagebutton_search = (ImageButton) findViewById(R.id.imagebutton_search_order);
		imagebutton_search.setOnClickListener(this);
	}

	// 返回电影，美食等数据源
	public ArrayList<Good> getdataSource(int allCategory_id) {
		List<Good> goods = appContext.getGoods();
		// 想返回的新的数据源
		ArrayList<Good> newGoods = new ArrayList<Good>();
		for (int i = 0; i < goods.size(); i++) {
			Good good = goods.get(i);
			if (good.getAllCategories_id() == allCategory_id) {
				newGoods.add(good);
			}
		}
		return newGoods;
	}

	private void switchAdapter(int flag) {

		switch (flag) {

		case 2:// 电影
			ArrayList<Good> newGoods1 = this.getdataSource(2);
			GroupBuyLikeAdapter adapter1 = new GroupBuyLikeAdapter(this,
					newGoods1);
			listviewlistView_todayNewOrder.setAdapter(adapter1);
			break;
		case 5:// KTV
			ArrayList<Good> newGoods2 = this.getdataSource(5);
			GroupBuyLikeAdapter adapter2 = new GroupBuyLikeAdapter(this,
					newGoods2);
			listviewlistView_todayNewOrder.setAdapter(adapter2);
			break;
		case 6:// 酒店
			ArrayList<Good> newGoods3 = this.getdataSource(6);
			GroupBuyLikeAdapter adapter3 = new GroupBuyLikeAdapter(this,
					newGoods3);
			listviewlistView_todayNewOrder.setAdapter(adapter3);
			break;
		case 1:// 美食
			ArrayList<Good> newGoods4 = this.getdataSource(1);
			GroupBuyLikeAdapter adapter4 = new GroupBuyLikeAdapter(this,
					newGoods4);
			listviewlistView_todayNewOrder.setAdapter(adapter4);
			break;
		case 7:// 足底按摩
			ArrayList<Good> newGoods5 = this.getdataSource(7);
			GroupBuyLikeAdapter adapter5 = new GroupBuyLikeAdapter(this,
					newGoods5);
			listviewlistView_todayNewOrder.setAdapter(adapter5);
			break;
		case 3:// 周边游
			ArrayList<Good> newGoods6 = this.getdataSource(3);
			GroupBuyLikeAdapter adapter6 = new GroupBuyLikeAdapter(this,
					newGoods6);
			listviewlistView_todayNewOrder.setAdapter(adapter6);
			break;
		default:
			break;
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.relativeLayout_groupbuydetails_left:// 返回
			finish();
			break;
		case R.id.imagebutton_map_order:// 地图
//			startActivity(new Intent(this, MyMapActivity.class));
			break;
		case R.id.imagebutton_search_order:// 搜索
			startActivity(new Intent(this, SeekActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent=new Intent(this,GroupBuyDetailsActivity.class);
		intent.setAction("MovieActivity");
	    int goods_id= this.getdataSource(flag_).get(position).getGoods_id();
		intent.putExtra("goods_id", goods_id);
	    startActivity(intent);		
	}
}
