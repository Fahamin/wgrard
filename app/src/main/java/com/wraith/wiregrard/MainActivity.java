package com.wraith.wiregrard;

import static com.wraith.wiregrard.utils.Fun.FreeServerList;
import static com.wraith.wiregrard.utils.Fun.checkInternet;
import static com.wraith.wiregrard.utils.Fun.foregroundServiceRunning;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.wraith.wiregrard.WirgurdUtils.WgTunnel;
import com.wraith.wiregrard.reciver.MyBroadcastReceiver;
import com.wraith.wiregrard.utils.Fun;
import com.wraith.wiregrard.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {




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

    List<VpnModel> list;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Fun(this);

        list = FreeServerList;

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
                            vpnServiceLauncher.launch(intentPrepare);
                        }
                        startVPN();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Check Inter", Toast.LENGTH_SHORT).show();
                }

            }
        });
        currentConnectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInternet()) {

                    Intent mIntent = new Intent(MainActivity.this, HomeActivity.class);
                    // someActivityResultLauncher.launch(mIntent);
                    serverLauncher.launch(mIntent);

                } else {
//chck internet
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


    public void startMyvpn() {

        if (checkInternet()) {
            if (foregroundServiceRunning()) {
                logTv.setText("Not Connected Server");
                stopService(new Intent(MainActivity.this, MyForegroundService.class));

            } else {

                Intent intentPrepare = GoBackend.VpnService.prepare(MainActivity.this);
                if (intentPrepare != null) {
                    vpnServiceLauncher.launch(intentPrepare);
                }
                startVPN();
            }
        } else {
            Toast.makeText(MainActivity.this, "Check Inter", Toast.LENGTH_SHORT).show();
        }

    }


    public void startVPN() {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        serviceIntent.putExtra("intentNetwork", list.get(pos).getIntentNetwork());
        serviceIntent.putExtra("countryName", list.get(pos).getCountryName());
        serviceIntent.putExtra("privateKey", list.get(pos).getPrivateKey());
        serviceIntent.putExtra("endPoint", list.get(pos).getEndPoint());
        serviceIntent.putExtra("publicKey", list.get(pos).getPublicKey());
        ContextCompat.startForegroundService(this, serviceIntent);
        logTv.setText("Connected Server");
        countryName.setText(list.get(pos).getCountryName());
    }

    ActivityResultLauncher<Intent> serverLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        pos = data.getIntExtra("server", 1);
                        countryName.setText(list.get(pos).getCountryName());
                        startMyvpn();

                    }
                }
            });

    ActivityResultLauncher<Intent> vpnServiceLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent serviceIntent = new Intent(MainActivity.this, MyForegroundService.class);
                        serviceIntent.putExtra("intentNetwork", list.get(pos).getIntentNetwork());
                        serviceIntent.putExtra("countryName", list.get(pos).getCountryName());
                        serviceIntent.putExtra("privateKey", list.get(pos).getPrivateKey());
                        serviceIntent.putExtra("endPoint", list.get(pos).getEndPoint());
                        serviceIntent.putExtra("publicKey", list.get(pos).getPublicKey());
                        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                        logTv.setText("Connected Server");

                    }
                }
            });
}