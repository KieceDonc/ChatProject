package com.vvdev.wifichatproject.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.vvdev.wifichatproject.R;
import com.vvdev.wifichatproject.interfaces.DefaultDialog;
import com.vvdev.wifichatproject.interfaces.DefaultUser;

import java.util.ArrayList;
import java.util.List;

public class DialogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogslistmain);

        DialogsList dialogsList = findViewById(R.id.dialogsList);


        @SuppressLint("ResourceType") DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<>(R.id.dialogsList, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.get().load(Integer.parseInt(url)).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogsListAdapter);
        List<IUser> TestListUser = new ArrayList<>();
        IUser t = new DefaultUser("0","test",String.valueOf(R.drawable.wifi_icon_good));
        IMessage =
        TestListUser.add(t);
        IDialog Test = new DefaultDialog("0",String.valueOf(R.drawable.wifi_icon_good),"tst",TestListUser,)
    }
}
