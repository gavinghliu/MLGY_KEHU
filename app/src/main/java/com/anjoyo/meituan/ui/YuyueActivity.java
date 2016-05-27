package com.anjoyo.meituan.ui;

import java.io.File;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Calendar;

import com.anjoyo.meituan.Parser.ProductParser;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.Beautician;
import com.anjoyo.meituan.domain.Seller;
import com.anjoyo.meituan.utils.ExcelRead;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class YuyueActivity extends BaseActivity implements OnClickListener,SocketListener {

	private ImageView imageview_back;
	private TextView textView_back2;
	Spinner mendianhaoTv;
	
	private String mendianhaoString;
	
	Button commitBtn;
	
	public static String SP_DIZHI = "sp_dizhi";
	
	private static final String[] time ={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	private static final String[] minu ={"00","15","30","45"};
	
	private String[] dizhi;
	
	private String[] mendian;
	
	private Spinner spinner1;
	private Spinner spinner2;
	private TextView hourTextView;
	private TextView minuteTextView;
	
	
	private EditText sellStartTime;
	
	private int mYear, mMonth, mDay, timeFlag;

	private String mHour, mMinute;
	
	public ProgressDialog dialog;
	
	private SocketUtils socketUtil;
	private ArrayAdapter<String> adapter1;
	private ArrayAdapter<String> adapter2;
	private ArrayAdapter<String> adapter3;
	private ArrayAdapter<String> adapter4;
	// private final static String KEY = "OsvOOCUMoAKmBbHq_jMFCtxLD";
	private final static String KEY = "7D3157DA1B76B50A2FA0E9C9AFF13834D72D330B";
	private double lat;
	private double lon;
	
	private CheckBox rdShangmen;
	private EditText shangmenEt;
	private Spinner shangmenBtn;
	private LinearLayout shangmenRl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyue_activity);
		
		socketUtil = new SocketUtils(this);
		
		final AppContext appContext = (AppContext) getApplicationContext();
		String sDizhi = appContext.getPreferences().getString(SP_DIZHI, "");
		if (sDizhi.length() > 0) {
			sDizhi += "请选择地址&&";
		} else {
			sDizhi += "请选择地址";
		}
		
		dizhi = sDizhi.split("&&");
		
		String filePath = SocketUtils.QUERY_XLS_FILE_DIR
				+ "/mendian.txt";
		String mendianString = Utils.ReadTxtFile(filePath);
		ArrayList<Seller> merchant = Seller.parseSeller(mendianString);
		mendian= new String[merchant.size()];
		for (int i = 0; i < merchant.size(); i++) {
			mendian[i] = merchant.get(i).getSeller_name();
		}
		this.initView();
	}

	public void initView() {
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		textView_back2 = (TextView) findViewById(R.id.imageview_back2);

		imageview_back.setOnClickListener(this);
		textView_back2.setOnClickListener(this);
		
		dialog = new ProgressDialog(this);
		
		mendianhaoTv = (Spinner) findViewById(R.id.mendianhao);
		
		sellStartTime = (EditText)findViewById(R.id.kaishiriqi);
		
		commitBtn = (Button) findViewById(R.id.commit);
		
		 spinner1 = (Spinner) findViewById(R.id.Spinner01);
		 spinner2 = (Spinner) findViewById(R.id.Spinner02);
		commitBtn.setOnClickListener(this);
		
		shangmenEt = (EditText)findViewById(R.id.fuwudizhi);
		shangmenBtn = (Spinner)findViewById(R.id.selectdizi);
		rdShangmen = (CheckBox)findViewById(R.id.shangmenfuwu);
		shangmenRl = (LinearLayout)findViewById(R.id.ll_fuwudizhi);
		rdShangmen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton btn, boolean isCheck) {
				if (isCheck) {
					shangmenEt.setVisibility(View.VISIBLE);
					shangmenBtn.setVisibility(View.VISIBLE);
					shangmenRl.setVisibility(View.VISIBLE);
				} else {
					shangmenEt.setVisibility(View.GONE);
					shangmenBtn.setVisibility(View.GONE);
					shangmenRl.setVisibility(View.GONE);
				}
			}
		});
		
		 //将可选内容与ArrayAdapter连接起来
        adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,dizhi);
         
        //设置下拉列表的风格
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        shangmenBtn.setAdapter(adapter4);
         
        //添加事件Spinner事件监听  
        shangmenBtn.setOnItemSelectedListener(new SpinnerSelectedListener4());
		
		 //将可选内容与ArrayAdapter连接起来
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time);
         
        //设置下拉列表的风格
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        spinner1.setAdapter(adapter1);
         
        //添加事件Spinner事件监听  
        spinner1.setOnItemSelectedListener(new SpinnerSelectedListener());
         
        //设置默认值
        spinner1.setVisibility(View.VISIBLE);
        
        //将可选内容与ArrayAdapter连接起来
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,minu);
         
        //设置下拉列表的风格
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        spinner2.setAdapter(adapter2);
         
        //添加事件Spinner事件监听  
        spinner2.setOnItemSelectedListener(new SpinnerSelectedListener2());
         
        //设置默认值
        spinner2.setVisibility(View.VISIBLE);
        
        //将可选内容与ArrayAdapter连接起来
        adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mendian);
         
        //设置下拉列表的风格
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        //将adapter 添加到spinner中
        mendianhaoTv.setAdapter(adapter3);
         
        //添加事件Spinner事件监听  
        mendianhaoTv.setOnItemSelectedListener(new SpinnerSelectedListener3());
         
        //设置默认值
        mendianhaoTv.setVisibility(View.VISIBLE);
		
		sellStartTime.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        timeFlag = 0;
		        showDialog(0);
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
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;
		case R.id.commit:
			if (rdShangmen.isChecked()) {
				if (shangmenEt.getText().toString().trim().length() < 0) {
					Toast.makeText(this, "上门地址不能为空~", Toast.LENGTH_SHORT).show();
				} else {
					boolean needAdd = true;
					final AppContext appContext = (AppContext) getApplicationContext();
					String s = shangmenEt.getText().toString().trim();
					for (int i = 0; i < dizhi.length; i++) {
						if (s.equals(dizhi[i])) {
							needAdd = false;
							break;
						}
					}
					
					if (needAdd) {
						String sDizhi = appContext.getPreferences().getString(SP_DIZHI, "") + "&&" + s;
						appContext.getPreferences().edit().putString(SP_DIZHI, sDizhi).commit();
					}
				}
			}
			
			onCreateDialog();
			
			String qianzhui = "khyy_";
			final AppContext appContext = (AppContext) getApplicationContext();

