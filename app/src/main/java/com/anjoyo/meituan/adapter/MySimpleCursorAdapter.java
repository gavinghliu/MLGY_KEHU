package com.anjoyo.meituan.adapter;
import java.util.List;

import com.anjoyo.mlgy.R;

import android.content.Context;  
import android.database.Cursor;  
import android.graphics.Color;  
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.BaseAdapter;
import android.widget.TextView;
  
public class MySimpleCursorAdapter extends BaseAdapter {  
  
	Context mContext;
	List<String> mList;
	Typeface fontFace;
	
    public MySimpleCursorAdapter(Context context, List<String> list) {  
    	mContext = context;
    	mList = list;
    	fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/STXINGKA.TTF");
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
        // listview每次得到一个item，都要view去绘制，通过getView方法得到view  
        // position为item的序号  
        Holder holder = null;
        if (convertView == null) {  
        	holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listviewlayout, null);
            holder.cl1 = (TextView) convertView.findViewById(R.id.caozou);
            holder.cl2 = (TextView) convertView.findViewById(R.id.explain);
            holder.cl3 = (TextView) convertView.findViewById(R.id.time);
            holder.cl1.setTypeface(fontFace);
            holder.cl3.setTypeface(fontFace);
            holder.cl2.setTypeface(fontFace);
            convertView.setTag(holder);
        } else {  
        	holder = (Holder) convertView.getTag();
        }  
        
        int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色  
  
        convertView.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同  
        
        String string = mList.get(position);
        
        String[] temp = string.split("&");
        if (temp.length < 3) {
			return convertView;
		}
        holder.cl1.setText(temp[0]);
        holder.cl2.setText(temp[1]);
        holder.cl3.setText(temp[2]);
        return convertView;  
    }
    
    
    class Holder {
    	TextView cl1;
    	TextView cl2;
    	TextView cl3;
    }

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}  
  
}  