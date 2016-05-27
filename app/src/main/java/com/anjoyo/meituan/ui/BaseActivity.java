package com.anjoyo.meituan.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.anjoyo.meituan.common.NetRequestConstant;
import com.anjoyo.meituan.http.ThreadPool;
import com.anjoyo.meituan.interfaces.Netcallback;
import com.anjoyo.meituan.utils.NetUtil;
import com.anjoyo.mlgy.R;

/**
 * 如果要请求网络的话，就继承BaseActivity，其余的不用
 * @author hp
 */
public abstract class BaseActivity extends Activity {
	
	private NetRequestConstant nrc;
	private Handler handler;
	public static final int SUCCESS = 10001; 
	public static final int FAIL = 10002; 
	public static final int ERROR = 10003; 
	public ProgressDialog dialog;

	abstract void init();
	
	public enum HttpRequestType{
		GET,POST;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new ProgressDialog(this);
		this.init();
	}
	
	class RunnableTask implements Runnable{
		
		private NetRequestConstant nrc;
		private Handler handler;
		
		public RunnableTask(NetRequestConstant nrc , Handler handler) {
			this.nrc = nrc;
			this.handler = handler;
		}

		
		public void run() {
			String res=null;
            if(NetUtil.isCheckNet(getApplicationContext())){
            	if(nrc.getType()==HttpRequestType.POST){//Post请求
            		res = NetUtil.httpPost(nrc);
				}else if(nrc.getType()==HttpRequestType.GET){//get请求
				    res = NetUtil.httpGet(nrc);
				}
				Message message = Message.obtain();
				message.obj = res;
				message.what = SUCCESS;
				handler.sendMessage(message);
				
			}else{
				Message message = Message.obtain();
				message.what = ERROR;
				handler.sendMessage(message);
			}
		}
	}
	
	class BaseHandler extends Handler{

		private Netcallback callBack;
		
		
		public BaseHandler(Netcallback callBack) {
			this.callBack = callBack;
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch (msg.what) {
			case SUCCESS:// 网络请求成功后回调
				callBack.preccess(msg.obj, true);
				
				break;
				
			case FAIL:// 网络请求失败后回调
				
			case ERROR:
				callBack.preccess(msg.obj, false);
				break;

			default:
				break;
			}
			
			super.handleMessage(msg);
		}
		
	}
	
	protected void getServer(Netcallback callBack ,NetRequestConstant nrc){
		
		Handler handler = new BaseHandler(callBack);
		RunnableTask task = new RunnableTask(nrc, handler); 
		ThreadPool.getInstance().addTask(task);
		
	}
	
	public void onCreateDialog() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View v = inflater
				.inflate(R.layout.selfdef_progress_dialog_layout, null);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
		dialog.show();
		dialog.setContentView(v);
	}
}
