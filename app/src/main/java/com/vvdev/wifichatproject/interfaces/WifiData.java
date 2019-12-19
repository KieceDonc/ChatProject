package com.vvdev.wifichatproject.interfaces;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.vvdev.wifichatproject.old.WifiHandler;

import java.util.List;
import java.util.regex.Pattern;

import cc.mvdan.accesspoint.WifiApControl;

public class WifiData {

    private String AP_Encryption; // Encryption for access point like WPA etc ...
    private CharSequence AP_Password; // Password for ap
    private CharSequence AP_SSID; // SSID name for ap
    private boolean AP_Hidden; // If true, ap is hidden, else not
    private WifiManager wifiManager;

    public final static String ACCESS_POINT_NOENCRYPTION = "nopass";
    public final static String ACCESS_POINT_WPA2_PSK = "WPA2-PSK";
    public final static String WIFI_ENCRYPTION_WPA = "WPA";
    public final static String WIFI_ENCRYPTION_WEP = "WEP";
    public final static String WIFI_ENCRYPTION_NONE = "nopass";
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

    public List<WifiConfiguration> getListWifiConfiguration(){
        return getWifiManager().getConfiguredNetworks();
    }

    /**
     * Function to disconnect from the currently connected WiFi AP.
     * @return true  if disconnection succeeded
     *               false if disconnection failed
     */

    public boolean disconnectFromWifi() {
        return (getWifiManager().disconnect());
    }

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
        config.preSharedKey =Password;
        config.hiddenSSID = hiddenSSID;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedKeyManagement.set(4);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        return config;
    } // give WPA2-PSK configuration

    public WifiConfiguration getWPAConfig(String NameSSID, String Password, boolean hiddenSSID){
        WifiConfiguration config = createConfig(NameSSID, hiddenSSID);
        //config.preSharedKey = "\""+ Password +"\"";
        config.preSharedKey = Password;
        return config;
    }

    public WifiConfiguration getWEPConfig(String NameSSID, String Password, boolean hiddenSSID){
        WifiConfiguration config = createConfig(NameSSID, hiddenSSID);
        //config.wepKeys[0] = "\"" + Password + "\"";
        config.wepKeys[0] = Password;
        config.wepTxKeyIndex = 0;
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        return config;
    }

    public WifiConfiguration getNoneConfig(String NameSSID, boolean hiddenSSID) {
        WifiConfiguration config = createConfig(NameSSID, hiddenSSID);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return config;
    } // give none configuration

    public void setupAccessPoint(Context CurrentContext,String NameSSID, String Encryption, boolean hiddenSSID,String Password){

        WifiApControl apControl = WifiApControl.getInstance(CurrentContext);

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

    /**
     * Function checkWifiEnabled checks if the WiFi connection
     * is enabled on the device.
     * @return true  if the WiFi connection is enabled,
     *               false if the WiFi connection is disabled
     */

    public boolean enableWifi() {
        // enables WiFi connection
        return getWifiManager().setWifiEnabled(true);
    }

    public boolean disableWifi() {
        // disables WiFi connection
        return getWifiManager().setWifiEnabled(false);
    }

    /**
     * Function checkWifiEnabled checks if the WiFi connection
     * is enabled on the device.
     * @return true  if the WiFi connection is enabled,
     *               false if the WiFi connection is disabled
     */
    public boolean checkWifiEnabled() {
        // checks if WiFi is enabled
        return (getWifiManager() != null && getWifiManager().isWifiEnabled());
    }

    /**
     * Function that scans for wifi networks available in the devices range.
     * @return true  if scan started
     *               false if scan could not be started
     */
    public boolean scanWifiInRange() {
        if (!checkWifiEnabled()) {
            enableWifi();
        }

        if (getWifiManager().startScan()) {
            Log.d("TAG", "Failed to scan wifi's in range.");
            return false;
        }

        return true;
    }

    /**
     * Function getWifiInRange returns all the WiFi networks that are
     * accessible through the access point (device AP) found during the
     * last scan.
     * @return List of ScanResult containing information on all WiFi networks
     *               discovered in the range.
     */
    public List<ScanResult> getWifiInRange() {
        // gets ~last~ list of WiFi networks accessible through the access point.
        return getWifiManager().getScanResults();
    }

    /**
     * Function to connect to a selected network
     * @param networkSSID         network SSID name
     * @param networkPassword     network password
     * @param networkId           network ID from WifiManager
     * @param SecurityProtocol    network security protocol
     * @return true  if connection to selected network succeeded
     *               false if connection to selected network failed
     */

    /**
     * 0 = success to connect to selected network
     * */

    public int connectToSelectedNetwork(String networkSSID, String networkPassword, String SecurityProtocol,boolean hiddenSSID) {
        int networkId;

        WifiConfiguration config = null;

        switch(SecurityProtocol) {
            // WEP "security".
            case WIFI_ENCRYPTION_WEP:
                config = getWEPConfig(networkSSID,networkPassword,hiddenSSID);
                break;

            // WAP security. We have to set preSharedKey.
            case WIFI_ENCRYPTION_WPA:
                config = getWPAConfig(networkSSID,networkPassword,hiddenSSID);
                break;

            // Network without security.
            case WIFI_ENCRYPTION_NONE:
                config = getNoneConfig(networkSSID,hiddenSSID);
                break;
        }

        // Add WiFi configuration to list of recognizable networks
        if ((networkId = getWifiManager().addNetwork(config)) == -1) {
            Log.d("TAG", "Failed to add network configuration!");
            return WIFI_FAIL_ADD_CONFIG;
        }

        // Disconnect from current WiFi connection
        if (!disconnectFromWifi()) {
            Log.d("TAG", "Failed to disconnect from network!");
            return WIFI_FAIL_DISCONNECT_OLD_NETWORK;
        }

        // Enable network to be connected
        if (!getWifiManager().enableNetwork(networkId, true)) {
            Log.d("TAG", "Failed to enable network!");
            return WIFI_FAIL_ENABLE_NETWORK;
        }

        // Connect to network
        if (!getWifiManager().reconnect()) {
            Log.d("TAG", "Failed to connect!");
            return WIFI_FAIL_TO_CONNECT;
        }

        return WIFI_CONNECT_SUCCESS;
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
