package com.vvdev.wifichatproject.ui;

import android.net.wifi.WifiConfiguration;
import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.WifiData;

import java.util.List;

public class RecycleViewJoinWifi extends RecyclerView.Adapter<RecycleViewJoinWifi.MyViewHolder> {

    private Context CurrentContext;
    private BroadcastReceiver ScanWifiNetwork;
    private List<ScanResult> WifiList;
    private WifiData CallData;
    private AlertDialog mDialog;
    private int mPosition = 0; // serve in my


    public RecycleViewJoinWifi(Context receive1, WifiData receive2, AlertDialog receive3){
        CurrentContext = receive1;
        CallData = receive2;
        mDialog = receive3;
        WifiList = CallData.getWifiInRange();
        TextViewNoNetwork();

        ScanWifiNetwork = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                WifiList = CallData.getWifiInRange();
                TextViewNoNetwork();
                notifyDataSetChanged();
                Log.e("test", String.valueOf(WifiList));
            }
        }; // create broadcast receive
        IntentFilter WifiChangeFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        WifiChangeFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        WifiChangeFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        WifiChangeFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        CurrentContext.registerReceiver(ScanWifiNetwork, WifiChangeFilter);
    }
    @Override
    public int getItemCount() {
        return WifiList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.network_join_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewJoinWifi.MyViewHolder holder, int position) {
        ScanResult pair = WifiList.get(position);
        holder.display(pair);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        CurrentContext.unregisterReceiver(ScanWifiNetwork);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView SSIDName;
        private ScanResult CurrentPair;
        private TextView mMacAddress;

        MyViewHolder(final View itemView) {
            super(itemView);

            SSIDName = itemView.findViewById(R.id.SSIDName);
            mMacAddress = itemView.findViewById(R.id.MacAddress);

            CurrentPair=WifiList.get(mPosition);
            mPosition++;
            int signalPower = CurrentPair.level;
            ImageView imgSignalPower = itemView.findViewById(R.id.SignalPower);
            if (signalPower > -50) {
                imgSignalPower.setImageResource(R.drawable.wifi_icon_excellent);
            }else if(signalPower >-60){
                imgSignalPower.setImageResource(R.drawable.wifi_icon_good);
            }else if(signalPower >-70){
                imgSignalPower.setImageResource(R.drawable.wifi_icon_fair);
            }else{
                imgSignalPower.setImageResource(R.drawable.wifi_icon_weak);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    List<WifiConfiguration>  AllWifiConfiguration = CallData.getListWifiConfiguration();
                    int WifiConfigurationMatch = -1; // -1 mean no wificonfiguration match but if wificonfiguration != -1 this mean that user have already connect to thsi network
                    for( int x=0;x<AllWifiConfiguration.size();x++){
                        if(AllWifiConfiguration.get(x).BSSID.equals(CurrentPair.BSSID)){
                            WifiConfigurationMatch = x;
                        }
                    }

                    if(WifiConfigurationMatch==-1){ // if no match, ask to user to enter password, else ...

                        if(CurrentPair.capabilities.contains("OPEN")){
                            CallData.connectToSelectedNetwork(CallData.getNoneConfig(CurrentPair.SSID,false),true);
                        } else if (CurrentPair.capabilities.contains("WPA2-PSK")){
                            // TODO need to handle password ask
                            CallData.connectToSelectedNetwork(CallData.getWPA2PSKConfig(CurrentPair.SSID," replace by the password handler ",false),true);
                        }else{
                            // TODO need to explain to user that this app don't handle this type of configuration and he need to add this by his own. Need to ask him if he want to redirect to wifi menu of the phone to add the wificonfiguration manually
                        }
                    }else {
                        WifiConfiguration GoodConfiguration = AllWifiConfiguration.get(WifiConfigurationMatch);
                        CallData.connectToSelectedNetwork(GoodConfiguration, false);
                    }
                }
            });


        }

        public void display(ScanResult pair) {
            SSIDName.setText(pair.SSID);
            mMacAddress.setText(pair.BSSID);
        }
    }


    private void TextViewNoNetwork(){

        TextView NoWifiNetwork = mDialog.findViewById(R.id.NoWifiNetwork);

        if(WifiList.size()==0){
            NoWifiNetwork.setVisibility(View.VISIBLE);
        }else{
            NoWifiNetwork.setVisibility(View.GONE);
        }
    }

}