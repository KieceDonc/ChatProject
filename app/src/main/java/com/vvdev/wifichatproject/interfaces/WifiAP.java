package com.vvdev.wifichatproject.interfaces;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.regex.Pattern;

import cc.mvdan.accesspoint.WifiApControl;

public class WifiAP {

    private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");
    private Context mContext;

    public WifiAP(Context receive){
        mContext=receive;
    }

    public void Setup(WifiData CallData){
        WifiApControl apControl = WifiApControl.getInstance(mContext);
        String Encryption = CallData.getAPEncryption();

        if(Encryption.equals("WPA2-PSK")){
            apControl.setEnabled(getWPA2PSKConfig(CallData), true);
        }else if(Encryption.equals("nopass")){
            apControl.setEnabled(getNoneConfig(CallData), true);
        }else{
            Log.e("WifiAP_Setup()","wrong encryption string receive :"+Encryption);
        }
    }

    private static WifiConfiguration CreateConfig(WifiData CallData) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        // Android API insists that an ascii SSID must be quoted to be correctly handled.
        config.SSID = quoteNonHex(CallData.getAPSSID());
        config.hiddenSSID = CallData.getAPHidden();
        return config;
    }

    private static WifiConfiguration getNoneConfig(WifiData CallData) {
        WifiConfiguration config = CreateConfig(CallData);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return config;
    }

    private static WifiConfiguration getWPA2PSKConfig(WifiData CallData){
        WifiConfiguration config = CreateConfig(CallData);
        config.SSID = CallData.getAPSSID();
        config.preSharedKey = CallData.getAPPassword();
        config.hiddenSSID = false;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedKeyManagement.set(4);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        return config;
    }

    private static String quoteNonHex(String value, int... allowedLengths) {
        return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
    }

    private static boolean isHexOfLength(CharSequence value, int... allowedLengths) {
        if (value == null || !HEX_DIGITS.matcher(value).matches()) {
            return false;
        }
        if (allowedLengths.length == 0) {
            return true;
        }
        for (int length : allowedLengths) {
            if (value.length() == length) {
                return true;
            }
        }
        return false;
    }

    private static String convertToQuotedString(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        // If already quoted, return as-is
        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s;
        }
        return '\"' + s + '\"';
    }

}
