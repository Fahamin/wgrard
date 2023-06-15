package com.wraith.wiregrard.utils;


import static android.content.Context.CLIPBOARD_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.tv.AdRequest;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.wraith.wiregrard.Model.VpnModel;
import com.wraith.wiregrard.Service.MyForegroundService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Fun {

    public static Context context;
    public static   List<VpnModel> FreeServerList;

    public Fun(Context context) {
        this.context = context;
        FreeServerList = new ArrayList<>();
        getServerList();
    }

    private void getServerList() {

        FreeServerList.add(new VpnModel("192.168.6.189/32", "cApuxMnQiHHDiZLDLiIPx9/0RSo7wN/uCpd70cO4eX8=",
                "ca2.vpnjantit.com:1024", "LZg89RAqejsZi6rhPIiSalWqDojKt08km4WIIlYh0zI=", "Canada"));

        FreeServerList.add(new VpnModel("192.168.6.189/32", "cApuxMnQiHHDiZLDLiIPx9/0RSo7wN/uCpd70cO4eX8=",
                "ca2.vpnjantit.com:1024", "LZg89RAqejsZi6rhPIiSalWqDojKt08km4WIIlYh0zI=", "UK"));

        FreeServerList.add(new VpnModel("192.168.6.189/32", "cApuxMnQiHHDiZLDLiIPx9/0RSo7wN/uCpd70cO4eX8=",
                "ca2.vpnjantit.com:1024", "LZg89RAqejsZi6rhPIiSalWqDojKt08km4WIIlYh0zI=", "Usa"));

    }


    public static boolean foregroundServiceRunning() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;

        }
    }

}