package com.wraith.wiregrard;

import static com.wireguard.android.backend.Tunnel.State.UP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.wireguard.android.backend.Backend;
import com.wireguard.android.backend.GoBackend;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;
import com.wireguard.config.InetEndpoint;
import com.wireguard.config.InetNetwork;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;
import com.wraith.wiregrard.WirgurdUtils.WgTunnel;

public class WirgurdNormalActivity extends AppCompatActivity {
    Config.Builder config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wirgurd_normal);


        concIONvpn();
    }

    void concIONvpn() {


        config = new Config.Builder();
        Tunnel tunnel = new WgTunnel();
        Intent intentPrepare = GoBackend.VpnService.prepare(this);
        if (intentPrepare != null) {
            startActivityForResult(intentPrepare, 0);
        }
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
                                    .setEndpoint(InetEndpoint.parse("ca2.vpnjantit.com:1024")).
                                    parsePublicKey("LZg89RAqejsZi6rhPIiSalWqDojKt08km4WIIlYh0zI=").build())
                            .build());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {

        }
    }
}