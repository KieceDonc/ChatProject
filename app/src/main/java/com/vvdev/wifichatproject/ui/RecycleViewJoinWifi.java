package com.vvdev.wifichatproject.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    public RecycleViewJoinWifi(Context receive1, WifiData receive2){
        CurrentContext = receive1;
        CallData = receive2;
        WifiList = CallData.getWifiInRange();

        ScanWifiNetwork = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                WifiList = CallData.getWifiInRange();
                notifyDataSetChanged();
            }
        }; // create broadcast receive
        CurrentContext.registerReceiver(ScanWifiNetwork, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); // BroadcastReceiver when wifi is detected

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
        private int SignalPower;
        private String encryption;

        MyViewHolder(final View itemView) {
            super(itemView);

            SSIDName = itemView.findViewById(R.id.SSIDName);
            ImageView imgSignalPower = itemView.findViewById(R.id.SignalPower);
            if (SignalPower > -50) {
                imgSignalPower.setImageResource(R.drawable.wifi_icon_excellent);
            }else if(SignalPower>-60){
                imgSignalPower.setImageResource(R.drawable.wifi_icon_good);
            }else if(SignalPower>-70){
                imgSignalPower.setImageResource(R.drawable.wifi_icon_fair);
            }else{
                imgSignalPower.setImageResource(R.drawable.wifi_icon_weak);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // CallData.connectToSelectedNetwork();// TODO need to handle password ( Does user have been connected to this network ? Yes -> Try to connect and if failure ask for password, No -> Ask for password
                }
            });
        }

        public void display(ScanResult pair) {
            SSIDName.setText(pair.SSID);
            SignalPower = pair.level;
            encryption = pair.capabilities;
        }
    }

}