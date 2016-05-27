package com.anjoyo.meituan.ui;

import java.util.ArrayList;
import java.util.List;

import com.anjoyo.meituan.Parser.ProductParser;
import com.anjoyo.meituan.adapter.HistoryAdapter;
import com.anjoyo.meituan.adapter.MySimpleCursorAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Product;
import com.anjoyo.meituan.domain.ServiceHistoryDetail;
import com.anjoyo.meituan.utils.FtpUtils;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.XListView;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.mlgy.R;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GridActivity extends BaseActivity implements OnClickListener {

	// DbHelper类在DbHelper.java文件里面创建的
	ListView lv;
	LinearLayout mTitleLayout;
	private ImageView imageview_back;
	private TextView imageview_back2;
	private SocketUtils socketUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		lv = (ListView) findViewById(R.id.lv);
		mTitleLayout = (LinearLayout) findViewById(R.id.title);
		mTitleLayout.setBackgroundColor(Color.rgb(219, 238, 244));
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (TextView) findViewById(R.id.imageview_back2);
		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
		socketUtil = new SocketUtils(this);
		Typeface fontFace = Typeface.createFromAsset(getAssets(),
				"fonts/STXINGKA.TTF");
		
		TextView t1 = (TextView)findViewById(R.id.title1);
		TextView t2 = (TextView)findViewById(R.id.title2);
		TextView t3 = (TextView)findViewById(R.id.title3);
		t1.setTypeface(fontFace);
		t2.setTypeface(fontFace);
		t3.setTypeface(fontFace);
		
//		onCreateDialog();
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				FtpUtils.ftpconnectDownload("fuwujilu_" + sime
//						+ ".txt", "//Data/guangzhu/" + sime
//						+ "/fuwujilu.txt",
//						new MyTransferListener(sime));
//			}
//		}).start();
		
		
		
		
		String qianzhui = "khl4_";
		final AppContext appContext = (AppContext) getApplicationContext();

		String requestString = qianzhui + appContext.getUser().getUsername()
				+ "_" + appContext.getSIME() + "_" + getIntent().getStringExtra("procode");
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
										updatelistview(respone);
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
						Toast.makeText(GridActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
	}

	// 更新listview
	public void updatelistview(String respone) {

//		List<String> list = new ArrayList<String>();
//		list.add("冥想&放松身心，让人心神愉悦&5");
//		list.add("热敷&温热皮肤，行气活血，舒缓疲劳，放松神经，补充能量，行气活血&5");
//		list.add("头部松筋(后面)&改善睡眠，增强记忆力，改善头部供氧供血&5");
//		list.add("背部&疏通经络，补充能量，调理各种亚健康&30");
//		list.add("腿部(后面)&疏通经络，，排寒气，各种湿毒热毒，调理脏腑各种亚健康&15");
//		list.add("头部松筋(前面)&改善睡眠，增强记忆力，改善头部供氧供血&5");
//		list.add("腹部&排肠毒，改善肠胃功能，预防肠胃疾病，预防妇科疾病&15");
//		list.add("腿部(前面)&疏通经络，，排寒气，各种湿毒热毒，调理脏腑各种亚健康&15");
//		list.add("药膳&排毒，补充水分，能量& ");
//		list.add("头部松筋(前面)&改善睡眠，增强记忆力，改善头部供氧供血&5");
//		list.add("腹部&排肠毒，改善肠胃功能，预防肠胃疾病，预防妇科疾病&15");
//		list.add("腿部(前面)&疏通经络，，排寒气，各种湿毒热毒，调理脏腑各种亚健康&15");
//		list.add("药膳&排毒，补充水分，能量& ");
//		ListAdapter adapter = new MySimpleCursorAdapter(this, list);
//		// layout为listView的布局文件，包括三个TextView，用来显示三个列名所对应的值
//		// ColumnNames为数据库的表的列名
//		// 最后一个参数是int[]类型的，为view类型的id，用来显示ColumnNames列名所对应的值。view的类型为TextView
//		lv.setAdapter(adapter);
		
		List<ServiceHistoryDetail> serviceList = ServiceHistoryDetail.parseDetail(respone);

		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < serviceList.size(); i++) {
			list.add(serviceList.get(i).getLiucheng() + "&" + serviceList.get(i).getShuoming() + "&" + serviceList.get(i).getShijian());
		}
		
		ListAdapter adapter = new MySimpleCursorAdapter(this, list);
		lv.setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {// 关闭数据库
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();

			break;

		default:
			break;
		}

	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		
	}

}