package io.mobitech.content.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

/**
 * Utility class to determine if user has a low-end device
 *
 * @author Sergey Pogoryelov
 */

public class LowEndDeviceDetectionUtil {
    private static final int REQUEST_CODE_PHONE = 111;
    private final Context context;

    public LowEndDeviceDetectionUtil(Context context) {
        this.context = context;
    }

    /**
     * Determines if user has a low-end device (amount of available RAM is less then 400Mb or network type is 2G)
     *
     * @return True - if device is low-end
     */
    public boolean isLowEndDevice() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        double availableMegs = 0;
        if (activityManager != null) {
            activityManager.getMemoryInfo(mi);
            availableMegs = (double) mi.availMem / 0x100000L;
        }
        String networkType = getNetworkClass();

        return "2G".equalsIgnoreCase(networkType) || availableMegs <= 400;
    }

    /**
     * Determines user's cellular network type
     *
     * @return Generation of mobile telecommunications technology
     */
    @SuppressLint("MissingPermission")
    private String getNetworkClass() {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PHONE);
            return "Unknown";
        } else {
            int networkType;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                networkType = mTelephonyManager.getDataNetworkType();
            } else {
                networkType = mTelephonyManager.getNetworkType();
            }
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "Unknown";
                case TelephonyManager.NETWORK_TYPE_GSM:
                    break;
                case TelephonyManager.NETWORK_TYPE_IWLAN:
                    break;
                case TelephonyManager.NETWORK_TYPE_NR:
                    break;
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    break;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    break;
            }
        }
        return "Unknown";
    }


}
