package com.anjoyo.meituan.ui;

import java.io.File;
import java.util.Calendar;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.utils.ExcelRead;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QueryActivity extends BaseActivity implements OnClickListener,SocketListener {

	private ImageView imageview_back;
	private TextView textView_back2;
	TextView chaxunxiangmudaimaTv;
	TextView mendianhaoTv;
	TextView kehubianhaoTv;
	TextView yuangonghaohaoTv;
	TextView gongyingshangdaimaTv;
	TextView xiangmudaimaTv;
	TextView huopingdaimaTv;
	TextView beiyongziduanTv1;
	TextView beiyongziduanTv2;
	TextView beiyongziduanTv3;
	Button commitBtn;
	
	private EditText sellStartTime;
	private EditText sellEndTime;
	
	private int mYear, mMonth, mDay, timeFlag;

	public ProgressDialog dialog;
	
	private SocketUtils socketUtil;

	// private final static String KEY = "OsvOOCUMoAKmBbHq_jMFCtxLD";
	private final static String KEY = "7D3157DA1B76B50A2FA0E9C9AFF13834D72D330B";
	private double lat;
	private double lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voucher_activity);
		this.initView();
		socketUtil = new SocketUtils(this);
	}

	public void initView() {
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		textView_back2 = (TextView) findViewById(R.id.imageview_back2);

		imageview_back.setOnClickListener(this);
		textView_back2.setOnClickListener(this);
		
		dialog = new ProgressDialog(this);
		
		chaxunxiangmudaimaTv = (TextView) findViewById(R.id.chaxunxiangmudaima);
		mendianhaoTv = (TextView) findViewById(R.id.mendianhao);
		kehubianhaoTv = (TextView) findViewById(R.id.kehubianhao);
		yuangonghaohaoTv = (TextView) findViewById(R.id.yuangonghao);
		gongyingshangdaimaTv = (TextView) findViewById(R.id.gongyingshangdaima);
		xiangmudaimaTv = (TextView) findViewById(R.id.xiangmudaima);
		huopingdaimaTv = (TextView) findViewById(R.id.huopindaima);
		beiyongziduanTv1 = (TextView) findViewById(R.id.beiyongziduan1);
		beiyongziduanTv2 = (TextView) findViewById(R.id.beiyongziduan2);
		beiyongziduanTv3 = (TextView) findViewById(R.id.beiyongziduan3);
		
		sellStartTime = (EditText)findViewById(R.id.kaishiriqi);
		sellEndTime = (EditText)findViewById(R.id.jiesuriqi);
		
		commitBtn = (Button) findViewById(R.id.commit);
		

		commitBtn.setOnClickListener(this);
		
		sellStartTime.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        timeFlag = 0;
		        showDialog(0);
		    }
		});
		 
		sellEndTime.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        timeFlag = 1;
		        showDialog(1);
		    }          
		});
		 
		sellStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            timeFlag = 0;
		            hideIM(v);
		            showDialog(0); 
		        }
		    }
		});
		 
		sellEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus == true) {
		            timeFlag = 1;
		            hideIM(v);
		            showDialog(1); 
		        }
		    }
		});
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		mYear = year;
        String mm;
        String dd;
         
        if (monthOfYear <= 9) {
            mMonth = monthOfYear + 1;
            mm = "0" + mMonth;
        }
        else {
            mMonth = monthOfYear + 1;
            mm = String.valueOf(mMonth);
        }
         
        if (dayOfMonth <= 9) {
            mDay = dayOfMonth;
            dd = "0" + mDay;
        }
        else{
            mDay = dayOfMonth;
            dd = String.valueOf(mDay);
        }
         
	        mDay = dayOfMonth;
        sellStartTime.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
        sellEndTime.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		case R.id.commit:
			String sendString = "NeiBuCaXun_CX_";
			String chaxunxiangmudaima = chaxunxiangmudaimaTv.getText().toString();
			if (chaxunxiangmudaima == null || chaxunxiangmudaima.length() == 0)
				chaxunxiangmudaima = "0000";

			String mendianhao = mendianhaoTv.getText().toString();
			if (mendianhao == null || mendianhao.length() == 0)
				mendianhao = "@";

			String kehubianma = kehubianhaoTv.getText().toString();
			if (kehubianma == null || kehubianma.length() == 0)
				kehubianma = "@";

			String yuangonghao = yuangonghaohaoTv.getText().toString();
			if (yuangonghao == null || yuangonghao.length() == 0)
				yuangonghao = "@";

			String gongyingshangdaima = gongyingshangdaimaTv.getText().toString();
			if (gongyingshangdaima == null || gongyingshangdaima.length() == 0)
				gongyingshangdaima = "@";

			String xiangmudaima = xiangmudaimaTv.getText().toString();
			if (xiangmudaima == null || xiangmudaima.length() == 0)
				xiangmudaima = "@";

			String huopingdaima = huopingdaimaTv.getText().toString();
			if (huopingdaima == null || huopingdaima.length() == 0)
				huopingdaima = "@";

			String beuyongziduan1 = beiyongziduanTv1.getText().toString();
			if (beuyongziduan1 == null || beuyongziduan1.length() == 0)
				beuyongziduan1 = "@";

			String beuyongziduan2 = beiyongziduanTv2.getText().toString();
			if (beuyongziduan2 == null || beuyongziduan2.length() == 0)
				beuyongziduan2 = "@";

			String beuyongziduan3 = beiyongziduanTv3.getText().toString();
			if (beuyongziduan3 == null || beuyongziduan3.length() == 0)
				beuyongziduan3 = "@";

			String startTime = (sellStartTime.getText().toString()).replace("-", "");
			if ("".equals(startTime) || 0 == startTime.length())
				startTime = Utils.getStringToday2();
			
			String endTime = (sellEndTime.getText().toString()).replace("-", "");
			if ("".equals(endTime) || 0 == endTime.length())
				endTime = Utils.getStringToday2();
			
			AppContext appContext = (AppContext) getApplicationContext();
			sendString += chaxunxiangmudaima + "_" + mendianhao + "_" + kehubianma
					+ "_" + yuangonghao + "_" + gongyingshangdaima + "_"
					+ xiangmudaima + "_" + huopingdaima + "_" + beuyongziduan1
					+ "_" + beuyongziduan2 + "_" + beuyongziduan3 + "_" + startTime
					+ "_" + endTime + "_" + Utils.getStringToday() + "_"
					+ appContext.getSIME();
			
			final String temp = sendString;
