/**
 * Uppsala University
 *
 * Project CS course, Fall 2012
 *
 * Projekt DV/Project CS, is a course in which the students develop software for
 * distributed systems. The aim of the course is to give insights into how a big
 * project is run (from planning to realization), how to construct a complex
 * distributed system and to give hands-on experience on modern construction
 * principles and programming methods.
 *
 * All rights reserved.
 *
 * Copyright (C) 2012 LISA team
 */

package com.vvdev.wifichatproject.interfaces;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import static com.vvdev.wifichatproject.interfaces.WifiData.WIFI_CONNECT_SUCCESS;
import static com.vvdev.wifichatproject.interfaces.WifiData.WIFI_FAIL_ADD_CONFIG;
import static com.vvdev.wifichatproject.interfaces.WifiData.WIFI_FAIL_DISCONNECT_OLD_NETWORK;
import static com.vvdev.wifichatproject.interfaces.WifiData.WIFI_FAIL_ENABLE_NETWORK;
import static com.vvdev.wifichatproject.interfaces.WifiData.WIFI_FAIL_TO_CONNECT;

public class WifiHandler {

    public static final String TAG = "LISA_Network";

    /** dfsdfsdfsdf. */
    WifiConfiguration wifiConf;             /* WifiConfiguration object */

    /** dfsdfsdfsdf. */
    WifiInfo wifiInfo;                              /* WifiInfo object */

    /** dfsdfsdfsdf. */
    List<ScanResult> wifiScan;              /* List of ScanResult objects */
    
    WifiData CallData;

    /**
     * Constructor initializes WifiManager and WifiInfo.
     * @param context
     */
    public WifiHandler(Context context, WifiData receive) {
        wifiInfo = getWifiInfo(context);            // gets wifiInfo in the current context
        wifiConf = getWifiConf(context);            // gets wifiConf in the current context
        wifiScan = getWifiInRange();                    // gets wifiScan in the current context
        CallData=receive;
    }

    /**
     * Function checkWifiEnabled checks if the WiFi connection
     * is enabled on the device.
     * @param
     * @return true  if the WiFi connection is enabled,
     *               false if the WiFi connection is disabled
     */
    public boolean checkWifiEnabled() {
        // checks if WiFi is enabled
        return (CallData.getWifiManager() != null && CallData.getWifiManager().isWifiEnabled());
    }

    /**
     * Function enableWifi enables WiFi connection on the device.
     * @return true  if the attempt to enable WiFi succeeded,
     *               false if the attempt to enable WiFi failed.
     */
    public boolean enableWifi() {
        // enables WiFi connection
        return CallData.getWifiManager().setWifiEnabled(true);
    }

    /**
     * Function disableWifi disables WiFi connection on the device.
     * @return true  if WiFi connection was disabled,
     *               false if attempt to disable WiFi failed.
     */
    public boolean disableWifi() {
        // disables WiFi connection
        return CallData.getWifiManager().setWifiEnabled(false);
    }

    /**
     * Function getWifiInfo gets the current WiFi connection information in a
     * WifiInfo object from the device.
     * @return wifiInfo created object or
     *               null       if wifi is not enabled.
     */
    public WifiInfo getWifiInfo(Context context) {
        WifiInfo wifiInfo = null;

        // gets WiFi network info of the current connection
        if (checkWifiEnabled()) {
            wifiInfo = (WifiInfo) CallData.getWifiManager().getConnectionInfo();
        }

        if (wifiInfo == null) {
            Log.d("TAG", "WifiInfo object is empty.");
        }

        return wifiInfo;
    }

    /**
     * Function that returns a WifiConfiguration object from the WifiInfo
     * object from the class. If wifiInfo exists, then we are able to retrieve
     * information from the current connection
     * @return WifiConfiguration object created.
     */
    public WifiConfiguration getWifiConf(Context context) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();

        if (wifiInfo == null) {
            Log.d("TAG", "WifiInfo object is empty");
            return null;
        }

        wifiConfiguration.SSID = wifiInfo.getSSID();
        wifiConfiguration.networkId = wifiInfo.getNetworkId();

        return wifiConfiguration;
    }

    /**
     * Creates a new WifiConfiguration object for wifiConf.
     */
    public void clearWifiConfig() {
        wifiConf = new WifiConfiguration();
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
        return (wifiScan = (List<ScanResult>) CallData.getWifiManager().getScanResults());
    }

    /**
     * Function that scans for wifi networks available in the devices range.
     * @return true  if scan started
     *               false if scan could not be started
     */
    public boolean scanWifiInRange() {
        if (!checkWifiEnabled()) {
            CallData.getWifiManager().setWifiEnabled(true);
        }

        if (!CallData.getWifiManager().startScan()) {
            Log.d("TAG", "Failed to scan wifi's in range.");
            return false;
        }

        return true;
    }

    /**
     * Function to disconnect from the currently connected WiFi AP.
     * @return true  if disconnection succeeded
     *               false if disconnection failed
     */
    public boolean disconnectFromWifi() {
        return (CallData.getWifiManager().disconnect());
    }

    /**
     * Function to connect to a selected network
     * @param networkSSID         network SSID name
     * @param   networkPassword     network password
     * @param networkId           network ID from WifiManager
     * @param SecurityProtocol    network security protocol
     * @return true  if connection to selected network succeeded
     *               false if connection to selected network failed
     */

    /**
     * 0 = success to connect to selected network
     * */
    public int connectToSelectedNetwork(String networkSSID, String networkPassword, String SecurityProtocol) {
        int networkId;

        // Clear wifi configuration variable
        clearWifiConfig();

        // Sets network SSID name on wifiConf
        wifiConf.SSID = "\"" + networkSSID + "\"";
        Log.d(TAG, "SSID Received: " + wifiConf.SSID);
        switch(SecurityProtocol) {
            // WEP "security".
            case "WEP":
                wifiConf.wepKeys[0] = "\"" + networkPassword + "\"";
                wifiConf.wepTxKeyIndex = 0;
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;

            // WAP security. We have to set preSharedKey.
            case "WAP":
                wifiConf.preSharedKey = "\""+ networkPassword +"\"";
                break;

            // Network without security.
            case "OPEN_NETWORK":
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
        }

        // Add WiFi configuration to list of recognizable networks
        if ((networkId = CallData.getWifiManager().addNetwork(wifiConf)) == -1) {
            Log.d("TAG", "Failed to add network configuration!");
            return WIFI_FAIL_ADD_CONFIG;
        }

        // Disconnect from current WiFi connection
        if (!disconnectFromWifi()) {
            Log.d("TAG", "Failed to disconnect from network!");
            return WIFI_FAIL_DISCONNECT_OLD_NETWORK;
        }

        // Enable network to be connected
        if (!CallData.getWifiManager().enableNetwork(networkId, true)) {
            Log.d("TAG", "Failed to enable network!");
            return WIFI_FAIL_ENABLE_NETWORK;
        }

        // Connect to network
        if (!CallData.getWifiManager().reconnect()) {
            Log.d("TAG", "Failed to connect!");
            return WIFI_FAIL_TO_CONNECT;
        }

        return WIFI_CONNECT_SUCCESS;
    }
}