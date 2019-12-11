package com.vvdev.wifichatproject.interfaces;

public class WifiData {

    private String AP_Encryption; // Encryption for access point like WPA etc ...
    private CharSequence AP_Password; // Password for ap
    private CharSequence AP_SSID; // SSID name for ap
    private boolean AP_Hidden; // If true, ap is hidden, else not

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




}
