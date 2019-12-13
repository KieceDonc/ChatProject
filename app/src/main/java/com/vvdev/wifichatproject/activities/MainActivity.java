package com.vvdev.wifichatproject.activities;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.ui.AskForWifiData;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network);

        /**
         * https://stackoverflow.com/questions/44175056/how-to-create-custom-wpa-hotspot-with-ssid-and-password-in-android
         * https://stackoverflow.com/questions/27653084/creating-wpa2-psk-access-point-in-android-programmatically
         * https://github.com/zxing/zxing/blob/master/android/src/com/google/zxing/client/android/wifi/WifiConfigManager.java*/
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.e("yoloy",String.valueOf(wifiManager.isWifiEnabled()));
        LinearLayout CreateAP = findViewById(R.id.LayoutWifiCreate);
        CreateAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AskForWifiData Call = new AskForWifiData();
                Call.ShowDialog(mContext, wifiManager);
            }
        });
    }
}
