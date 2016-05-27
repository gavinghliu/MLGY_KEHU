package com.anjoyo.meituan.ui;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.utils.FtpUtils;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

public class MoreActivity extends BaseActivity implements OnClickListener {
	private Button button_aboutmeituan;
	private LinearLayout /*linearlayout_picturemode,linearlayout_emptybuffer,*/linearlayout_checkupdate;
	private TextView currenVersion;
	private LinearLayout gongjuLayout;
//	private LinearLayout moreAppLayout;
	private Button gongjuBtn;
//	private Button moreAppBtn;
	private int iCount;
	View v;
	ProgressBar progressBar;
	private static final int APP_DOWN_SUC = 102;
	private static final int APP_DOWN_FAIL = 103;
	protected static final int STOP = 0x10000;
	protected static final int NEXT = 0x10001;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	void init() {
		setContentView(R.layout.more_activity);
		button_aboutmeituan = (Button) findViewById(R.id.button_meituan);
		linearlayout_checkupdate = (LinearLayout) findViewById(R.id.linearlayout_inspectupdate);
		currenVersion = (TextView)findViewById(R.id.textview_versions);

		button_aboutmeituan.setOnClickListener(this);
		linearlayout_checkupdate.setOnClickListener(this);
		
		gongjuLayout = (LinearLayout) findViewById(R.id.linearlayout_gongju);
		gongjuLayout.setOnClickListener(this);
//		moreAppLayout.setOnClickListener(this);
		
		gongjuBtn = (Button) findViewById(R.id.button_gongju);
//		moreAppBtn.setOnClickListener(this);
		gongjuBtn.setOnClickListener(this);
		gongjuLayout.setVisibility(View.GONE);
		
		currenVersion.setText("V" + AppContext.getVersion() + ".0");
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		v = inflater.inflate(
				R.layout.selfdef_welcome_progress_dialog_layout, null);
		progressBar = (ProgressBar) v.findViewById(R.id.rectangleProgressBar);
		progressBar.setIndeterminate(false);
		progressBar.setMax((int) (2.4 * 1000 * 1024));
		progressBar.setProgress(0);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
		
		AppContext appContext = (AppContext) getApplicationContext();
		gongjuLayout.setVisibility(View.GONE);
//		if (null != appContext.getUser() && 1 == appContext.getUser().getType()) {
			gongjuLayout.setVisibility(View.VISIBLE);
//		}
	}

	int i = 1;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_meituan:
			startActivity(new Intent(this, AboutMeiTuanActivity.class));
			break;
		case R.id.linearlayout_inspectupdate:
			break;
		case R.id.linearlayout_gongju:
		case R.id.button_gongju:
			Intent intent = new Intent();
			intent.setComponent(new ComponentName("com.example.signin",
					"baidulocationsdk.demo.MainActivity"));
			intent.setAction(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				onCreateDialog();
			}
			break;
		default:
			break;
		}

	}

	
	public void onCreateDialog() {
		new AlertDialog.Builder(MoreActivity.this).setTitle("安装工具").setIcon(
			android.R.drawable.ic_dialog_info).setMessage("是否下载工具软件？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface mdialog, int which) {

					if (1 == Utils.getConnectedType(MoreActivity.this)) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									public void run() {
										dialog.show();
										dialog.setContentView(v);
										dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
										LayoutParams.WRAP_CONTENT);
									}
								});
								iCount = 0;
								progressBar.setMax(3000000);
								FtpUtils.ftpconnectDownload("gongjuapp.apk",
										"//Data/" + AppContext.getConfigration(MoreActivity.this, "wenjianjia") + "/Gongju/gongju.apk",
										new AppTransferListener());
								
							}
						}).start();
					} else {
						AlertDialog.Builder builder = new Builder(MoreActivity.this);
						builder.setMessage("当前不是wifi环境,是否继续？");
						builder.setTitle("提示");
						builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog2, int which) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										runOnUiThread(new Runnable() {
											public void run() {
												dialog.show();
												dialog.setContentView(v);
												dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
												LayoutParams.WRAP_CONTENT);
											}
										});
										iCount = 0;
										progressBar.setMax(3000000);
										FtpUtils.ftpconnectDownload("gongjuapp.apk",
												 "//Data/" + AppContext.getConfigration(MoreActivity.this, "wenjianjia") +"/Gongju/gongju.apk",
												new AppTransferListener());
										
									}
								}).start();
							}
						});
						builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
	}
	
	public class AppTransferListener implements FTPDataTransferListener {
		long downSize = 0L;
		double speed = 0.0F;
		long percent = 0L;

		// 传输放弃时触发
		@Override
		public void aborted() {
			mHandler.sendEmptyMessage(APP_DOWN_FAIL);
		}

		// 文件传输完成时，触发
		@Override
		public void completed() {
			mHandler.sendEmptyMessage(APP_DOWN_SUC);
		}

		// 传输失败时触发
		@Override
		public void failed() {
			mHandler.sendEmptyMessage(APP_DOWN_FAIL);
		}

		// 文件开始上传或下载时触发
		@Override
		public void started() {
		}

		// 显示已经传输的字节数
		@Override
		public void transferred(int arg0) {// 以下用于显示进度的
			iCount += arg0;
			Message msg = new Message();
			msg.what = NEXT;
			mHandler.sendMessage(msg);
		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP:
				progressBar.setVisibility(View.GONE);
				Thread.currentThread().interrupt();
				break;
			case NEXT:
				if (!Thread.currentThread().isInterrupted()) {
					progressBar.setProgress(iCount);
				}
				break;
			case APP_DOWN_FAIL:
				progressBar.setVisibility(View.GONE);
				dialog.dismiss();
				break;
			case APP_DOWN_SUC:
				dialog.dismiss();
				progressBar.setVisibility(View.GONE);
				File file = new File(SocketUtils.QUERY_XLS_FILE_DIR + "/gongjuapp.apk");
				if (null != file && file.exists()) {
					Intent apkIntent = new Intent(); 
		            apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            apkIntent.setAction(android.content.Intent.ACTION_VIEW);
		            apkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		            startActivity(apkIntent);
				}
				break;
			}
		}
	};
}
