package com.anjoyo.meituan.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NetPathToBitmapUtils {
    private String netPath;

	public NetPathToBitmapUtils(String netPath) {
		super();
		this.netPath = netPath;
	}
     
	public void run() {
		// 把网络地址转换为BitMap
		URL picUrl; 
		try {
			picUrl = new URL(netPath);
			System.out.println("网络图片地址"+picUrl);
		    Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
