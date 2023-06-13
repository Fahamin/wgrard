package com.wraith.wiregrard;

import static com.wireguard.android.backend.Tunnel.State.DOWN;
import static com.wireguard.android.backend.Tunnel.State.UP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wireguard.android.backend.Backend;
import com.wireguard.android.backend.GoBackend;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;
import com.wireguard.config.InetEndpoint;
import com.wireguard.config.InetNetwork;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;
//import net.wg.vpn.VPNServices;


public class MainActivity extends AppCompatActivity {

    private Thread b;
    private ParcelFileDescriptor c;
    private PendingIntent a;
    private static int currentTunnelHandle = -1;
    private Process process;

    Interface.Builder interfaceBuilder;
    Peer.Builder peerBuilder;
    Backend backend;
    Tunnel tunnel;
    Config.Builder config;
    MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        serviceIntent.putExtra("intentNetwork", "192.168.6.162/32");
        serviceIntent.putExtra("countryName", "Usa Alabama");

        serviceIntent.putExtra("privateKey", "oHJjrb1E59KtULqEBHVPefEi5YCYKga5FApXUOpe2G8=");
        serviceIntent.putExtra("endPoint", "usa1.vpnjantit.com:1024");
        serviceIntent.putExtra("publicKey", "ycqlMDMLhJNKjT+cVThGo1COfwIplHQhBS6ptH2BmQw=");
        ContextCompat.startForegroundService(this, serviceIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
    }

    public boolean foregroundServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void startMyvpn(View view) {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        serviceIntent.putExtra("intentNetwork", "192.168.6.162/32");
        serviceIntent.putExtra("countryName", "Usa Alabama");

        serviceIntent.putExtra("privateKey", "oHJjrb1E59KtULqEBHVPefEi5YCYKga5FApXUOpe2G8=");
        serviceIntent.putExtra("endPoint", "usa1.vpnjantit.com:1024");
        serviceIntent.putExtra("publicKey", "ycqlMDMLhJNKjT+cVThGo1COfwIplHQhBS6ptH2BmQw=");
        ContextCompat.startForegroundService(this, serviceIntent);

    }

    public void offf(View view) {

        if (foregroundServiceRunning()) {
            stopService(new Intent(this, MyVpnService.class));
            stopService(new Intent(this, MyForegroundService.class));
        }
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}