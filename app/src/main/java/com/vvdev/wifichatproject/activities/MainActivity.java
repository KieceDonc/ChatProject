package com.vvdev.wifichatproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vvdev.wifichatproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DialogsActivity.class);
        startActivity(intent);

        /*Intent intent = new Intent(this, Network.class);
        startActivity(intent);*/


    }

}



/**
 * TODO need to sign app for AP create*/
