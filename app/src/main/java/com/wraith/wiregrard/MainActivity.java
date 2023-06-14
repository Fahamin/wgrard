package com.wraith.wiregrard;

import static com.wraith.wiregrard.utils.Fun.checkInternet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wireguard.android.backend.Backend;
import com.wireguard.android.backend.GoBackend;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;
import com.wraith.wiregrard.Model.VpnModel;
import com.wraith.wiregrard.Service.MyForegroundService;
import com.wraith.wiregrard.Service.MyVpnService;
import com.wraith.wiregrard.WirgurdUtils.WgTunnel;
import com.wraith.wiregrard.reciver.MyBroadcastReceiver;
import com.wraith.wiregrard.utils.Fun;
import com.wraith.wiregrard.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;


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

    //ui
    MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
    LinearLayout purchaseLayout;
    FrameLayout category, flAdplaceholder;
    TextView countryName, logTv, durationTv, byteInTv, byteOutTv;
    ImageView selectedServerIcon;
    FrameLayout vpnBtn;
    RelativeLayout currentConnectionLayout;
    List<VpnModel> serverList;
    private static final String ARG_PARAM1 = "param1";
    int serverPos;
    TinyDB tinyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = new Config.Builder();
        Tunnel tunnel = new WgTunnel();

        Interface.Builder interfaceBuilder = new Interface.Builder();
        Peer.Builder peerBuilder = new Peer.Builder();
        Backend backend = new GoBackend(this);

        initView();

        if (foregroundServiceRunning()) {
            logTv.setText("Connected Server");
        } else {
            logTv.setText("Not connected Server");
        }

        vpnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInternet()) {
                    if (foregroundServiceRunning()) {
                        logTv.setText("Not Connected Server");
                        stopService(new Intent(MainActivity.this, MyForegroundService.class));
                    } else {

                        Intent intentPrepare = GoBackend.VpnService.prepare(MainActivity.this);
                        if (intentPrepare != null) {
                            startActivityForResult(intentPrepare, 0);
                        }
                        startMyvpn();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Check Inter", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initView() {
        tinyDB = new TinyDB(getApplicationContext());
        purchaseLayout = findViewById(R.id.purchase_layout);
        category = findViewById(R.id.category);
        countryName = findViewById(R.id.countryName);
        logTv = findViewById(R.id.logTv);
        selectedServerIcon = findViewById(R.id.selectedServerIcon);
        durationTv = findViewById(R.id.durationTv);
        byteInTv = findViewById(R.id.byteInTv);
        byteOutTv = findViewById(R.id.byteOutTv);
        vpnBtn = findViewById(R.id.vpnBtn);
        currentConnectionLayout = findViewById(R.id.currentConnectionLayout);
        serverList = new ArrayList<>();
        serverPos = tinyDB.getInt("serverPos");

        new Fun(this);
        if (checkInternet()) {

        } else {
            Toast.makeText(this, "Check Internet", Toast.LENGTH_SHORT).show();
        }
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

    public void startMyvpn() {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        serviceIntent.putExtra("intentNetwork", "192.168.6.189/32");
        serviceIntent.putExtra("countryName", "Canada Alabama");
        serviceIntent.putExtra("privateKey", "cApuxMnQiHHDiZLDLiIPx9/0RSo7wN/uCpd70cO4eX8=");
        serviceIntent.putExtra("endPoint", "ca2.vpnjantit.com:1024");
        serviceIntent.putExtra("publicKey", "LZg89RAqejsZi6rhPIiSalWqDojKt08km4WIIlYh0zI=");
        ContextCompat.startForegroundService(this, serviceIntent);
        logTv.setText("Connected Server");

    }

    public void offf() {

        if (foregroundServiceRunning()) {
            stopService(new Intent(this, MyVpnService.class));
            stopService(new Intent(this, MyForegroundService.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            Intent serviceIntent = new Intent(this, MyForegroundService.class);
            serviceIntent.putExtra("intentNetwork", "192.168.6.162/32");
            serviceIntent.putExtra("countryName", "Canada Alabama");
            serviceIntent.putExtra("privateKey", "cApuxMnQiHHDiZLDLiIPx9/0RSo7wN/uCpd70cO4eX8=");
            serviceIntent.putExtra("endPoint", "ca2.vpnjantit.com:1024");
            serviceIntent.putExtra("publicKey", "LZg89RAqejsZi6rhPIiSalWqDojKt08km4WIIlYh0zI=");
            ContextCompat.startForegroundService(this, serviceIntent);
            logTv.setText("Connected Server");
            Log.e("vpnStart", "onActivityResult");
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}