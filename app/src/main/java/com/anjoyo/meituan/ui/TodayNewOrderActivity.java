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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.adapter.GroupBuyLikeAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Good;
import com.anjoyo.meituan.myview.MyListView;

public class TodayNewOrderActivity extends Activity implements OnClickListener,OnItemClickListener{

    private MyListView listviewlistView_todayNewOrder;
    private AppContext appContext;
    //今日新单数据源
	private ArrayList<Good> newGoods;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_new_order);
        
        appContext = (AppContext) getApplicationContext();
        this.initView();
        
        List<Good> goods=appContext.getGoods();
        //今日新单数据源
        newGoods = new ArrayList<Good>();
        for(int i=0;i<goods.size();i++){
        	int flag=goods.get(i).getGoods_isLatest();
        	if(flag==1){
        		newGoods.add(goods.get(i));
        	}
        }
        GroupBuyLikeAdapter adapter=new GroupBuyLikeAdapter(this, newGoods);
        listviewlistView_todayNewOrder.setAdapter(adapter);
    }

	private void initView() {
		listviewlistView_todayNewOrder =(MyListView) findViewById(R.id.listView_todayNewOrder);
		listviewlistView_todayNewOrder.setOnItemClickListener(this);
		//返回
		RelativeLayout relativeLayout_groupbuydetails_left = (RelativeLayout) findViewById(R.id.relativeLayout_groupbuydetails_left);
		relativeLayout_groupbuydetails_left.setOnClickListener(this);
		//地图
		ImageButton imagebuttton_map=(ImageButton) findViewById(R.id.imagebutton_map_order);
		imagebuttton_map.setOnClickListener(this);
		//搜索
		ImageButton imagebutton_search=(ImageButton) findViewById(R.id.imagebutton_search_order);
		imagebutton_search.setOnClickListener(this);
		//显示的位置
		LinearLayout linearlayout_todayNewOrder=(LinearLayout) findViewById(R.id.linearlayout_todayNewOrder);
		linearlayout_todayNewOrder.setVisibility(View.GONE);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.relativeLayout_groupbuydetails_left://返回
			finish();
			break;
		case R.id.imagebutton_map_order://地图
//			startActivity(new Intent(this,MyMapActivity.class));
			break;
		case R.id.imagebutton_search_order://搜索
			startActivity(new Intent(this,SeekActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent=new Intent(this,GroupBuyDetailsActivity.class);
		intent.setAction("TodayNewOrderActivity");
		int goods_id= newGoods.get(position).getGoods_id();
		intent.putExtra("goods_id", goods_id);
		startActivity(intent);		
	}

}
