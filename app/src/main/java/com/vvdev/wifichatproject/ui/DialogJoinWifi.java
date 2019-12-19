package com.vvdev.wifichatproject.ui;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.WifiData;
import java.util.Objects;

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

        int[] DimensionScreen = GetSizeOfScreen(CurrentActivity);// get dimension of the screen
        /**
         * TODO review how to manage the ui interface with an reycleview
         **/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = DimensionScreen[0]/100*75; // set width of dialog to 75 % of width of the screen
        lp.height = DimensionScreen[1]/100*75; // set height of dialog to 75 % of height of the screen
//lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 330/*height value*/, getResources().getDisplayMetrics()); for custom height value
        dialog.getWindow().setAttributes(lp);

        setupRecycleView(dialog);
    }


    private int[] GetSizeOfScreen(Activity CurrentActivity){
        Display display = CurrentActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);
        return new int[]{width, height};
    }

    private void setupRecycleView(AlertDialog dialog){
        final RecyclerView rv = dialog.findViewById(R.id.WifiNetworkRecycleView);
        rv.setLayoutManager(new LinearLayoutManager(CurrentContext));
        RecycleViewJoinWifi adapter = new RecycleViewJoinWifi(CurrentContext,DataCall);
        rv.setAdapter(adapter);
    }


}
