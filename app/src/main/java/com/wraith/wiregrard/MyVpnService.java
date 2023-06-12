package com.wraith.wiregrard;

import static com.wireguard.android.backend.Tunnel.State.UP;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

import com.wireguard.android.backend.Backend;
import com.wireguard.android.backend.GoBackend;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;
import com.wireguard.config.InetEndpoint;
import com.wireguard.config.InetNetwork;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;

public class MyVpnService extends VpnService {
    private Thread vpnThread;
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the VPN connection in a separate thread
        vpnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                config = new Config.Builder();
                tunnel = new WgTunnel();
                Intent intentPrepare = GoBackend.VpnService.prepare(getApplicationContext());
                if (intentPrepare != null) {
                   // startMyActivityForResult(intentPrepare, 0);
                }
                interfaceBuilder = new Interface.Builder();
                peerBuilder = new Peer.Builder();
                backend = new GoBackend(getApplicationContext());

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
        });
        vpnThread.start();

        // Return START_STICKY to keep the service running
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        if (vpnThread != null)
            vpnThread.interrupt();

        super.onDestroy();
    }
}
