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
  
public class MySimpleCursorAdapter2 extends BaseAdapter {  
  
	Context mContext;
	List<String> mList;
	Typeface fontFace;
	
    public MySimpleCursorAdapter2(Context context, List<String> list) {  
    	mContext = context;
    	mList = list;
    	fontFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/STXINGKA.TTF");
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        // listview每次得到一个item，都要view去绘制，通过getView方法得到view  
        // position为item的序号  
        Holder holder = null;
        if (convertView == null) {  
        	holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listviewlayout2, null);
            holder.cl1 = (TextView) convertView.findViewById(R.id.caozou);
            holder.cl2 = (TextView) convertView.findViewById(R.id.explain);
            holder.cl3 = (TextView) convertView.findViewById(R.id.time);
            holder.cl4 = (TextView) convertView.findViewById(R.id.text4);
            holder.cl5 = (TextView) convertView.findViewById(R.id.text5);
            holder.cl6 = (TextView) convertView.findViewById(R.id.text6);
            holder.cl1.setTypeface(fontFace);
            holder.cl3.setTypeface(fontFace);
            holder.cl2.setTypeface(fontFace);
            holder.cl4.setTypeface(fontFace);
            holder.cl5.setTypeface(fontFace);
            holder.cl6.setTypeface(fontFace);
            convertView.setTag(holder);
        } else {  
        	holder = (Holder) convertView.getTag();
        }  
        
        int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色  
  
        convertView.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同  
        
        String string = mList.get(position);
        
        String[] temp = string.split("&");
        if (temp.length < 6) {
			return convertView;
		}
        holder.cl1.setText(temp[0]);
        holder.cl2.setText(temp[1]);
        holder.cl3.setText(temp[2]);
        holder.cl4.setText(temp[3]);
        holder.cl5.setText(temp[4]);
        holder.cl6.setText(temp[5]);
        return convertView;  
    }
    
    
    class Holder {
    	TextView cl1;
    	TextView cl2;
    	TextView cl3;
    	TextView cl4;
    	TextView cl5;
    	TextView cl6;
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