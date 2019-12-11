package com.vvdev.wifichatproject.interfaces;

public class WifiData {

    private String AP_Encryption; // Encryption for access point like WPA etc ...
    private String AP_Password; // Password for ap
    private String AP_SSID; // SSID name for ap
    private boolean AP_Hidden; // If true, ap is hidden, else not

    public void setAPEncryption(String ReceiveString){
        AP_Encryption = ReceiveString;
    }

    public String getAPEncryption(){
        return AP_Encryption;
    }

    public void setAPPassword(String ReceiveString){
        AP_Password = ReceiveString;
    }

    public String setAPPassword(){
        return AP_Password;
    }

    public void setAPSSSID(String ReceiveString){
        AP_SSID = ReceiveString;
    }

    public String getAPSSID(){
        return AP_SSID;
    }

    public void setAPHidden(boolean Receive){
        AP_Hidden = Receive;
    }

    public boolean getAPHidden(){
        return AP_Hidden;
    }




}
