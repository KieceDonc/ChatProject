package com.vvdev.wifichatproject.old;

/*
 * Copyright (C) 2011 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.vvdev.wifichatproject.interfaces.WifiData;
import com.vvdev.wifichatproject.old.WifiNetworkType;

import java.util.regex.Pattern;


/**
 * @author Vikram Aggarwal
 * @author Sean Owen
 * @author Steffen Kieß
 */
public final class WifiConfigManager extends AsyncTask<WifiData,Object,Object> {


    private static final String TAG = WifiConfigManager.class.getSimpleName();

    private static final Pattern HEX_DIGITS = Pattern.compile("[0-9A-Fa-f]+");

    private final WifiManager wifiManager;

    public WifiConfigManager(WifiManager mWifiManager) {
        this.wifiManager = mWifiManager;
    }

    @Override
    protected WifiData doInBackground(WifiData... receive) {

        WifiData CallData = receive[0];
        // Start WiFi, otherwise nothing will work
        if (!wifiManager.isWifiEnabled()) {
            Log.i(TAG, "Enabling wi-fi...");
            if (wifiManager.setWifiEnabled(true)) {
                Log.i(TAG, "Wi-fi enabled");
            } else {
                Log.w(TAG, "Wi-fi could not be enabled!");
                return null;
            }
            // This happens very quickly, but need to wait for it to enable. A little busy wait?
            int count = 0;
            while (!wifiManager.isWifiEnabled()) {
                if (count >= 10) {
                    Log.i(TAG, "Took too long to enable wi-fi, quitting");
                    return null;
                }
                Log.i(TAG, "Still waiting for wi-fi to enable...");
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ie) {
                    // continue
                }
                count++;
            }
        }
        String networkTypeString = CallData.getAPEncryption();
        WifiNetworkType networkType;
        try {
            networkType = WifiNetworkType.forIntentValue(networkTypeString);
        } catch (IllegalArgumentException ignored) {
            Log.w(TAG, "Bad network type");
            return null;
        }
        if (networkType == WifiNetworkType.NO_PASSWORD) {
            changeNetworkUnEncrypted(wifiManager, CallData);
        } else {
            String password = CallData.getAPPassword();
            if (password != null && !password.isEmpty()) {
                switch (networkType) {
                    case WEP:
                        changeNetworkWEP(wifiManager, CallData);
                        break;
                    case WPA:
                        changeNetworkWPA(wifiManager, CallData);
                        break;
                    case WPA2_PSK:
                        changeNetworkWPA2PSK(wifiManager, CallData);
                        break;
                    case WPA2_EAP:
                        changeNetworkWPA2EAP(wifiManager, CallData);
                        break;
                }
            }
        }
        return null;
    }


    private static void updateNetwork(WifiManager wifiManager, WifiConfiguration config) {
        Integer foundNetworkID = findNetworkInExistingConfig(wifiManager, config.SSID);
        if (foundNetworkID != null) {
            Log.i(TAG, "Removing old configuration for network " + config.SSID);
            wifiManager.removeNetwork(foundNetworkID);
            wifiManager.saveConfiguration();
        }
        int networkId = wifiManager.addNetwork(config);
        if (networkId >= 0) {
            // Try to disable the current network and start a new one.
            if (wifiManager.enableNetwork(networkId, true)) {
                Log.i(TAG, "Associating to network " + config.SSID);
                wifiManager.saveConfiguration();
            } else {
                Log.w(TAG, "Failed to enable network " + config.SSID);
            }
        } else {
            Log.w(TAG, "Unable to add network " + config.SSID);
        }
    }

    private static WifiConfiguration changeNetworkCommon(WifiData CallData) {
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

    // Adding a WEP network
    private static void changeNetworkWEP(WifiManager wifiManager, WifiData CallData) {
        WifiConfiguration config = changeNetworkCommon(CallData);
        config.wepKeys[0] = quoteNonHex(CallData.getAPPassword(), 10, 26, 58);
        config.wepTxKeyIndex = 0;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        updateNetwork(wifiManager, config);
    }

    // Adding a WPA or WPA2 network
    private static void changeNetworkWPA(WifiManager wifiManager, WifiData CallData) {
        WifiConfiguration config = changeNetworkCommon(CallData);
        // Hex passwords that are 64 bits long are not to be quoted.
        config.preSharedKey = quoteNonHex(CallData.getAPPassword(), 64);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // For WPA
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        updateNetwork(wifiManager, config);
    }

    private static void changeNetworkWPA2PSK(WifiManager wifiManager, WifiData CallData){
        WifiConfiguration config = changeNetworkCommon(CallData);
        config.SSID = CallData.getAPSSID();
        config.preSharedKey = CallData.getAPPassword();
        config.hiddenSSID = false;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedKeyManagement.set(4);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        updateNetwork(wifiManager,config);
    }

    // Adding a WPA2 enterprise (EAP) network
    private static void changeNetworkWPA2EAP(WifiManager wifiManager, WifiData CallData) {
        WifiConfiguration config = changeNetworkCommon(CallData);
        // Hex passwords that are 64 bits long are not to be quoted.
        /*config.preSharedKey = quoteNonHex(CallData.getAPPassword(), 64);
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.enterpriseConfig.setIdentity(wifiResult.getIdentity());
        config.enterpriseConfig.setAnonymousIdentity(wifiResult.getAnonymousIdentity());
        config.enterpriseConfig.setPassword(CallData.getAPPassword());
        config.enterpriseConfig.setEapMethod(parseEap(wifiResult.getEapMethod()));
        config.enterpriseConfig.setPhase2Method(parsePhase2(wifiResult.getPhase2Method()));
        updateNetwork(wifiManager, config);*/
    }

    // Adding an open, unsecured network
    private static void changeNetworkUnEncrypted(WifiManager wifiManager, WifiData CallData) {
        WifiConfiguration config = changeNetworkCommon(CallData);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        updateNetwork(wifiManager, config);
    }

    private static Integer findNetworkInExistingConfig(WifiManager wifiManager, String ssid) {
        Iterable<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                String existingSSID = existingConfig.SSID;
                if (existingSSID != null && existingSSID.equals(ssid)) {
                    return existingConfig.networkId;
                }
            }
        }
        return null;
    }

    private static String quoteNonHex(String value, int... allowedLengths) {
        return isHexOfLength(value, allowedLengths) ? value : convertToQuotedString(value);
    }

    /**
     * Encloses the incoming string inside double quotes, if it isn't already quoted.
     * @param s the input string
     * @return a quoted string, of the form "input".  If the input string is null, it returns null
     * as well.
     */
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

    /**
     * @param value input to check
     * @param allowedLengths allowed lengths, if any
     * @return true if value is a non-null, non-empty string of hex digits, and if allowed lengths are given, has
     *  an allowed length
     */
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

    private static int parseEap(String eapString) {
        if (eapString == null) {
            return WifiEnterpriseConfig.Eap.NONE;
        }
        switch (eapString) {
            case "NONE":
                return WifiEnterpriseConfig.Eap.NONE;
            case "PEAP":
                return WifiEnterpriseConfig.Eap.PEAP;
            case "PWD":
                return WifiEnterpriseConfig.Eap.PWD;
            case "TLS":
                return WifiEnterpriseConfig.Eap.TLS;
            case "TTLS":
                return WifiEnterpriseConfig.Eap.TTLS;
            default:
                throw new IllegalArgumentException("Unknown value for EAP method: " + eapString);
        }
    }

    private static int parsePhase2(String phase2String) {
        if (phase2String == null) {
            return WifiEnterpriseConfig.Phase2.NONE;
        }
        switch (phase2String) {
            case "GTC":
                return WifiEnterpriseConfig.Phase2.GTC;
            case "MSCHAP":
                return WifiEnterpriseConfig.Phase2.MSCHAP;
            case "MSCHAPV2":
                return WifiEnterpriseConfig.Phase2.MSCHAPV2;
            case "NONE":
                return WifiEnterpriseConfig.Phase2.NONE;
            case "PAP":
                return WifiEnterpriseConfig.Phase2.PAP;
            default:
                throw new IllegalArgumentException("Unknown value for phase 2 method: " + phase2String);
        }
    }

}