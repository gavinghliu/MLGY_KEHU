package com.anjoyo.meituan.ui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Random;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.utils.FileUtil;
import com.anjoyo.meituan.utils.FtpUtils;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.meituan.utils.Utils;
import com.anjoyo.mlgy.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import me.storm.volley.data.RequestManager;

public class WelcomeActivity extends BaseActivity {
	/** Called when the activity is first created. */

	public ProgressDialog dialog;
	private int iCount;
	double fileSize = 2.4 * 1000 * 1024;
	protected static final int STOP = 0x10000;
	protected static final int NEXT = 0x10001;
	ProgressBar progressBar;
	Intent intent;
	View v;
	String sime;
	private NetworkImageView mImageView;

	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
	public static final String IMAGES_FOLDER = SDCARD_PATH + File.separator + "demo" + File.separator + "images"
			+ File.separator;
	public static final String LONGIN_TIME = "login_time";
	AppContext appContext;
	private static final int CONFIG_DOWN_SUC = 100;
	private static final int CONFIG_DOWN_FAIL = 101;
	private static final int APP_DOWN_SUC = 102;
	private static final int APP_DOWN_FAIL = 103;
	static public String FTP_CONFIG = "http://www.palaceb.com/Data/%s/kehuapp/config.txt";
	// static public String FTP_CONFIG = "//Data/kehuapp/%s/config.txt";
	static public String FTP_ADDRESS = "//Data/%s/kehuapp/updateApp.apk";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		dialog = new ProgressDialog(this);
		appContext = (AppContext) getApplicationContext();
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		appContext.setPreferences(preferences);
		sime = appContext.getSIME();

		FTP_CONFIG = FTP_CONFIG.replace("%s", AppContext.getConfigration(WelcomeActivity.this, "wenjianjia"));
		FTP_ADDRESS = FTP_ADDRESS.replace("%s", AppContext.getConfigration(WelcomeActivity.this, "wenjianjia"));
		AppContext.version = AppContext.getConfigration(WelcomeActivity.this, "version");

		if (null == ((AppContext) getApplicationContext()).getUser()) {
			intent = new Intent(WelcomeActivity.this, LoginActivity.class);
		} else {
			long loginTime = preferences.getLong(LONGIN_TIME, System.currentTimeMillis());
			if (System.currentTimeMillis() - loginTime > 30 * 60 * 1000) {
				appContext.setUser(null);
				intent = new Intent(WelcomeActivity.this, LoginActivity.class);
			} else {
				intent = new Intent(WelcomeActivity.this, MainActivity.class);
				preferences.edit().putLong(LONGIN_TIME, System.currentTimeMillis()).commit();
			}
		}

		// TODO:
		intent = new Intent(WelcomeActivity.this, MainActivity.class);
		// 系统会为需要启动的activity寻找与当前activity不同的task;
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mImageView = (NetworkImageView) findViewById(R.id.splash_img);
		// mImageView2 = (NetworkImageView)findViewById(R.id.splash_img2);

