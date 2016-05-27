package com.anjoyo.meituan.utils;

import java.io.File;

import android.os.Environment;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;


public class FtpUtils {
	
	public static String FTP_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/MLGY";
	
	public static String ROOT_FTP = "112.124.180.101";
	public static String ROOT_FTP_ACCOUNT = "hxw0010825";
	public static String ROOT_FTP_PWD = "PALACEB888";
	
	public static String SUB_FTP = ROOT_FTP;
	public static String SUB_FTP_ACOUNT = ROOT_FTP_ACCOUNT;
	public static String SUB_FTP_PWD = ROOT_FTP_PWD;
	public static String SPLASH_URL = "http://www.palaceb.com/data/Splash/splash.jpg";
	
	static public void ftpconnectDownload(String localName, String ftpPath, FTPDataTransferListener listener) {
		try {
			FTPClient ftp = new FTPClient();
			ftp.connect(SUB_FTP, 21);
			ftp.login(SUB_FTP_ACOUNT, SUB_FTP_PWD);
			ftp.setCharset("utf8");
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File fileDir = Environment.getExternalStorageDirectory();
				// 下载文件
				File downloadDir = new File(fileDir, "MLGY"); // 用于保存文件的文件夹
				if (!downloadDir.exists()) {
					downloadDir.mkdirs();
				}
				File localFile = new File(downloadDir.toString() + "/" + localName);
				if(localFile.exists()) {
					localFile.delete();
				}
				localFile.createNewFile();
				ftp.download(ftpPath, localFile, listener); // 第一个参数是远程服务器上的某文件的相对路径
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void ftpDeleteFile(String file) {
		try {
			FTPClient ftp = new FTPClient();
			ftp.connect(SUB_FTP, 21);
			ftp.login(SUB_FTP_ACOUNT, SUB_FTP_PWD);
			ftp.setCharset("utf8");
			ftp.deleteFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
