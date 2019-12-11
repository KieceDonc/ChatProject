package com.vvdev.wifichatproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.WifiData;

import java.lang.reflect.Array;
import java.util.List;

public class AskForWifiData extends WifiData {

    private final static String ACCESS_POINT_NOENCRYPTION = "nopass";
    private final static String ACCESS_POINT_WPA = "WPA";
    private final static String ACCESS_POINT_WPA2 = "WPA2";
    private final static String ACCESS_POINT_WPA2_EAP = "WPA2-EAP";
    private final static String ACCESS_POINT_WEP = "WEP";

    public void ShowDialog(Context CurrentContext){

        Activity CurrentActivity = (Activity) CurrentContext;

        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentContext);
        final View CustomLayout = CurrentActivity.getLayoutInflater().inflate(R.layout.access_point_parameters,null);
        builder.setView(CustomLayout);        // add a button
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        SetupViewFinish(CurrentContext, CurrentActivity);
    }    // do something with the data coming from the AlertDialog

    private void SetupViewFinish(Context CurrentContext, Activity CurrentActivity){
        SpinnerSetup(CurrentContext,CurrentActivity);

    }


    private void SpinnerSetup(Context CurrentContext,final Activity CurrentActivity){
        final Spinner spinner = CurrentActivity.findViewById(R.id.SpinnerPassword);  // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CurrentContext,
                R.array.APP_TypeEncryption, android.R.layout.simple_spinner_item);  // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ItemSelected = spinner.getSelectedItem().toString();
                WifiData DataCall = new WifiData();
                final String[] ListEncryption =  CurrentActivity.getResources().getStringArray(R.array.APP_TypeEncryption);
                if(ItemSelected.equals(ListEncryption[0])){ // 0 normally equals to "None"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_NOENCRYPTION);
                }else if(ItemSelected.equals(ListEncryption[3])){// 3 normally equals to "WPA2-EAP ( recommended )"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WPA2_EAP);
                }else if(ItemSelected.equals(ListEncryption[2])){ //2 normally equals to "WPA2"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WPA2);
                }else if(ItemSelected.equals(ListEncryption[1])){ //1 normally equals to "WPA"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WPA);
                }else if(ItemSelected.equals(ListEncryption[4])){  //4 normally equals to "WEP"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WEP);
                }else{
                    Log.e("AskForWifiData-Spinner","Error to get encryption. String value of selected item="+ItemSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
    }
}
