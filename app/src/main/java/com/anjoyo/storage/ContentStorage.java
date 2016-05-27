package com.anjoyo.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.os.Environment;
import android.support.v4.util.LruCache;

public class ContentStorage implements Storage, Serializable {

    public static final int LRUCACHE_SIZE = 150;

    private static final long serialVersionUID = -2556174770329584664L;

    // url反查路径和cache
    private static final LruCache<String, String> sFilePathCache =
            new LruCache<String, String>(LRUCACHE_SIZE);

    // 需要更新最后访问时间的栈. 在弹出缓存栈的时候批量存入数据库
    private static final MyLruCache sFileLastUsedCache =
            new MyLruCache(LRUCACHE_SIZE);

    public String url;

    public String localPath;

    public ContentStorage() {}

    public ContentStorage(String localPath, String url) {
        if (Checker.isEmpty(localPath) && Checker.isEmpty(url))
            throw new IllegalArgumentException("" +
                    "localPath and url cannot be null at the same time");

        this.localPath = localPath;
        this.url = url;
    }

    public File getLocalFile() {
        File file = null;
        String key = url;

        // 从本地上传的ContentStorage应该都带有localPath, 而且上传成功后返回的url
        // 是不带尺寸参数的. 为了从LruCache找出localPath, 需要先把尺寸参数去掉.

        // 不同来源生成的ContentStorage, 可能url是相同的, 但不一定都有localPath.
        // 如果localPath为空, 尝试找出有url的localPath.
        if (Checker.isEmpty(localPath)) {

            //从LruCache里面找
            file = !Checker.isEmpty(getFromCache(key))
                    ? new File(getFromCache(key)) : null;

            //LruCache找不到, 所有规格的图片都先从无规格图片的缓存路径查找图片
            if (Checker.isEmpty(file))
                file = new HashCacheStorage(key).getFile();



        } else {
             //有localPath的情况：直接获取本地文件
             file = new File(localPath);
        }
        return file;
    }

    public File getCacheFile() {
        if (Checker.isEmpty(url)) return null;

        //先从LruCache里面找
        File file = !Checker.isEmpty(getFromCache(url)) ? new File(
                getFromCache(url)) : null;

        if (file == null) {
            //LruCache找不到，Hash出路径并放到LurCache和数据库
            file = new HashCacheStorage(url).getFile();
        } else {
            //更新最后访问时间, 放在这里做是为了避免频繁操作子线程
            putInCache(url, file.getAbsolutePath(), System.currentTimeMillis());
        }

        return file;
    }

    @Override
    public File getFile() {
        File contentFile = getLocalFile();
        if (Checker.isEmpty(contentFile)) contentFile = getCacheFile();
        return contentFile;
    }


    public static void putInCache(String url ,String path , long lastUsed) {
        if (null == url || null == path) return;
        sFilePathCache.put(url, path);
        sFileLastUsedCache.put(url, lastUsed);
    }

    public static String getFromCache(String url) {
        if (null == url) return null;
        return sFilePathCache.get(url);
    }

    public static Long getFromMyCache(String url) {
        if (null == url) return 0l;
        return sFileLastUsedCache.get(url);
    }

    public static void clearLruCache(String url) {
        if (null == url) return;
        sFilePathCache.remove(url);
        sFileLastUsedCache.remove(url);
    }

    public static void removeAllLruCache() {
        sFilePathCache.evictAll();
        sFileLastUsedCache.evictAll();
    }

    public static class MyLruCache extends LruCache<String, Long> {

        public MyLruCache(int maxSize) {
            super(maxSize);
        }
    }
    
    public String download() {
        OutputStream output=null;   
        try {   
            /*  
             * 通过URL取得HttpURLConnection  
             * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置  
             * <uses-permission android:name="android.permission.INTERNET" />  
             */  
            URL downLoadUrl= new URL(url);   
            HttpURLConnection conn=(HttpURLConnection)downLoadUrl.openConnection();   
            //取得inputStream，并将流中的信息写入SDCard   
            
            ContentStorage storage = new ContentStorage(null, url);
            
            String pathName=storage.getLocalFile().getAbsolutePath();
               
            File file=new File(pathName);   
            if (null != file.getParentFile() && !file.getParentFile().exists()) {
            	file.getParentFile().mkdirs();
			}
            
            InputStream input=conn.getInputStream();   
            if(file.exists()){   
                System.out.println("exits");   
                return null;   
            }else{   
                file.createNewFile();//新建文件   
                output = new FileOutputStream(file);   
                //读取大文件   
                byte[] buffer=new byte[4*1024];   
                while(input.read(buffer)!=-1){   
                    output.write(buffer);   
                }   
                output.flush();  
            }   
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
            return null;  
        } catch (IOException e) {   
            e.printStackTrace();   
            return null;  
        }finally{   
            try {   
            	if (null != output) {
            		output.close(); 
				}
                      
                    System.out.println("success");   
                } catch (IOException e) {   
                    System.out.println("fail");   
                    e.printStackTrace();   
                    return null;
                }   
        }
		return "success";
    }
}