//			if (bmanager != null) {
//				bmanager.start();
//				locationManager.requestLocationUpdates(new LocationListener() {
//
//					public void onLocationChanged(Location arg0) {
//						lat = arg0.getLatitude();
//						lon = arg0.getLongitude();
//						
//						String finalSendString = temp + "_" + lat + "_" + lon;
//						Log.d("test", finalSendString);
//						socketUtil.query(finalSendString, QueryActivity.this);
//					}
//				});
//			}
			String finalSendString = temp + "_" + "@" + "_" + "@";
			Log.d("test", finalSendString);
			socketUtil.query(finalSendString, QueryActivity.this);
			onCreateDialog();
			break;
		default:
			break;
		}
	}

	@Override
	void init() {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		socketUtil = null;
	}
	
	public void onCreateDialog() {// 显示网络连接Dialog
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());  
        View v = inflater  
                .inflate(R.layout.selfdef_progress_dialog_layout, null);  
//		dialog.setTitle("请稍候。。。");
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
//		dialog.setMessage("请稍候。。。");
		dialog.show();
		dialog.setContentView(v);
	}
	
//	 public ProgressDialog createProgressDialog(Context context, String msg) {  
//		    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());  
//	        View v = inflater  
//	                .inflate(R.layout.selfdef_progress_dialog_layout, null);  
//	        TextView txt = (TextView) v.findViewById(R.id.progress_dialog_tv);  
//	        ProgressDialog dialog = new ProgressDialog(context);  
//	        dialog.show();  
//	        dialog.setContentView(v);  
//	        if (msg != null && !msg.equals(""))  
//	            txt.setText(msg);  
//	          
//	        return dialog;  
//	    }  
	  

	
	@Override
	public void downLoadSuccess(String respone) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				File file = new File(SocketUtils.QUERY_XLS_FILE_PATH);
				Intent i = new Intent();
				i.setClass(QueryActivity.this, ExcelRead.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", file.getAbsolutePath());
				i.putExtras(bundle);
				startActivity(i);
			}
		});
	}

	@Override
	public void downLoadFail() {
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				
				Toast.makeText(QueryActivity.this, "查询失败", Toast.LENGTH_LONG)
						.show();
			}
		});
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	        mYear = year;
	        String mm;
	        String dd;
	         
	        if (monthOfYear <= 9) {
	            mMonth = monthOfYear + 1;
	            mm = "0" + mMonth;
	        }
	        else {
	            mMonth = monthOfYear + 1;
	            mm = String.valueOf(mMonth);
	        }
	         
	        if (dayOfMonth <= 9) {
	            mDay = dayOfMonth;
	            dd = "0" + mDay;
	        }
	        else{
	            mDay = dayOfMonth;
	            dd = String.valueOf(mDay);
	        }
	         
	        mDay = dayOfMonth;
	         
	        if (timeFlag == 0) {
	            sellStartTime.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
	        }
	        else {
	            sellEndTime.setText(String.valueOf(mYear) + "-" + mm + "-" + dd);
	        }
	    }
	};
	    
	protected Dialog onCreateDialog(int id) {
	    Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int monthOfYear = calendar.get(Calendar.MONTH);
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
	    switch (id) {  
	        case 0:
	            return new DatePickerDialog(this, mDateSetListener, year, monthOfYear, dayOfMonth);
	        case 1:
	            return new DatePickerDialog(this, mDateSetListener, year, monthOfYear, dayOfMonth);
	    }
	    return null;
	}
	 
	// 隐藏手机键盘
	private void hideIM(View edt){
	    try {
	        InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	        IBinder windowToken = edt.getWindowToken();
	         
	        if (windowToken != null) {
	            im.hideSoftInputFromWindow(windowToken, 0);
	        }
	    }
	    catch (Exception e) {
	    }
	}
	
	@Override
	public void timeOut() {
		// TODO Auto-generated method stub
		
	}
}
