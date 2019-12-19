package com.vvdev.wifichatproject.ui;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.WifiData;

public class DialogJoinWifi extends AlertDialog.Builder {

    private Context CurrentContext;
    private WifiData DataCall;

    public DialogJoinWifi(Context receive1,WifiData receive2) {
        super(receive1);
        CurrentContext=receive1;
        DataCall=receive2;
    }

    public void Show(){
        Activity CurrentActivity = (Activity) CurrentContext;

        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentContext);
        View CustomLayout = CurrentActivity.getLayoutInflater().inflate(R.layout.network_join,null);
        builder.setView(CustomLayout);

        AlertDialog dialog = builder.create(); //create dialog
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        setupRecycleView(dialog);
    }

    private void setupRecycleView(AlertDialog dialog){
        final RecyclerView rv = dialog.findViewById(R.id.WifiNetworkRecycleView);
        rv.setLayoutManager(new LinearLayoutManager(CurrentContext));
        RecycleViewJoinWifi adapter = new RecycleViewJoinWifi(CurrentContext,DataCall,dialog);
        rv.setAdapter(adapter);
    }


}