		new Thread(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				try {
					URL url = new URL(FTP_CONFIG);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					// 取得inputStream，并进行读取
					InputStream input = conn.getInputStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(input));
					String line = null;
					StringBuffer sb = new StringBuffer();
					while ((line = in.readLine()) != null) {
						sb.append(line);
					}
					System.out.println(sb.toString());
					String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/config.txt";
					File file = new File(filePath);
					if (file.exists()) {
						file.delete();
					}
					FileUtil.stringToFile(sb.toString(), file);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(CONFIG_DOWN_SUC);
			}
		}).start();

		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		v = inflater.inflate(R.layout.selfdef_welcome_progress_dialog_layout, null);
		progressBar = (ProgressBar) v.findViewById(R.id.rectangleProgressBar);
		progressBar.setIndeterminate(false);
		progressBar.setMax((int) (2.4 * 1000 * 1024));
		progressBar.setProgress(0);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
	}

	@Override
	void init() {
	}

	// 获取端口
	public void queryPort(final SocketListener listener) {
		new Thread() {
			public void run() {
				DataOutputStream socketOut = null;
				DataInputStream inPutStream = null;
				Socket socket = null;
				byte[] buf = null;
				try {
					// 连接Socket
					socket = new Socket(AppContext.getServerIP(), SocketUtils.SERVICE_PORT);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = getQueryPortString().getBytes("GB2312");
					socketOut.write(responseBuffer, 0, responseBuffer.length);

					// 1. 读取Socket的输入流
					inPutStream = new DataInputStream(socket.getInputStream());
					int bufferSize = 1024;
					buf = new byte[bufferSize];
					int b;
					// 顺序读取文件text里的内容并赋值给整型变量b,直到文件结束为止。
					StringBuffer sb = new StringBuffer();
					/* 开始循环读取PC端发送过来的数据 */
					int readCount = inPutStream.read(buf);
					System.out.println("readCount=" + readCount);
					for (int i = 0; i < readCount; i++) {
						b = buf[i];
						sb.append((char) b);
					}
					int downPort = Integer.parseInt(sb.toString());
					listener.downLoadSuccess(downPort + "");
				} catch (Exception e) {
					e.printStackTrace();
					listener.downLoadFail();
				} finally {
					try {
						buf = null;
						inPutStream.close();
						socket.close();
					} catch (Exception e) {
						listener.downLoadFail();
					}
				}
			};
		}.start();
	}

	private String getQueryPortString() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int day = t.monthDay;

		String monthString = (month > 9 ? "" : "0") + month;
		String dayString = (day < 10 ? "0" : "") + day;
		Random random = new Random();

		String randomNumber = Math.abs(random.nextInt()) % 10 + "";

		// ***mlgy**yyyy*mm**dd**app 一共长25位字符，*是随机字符或数字，yyyymmdd当日的年 月日
		String queryString = randomNumber + randomNumber + randomNumber + "mlgy" + randomNumber + randomNumber + year
				+ randomNumber + monthString + randomNumber + randomNumber + dayString + randomNumber + randomNumber
				+ "appKH";

		return queryString;
	}

	public void onCreateDialog() {
		new AlertDialog.Builder(WelcomeActivity.this).setTitle("软件更新").setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("是否下载更新软件？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface mdialog, int which) {
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
								progressBar.setMax(10000000);
								FtpUtils.ftpconnectDownload("app.apk", FTP_ADDRESS, new AppTransferListener());

							}
						}).start();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(3000);
									// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
									getApplicationContext().startActivity(intent);
									finish();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}).show();
	}

	// 定义一个Handler
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
			case CONFIG_DOWN_SUC:
				String filePath = SocketUtils.QUERY_XLS_FILE_DIR + "/config.txt";
				String json = Utils.ReadTxtFile(filePath);

				if (null != json) {
					String[] array = json.replace("\n", "").trim().split(",");
					String[] vesionArray = array[0].split("::");
					String[] ipArray = array[1].split("::");
					String version = vesionArray[1];
					String ip = ipArray[1];

					FtpUtils.SUB_FTP = array[2].split("::")[1];
					FtpUtils.SUB_FTP_ACOUNT = array[3].split("::")[1];
					FtpUtils.SUB_FTP_PWD = array[4].split("::")[1];
					FtpUtils.SPLASH_URL = array[5].split("::")[1];

					if (null != ip && ip.length() > 0) {
						AppContext.setServerIP(ip);
					} else {
						Toast.makeText(WelcomeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
						return;
					}

					ImageLoader imageLoader = RequestManager.getImageLoader();
					mImageView.setImageUrl(FtpUtils.SPLASH_URL, imageLoader);
					WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
					DisplayMetrics dm = new DisplayMetrics();
					manager.getDefaultDisplay().getMetrics(dm);
					int width = dm.widthPixels;
					float scale = (float) ((float) width / 1200.0);
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, (int) (scale * 1600.0));
					mImageView.setLayoutParams(lp);
					new Thread(new Runnable() {

						@Override
						public void run() {
							FtpUtils.ftpconnectDownload("zhazhi.txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Wenzhang/zhazhi.txt",
									null);
							
							FtpUtils.ftpconnectDownload("zhazhi_" + appContext.getSIME() + ".txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Wenzhang/zhazhi_"+  appContext.getSIME() + ".txt",
									null);
							
							FtpUtils.ftpconnectDownload("tongzhi.txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Wenzhang/tongzhi.txt",
									null);
							
							FtpUtils.ftpconnectDownload("tongzhi_" + appContext.getSIME() + ".txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Wenzhang/tongzhi_"+  appContext.getSIME() + ".txt",
									null);
							
							FtpUtils.ftpconnectDownload("product.txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Product/product.txt",
									null);
							FtpUtils.ftpconnectDownload("ad.txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Wenzhang/ad.txt",
									null);
							FtpUtils.ftpconnectDownload("service.txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Service/service.txt",
									null);
							FtpUtils.ftpconnectDownload("mendian.txt",
									"//Data/" + AppContext.getConfigration(WelcomeActivity.this, "wenjianjia")
											+ "/kehuapp/Mendian/mendian.txt",
									null);
						}
					}).start();

					queryPort(new SocketListener() {

						@Override
						public void downLoadSuccess(String respone) {
							SocketUtils.CURRENT_SERVICE_PORT = Integer.parseInt(respone.trim());
						}

						@Override
						public void downLoadFail() {
						}

						@Override
						public void timeOut() {
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
						}
					});

					if (Float.parseFloat(version) > Float.parseFloat(AppContext.getVersion())) {
						onCreateDialog();
					} else {
						// 创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(4000);
									// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
									getApplicationContext().startActivity(intent);
									finish();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}
				break;
			case CONFIG_DOWN_FAIL:
				// 创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(4000);
							// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
							getApplicationContext().startActivity(intent);
							finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				break;
			case APP_DOWN_FAIL:
				// 创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(4000);
							// 获取应用的上下文，生命周期是整个应用，应用结束才会结束
							getApplicationContext().startActivity(intent);
							finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				break;
			case APP_DOWN_SUC:
				File file = new File(SocketUtils.QUERY_XLS_FILE_DIR + "/app.apk");
				if (null != file && file.exists()) {
					Intent apkIntent = new Intent();
					apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					apkIntent.setAction(android.content.Intent.ACTION_VIEW);
					apkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
					startActivity(apkIntent);
				}

				startActivity(intent);
				finish();

				break;
			}
		}
	};

	public class MyTransferListener implements FTPDataTransferListener {

		// 传输放弃时触发
		@Override
		public void aborted() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_FAIL);
		}

		// 文件传输完成时，触发
		@Override
		public void completed() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_SUC);
		}

		// 传输失败时触发
		@Override
		public void failed() {
			mHandler.sendEmptyMessage(CONFIG_DOWN_FAIL);
		}

		// 文件开始上传或下载时触发
		@Override
		public void started() {
		}

		// 显示已经传输的字节数
		@Override
		public void transferred(int arg0) {// 以下用于显示进度的
		}
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
}