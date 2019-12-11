package com.vvdev.wifichatproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.vvdev.wifichatproject.interfaces.WifiData;

public class AskForWifiData{

    private final static String ACCESS_POINT_NOENCRYPTION = "nopass";
    private final static String ACCESS_POINT_WPA = "WPA";
    private final static String ACCESS_POINT_WPA2 = "WPA2";
    private final static String ACCESS_POINT_WPA2_EAP = "WPA2-EAP";
    private final static String ACCESS_POINT_WEP = "WEP";

    private WifiData DataCall;

    private EditText PasswordInput;
    private EditText SSIDName;
    private LinearLayout LinearLayoutPassword;
    private CheckBox ShowPassword;
    private Spinner SinnerEncryption;

    public void ShowDialog(Context CurrentContext){

        DataCall = new WifiData();
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
                DataCall.setAPHidden(true);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        Setup(CurrentContext, CurrentActivity); // DO NOT PASS Setup() BEFORE DIALOG.SHOW(), you will get "try to invoke object on null object reference" error
    }    // do something with the data coming from the AlertDialog

    private void Setup(Context CurrentContext, Activity CurrentActivity){
        SetupFindViewById(CurrentActivity);
        SetupSSIDName();
        SetupSpinner(CurrentContext,CurrentActivity);
        SetupPassword();
        SetupCheckbox();
    }

    private void SetupFindViewById(Activity CurrentActivity){
        PasswordInput = CurrentActivity.findViewById(R.id.PasswordInput);
        SSIDName = CurrentActivity.findViewById(R.id.SSIDInput);
        LinearLayoutPassword = CurrentActivity.findViewById(R.id.LinearLayoutPassword);
        ShowPassword = CurrentActivity.findViewById(R.id.ShowPassword);
        SinnerEncryption = CurrentActivity.findViewById(R.id.SpinnerEncryption);
    }

    private void SetupSSIDName(){
        SSIDName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DataCall.setAPSSSID(s);
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
        SinnerEncryption.setAdapter(adapter);
        SinnerEncryption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ItemSelected = SinnerEncryption.getSelectedItem().toString();
                final String[] ListEncryption =  CurrentActivity.getResources().getStringArray(R.array.APP_TypeEncryption);
                if(ItemSelected.equals(ListEncryption[0])){ // 0 normally equals to "None"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_NOENCRYPTION);
                    HideLinearLayoutPassword();
                }else if(ItemSelected.equals(ListEncryption[3])){// 3 normally equals to "WPA2-EAP ( recommended )"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WPA2_EAP);
                    ShowLinearLayoutPassword();
                }else if(ItemSelected.equals(ListEncryption[2])){ //2 normally equals to "WPA2"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WPA2);
                    ShowLinearLayoutPassword();
                }else if(ItemSelected.equals(ListEncryption[1])){ //1 normally equals to "WPA"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WPA);
                    ShowLinearLayoutPassword();
                }else if(ItemSelected.equals(ListEncryption[4])){  //4 normally equals to "WEP"   11/12/2019
                    DataCall.setAPEncryption(ACCESS_POINT_WEP);
                    ShowLinearLayoutPassword();
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

    private void ShowLinearLayoutPassword(){
        LinearLayoutPassword.setVisibility(View.VISIBLE);
    }

    private void HideLinearLayoutPassword(){
        LinearLayoutPassword.setVisibility(View.INVISIBLE);
    }

    private void SetupPassword(){
        PasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DataCall.setAPPassword(s);
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
