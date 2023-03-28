package com.wraith.wiregrard;

import static com.wireguard.android.backend.Tunnel.State.DOWN;
import static com.wireguard.android.backend.Tunnel.State.UP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = VpnService.prepare(this);
        Log.v("CHECKSTATE", "start");
        if (intent != null) {
            startActivityForResult(intent, 1);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
    }

    public void startMyvpn(View view) {
        config = new Config.Builder();
        tunnel = new WgTunnel();
        Intent intentPrepare = GoBackend.VpnService.prepare(this);
        if (intentPrepare != null) {
            startActivityForResult(intentPrepare, 0);
        }
        interfaceBuilder = new Interface.Builder();
        peerBuilder = new Peer.Builder();
        backend = new GoBackend(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    backend.setState(tunnel, UP, config
                            .setInterface(interfaceBuilder.addAddress(InetNetwork.parse("192.168.6.162/32")).parsePrivateKey("oHJjrb1E59KtULqEBHVPefEi5YCYKga5FApXUOpe2G8=").build())
                            .addPeer(peerBuilder.addAllowedIp(InetNetwork.parse("0.0.0.0/0")).setEndpoint(InetEndpoint.parse("usa1.vpnjantit.com:1024")).parsePublicKey("ycqlMDMLhJNKjT+cVThGo1COfwIplHQhBS6ptH2BmQw=").build())
                            .build());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void offf(View view) {

        try {
            backend.setState(tunnel, DOWN, config.build());

            showToast("Disconnect");
        } catch (Exception e) {
            e.printStackTrace();
            showToast("" + e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(this, MainActivity.class);
            startService(intent);
        }
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                // updateUI(intent.getStringExtra("state"));
                // setStatus(intent.getStringExtra("state"));
                showToast("m");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");
                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " ";
                if (byteOut == null) byteOut = " ";
                showToast(duration);
                //  updateConnectionStatus(duration, lastPacketReceive, byteIn, byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

}