//			808462khyy+手机号+手机码+年+月+日+时间+分店代码
//			khyy+手机号+手机码+年+月+日+时间+分店代码+是否上门+上门地址  2015_9_5_18:30_东升店_是_中山路
			String requestString = qianzhui + appContext.getUser().getUsername()
					+ "_" + appContext.getSIME() + "_" + mYear + "_" + mMonth + "_" + mDay + "_" + ((Integer.parseInt(mHour) < 10) ? ("0" + mHour) : mHour) + ":" + mMinute + "_" + mendianhaoString + "_" + (rdShangmen.isChecked() ? "是" : "否") + "_" + (rdShangmen.isChecked() ? shangmenEt.getText().toString() : "");
			socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString,
					SocketUtils.ZHUCHE_REQUEST2 + qianzhui + appContext.getUser().getUsername(),
					new SocketListener() {

						@Override
						public void downLoadSuccess(final String respone) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Log.d("yueyue", respone);
											if (respone.contains("保存预约信息成功")) {
												finish();
											}
											Toast.makeText(YuyueActivity.this, respone, Toast.LENGTH_SHORT).show();
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
							Toast.makeText(YuyueActivity.this, "网络请求超时",
									Toast.LENGTH_SHORT).show();
							
						}
					});
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
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
		dialog.show();
		dialog.setContentView(v);
	}
	
	//使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	mHour = time[arg2];
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    
    class SpinnerSelectedListener2 implements OnItemSelectedListener{
    	 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	mMinute = minu[arg2];
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    
    class SpinnerSelectedListener3 implements OnItemSelectedListener{
   	 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	mendianhaoString = mendian[arg2];
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    
    
  //使用数组形式操作
    class SpinnerSelectedListener4 implements OnItemSelectedListener{
 
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	if (arg2 > 0) {
        		shangmenEt.setText(dizhi[arg2]);
			}
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
	
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
				i.setClass(YuyueActivity.this, ExcelRead.class);
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
				
				Toast.makeText(YuyueActivity.this, "查询失败", Toast.LENGTH_LONG)
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
//	        	return new TimePickerDialog(this, mTimeSetListener, hourOfDay, minute, true);
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
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		Toast.makeText(YuyueActivity .this, "网络请求超时",
				Toast.LENGTH_SHORT).show();
		
	}
}
