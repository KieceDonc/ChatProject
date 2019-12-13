package com.vvdev.wifichatproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.WifiAP;
import com.vvdev.wifichatproject.interfaces.WifiData;

public class DialogAPWifiData {

    private final static String ACCESS_POINT_NOENCRYPTION = "nopass";
    private final static String ACCESS_POINT_WPA2_PSK = "WPA2-PSK";

    private WifiData DataCall;

    private EditText PasswordInput;
    private EditText SSIDName;
    private LinearLayout LinearLayoutPassword;
    private CheckBox ShowPassword;
    private Spinner SpinnerEncryption;
    private boolean Everythinggood = true; // We use this boolean to make sure everything is good before the setup of AP. This avoid stupid error to handle later

    public void ShowDialog(final Context CurrentContext, final WifiData receiveDataCall){
        DataCall=receiveDataCall;
        final Activity CurrentActivity = (Activity) CurrentContext;

        final AlertDialog.Builder builder = new AlertDialog.Builder(CurrentContext);
        final View CustomLayout = CurrentActivity.getLayoutInflater().inflate(R.layout.access_point_parameters,null);
        builder.setTitle(R.string.APP_TITLE);
        builder.setView(CustomLayout);        // add a button
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.Create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                if(Everythinggood){
                    dialog.dismiss();
                    DataCall.setAPHidden(false);
                    WifiAP CallAP = new WifiAP(CurrentContext);
                    CallAP.Setup(DataCall); // Setup AP
                }else{
                    /**TODO
                     * create small interaction that explain why user can't continue
                     * Like too many caracters. Inval input, etc ....
                     * also you need to handle illegal characters **/
                }

            }
        });
        AlertDialog dialog = builder.create();
        Setup(CurrentContext, CurrentActivity,CustomLayout); // DO NOT PASS Setup() BEFORE DIALOG.SHOW(), you will get "try to invoke object on null object reference" error
        dialog.show();

    }    // do something with the data coming from the AlertDialog

    private void Setup(Context CurrentContext, Activity CurrentActivity, View ViewCustomLayout){
        SetupFindViewById(ViewCustomLayout);
        SetupSSIDName();
        SetupSpinner(CurrentContext,CurrentActivity);
        SetupPassword();
        SetupCheckbox();
    }

    private void SetupFindViewById(View ViewCustomLayout){
        PasswordInput = ViewCustomLayout.findViewById(R.id.PasswordInput);
        SSIDName = ViewCustomLayout.findViewById(R.id.SSIDInput);
        LinearLayoutPassword = ViewCustomLayout.findViewById(R.id.LinearLayoutPassword);
        ShowPassword = ViewCustomLayout.findViewById(R.id.ShowPassword);
        SpinnerEncryption = ViewCustomLayout.findViewById(R.id.SpinnerEncryption);
    }

    private void SetupSSIDName(){
        SSIDName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DataCall.setAPSSSID(s);
                Everythinggood = count < 32; // Handle error of more than 32 character
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void SetupSpinner(Context CurrentContext,final Activity CurrentActivity){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CurrentContext, R.array.APP_TypeEncryption, android.R.layout.simple_spinner_item);  // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  // Apply the adapter to the spinner
        SpinnerEncryption.setAdapter(adapter);
        SpinnerEncryption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ItemSelected = SpinnerEncryption.getSelectedItem().toString();
                final String[] ListEncryption =  CurrentActivity.getResources().getStringArray(R.array.APP_TypeEncryption);
                if(ItemSelected.equals(ListEncryption[0])){ // 0 normally equals to "None"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_NOENCRYPTION);
                    HideLinearLayoutPassword();
                }else if(ItemSelected.equals(ListEncryption[1])){// 3 normally equals to "WPA2-EAP ( recommended )"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WPA2_PSK);
                    ShowLinearLayoutPassword();
                }else{
                    Log.e("DialogAPWifiDataSpinner","Error to get encryption. String value of selected item="+ItemSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
    }

    private void ShowLinearLayoutPassword(){
        LinearLayoutPassword.setVisibility(View.VISIBLE);
    }

    private void HideLinearLayoutPassword(){
        LinearLayoutPassword.setVisibility(View.GONE);
    }

    private void SetupPassword(){
        PasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DataCall.setAPPassword(s);
                Everythinggood = count < 32; // Handle error of more than 32 character
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void SetupCheckbox(){
        ShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShowPassword.isChecked()){
                    PasswordInput.setTransformationMethod(null);
                }else{
                    PasswordInput.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
    }
}
