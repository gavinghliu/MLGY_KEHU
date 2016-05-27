package com.anjoyo.meituan.myview;

import com.anjoyo.mlgy.R;
import com.anjoyo.meituan.adapter.TextAdapter;
import com.anjoyo.meituan.app.AppContext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class ViewRight extends RelativeLayout implements ViewBaseAction{

	private ListView mListView;
	private String[] items;
	private final String[] itemsVaule = new String[] { "1", "2", "3", "4", "5"};//隐藏id
	private OnSelectListener mOnSelectListener;
	private TextAdapter adapter;
	private String mDistance;
	private String showText = "全部";
	private Context mContext;
    
	public String getShowText() {
		return showText;
	}
  
	public ViewRight(Context context) {
		super(context);
		init(context);
	}

	public ViewRight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewRight(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_distance, this, true);
		
		items = new String[5];
		items[0] = "全部";

		String[] listZl = AppContext.getConfigration(mContext, "zhonglei3").split(",");
		for (int j = 0; j < listZl.length; j++)
			items[1 + j] = listZl[j];
		
		mListView = (ListView) findViewById(R.id.listView);
		adapter = new TextAdapter(context, items, R.drawable.choose_eara_item_selector);
		adapter.setTextSize(12);
		if (mDistance != null) {
			for (int i = 0; i < itemsVaule.length; i++) {
				if (itemsVaule[i].equals(mDistance)) {
					adapter.setSelectedPositionNoNotify(i);
					showText = items[i];
					break;
				}
			}
		}
		mListView.setAdapter(adapter);
		adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			public void onItemClick(View view, int position) {

				if (mOnSelectListener != null) {
					showText = items[position];
					mOnSelectListener.getValue(itemsVaule[position], items[position]);
				}
			}
		});
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String distance, String showText);
	}

	public void hide() {
		
	}

	public void show() {
		
	}

	public int getShowPhoto() {
		// TODO Auto-generated method stub
		return 0;
	}

}
