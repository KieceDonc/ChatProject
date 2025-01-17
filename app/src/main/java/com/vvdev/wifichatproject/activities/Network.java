package com.vvdev.wifichatproject.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.WifiData;
import com.vvdev.wifichatproject.ui.DialogAPWifiData;
import com.vvdev.wifichatproject.ui.DialogJoinWifi;
import com.vvdev.wifichatproject.ui.DialogSystemWritePerm;

public class Network extends AppCompatActivity {

    private Context mContext;
    private AlertDialog PermSystemWrite;
    private WifiData DataCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network);
        mContext=this;

        DialogSystemWritePerm CallDialogSystem =  new DialogSystemWritePerm(mContext);
        PermSystemWrite = CallDialogSystem.get(); // Get the alert dialog to ask to enable write system perm

        DataCall = new WifiData();
        DataCall.setWifiManager((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));

        LinearLayout CreateAP = findViewById(R.id.LayoutWifiCreate);
        LinearLayout WifiJoin = findViewById(R.id.LayoutWifiJoin);
        LinearLayout OfflineAccess = findViewById(R.id.LayoutOfflineAccess);

        CreateAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Set LinearLayoutWifiCreate on click listener
                if(!checkSystemWritePermission()){ // if we don't have write system perm
                    PermSystemWrite.show(); // we show dialog
                }else{
                    DialogAPWifiData Call = new DialogAPWifiData();
                    Call.ShowDialog(mContext,DataCall); // Show dialog to create AP
                }
            }
        });
        WifiJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCall.scanWifiInRange(); // start scanning all wifi network
                DialogJoinWifi Call = new DialogJoinWifi(mContext, DataCall);
                Call.Show();
                /*TODO Need to handle location permission*/
                /**TODO need to create interface to join a wifi */
            }
        });
        OfflineAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**TODO need to handle the fact that user want to use app offline */
            }
        });
    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= 23) { // check comment under
            return Settings.System.canWrite(this); // API lvl for this is 23
        }else{
            return true;
        }
    }
}

/*
 * https://stackoverflow.com/questions/44175056/how-to-create-custom-wpa-hotspot-with-ssid-and-password-in-android
 * https://stackoverflow.com/questions/27653084/creating-wpa2-psk-access-point-in-android-programmatically
 * https://github.com/zxing/zxing/blob/master/android/src/com/google/zxing/client/android/wifi/WifiConfigManager.java*/
