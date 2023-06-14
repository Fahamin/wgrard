package com.wraith.wiregrard.Service;

import static com.wireguard.android.backend.Tunnel.State.DOWN;
import static com.wireguard.android.backend.Tunnel.State.UP;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;

import androidx.core.app.NotificationCompat;

import com.wireguard.android.backend.Backend;
import com.wireguard.android.backend.GoBackend;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;
import com.wireguard.config.InetEndpoint;
import com.wireguard.config.InetNetwork;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;
import com.wraith.wiregrard.MainActivity;
import com.wraith.wiregrard.R;
import com.wraith.wiregrard.WirgurdUtils.WgTunnel;

public class MyForegroundService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "vpn_channel";
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
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_MUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(intent.getStringExtra("countryName"))
                .setContentText("Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

       // concIONvpn();


            config = new Config.Builder();
        Tunnel tunnel = new WgTunnel();
        GoBackend.VpnService.prepare(this);
        Interface.Builder interfaceBuilder = new Interface.Builder();
        Peer.Builder peerBuilder = new Peer.Builder();
        Backend backend = new GoBackend(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    backend.setState(tunnel, UP, config
                            .setInterface(interfaceBuilder.addAddress(
                                            InetNetwork.parse(intent.getStringExtra("intentNetwork")))
                                    .parsePrivateKey(intent.getStringExtra("privateKey")).build())
                            .addPeer(peerBuilder.addAllowedIp(InetNetwork.parse("0.0.0.0/0"))
                                    .setEndpoint(InetEndpoint.parse(intent.getStringExtra("endPoint")))
                                    .parsePublicKey(intent.getStringExtra("publicKey")).build())
                            .build());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        startForeground(NOTIFICATION_ID, notification);

        // Return START_STICKY to keep the service running
        return START_STICKY;
    }


    void concIONvpn() {
        config = new Config.Builder();
        Tunnel tunnel = new WgTunnel();
        GoBackend.VpnService.prepare(this);
        Interface.Builder interfaceBuilder = new Interface.Builder();
        Peer.Builder peerBuilder = new Peer.Builder();
        Backend backend = new GoBackend(this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    backend.setState(tunnel, UP, config
                            .setInterface(interfaceBuilder.addAddress(InetNetwork.parse("192.168.6.189/32"))
                                    .parsePrivateKey("cApuxMnQiHHDiZLDLiIPx9/0RSo7wN/uCpd70cO4eX8=").build())
                            .addPeer(peerBuilder.addAllowedIp(InetNetwork.parse("0.0.0.0/0"))
                                    .setEndpoint(InetEndpoint.parse("ca2.vpnjantit.com:1024"))
                                    .parsePublicKey("LZg89RAqejsZi6rhPIiSalWqDojKt08km4WIIlYh0zI=").build())
                            .build());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "VPN Service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        try {
            stopSelf();
            backend.setState(tunnel, DOWN, config.build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

