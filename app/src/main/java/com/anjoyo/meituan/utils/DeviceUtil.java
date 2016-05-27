package com.anjoyo.meituan.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.os.Debug;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;


public class DeviceUtil {

	final static String TAG = "DeviceUtil";
	

    /**
     * get local ip, mybe return null
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return "";
    }

    public static String getAllowedLocationProviders(Context context) {
        return Settings.System.getString(
                context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    }

    // ref: http://stackoverflow.com/questions/5832368/tablet-or-phone-android
    public static boolean isTabletDevice(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
            // test screen size, use reflection because isLayoutSizeAtLeast is
            // only available since 11
            android.content.res.Configuration con = context.getResources().getConfiguration();
            try {
                Method mIsLayoutSizeAtLeast = con.getClass().getMethod(
                        "isLayoutSizeAtLeast", int.class);
                boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con,
                        0x04); //Configuration.SCREENLAYOUT_SIZE_XLARGE
                return r;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static final String DEFAULT_DUMP_LOG_FILE_SIZE_KB = "512";

    public static final String TAG_ACTIVITY_THREAD = "ActivityThread";
    public static final String TAG_ACTIVITY_RUNTIME = "AndroidRuntime";
    public static final String TAG_PROCESS = "Process";
    public static final String TAG_DALVIKVM = "dalvikvm";


    private static DisplayMetrics sMetrics;
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        if (sMetrics == null) {
            sMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(sMetrics);
        }
        return sMetrics;
    }

    public static Pair<Integer, Integer> getDeviceResolution(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int rawWidth = context.getResources().getDisplayMetrics().widthPixels;
        int rawHeight = context.getResources().getDisplayMetrics().heightPixels;
        return new Pair<Integer, Integer>(Math.round(rawWidth/density), Math.round(rawHeight/density));
    }
 
}
