package com.vvdev.wifichatproject.interfaces;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.regex.Pattern;

import cc.mvdan.accesspoint.WifiApControl;

public class WifiData {

    private String AP_Encryption; // Encryption for access point like WPA etc ...
    private CharSequence AP_Password; // Password for ap
    private CharSequence AP_SSID; // SSID name for ap
    private boolean AP_Hidden; // If true, ap is hidden, else not
    private WifiManager wifiManager;
    private WifiHandler CallWifiHandler;

    public final static String ACCESS_POINT_NOENCRYPTION = "nopass";
    public final static String ACCESS_POINT_WPA2_PSK = "WPA2-PSK";
    public static final int WIFI_CONNECT_SUCCESS = 565112; // Success Code
    public static final int WIFI_FAIL_ADD_CONFIG = 456465; // Error code
    public static final int WIFI_FAIL_DISCONNECT_OLD_NETWORK = 455685; // Error code
    public static final int WIFI_FAIL_ENABLE_NETWORK = 454864;// Error code
    public static final int WIFI_FAIL_TO_CONNECT = 445358;// Error code

    public void setAPEncryption(String ReceiveString){
        AP_Encryption = ReceiveString;
    }

    public String getAPEncryption(){
        return AP_Encryption;
    }

    public void setAPPassword(CharSequence ReceiveString){
        AP_Password = ReceiveString;
    }

    public String getAPPassword(){
        return String.valueOf(AP_Password);
    }

    public void setAPSSSID(CharSequence ReceiveString){
        AP_SSID = ReceiveString;
    }

    public String getAPSSID(){
        return String.valueOf(AP_SSID);
    }

    public void setAPHidden(boolean Receive){
        AP_Hidden = Receive;
    }

    public boolean getAPHidden(){
        return AP_Hidden;
    }

    public void setWifiManager(WifiManager receive){
        wifiManager = receive;
    }

    public WifiManager getWifiManager(){
        return wifiManager;
    }

    public void setCallWifiHandler(WifiHandler t){
        CallWifiHandler =t;
    }

    public WifiHandler getCallWifiHandler(){
        return CallWifiHandler;
    }

    public WifiConfiguration createConfig() {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        // Android API insists that an ascii SSID must be quoted to be correctly handled.
        config.SSID = quoteNonHex(getAPSSID());
        config.hiddenSSID = getAPHidden();
        return config;
    } // Create a wifi configuration

    public WifiConfiguration getWPA2PSKConfig(){
        WifiConfiguration config = createConfig();
        config.SSID = getAPSSID();
        config.preSharedKey = getAPPassword();
        config.hiddenSSID = false;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedKeyManagement.set(4);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        return config;
    } // give WPA2-PSK configuration

    public WifiConfiguration getNoneConfig() {
        WifiConfiguration config = createConfig();
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return config;
    } // give none configuration

    public void setupAccessPoint(Context CurrentContext){

        WifiApControl apControl = WifiApControl.getInstance(CurrentContext);
        String Encryption = getAPEncryption();
        WifiManager wifiManager = getWifiManager();


        wifiManager.setWifiEnabled(false);
        if(Encryption.equals(ACCESS_POINT_WPA2_PSK)){
            apControl.setEnabled(getWPA2PSKConfig(), true);
            apControl.enable();
            wifiManager.setWifiEnabled(true);
        }else if(Encryption.equals(ACCESS_POINT_NOENCRYPTION)){
            apControl.setEnabled(getNoneConfig(), true);
            apControl.enable();
            wifiManager.setWifiEnabled(true);
        }else{
            Log.e("WifiAP_Setup()","wrong encryption string receive :"+Encryption);
        }
    }

    public static String quoteNonHex(String value, int... allowedLengths) {
        return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
    }

    public static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");

    public static boolean isHexOfLength(CharSequence value, int... allowedLengths) {
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

    public static String convertToQuotedString(String s) {
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
