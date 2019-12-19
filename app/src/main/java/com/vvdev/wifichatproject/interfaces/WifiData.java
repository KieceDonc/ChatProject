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
    } // set the encryption chosen by the user for the access point ( WPA2-PSK, none, etc ... )

    public String getAPEncryption(){
        return AP_Encryption;
    } // get the encryption chosen by the user for the access point ( WPA2-PSK, none, etc ... )

    public void setAPPassword(CharSequence ReceiveString){
        AP_Password = ReceiveString;
    } // set the password chosen by the user for the access point

    public String getAPPassword(){
        return String.valueOf(AP_Password);
    } // get the password chosen by the user for the access point

    public void setAPSSSID(CharSequence ReceiveString){
        AP_SSID = ReceiveString;
    } // set the ssid name chosen by the user for access point

    public String getAPSSID(){
        return String.valueOf(AP_SSID);
    } // return the ssid name chosen by the user for access point

    public void setAPHidden(boolean Receive){
        AP_Hidden = Receive;
    } // set if the wifi access point will be hide or not. Chosen by user

    public boolean getAPHidden(){
        return AP_Hidden;
    } // get if the wifi access point will be hide or not. Chosen by user

    public void setWifiManager(WifiManager receive){
        wifiManager = receive;
    }

    public WifiManager getWifiManager(){
        return wifiManager;
    } // return the WifiManager

    public void setCallWifiHandler(WifiHandler t){
        CallWifiHandler =t;
    }

    public WifiHandler getCallWifiHandler(){
        return CallWifiHandler;
    } // Call this when you want to call method in WifiHandler

    public WifiConfiguration createConfig(String NameSSID, boolean hiddenSSID) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        // Android API insists that an ascii SSID must be quoted to be correctly handled.
        config.SSID = quoteNonHex(NameSSID);
        config.hiddenSSID = hiddenSSID;
        return config;
    } // Create a wifi configuration

    public WifiConfiguration getWPA2PSKConfig(String NameSSID, String Password, boolean hiddenSSID){
        WifiConfiguration config = createConfig(NameSSID, hiddenSSID);
        config.SSID = NameSSID;
        config.preSharedKey = Password;
        config.hiddenSSID = false;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedKeyManagement.set(4);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        return config;
    } // give WPA2-PSK configuration

    public WifiConfiguration getNoneConfig(String NameSSID, boolean hiddenSSID) {
        WifiConfiguration config = createConfig(NameSSID, hiddenSSID);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return config;
    } // give none configuration

    public void setupAccessPoint(Context CurrentContext,String NameSSID, String Encryption, boolean hiddenSSID,String Password){

        WifiApControl apControl = WifiApControl.getInstance(CurrentContext);
        WifiManager wifiManager = getWifiManager();


        disableWifi();
        if(Encryption.equals(ACCESS_POINT_WPA2_PSK)){
            apControl.setEnabled(getWPA2PSKConfig(NameSSID,Password,hiddenSSID), true);
            apControl.enable();
            enableWifi();
        }else if(Encryption.equals(ACCESS_POINT_NOENCRYPTION)){
            apControl.setEnabled(getNoneConfig(NameSSID,hiddenSSID), true);
            apControl.enable();
            enableWifi();
        }else{
            Log.e("WifiAP_Setup()","wrong encryption string receive :"+Encryption);
        }
    } //setup the access point.

    public boolean enableWifi() {
        // enables WiFi connection
        return getWifiManager().setWifiEnabled(true);
    }

    public boolean disableWifi() {
        // disables WiFi connection
        return getWifiManager().setWifiEnabled(false);
    }

    public static String quoteNonHex(String value, int... allowedLengths) {
        return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
    } // use for WPA2_PSK

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
    } // use for WPA2_PSK

    public static String convertToQuotedString(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        // If already quoted, return as-is
        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
            return s;
        }
        return '\"' + s + '\"';
    } // use for WPA2_PSK


}
