package com.wraith.wiregrard;
import com.wireguard.android.backend.Tunnel;

public class WgTunnel implements Tunnel {
    @Override
    public String getName() {
        return "Myname";
    }

    @Override
    public void onStateChange(State newState) {
    }
}
