package com.anjoyo.meituan.utils;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Character.UnicodeBlock;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {

	public static boolean writeFile(String str, String filePath) {

		FileOutputStream out = null;
		File wfile = new File(filePath);

		boolean ret = true;
		if (!wfile.exists()) {
			try {
				wfile.createNewFile();
			} catch (IOException e) {
				ret = false;
			}
		}
		try {
			out = new FileOutputStream(wfile, false);
		} catch (FileNotFoundException e) {
			ret = false;
		}
		try {
			str = str + "\r\n";
			if (out != null)
				out.write(str.getBytes());
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.flush();
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			ret = false;
		}
		return ret;
	}

	public static boolean writeFileForPandian(String str, String filePath) {

		FileOutputStream out = null;
		File wfile = new File(filePath);

		boolean ret = true;
		if (!wfile.exists()) {
			try {
				wfile.createNewFile();
			} catch (IOException e) {
				ret = false;
			}
		}
		try {
			out = new FileOutputStream(wfile, true);
		} catch (FileNotFoundException e) {
			ret = false;
		}
		try {
			str = str + "\r\n";
			if (out != null)
				out.write(str.getBytes());
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.flush();
		} catch (IOException e) {
			ret = false;
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			ret = false;
		}
		return ret;
	}

	public static String getStringToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringToday2() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	public static String getStringTodayFormat(String format) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	
	//读取文本文件中的内容
    public static String ReadTxtFile(String strFilePath)
     {
	  String path = strFilePath;
      String content = ""; //文件内容字符串
         //打开文件
         File file = new File(path);
         if(null == file ||  !file.exists()) return null;
          //如果path是传递过来的参数，可以做一个非目录的判断
              try {
                  InputStream instream = new FileInputStream(file); 
                  if (instream != null) 
                  {
                      InputStreamReader inputreader = new InputStreamReader(instream);
                      BufferedReader buffreader = new BufferedReader(inputreader);
                      String line;
                      //分行读取
                     while (( line = buffreader.readLine()) != null) {
                          content += line + "\n";
                      }                
                      instream.close();
                  }
              }
              catch (java.io.FileNotFoundException e) 
              {
                  Log.d("TestFile", "The File doesn't not exist.");
              } 
              catch (IOException e) 
              {
                   Log.d("TestFile", e.getMessage());
              }
          return content;
  }
    
  //读取文本文件中的内容
    public static String ReadTxtFile2(String strFilePath)
     {
	  String path = strFilePath;
      String content = ""; //文件内容字符串
         //打开文件
         File file = new File(path);
         if(null == file ||  !file.exists()) return null;
          //如果path是传递过来的参数，可以做一个非目录的判断
              try {
                  InputStream instream = new FileInputStream(file); 
                  if (instream != null) 
                  {
                      InputStreamReader inputreader = new InputStreamReader(instream,"gbk");
                      BufferedReader buffreader = new BufferedReader(inputreader);
                      String line;
                      //分行读取
                     while (( line = buffreader.readLine()) != null) {
                          content += line + "\n";
                      }                
                      instream.close();
                  }
              }
              catch (java.io.FileNotFoundException e) 
              {
                  Log.d("TestFile", "The File doesn't not exist.");
              } 
              catch (IOException e) 
              {
                   Log.d("TestFile", e.getMessage());
              }
          return content;
  }
    
    public static int getConnectedType(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
                return mNetworkInfo.getType();  
            }  
        }  
        return -1;  
    }
    
    
    public String gbk2utf8(String gbk) {
        String l_temp = GBK2Unicode(gbk);
        l_temp = unicodeToUtf8(l_temp);
     
        return l_temp;
    }
     
    public String utf82gbk(String utf) {
        String l_temp = utf8ToUnicode(utf);
        l_temp = Unicode2GBK(l_temp);
     
        return l_temp;
    }
     
    /**
     * 
     * @param str
     * @return String
     */
     
    public static String GBK2Unicode(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr1 = (char) str.charAt(i);
     
            if (!isNeedConvert(chr1)) {
                result.append(chr1);
                continue;
            }
     
            result.append("\\u" + Integer.toHexString((int) chr1));
        }
     
        return result.toString();
    }
     
    /**
     * 
     * @param dataStr
     * @return String
     */
     
    public static String Unicode2GBK(String dataStr) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();
     
        int li_len = dataStr.length();
        while (index < li_len) {
            if (index >= li_len - 1
                    || !"\\u".equals(dataStr.substring(index, index + 2))) {
                buffer.append(dataStr.charAt(index));
     
                index++;
                continue;
            }
     
            String charStr = "";
            charStr = dataStr.substring(index + 2, index + 6);
     
            char letter = (char) Integer.parseInt(charStr, 16);
     
            buffer.append(letter);
            index += 6;
        }
     
        return buffer.toString();
    }
     
    public static boolean isNeedConvert(char para) {
        return ((para & (0x00FF)) != para);
    }
     
    /**
     * utf-8 转unicode
     * 
     * @param inStr
     * @return String
     */
    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
     
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                sb.append(myBuffer[i]);
            } else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }
     
    /**
     * 
     * @param theString
     * @return String
     */
    public static String unicodeToUtf8(String theString) {
        char aChar;
        if(theString==null){
            return "";
        }
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = 't';
                    else if (aChar == 'r')
                        aChar = 'r';
                    else if (aChar == 'n')
                        aChar = 'n';
                    else if (aChar == 'f')
                        aChar = 'f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
