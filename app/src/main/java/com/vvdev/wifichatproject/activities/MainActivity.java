package com.vvdev.wifichatproject.activities;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vvdev.wifichatproject.R;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network);

        /**
         * https://stackoverflow.com/questions/44175056/how-to-create-custom-wpa-hotspot-with-ssid-and-password-in-android
         * https://stackoverflow.com/questions/27653084/creating-wpa2-psk-access-point-in-android-programmatically
         * https://github.com/zxing/zxing/blob/master/android/src/com/google/zxing/client/android/wifi/WifiConfigManager.java*/

    }

    public WifiConfiguration CreateWifiConfiguration(String SSIDName, String SSIDPassword, int SecurityKey, boolean HiddenSSID){
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = SSIDName;
        wifiConfiguration.preSharedKey = SSIDPassword;
        wifiConfiguration.hiddenSSID = HiddenSSID;
        wifiConfiguration.status =   WifiConfiguration.Status.ENABLED;
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        return wifiConfiguration;
    }

}
