package com.anjoyo.meituan.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.anjoyo.meituan.app.AppContext;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

public class SocketUtils {

	// 传给Socket服务器端的上传和下载标志
	public static int SERVICE_PORT = 4455;
	public static String SERVICE_IP = "115.28.85.63";
	
	public static int CURRENT_SERVICE_PORT;
	public static String MINGLING_GENFILE = "908462";
	public static String MINGLING_DOWNLOAD = "908431";
	public static String QUERY_XLS_FILE_DIR = Environment
			.getExternalStorageDirectory() + "/MLGY";
	public static String QUERY_XLS_FILE_PATH = QUERY_XLS_FILE_DIR + "/test.xls";
	public static String QUERY_TXT_FILE_PATH = QUERY_XLS_FILE_DIR + "/test.txt";
	
	//注册 、登录
	public static String ZHUCHE_REQUEST = "808462";
	public static String ZHUCHE_REQUEST2 = "808431";
	public static String ZHUCHE_REQUEST3 = "808432";
	
	public Context mContext;
	
	boolean socketWorking;
	
	SocketListener curListener;
	
	Handler handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	    	curListener.timeOut();
	    	socketWorking = false;
	        super.handleMessage(msg);
	    }
	};
	
	private TimerTask task = new TimerTask() {
	    @Override
	    public void run() {
	        Message message = new Message();
	        message.what = 1;
	        handler.sendMessage(message);
	    }
	}; 
	
	

	public SocketUtils(Context ct) {
		this.mContext = ct;
	}

	private String getQueryPortString() {
		AppContext appContext = (AppContext) mContext.getApplicationContext();

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
		String queryString = randomNumber + randomNumber + randomNumber
				+ "mlgy" + randomNumber + randomNumber + year + randomNumber
				+ monthString + randomNumber + randomNumber + dayString
				+ randomNumber + randomNumber + "appKH";

		return queryString;
	}

	public void sendRequest(final String firstRequestString, final String secondRequestString, final SocketListener listener) {
		new Thread() {
			public void run() {
				try {
					if (firstRequestString.length() > 0)
						sendQueryFirst(AppContext.getServerIP(), CURRENT_SERVICE_PORT, firstRequestString, listener);
					Thread.sleep(3000);

					request(AppContext.getServerIP(), CURRENT_SERVICE_PORT, secondRequestString, listener);
				} catch (Exception e) {
					e.printStackTrace();
					listener.downLoadFail();
				} 
			};
		}.start();
	}
	
	public void sendRequest(final String firstRequestString, final SocketListener listener) {
		new Thread() {
			public void run() {
				try {
					if (firstRequestString.length() > 0)
						sendQueryFirst(AppContext.getServerIP(), CURRENT_SERVICE_PORT, firstRequestString, listener);
				} catch (Exception e) {
					e.printStackTrace();
					listener.downLoadFail();
				} 
			};
		}.start();
	}
	
	public void sendRequestForTxtFile(final String firstRequestString, final String secondRequestString, final SocketListener listener) {
		new Thread() {
			public void run() {
				try {
					if (firstRequestString.length() > 0)
						sendQueryFirst(AppContext.getServerIP(), CURRENT_SERVICE_PORT, firstRequestString, listener);
					Thread.sleep(3000);

					sendQueryForTxt(AppContext.getServerIP(), CURRENT_SERVICE_PORT, secondRequestString, listener);
				} catch (Exception e) {
					e.printStackTrace();
					listener.downLoadFail();
				} 
			};
		}.start();
	}
	
	
	private void request(final String ip, final int port, final String requestString, final SocketListener listener) {
		if (requestString.length() <= 0)
			return;
		
		curListener = listener;
//		timer.sch(task, 3000);
		new Thread() {
			public void run() {
				Log.i("Socket", "requestString=" + requestString);
				DataOutputStream socketOut = null;
				BufferedReader inPutStream;
				Socket socket = null;
				try {
					// 连接Socket
					socket = new Socket(ip, port);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = requestString.getBytes("GB2312");
					socketOut.write(responseBuffer, 0, responseBuffer.length);
					

					// 1. 读取Socket的输入流
					inPutStream =  new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
					
					int bufferSize = 1024 * 400;
					char[] buf2 = new char[bufferSize];
					
					inPutStream.read(buf2);
					int count = 0;
					for (int i = 0; i < buf2.length; i++) {
						if (buf2[i] == '\000') {
							break;
						}
						count++;
					}
					char[] buf3 = new char[count];
					for (int i = 0; i < buf3.length; i++) {
						buf3[i] = buf2[i];
					}
					
					String sb = new String(buf3);
					Log.i("Socket", "respone=" + sb.toString());
					listener.downLoadSuccess(sb.toString());
					
				} catch (Exception e) {
					listener.downLoadFail();
					return;
				} finally {
					try {
						socket.close();
					} catch (Exception e) {
						listener.downLoadFail();
					}
				}
			};
		}.start();
	}
	
	public void query(final String requestString, final SocketListener listener) {
//		timer.
		curListener = listener;
//		timer.schedule(task, 3000);
		new Thread() {
			public void run() {
				DataOutputStream socketOut = null;
				DataInputStream inPutStream = null;
				Socket socket = null;
				byte[] buf = null;
				try {
					// 连接Socket
					socket = new Socket(AppContext.getServerIP(), SERVICE_PORT);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = getQueryPortString().getBytes(
							"GB2312");
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

					String firstRequestString = MINGLING_GENFILE + requestString;
					if (requestString.length() > 0)
						sendQueryFirst(AppContext.getServerIP(), downPort, firstRequestString, listener);
					Thread.sleep(3000);

					String secondRequestString = MINGLING_DOWNLOAD + requestString;
					sendQuerySecond(AppContext.getServerIP(), downPort, secondRequestString, listener);
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

	private void sendQueryFirst(final String ip, final int port,
			final String requestString, final SocketListener listener) {
		if (requestString.length() <= 0)
			return;
		
		new Thread() {
			public void run() {
				DataOutputStream socketOut = null;
				Socket socket = null;
				try {
					// 连接Socket
					socket = new Socket(ip, port);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = requestString.getBytes("GB2312");
					Log.d("Socket", "sendQueryFirst__" + requestString);
					socketOut.write(responseBuffer, 0, responseBuffer.length);
				} catch (Exception e) {
					if (listener != null) {
						listener.downLoadFail();
					}
					
					return;
				} finally {
					try {
						socket.close();
					} catch (Exception e) {
						if (listener != null) {
							listener.downLoadFail();
						}
					}
				}
			};
		}.start();
	}
	
	private void sendQueryForTxt(final String ip, final int port, final String requestString, final SocketListener listener) {
		if (requestString.length() <= 0)
			return;
		
		curListener = listener;
//		timer.schedule(task, 3000);
		new Thread() {
			public void run() {
				Log.i("Socket", "sendQueryForTxt__" + requestString);
				DataOutputStream socketOut = null;
				DataInputStream inPutStream = null;
				Socket socket = null;
				byte[] buf = null;
				try {
					// 连接Socket
					socket = new Socket(ip, port);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = requestString.getBytes("GB2312");
					socketOut.write(responseBuffer, 0, responseBuffer.length);
					
					
					// 本地保存路径，文件名会自动从服务器端继承而来。
					int bufferSize = 1024;
					buf = new byte[bufferSize];
					
					// 1. 读取Socket的输入流
					inPutStream = new DataInputStream(socket.getInputStream());
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED))
						return;
					
					// 下载文件
					File downloadDir = new File(QUERY_XLS_FILE_DIR); // 用于保存文件的文件夹
					if (!downloadDir.exists()) {
						downloadDir.mkdirs();
					}
					File queryFile = new File(QUERY_TXT_FILE_PATH);
					if (queryFile.exists()) {
						queryFile.delete();
					}
					queryFile.createNewFile();
					
					DataOutputStream fileOut = new DataOutputStream(
							new BufferedOutputStream(new BufferedOutputStream(
									new FileOutputStream(queryFile))));
		            int length;
		            String result = "";
		            while((length = inPutStream.read(buf, 0, buf.length))>0){
		            	Log.d("test", "readCount=" + length + "____buf=" + new String(buf, 0, length));
		            	result += new String(buf, 0, length);
		            	fileOut.write(buf,0,length);
		            	if (new String(buf, 0, length).equals("没有查询记录!") || new String(buf, 0, length).contains("}]}")) {
		            		break;
						}
//		            	if (length < 1024) {
//							break;
//						}
		            	fileOut.flush();
		            }
		            
		            
//		            FileUtil.bytesToFile(result.getBytes(), queryFile);;
//					listener.downLoadSuccess(FileUtil.ReadTxtFile(queryFile.getAbsolutePath()));
					listener.downLoadSuccess(result);
					fileOut.close();
				} catch (Exception e) {
					listener.downLoadFail();
					return;
				} finally {
					try {
						// 善后处理
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

	public String convertCodeAndGetText(String str_filepath) {// 转码

        File file = new File(str_filepath);
        BufferedReader reader;
        String text = "";
        try {
                // FileReader f_reader = new FileReader(file);
                // BufferedReader reader = new BufferedReader(f_reader);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(fis);
                in.mark(4);
                byte[] first3bytes = new byte[3];
                in.read(first3bytes);//找到文档的前三个字节并自动判断文档类型。
                in.reset();
                if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                                && first3bytes[2] == (byte) 0xBF) {// utf-8

                        reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

                } else if (first3bytes[0] == (byte) 0xFF
                                && first3bytes[1] == (byte) 0xFE) {

                        reader = new BufferedReader(
                                        new InputStreamReader(in, "unicode"));
                } else if (first3bytes[0] == (byte) 0xFE
                                && first3bytes[1] == (byte) 0xFF) {

                        reader = new BufferedReader(new InputStreamReader(in,
                                        "utf-16be"));
                } else if (first3bytes[0] == (byte) 0xFF
                                && first3bytes[1] == (byte) 0xFF) {

                        reader = new BufferedReader(new InputStreamReader(in,
                                        "utf-16le"));
                } else {

                        reader = new BufferedReader(new InputStreamReader(in, "GBK"));
                }
                String str = reader.readLine();

                while (str != null) {
                        text = text + str + "\n";
                        str = reader.readLine();

                }
                reader.close();

        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return text;
}
	
	private void sendQuerySecond(final String ip, final int port, final String requestString, final SocketListener listener) {
		if (requestString.length() <= 0)
			return;
		
		curListener = listener;
//		timer.schedule(task, 3000);
		new Thread() {
			public void run() {
				Log.i("test", "sendQuerySecond__" + requestString);
				DataOutputStream socketOut = null;
				DataInputStream inPutStream = null;
				Socket socket = null;
				byte[] buf = null;
				try {
					// 连接Socket
					socket = new Socket(ip, port);
					// 向服务端发送请求及数据
					socketOut = new DataOutputStream(socket.getOutputStream());
					byte[] responseBuffer = requestString.getBytes("GB2312");
					socketOut.write(responseBuffer, 0, responseBuffer.length);

					// 本地保存路径，文件名会自动从服务器端继承而来。
					int bufferSize = 1024;
					buf = new byte[bufferSize];
					// 1. 读取Socket的输入流
					inPutStream = new DataInputStream(socket.getInputStream());
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED))
						return;
					
					// 下载文件
					File downloadDir = new File(QUERY_XLS_FILE_DIR); // 用于保存文件的文件夹
					if (!downloadDir.exists()) {
						downloadDir.mkdirs();
					}
					File queryFile = new File(QUERY_XLS_FILE_PATH);
					if (queryFile.exists()) {
						queryFile.delete();
					}
					queryFile.createNewFile();

					DataOutputStream fileOut = new DataOutputStream(
							new BufferedOutputStream(new BufferedOutputStream(
									new FileOutputStream(queryFile))));
					/* 开始循环读取PC端发送过来的数据 */
					while (true) {
						int readCount = inPutStream.read(buf);
						Log.d("test", "readCount=" + readCount + "____buf=" + new String(buf, 0, readCount));
						/* 由于Java的Socket是阻塞式的,所以最后需要发送确认信息让循环退出 */
						if (new String(buf, 0, readCount).equals("End!") || new String(buf, 0, readCount).contains("End!"))
							break;
						fileOut.write(buf, 0, readCount);
						fileOut.flush();
					}
					listener.downLoadSuccess(null);
					fileOut.close();
				} catch (Exception e) {
					listener.downLoadFail();
					return;
				} finally {
					try {
						// 善后处理
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
	
	public interface SocketListener {
		public void downLoadSuccess(String respone);
		public void downLoadFail();
		public void timeOut();
	}
	
	
	public byte[] gbk2utf8(String chenese){
		  char c[] = chenese.toCharArray();
		        byte [] fullByte =new byte[3*c.length];
		        for(int i=0; i<c.length; i++){
		         int m = (int)c[i];
		         String word = Integer.toBinaryString(m);
//		         System.out.println(word);
		        
		         StringBuffer sb = new StringBuffer();
		         int len = 16 - word.length();
		         //补零
		         for(int j=0; j<len; j++){
		          sb.append("0");
		         }
		         sb.append(word);
		         sb.insert(0, "1110");
		         sb.insert(8, "10");
		         sb.insert(16, "10");
		         
//		         System.out.println(sb.toString());
		         
		         String s1 = sb.substring(0, 8);          
		         String s2 = sb.substring(8, 16);          
		         String s3 = sb.substring(16);
		         
		         byte b0 = Integer.valueOf(s1, 2).byteValue();
		         byte b1 = Integer.valueOf(s2, 2).byteValue();
		         byte b2 = Integer.valueOf(s3, 2).byteValue();
		         byte[] bf = new byte[3];
		         bf[0] = b0;
		         fullByte[i*3] = bf[0];
		         bf[1] = b1;
		         fullByte[i*3+1] = bf[1];
		         bf[2] = b2;
		         fullByte[i*3+2] = bf[2];
		         
		        }
		        return fullByte;
		}
}
